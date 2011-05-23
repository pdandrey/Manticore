package com.ncgeek.manticore.parsers;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ncgeek.manticore.ICompendiumRepository;
import com.ncgeek.manticore.character.PlayerCharacter;

class CharacterHandler extends DefaultHandler {

	private enum Section { 
		Powers("PowerStats", null, null),
		Loot("LootTally", LootHandler.getInstance(), Powers),
		Rules("RulesElementTally", RulesElementHandler.getInstance(), Loot),
		Stats("StatBlock", StatBlockHandler.getInstance(), Rules),
		Details("Details", DetailsHandler.getInstance(), Stats);
		
		private String localname;
		private IElementHandler handler;
		private Section next;
		Section(String name, IElementHandler handler, Section next) {
			localname = name;
			this.handler = handler;
			this.next = next;
		}
		public String getName() { return localname; }
		public Section getNext() { return next; }
		public IElementHandler getHandler() { return handler; }
	}
	
	private final CharacterParser parser;
	private PlayerCharacter pc;
	
	private Section currentSection;
	private Section nextSection;
	private IElementHandler sectionHandler;
	private Stack<StringBuilder> textQueue;
	
	public CharacterHandler(CharacterParser parser, ICompendiumRepository repos) {
		this.parser = parser;
		((LootHandler)Section.Loot.getHandler()).setRepository(repos);
		textQueue = new Stack<StringBuilder>();
	}
	
	public PlayerCharacter getPlayerCharacter() { return pc; }
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		pc = new PlayerCharacter();
		currentSection = null;
		nextSection = Section.Details;
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		
		if(nextSection != null && name.equals(nextSection.getName())) {
			currentSection = nextSection;
			nextSection = currentSection.getNext();
			sectionHandler = currentSection.getHandler();
			parser.sectionStart(currentSection.toString());
		} else if(sectionHandler != null) {
			sectionHandler.startElement(pc, name, attributes);
		} 
		textQueue.push(new StringBuilder(100));
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		textQueue.peek().append(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		String body = textQueue.pop().toString();
		
		if(currentSection == null)
			return;
		
		if(name.equals(currentSection.getName())) {
			sectionHandler = null;
		}
		
		if(sectionHandler != null) {
			sectionHandler.endElement(pc, name, body);
			
			if(currentSection == Section.Rules) {
				pc.add(((RulesElementHandler)sectionHandler).getRule());
			}
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		parser.finished(pc);
	}
	
}
