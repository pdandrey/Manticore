package com.ncgeek.manticore.powers;

public enum PowerUsages {
	AtWill("At-Will", 1),
	Daily("Daily", 3),
	Encounter("Encounter", 2);

	public static PowerUsages forName(String name) {
		for(PowerUsages item : PowerUsages.values())
			if(name.equals(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	PowerUsages(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}