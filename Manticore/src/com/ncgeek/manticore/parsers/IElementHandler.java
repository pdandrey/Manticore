package com.ncgeek.manticore.parsers;

import org.xml.sax.Attributes;

import com.ncgeek.manticore.character.PlayerCharacter;

public interface IElementHandler {
	
	public abstract void startElement(PlayerCharacter pc, String name, Attributes attributes);
	public abstract void endElement(PlayerCharacter pc, String name, String body);
}
