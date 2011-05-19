package com.ncgeek.manticore.items;

public enum WeaponCategories {
	ImprovisedMelee("Improvised Melee", 4),
	MilitaryMelee("Military Melee", 2),
	MilitaryRanged("Military Ranged", 6),
	SimpleMelee("Simple Melee", 1),
	SimpleRanged("Simple Ranged", 5),
	SuperiorMelee("Superior Melee", 3),
	SuperiorRanged("Superior Ranged", 7);

	public static WeaponCategories forName(String name) {
		for(WeaponCategories item : WeaponCategories.values())
			if(name.equals(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	WeaponCategories(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}