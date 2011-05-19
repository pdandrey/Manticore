package com.ncgeek.manticore.character.stats;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Stat {
	
	private List<String> _aliases;
	private List<Addition> _additions;
	private int _setValue;
	
	public Stat() {
		this(new ArrayList<String>(), new ArrayList<Addition>());
	}
	
	public Stat(List<String> aliasList, List<Addition> additionList) {
		_aliases = aliasList;
		_additions = additionList;
	}
	
	public void addAlias(String alias) {
		if(alias == null)
			throw new IllegalArgumentException("null alias");
		
		alias = alias.trim();
		
		if(alias.length() == 0)
			throw new IllegalArgumentException("empty alias");
		
		int index = Collections.binarySearch(_aliases, alias);
		
		if(index < 0)
			_aliases.add(-index - 1, alias);
	}
	
	public List<String> getAliases() {
		return Collections.unmodifiableList(_aliases);
	}
	
	public void addAddition(Addition addition) {
		if(!_additions.contains(addition))
			_additions.add(addition);
	}
	
	public List<Addition> getAdditions() {
		return Collections.unmodifiableList(_additions);
	}
	
	public List<Addition> getAppliedAdditions() {
		HashMap<String, Addition> mapTypes = new HashMap<String,Addition>();
		List<Addition> lst = new ArrayList<Addition>();
		
		for(Addition a : _additions) {
			String type = a.getType();
			
			if(a.shouldApply()) {
				if(type == null) {
					lst.add(a);
				} else {
					if(!mapTypes.containsKey(type) || mapTypes.get(type).getValue() < a.getValue()) {
						mapTypes.put(type, a);
					}
				}
			}
		}
		lst.addAll(mapTypes.values());
		return Collections.unmodifiableList(lst);
	}
	
	public int getAbsoluteValue() {
		return _setValue;
	}
	
	public void setAbsoluteValue(int value) {
		_setValue = value;
	}
	
	public int getCalculatedValue() {
		int sum = 0;
		for(Addition a : getAppliedAdditions())
			sum += a.getValue();
		return sum;
	}
	
	public int getModifier() {
		return _setValue / 2 - 5;
	}
	
	public boolean equals(String name) {
		if(name == null || name.trim().length() == 0)
			return false;
		
		for(String s : _aliases)
			if(name.equalsIgnoreCase(s))
				return true;
		return false;
	}
}
