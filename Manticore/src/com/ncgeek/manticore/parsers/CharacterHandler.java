package com.ncgeek.manticore.parsers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.util.Logger;

class CharacterHandler extends DefaultHandler {

	private static final String LOG_TAG = "CharacterHandler";
	
	private enum Section { 
		Misc("textstring", TextStringHandler.getInstance(), null),
		Powers("PowerStats", PowerHandler.getInstance(), Misc),
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
	
	private final CharacterParser<? extends PlayerCharacter> parser;
	private PlayerCharacter pc;
	
	private Section currentSection;
	private Section nextSection;
	private IElementHandler sectionHandler;
	private Stack<StringBuilder> textQueue;
	
	public CharacterHandler(CharacterParser<? extends PlayerCharacter> parser, PlayerCharacter pc) {
		this.parser = parser;
		textQueue = new Stack<StringBuilder>();
		this.pc = pc;
	}
	
	public PlayerCharacter getPlayerCharacter() { return pc; }
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		currentSection = null;
		nextSection = Section.Details;
		((TextStringHandler)TextStringHandler.getInstance()).reset();
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		
		if(nextSection != null && name.equals(nextSection.getName())) {
			currentSection = nextSection;
			nextSection = currentSection.getNext();
			sectionHandler = currentSection.getHandler();
			parser.sectionStart(currentSection.toString());
			if(name.equals("textstring")) {
				sectionHandler.startElement(pc, name, attributes);
			}
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
		
		if(name.equals(currentSection.getName()) && !name.equals("textstring")) {
			sectionHandler = null;
		}
		
		if(sectionHandler != null) {
			sectionHandler.endElement(pc, name, body);
			
			if(currentSection == Section.Rules && name.equals("RulesElement")) {
				pc.add(((RulesElementHandler)sectionHandler).getRule());
			}
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		
		TextStringHandler misc = (TextStringHandler)Section.Misc.getHandler();
		if(misc.getPortraitID() != null) {
			try {
				String url = misc.getCustomPortraitUrl();
				if(url == null)
					url = String.format("id:%d", misc.getPortraitID());
				pc.setPortraitUri(new URI(url));
			} catch (URISyntaxException ex) {
				Logger.error(LOG_TAG, "Could not create URI", ex);
			}
		}
		
		parser.finished(pc);
	}
	
}
