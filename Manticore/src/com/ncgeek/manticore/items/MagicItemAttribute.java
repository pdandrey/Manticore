package com.ncgeek.manticore.items;

import java.io.Serializable;

public class MagicItemAttribute implements Serializable {
	
	private static final long serialVersionUID = 1L;
	

	private String name;
	private String value;
	
	public MagicItemAttribute(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() { return name; }
	public String getValue() { return value; }
}
