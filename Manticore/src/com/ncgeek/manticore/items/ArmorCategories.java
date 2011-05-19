package com.ncgeek.manticore.items;

public enum ArmorCategories {
	Chain("Chain", 1),
	Cloth("Cloth", 2),
	HeavyShields("Heavy Shields", 3),
	Hide("Hide", 4),
	Leather("Leather", 5),
	LightShields("Light Shields", 6),
	Plate("Plate", 7),
	Scale("Scale", 8);

	public static ArmorCategories forName(String name) {
		for(ArmorCategories item : ArmorCategories.values())
			if(name.equals(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	ArmorCategories(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}