package com.ncgeek.manticore;

public enum RitualCategory {
	Binding("Binding", 1),
	Creation("Creation", 2),
	Curative("Curative", 3),
	Deception("Deception", 4),
	Divination("Divination", 5),
	Exploration("Exploration", 6),
	MartialPractice("Martial Practice", 7),
	Oil("Oil", 8),
	Other("Other", 9),
	Poison("Poison", 10),
	Restoration("Restoration", 11),
	Scrying("Scrying", 12),
	Teleportation("Teleportation", 13),
	Travel("Travel", 14),
	Volatile("Volatile", 15),
	Warding("Warding", 16);

	public static RitualCategory forName(String name) {
		for(RitualCategory item : RitualCategory.values())
			if(name.equalsIgnoreCase(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	RitualCategory(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}