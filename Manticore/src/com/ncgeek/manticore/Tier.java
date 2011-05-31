package com.ncgeek.manticore;

public enum Tier {
	Epic("Epic", 3),
	Heroic("Heroic", 1),
	Paragon("Paragon", 2);

	public static Tier forName(String name) {
		for(Tier item : Tier.values())
			if(name.equalsIgnoreCase(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	Tier(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}