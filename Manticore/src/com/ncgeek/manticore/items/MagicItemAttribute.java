package com.ncgeek.manticore.items;

public class MagicItemAttribute {

	private String name;
	private String value;
	
	public MagicItemAttribute(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() { return name; }
	public String getValue() { return value; }
}
