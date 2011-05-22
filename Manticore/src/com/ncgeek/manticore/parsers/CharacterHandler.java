package com.ncgeek.manticore.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
		}
		public String getName() { return localname; }
		public Section getNext() { return next; }
		public IElementHandler getHandler() { return handler; }
	}
	
	private final CharacterParser parser;
	private PlayerCharacter pc;
	
	private StringBuilder buf;
	private Section currentSection;
	private Section nextSection;
	private IElementHandler sectionHandler;
	
	public CharacterHandler(CharacterParser parser) {
		this.parser = parser;
	}
	
	public PlayerCharacter getPlayerCharacter() { return pc; }
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		pc = new PlayerCharacter();
		buf = new StringBuilder(500);
		currentSection = null;
		nextSection = Section.Details;
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		
		if(sectionHandler != null) {
			sectionHandler.startElement(pc, localName, attributes);
		} else if(nextSection != null && nextSection.getName().equals(name)) {
			currentSection = nextSection;
			nextSection = currentSection.getNext();
			sectionHandler = currentSection.getHandler();
			parser.sectionStart(currentSection.toString());
		}
		
		buf.setLength(0);
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		buf.append(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		String body = buf.toString();
		buf.setLength(0);
		
		if(currentSection == null)
			return;
		
		if(name.equals(currentSection.getName())) {
			// do nothing
		} else if(sectionHandler != null) {
			sectionHandler.endElement(pc, localName, body);
			
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
