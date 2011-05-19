package com.ncgeek.manticore.character;

public enum Alignment {
	LawfulGood("Lawful Good"),
	Good("Good"),
	Evil("Evil"),
	ChaoticEvil("Chaotic Evil"),
	Unaligned("Unaligned");
	
	public static Alignment forName(String name) {
		for(Alignment item : Alignment.values())
			if(name.equalsIgnoreCase(item._display))
				return item;
		return null;
	}
	
	private String _display;
	
	Alignment(String display) { _display = display; }
	
	public String getDisplay() { return _display; }
}
