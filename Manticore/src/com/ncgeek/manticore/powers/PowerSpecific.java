package com.ncgeek.manticore.powers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class PowerSpecific {
	
	private PowerUsages _usage;
	private boolean _isSpecial;
	private List<PowerSpecificAttribute> _attributes;
	private EnumSet<PowerKeywords> _keywords;
	private EnumSet<PowerKeywords> _alternativeKeywords;
	private PowerAttack _attack;
	
	public PowerSpecific(List<PowerSpecificAttribute> lstAttr, EnumSet<PowerKeywords> lstKeywords, EnumSet<PowerKeywords> lstAltKeywords) {
		_usage = null;
		_isSpecial = false;
		_attributes = lstAttr;
		_keywords = lstKeywords;
		_alternativeKeywords = lstAltKeywords;
		_attack = null;
	}
	
	public PowerSpecific() {
		this(new ArrayList<PowerSpecificAttribute>(), EnumSet.noneOf(PowerKeywords.class), null);
	}
	
	public PowerUsages getUsage() { return _usage; }
	public void setUsage(PowerUsages usage) { _usage = usage; }
	
	public boolean isSpecial() { return _isSpecial; }
	public void setSpecial(boolean special) { _isSpecial = special; }
	
	public List<PowerSpecificAttribute> getAttributes() {
		 return Collections.unmodifiableList(_attributes);
	}
	public void add(PowerSpecificAttribute attr)  { 
		_attributes.add(attr);
	}
	
	public Set<PowerKeywords> getKeywords()  { 
		return Collections.unmodifiableSet(_keywords);
	}
	public Set<PowerKeywords> getAlternativeKeywords()  { 
		if(_alternativeKeywords == null)
			return null;
		else
			return Collections.unmodifiableSet(_alternativeKeywords);
	}
	public void add(PowerKeywords keyword) {
		  add(keyword, false);
	}
	public void add(PowerKeywords keyword, boolean isAlt) {
		if(isAlt) {
			if(_alternativeKeywords == null)
				_alternativeKeywords = EnumSet.noneOf(PowerKeywords.class);
			_alternativeKeywords.add(keyword);
		} else {
			_keywords.add(keyword);
		}
	}
	
	public PowerAttack getAttack() { return _attack; }
	public void setAttack(PowerAttack attack) { _attack = attack; }
}
