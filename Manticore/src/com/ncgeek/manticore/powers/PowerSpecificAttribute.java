package com.ncgeek.manticore.powers;

import java.io.Serializable;

public final class PowerSpecificAttribute implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String _name;
	private String _value;
	
	public PowerSpecificAttribute(String name, String value) {
		_name = name;
		_value = value;
	}
	
	public String getName() { return _name; }
	public String getValue() { return _value; }
}
