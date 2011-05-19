package com.ncgeek.manticore.powers;

public enum PowerActions {
	FreeAction("Free Action", 1),
	ImmediateInterrupt("Immediate Interrupt", 2),
	ImmediateReaction("Immediate Reaction", 3),
	MinorAction("Minor Action", 4),
	MoveAction("Move Action", 5),
	NoAction("No Action", 6),
	OpportunityAction("Opportunity Action", 7),
	StandardAction("Standard Action", 8);

	public static PowerActions forName(String name) {
		for(PowerActions item : PowerActions.values())
			if(name.equals(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	PowerActions(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}