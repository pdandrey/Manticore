package com.ncgeek.manticore.items;

public enum WeaponProperties {
	Brutal("Brutal", 1),
	Defensive("Defensive", 2),
	HeavyThrown("Heavy Thrown", 3),
	HighCrit("High Crit", 4),
	LightThrown("Light Thrown", 5),
	LoadFree("Load Free", 6),
	LoadMinor("Load Minor", 7),
	OffHand("Off-Hand", 8),
	Reach("Reach", 9),
	Small("Small", 10),
	Stout("Stout", 11),
	Versatile("Versatile", 12);

	public static WeaponProperties forName(String name) {
		for(WeaponProperties item : WeaponProperties.values())
			if(name.equals(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	WeaponProperties(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}