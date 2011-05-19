package com.ncgeek.manticore.items;

public enum ArmorTypes {
	Heavy("Heavy", 1),
	Light("Light", 2),
	Shield("Shield", 3);

	public static ArmorTypes forName(String name) {
		for(ArmorTypes item : ArmorTypes.values())
			if(name.equalsIgnoreCase(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	ArmorTypes(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}