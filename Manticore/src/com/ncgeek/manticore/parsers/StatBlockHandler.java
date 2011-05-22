package com.ncgeek.manticore.parsers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.xml.sax.Attributes;

import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.util.Logger;

public class StatBlockHandler implements IElementHandler  {
	
	private static final IElementHandler instance = new StatBlockHandler();
	public static final IElementHandler getInstance() { return instance; }
	
	private enum Section { Stat, alias, statadd };
	
	private Stat stat;
	
	private StatBlockHandler() {}

	@Override
	public void startElement(PlayerCharacter pc, String name, Attributes attributes) {
		
		Section section = Enum.valueOf(Section.class, name);
		
		switch(section) {
			case Stat:
				stat = new Stat();
				stat.setAbsoluteValue(Integer.parseInt(attributes.getValue("value")));
				break;
				
			case alias:
				stat.addAlias(attributes.getValue("name"));
				break;
				
			case statadd:
				stat.addAddition(parseAddition(attributes));
				break;
		}
	}

	@Override
	public void endElement(PlayerCharacter pc, String name, String body) {
		if(name.equals("Stat")) {
			pc.getStats().add(stat);
		}
	}
	
	private Addition parseAddition(Attributes attrs) {
		Addition add = new Addition();
		
		String s;
		s = attrs.getValue("abilmod");
		add.setAbilityModified(s != null && s.trim().equalsIgnoreCase("true"));
		
		s = attrs.getValue("Level");
		if(s != null) {
			add.setLevel(new Integer(s));
		}
		
		s = attrs.getValue("not-wearing");
		if(s != null)
			add.setNotWearing(s);
		
		s = attrs.getValue("requires");
		if(s != null)
			add.setRequires(s);
		
		s = attrs.getValue("statlink");
		if(s != null)
			add.setStatLink(s);
		
		s = attrs.getValue("type");
		if(s != null)
			add.setType(s);
		
		s = attrs.getValue("wearing");
		if(s != null)
			add.setWearing(s);
		
		s = attrs.getValue("conditional");
		if(s != null)
			add.setConditional(s);
		
		add.setValue(Integer.parseInt(attrs.getValue("value")));
		
		Set<String> known = new HashSet<String>(Arrays.asList(new String[]{"abilmod", "Level", "not-wearing", "requires", "statlink", "type", "wearing", "conditional", "value", "charelem"}));
		for(int i = 0; i<attrs.getLength(); ++i) {
			String name = attrs.getLocalName(i);
			if(!known.contains(name)) {
				Logger.warn(CharacterParser.LOG_TAG, "Found unknown attribute " + name);
				add.setAdditionalAttribute(name, attrs.getValue(i));
			}
		}
		
		return add;
	}
	
}
