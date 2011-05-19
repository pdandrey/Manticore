package com.ncgeek.manticore.powers;

public enum PowerTypes {
	Attack("Attack", 2),
	Feature("Feature", 3),
	PactBoon("Pact Boon", 4),
	Utility("Utility", 1);

	public static PowerTypes forName(String name) {
		for(PowerTypes item : PowerTypes.values())
			if(name.equals(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	PowerTypes(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}