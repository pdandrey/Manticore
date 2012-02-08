package com.ncgeek.manticore.rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Specific implements Serializable {

	private static final long serialVersionUID = 1L;
	private String _name;
	private String _value;
	private List<Specific> _children;
	
	public Specific(String name, String value, List<Specific> childrenSpecifics) {
		if(name == null)
			throw new IllegalArgumentException("name cannot be null");
		if(value == null)
			value = "";
		name = name.trim();
		value = value.trim();
		
		if(name.length() == 0)
			throw new IllegalArgumentException("name cannot be empty");
		
		_name = name;
		_value = value;
		_children = childrenSpecifics;
	}
	
	public Specific(String name, String value) {
		this(name, value, null);
	}
	
	public String getName() { return _name; }
	
	public String getValue() { return _value; }
	
	public int toInt() { return toInt(false); }
	public Integer toInt(boolean allowNull) {
		try {
			return new Integer(_value);
		} catch(NumberFormatException nfe) {
			if(allowNull)
				return null;
			else
				throw nfe;
		}
	}
	
	public double toDouble() { return toDouble(false); }
	public Double toDouble(boolean allowNull) {
		try {
			return new Double(_value);
		} catch(NumberFormatException nfe) {
			if(allowNull)
				return null;
			else
				throw nfe;
		}
	}
	
	public List<Specific> getChildren() {
		if(_children == null)
			return null;
		return Collections.unmodifiableList(_children);
	}
	
	public void add(Specific child) {
		if(_children == null)
			_children = new ArrayList<Specific>(2);
		_children.add(child);
	}
}
