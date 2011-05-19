package com.ncgeek.manticore.character.inventory;

public enum EquipmentSlot {
	
	Arms,
	Feet,
	Hands,
	Head,
	KiFocus,
	Neck,
	MainHand,
	OffHand,
	Ring1,
	Ring2,
	Tattoo,
	Waist,
	Body;
	
	public static EquipmentSlot forName(String name) {
		name = name.replace(" ", "");
		for(EquipmentSlot es : EquipmentSlot.values()) {
			if(es.name().equalsIgnoreCase(name))
				return es;
		}
		
		if(name.equals("ring"))
			return Ring1;
		
		return null;
	}
}
