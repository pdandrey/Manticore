package com.ncgeek.manticore.powers;

public enum AttackTypes {
	AreaBurst("Area Burst", 1),
	AreaWall("Area Wall", 2),
	CloseBlast("Close Blast", 3),
	CloseBurst("Close Burst", 4),
	CloseWall("Close Wall", 5),
	Melee("Melee", 6),
	Personal("Personal", 7),
	Ranged("Ranged", 8),
	Touch("Touch", 9),
	Varies("Varies", 10);

	public static AttackTypes forName(String name) {
		for(AttackTypes item : AttackTypes.values())
			if(name.equals(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	AttackTypes(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}