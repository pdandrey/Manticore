package com.ncgeek.manticore.items;

public enum MagicItemRarity {
	Common("Common", 1),
	Rare("Rare", 2),
	Uncommon("Uncommon", 3);

	public static MagicItemRarity forName(String name) {
		for(MagicItemRarity item : MagicItemRarity.values())
			if(name.equals(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	MagicItemRarity(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}