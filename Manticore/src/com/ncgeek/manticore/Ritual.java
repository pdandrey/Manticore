package com.ncgeek.manticore;

import java.io.Serializable;
import java.util.List;

import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.Specific;
import com.ncgeek.manticore.util.Logger;

public class Ritual implements Serializable, Comparable<Ritual> {
	
	private static final long serialVersionUID = 1L;

	public static Ritual fromRule(Rule rule) {
		if(rule == null)
			throw new IllegalArgumentException("Rule cannot be null");
		
		List<Specific> specs = rule.getSpecifics();
		if(specs == null || specs.size() == 0)
			throw new IllegalArgumentException("Cannot parse a rule that does not have specifics");
		
		Ritual r = new Ritual();
		r.setName(rule.getName());
		r.setDescription(rule.getBody());
		
		for(Specific s : specs) {
			String name = s.getName();
			if(name.equals("Flavor"))
				r.setFlavor(s.getValue());
			else if(name.equals("Category"))
				r.setCategory(RitualCategory.forName(s.getValue()));
			else if(name.equals("Key Skill"))
				r.setKeySkills(s.getValue());
			else if(name.equals("Component Cost"))
				r.setComponentCost(s.getValue());
			else if(name.equals("Duration"))
				r.setDuration(s.getValue());
			else if(name.equals("Level"))
				r.setLevel(Integer.parseInt(s.getValue()));
			else if(name.equals("Time"))
				r.setTime(s.getValue());
			else if(name.equals("Market Price")) 
				r.setMarketPrice(new Money(s.getValue()));
			else if(name.equals("Prerequisite"))
				r.setPrerequisite(s.getValue());
			else if(name.equals("Type"))
				r.setType(RitualType.forName(s.getValue()));
			else
				Logger.warn("Ritual", "Unknown specific: " + name);
		}
		
		return r;
	}
	
	private String name;
	private String flavor;
	private RitualCategory category;
	private String componentCost;
	private int level;
	private String time;
	private Money marketPrice;
	private RitualType type;
	private String duration;
	private String prerequisite;
	private String description;
	private String keySkills;
	
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final String getFlavor() {
		return flavor;
	}
	public final void setFlavor(String flavor) {
		this.flavor = flavor;
	}
	public final RitualCategory getCategory() {
		return category;
	}
	public final void setCategory(RitualCategory category) {
		this.category = category;
	}
	public final String getComponentCost() {
		return componentCost;
	}
	public final void setComponentCost(String componentCost) {
		this.componentCost = componentCost;
	}
	public final int getLevel() {
		return level;
	}
	public final void setLevel(int level) {
		this.level = level;
	}
	public final String getTime() {
		return time;
	}
	public final void setTime(String time) {
		this.time = time;
	}
	public final Money getMarketPrice() {
		return marketPrice;
	}
	public final void setMarketPrice(Money marketPrice) {
		this.marketPrice = marketPrice;
	}
	public final RitualType getType() {
		return type;
	}
	public final void setType(RitualType type) {
		this.type = type;
	}
	public final String getDuration() {
		return duration;
	}
	public final void setDuration(String duration) {
		this.duration = duration;
	}
	public final String getPrerequisite() {
		return prerequisite;
	}
	public final void setPrerequisite(String prerequisite) {
		this.prerequisite = prerequisite;
	}
	public final String getDescription() {
		return description;
	}
	public final void setDescription(String description) {
		this.description = description;
	}
	public final String getKeySkills() {
		return keySkills;
	}
	public final void setKeySkills(String keySkills) {
		this.keySkills = keySkills;
	}
	@Override
	public int compareTo(Ritual other) {
		return getName().compareToIgnoreCase(other.getName());
	}
	
}
