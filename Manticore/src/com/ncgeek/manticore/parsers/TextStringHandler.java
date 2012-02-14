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
	private String customPortraitUrl;
	private Integer portraitID;
	
	public Integer getPortraitID() { return portraitID; }
	public String getCustomPortraitUrl() { return customPortraitUrl; }
	
	public void reset() {
		portraitID = 0;
		customPortraitUrl = null;
	}
	
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
		} else if(name.equalsIgnoreCase("Custom Character Portrait")) {
			customPortraitUrl = body.trim();
		}
		
		name = null;
	}

}
