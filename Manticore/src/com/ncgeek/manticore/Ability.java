package com.ncgeek.manticore;

public enum Ability {
	Charisma("Charisma", 6),
	Constitution("Constitution", 2),
	Dexterity("Dexterity", 3),
	Intelligence("Intelligence", 4),
	Strength("Strength", 1),
	Wisdom("Wisdom", 5);

	public static Ability forName(String name) {
		for(Ability item : Ability.values())
			if(name.equalsIgnoreCase(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	Ability(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}