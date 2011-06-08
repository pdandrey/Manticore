package com.ncgeek.manticore.items;

public enum ItemType {
	Alchemical("Alchemical", 1),
	Ammunition("Ammunition", 2),
	Any("Any", 3),
	Armor("Armor", 4),
	ArmsSlotItem("Arms Slot Item", 5),
	Artifact("Artifact", 6),
	BattleScars("Battle Scars", 7),
	CompanionSlotItem("Companion Slot Item", 8),
	Consumable("Consumable", 9),
	DivineBoon("Divine Boon", 10),
	DragonshardAugment("Dragonshard Augment", 11),
	EchoofPower("Echo of Power", 12),
	ElementalGift("Elemental Gift", 13),
	Elixir("Elixir", 14),
	FamiliarSlotItem("Familiar Slot Item", 15),
	FeetSlotItem("Feet Slot Item", 16),
	GloryBoon("Glory Boon", 17),
	GrandmasterTraining("Grandmaster Training", 18),
	HandsSlotItem("Hands Slot Item", 19),
	HeadSlotItem("Head Slot Item", 20),
	HolySymbol("Holy Symbol", 21),
	IntelligentItem("Intelligent Item", 22),
	KiFocus("Ki Focus", 23),
	LegendaryBoon("Legendary Boon", 24),
	MountSlotItem("Mount Slot Item", 25),
	NeckSlotItem("Neck Slot Item", 26),
	Orb("Orb", 27),
	OtherConsumable("Other Consumable", 28),
	Potion("Potion", 29),
	PrimalBlessing("Primal Blessing", 30),
	PsionicTalent("Psionic Talent", 31),
	Reagent("Reagent", 32),
	Ring("Ring", 33),
	Rod("Rod", 34),
	SecretoftheWay("Secret of the Way", 35),
	SorcererKingsBoon("Sorcerer-King's Boon", 36),
	Soulfang("Soulfang", 37),
	Staff("Staff", 38),
	TemplarBrand("Templar Brand", 39),
	Tome("Tome", 40),
	Totem("Totem", 41),
	VeiledAllianceMystery("Veiled Alliance Mystery", 42),
	WaistSlotItem("Waist Slot Item", 43),
	Wand("Wand", 44),
	WanderersSecret("Wanderer's Secret", 45),
	Weapon("Weapon", 46),
	Whetstones("Whetstones", 47),
	WondrousItem("Wondrous Item", 48),
	Gear("Gear", -1)
	;

	public static ItemType forName(String name) {
		for(ItemType item : ItemType.values())
			if(name.equals(item._name))
				return item;
		return null;
	}
	private String _name;
	private int _id;
	ItemType(String name, int id) {
		_name = name;
		_id = id;
	}
	public String getName() { return _name; } 
	public int getID() { return _id; }
}