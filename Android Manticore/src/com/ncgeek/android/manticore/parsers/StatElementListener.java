package com.ncgeek.android.manticore.parsers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.ElementListener;
import android.sax.StartElementListener;

import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.util.Logger;

class StatElementListener implements ElementListener {
	
	private Stat stat;
	private final PlayerCharacter _pc;
	
	public StatElementListener(Element xmlStat, PlayerCharacter pc) {
		_pc = pc;
		Element alias = xmlStat.getChild("alias");
		Element statadd = xmlStat.getChild("statadd");
		
		xmlStat.setElementListener(this);
		alias.setStartElementListener(aliasListener);
		statadd.setStartElementListener(stataddListener);
	}
	
	@Override
	public void start(Attributes attributes) {
		stat = new Stat();
		stat.setAbsoluteValue(Integer.parseInt(attributes.getValue("value").trim()));
	}
	
	@Override
	public void end() {
		_pc.getStats().add(stat);
		stat = null;
	}
	
	private StartElementListener aliasListener = new StartElementListener() {
		public void start(Attributes attributes) {
			stat.addAlias(attributes.getValue("name"));
		}
	};
	
	private StartElementListener stataddListener = new StartElementListener() {
		public void start(Attributes attrs) {
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
			
			stat.addAddition(add);
		}
	};
}