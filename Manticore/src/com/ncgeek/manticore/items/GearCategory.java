package com.ncgeek.manticore.items;

public enum GearCategory {
	Ammunition("Ammunition", 1),
	Component("Component", 2),
	Drink("Drink", 3),
	Food("Food", 4),
	Gear("Gear", 5),
	Lodging("Lodging", 6),
	Mount("Mount", 7),
	MusicalInstrument("Musical Instrument", 8),
	Service("Service", 9),
	Transportation("Transportation", 10);

	public static GearCategory forName(String name) {
		for(GearCategory item : GearCategory.values())
			if(name.equals(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	GearCategory(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}