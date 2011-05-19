package com.ncgeek.manticore.powers;

public enum PowerKeywords {
	Acid("Acid", 1),
	Arcane("Arcane", 2),
	Augmentable("Augmentable", 3),
	Aura("Aura", 4),
	Beast("Beast", 5),
	BeastForm("Beast Form", 6),
	ChannelDivinity("Channel Divinity", 7),
	Charm("Charm", 8),
	Cold("Cold", 9),
	Conjuration("Conjuration", 10),
	Divine("Divine", 11),
	Enchantment("Enchantment", 12),
	Evocation("Evocation", 13),
	Fear("Fear", 14),
	Fire("Fire", 15),
	FireorLightning("Fire or Lightning", 16),
	Force("Force", 17),
	ForceorLightning("Force or Lightning", 18),
	FullDiscipline("Full Discipline", 19),
	Healing("Healing", 20),
	Illusion("Illusion", 21),
	Implement("Implement", 22),
	Invigorating("Invigorating", 23),
	Lightning("Lightning", 24),
	Martial("Martial", 25),
	Necrotic("Necrotic", 26),
	Paralysis("Paralysis", 27),
	Poison("Poison", 28),
	Polymorph("Polymorph", 29),
	Primal("Primal", 30),
	Psionic("Psionic", 31),
	Psychic("Psychic", 32),
	Radiant("Radiant", 33),
	Rage("Rage", 34),
	Rattling("Rattling", 35),
	Reliable("Reliable", 36),
	Runic("Runic", 37),
	Shadow("Shadow", 38),
	Sleep("Sleep", 39),
	Special("Special", 40),
	Spirit("Spirit", 41),
	Stance("Stance", 42),
	Summoning("Summoning", 43),
	Teleportation("Teleportation", 44),
	Thunder("Thunder", 45),
	Transmutation("Transmutation", 46),
	Varies("Varies", 47),
	Weapon("Weapon", 48),
	Zone("Zone", 49);

	public static PowerKeywords forName(String name) {
		for(PowerKeywords item : PowerKeywords.values())
			if(name.equals(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	PowerKeywords(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}