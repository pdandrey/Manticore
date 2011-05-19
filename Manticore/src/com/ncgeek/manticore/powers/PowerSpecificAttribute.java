package com.ncgeek.manticore.powers;

public final class PowerSpecificAttribute {
	
	private String _name;
	private String _value;
	
	public PowerSpecificAttribute(String name, String value) {
		_name = name;
		_value = value;
	}
	
	public String getName() { return _name; }
	public String getValue() { return _value; }
}
