package com.ncgeek.manticore.powers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Power implements Serializable {

	private static final long serialVersionUID = 3L;
	private static final Pattern REGEX_ATTACK_TYPE = Pattern.compile("^Area (?:Burst|Wall)|Close (?:Burst|Blast|Wall)|Melee|Ranged|Personal|Touch|Varies");

	private String name;
	private int level;
	private String keywords;	
	private String flavor; 		
	private String display; 	
	private String range;		
	private PowerUsages usage; 	
	private String target;		
	private String toHit;		
	private PowerActions action;
	private AttackTypes attackType;
	private PowerTypes powerType;
	
	private List<Specific> lstSpecifics;
	
	public Power(List<Specific> lstSpecifics) {
		this.lstSpecifics = lstSpecifics;
		level = 1;
	}
	
	public Power() {
		this(new ArrayList<Specific>());
	}
	
	public void setName(String name) { this.name = name; }
	public String getName() { return name; }
	
	public void addSpecific(String name, String value) {
		String comp = name.toLowerCase();
		value = value.trim();
		if(comp.equals("flavor")) {
			flavor = value;
		} else if(comp.equals("power usage")) {
			usage = PowerUsages.forName(value);
		} else if(comp.equals("display")) {
			display = value;
		} else if(comp.equals("keywords")) {
			keywords = value;
		} else if(comp.equals("action type")) {
			action = PowerActions.forName(value);
		} else if(comp.equals("attack type")) {
			range = value;
			Matcher m = REGEX_ATTACK_TYPE.matcher(value);
			if(m.find()) {
				String type = m.group();
				attackType = AttackTypes.forName(type);
			}
		} else if(comp.equals("target")) {
			target = value;
		} else if(comp.equals("attack")) {
			toHit = value;
		} else if(comp.equals("level")) {
			try {
				level = Integer.parseInt(value);
			} catch(NumberFormatException nfex) {}
		} else if(comp.equals("class")) {
			// ignore
		} else if(comp.equals("power type")) {
			powerType = PowerTypes.forName(value);
		} else{
			Specific s = new Specific(name, value);
			if(!lstSpecifics.contains(s))
				lstSpecifics.add(s);
		}
	}
	public List<Specific> getSpecifics() {
		return Collections.unmodifiableList(lstSpecifics);
	}
	
	
	public static class Specific implements Serializable {
		private static final long serialVersionUID = 1L;
		private static final Pattern WHITESPACE_REGEX = Pattern.compile("^\\s+");
		private String name;
		private String value;
		private int level;
		
		public Specific(String name, String value) {
			this.name = name.trim();
			this.value = value;
			Matcher m = WHITESPACE_REGEX.matcher("Test");
			if(m.find()) {
				String ws = m.group();
				level = ws.length();
			} else {
				level = 0;
			}
		}
		
		public String getName() { return name; }
		public String getValue() { return value; }
		public int getLevel() { return level; }
		
		@Override
		public boolean equals(Object other) {
			if(other instanceof Specific) {
				Specific s = (Specific)other;
				return name.equals(s.name) && value.equals(s.value) && level == s.level;
			} else {
				return false;
			}
		}
	}

	public final int getLevel() { return level; }

	public final String getKeywords() { return keywords; }

	public final String getFlavor() { return flavor; }

	public final String getDisplay() { return display; }

	public final String getRange() { return range; }

	public final PowerUsages getUsage() { return usage; }

	public final String getTarget() { return target; }

	public final String getToHit() { return toHit; }

	public final PowerActions getAction() { return action; }
	
	public final AttackTypes getAttackType() { return attackType; }
	
	public final PowerTypes getPowerType() { return powerType; }
}
