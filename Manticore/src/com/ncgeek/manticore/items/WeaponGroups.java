package com.ncgeek.manticore.items;

public enum WeaponGroups {
	Axe("Axe", 1),
	Blowgun("Blowgun", 2),
	Bow("Bow", 3),
	Crossbow("Crossbow", 4),
	Flail("Flail", 5),
	Garrote("Garrote", 6),
	Hammer("Hammer", 7),
	HeavyBlade("Heavy Blade", 8),
	LightBlade("Light Blade", 9),
	Mace("Mace", 10),
	Pick("Pick", 11),
	Polearm("Polearm", 12),
	Sling("Sling", 13),
	Spear("Spear", 14),
	Staff("Staff", 15),
	Unarmed("Unarmed", 16);

	public static WeaponGroups forName(String name) {
		for(WeaponGroups item : WeaponGroups.values())
			if(name.equalsIgnoreCase(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	WeaponGroups(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}