package com.ncgeek.manticore;

public enum RitualType {
	AlchemicalFormula("Alchemical Formula", 1),
	MartialPractice("Martial Practice", 2),
	Ritual("Ritual", 3);

	public static RitualType forName(String name) {
		for(RitualType item : RitualType.values())
			if(name.equalsIgnoreCase(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	RitualType(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}