package com.ncgeek.manticore.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;

import com.ncgeek.manticore.character.PlayerCharacter;

public class TextStringHandler implements IElementHandler {

	private static final IElementHandler instance = new TextStringHandler();
	public static final IElementHandler getInstance() { return instance; }
	
	private static final Pattern PATTERN_CONTENTID = Pattern.compile("<ContentID>(\\d+)</ContentID>");
	
	private TextStringHandler() {}
	
	private String name;
	
	private Integer portraitID;
	
	public Integer getPortraitID() { return portraitID; }
	
	@Override
	public void startElement(PlayerCharacter pc, String nodeName, Attributes attributes) {
		this.name = attributes.getValue("name");
	}

	@Override
	public void endElement(PlayerCharacter pc, String nodeName, String body) {
		
		if(body != null) {
			body = body.trim();
			if(body.length() == 0)
				body = null;
		}
	
		if(body == null) {
			name = null;
			return;
		}
		
		if(name.equalsIgnoreCase("PortraitID")) {
			Matcher m = PATTERN_CONTENTID.matcher(body);
			if(m.find()) {
				portraitID = new Integer(m.group(1));
			} else {
				portraitID = null;
			}
		}
		
		name = null;
	}

}
