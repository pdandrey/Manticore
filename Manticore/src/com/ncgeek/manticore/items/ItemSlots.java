package com.ncgeek.manticore.items;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ncgeek.manticore.character.inventory.EquipmentSlot;

public enum ItemSlots {
	Arms("Arms", 7, EquipmentSlot.Arms),
	Companion("Companion", 8),
	Familiar("Familiar", 9),
	Feet("Feet", 10, EquipmentSlot.Feet),
	Hands("Hands", 5, EquipmentSlot.Hands),
	Head("Head", 11, EquipmentSlot.Head),
	KiFocus("Ki Focus", 6, EquipmentSlot.KiFocus),
	Mount("Mount", 12),
	Neck("Neck", 13, EquipmentSlot.Neck),
	MainHand("Main Hand", 18, EquipmentSlot.MainHand),
	OffHand("Off-Hand", 2, false, EquipmentSlot.MainHand, EquipmentSlot.OffHand),
	Ring("Ring", 14, EquipmentSlot.Ring1, EquipmentSlot.Ring2),
	Tattoo("Tattoo", 15, EquipmentSlot.Tattoo),
	OneHand("One-Hand", 3, false, EquipmentSlot.MainHand, EquipmentSlot.OffHand),
	TwoHands("Two-Hands", 4, EquipmentSlot.MainHand, EquipmentSlot.OffHand),
	Waist("Waist", 16, EquipmentSlot.Waist),
	Body("Body", 1, EquipmentSlot.Body),
	HeadandNeck("Head and Neck", 17, EquipmentSlot.Head, EquipmentSlot.Neck)
	;

	public static ItemSlots forName(String name) {
		if(name == null)
			return null;
	
		for(ItemSlots at : ItemSlots.values())
			if(name.equalsIgnoreCase(at._name))
				return at;
		return null;
	}

	private String _name;
	private List<EquipmentSlot> _fills;
	private boolean _fillsAll;
	private int _id;

	ItemSlots(String name, int id, EquipmentSlot...fills) {
		this(name, id, true, fills);
	}
	ItemSlots(String name, int id, boolean fillsAll, EquipmentSlot...fills) {
		_name = name;
		_fillsAll = fillsAll;
		if(fills != null && fills.length > 0)
			_fills = Collections.unmodifiableList(Arrays.asList(fills));
		else {
			_fills = Collections.emptyList();
			_fills = Collections.unmodifiableList(_fills);
		}
		_id = id;
	}
	public String getName() { return _name; }
	public boolean fillsAll() { return _fillsAll; }
	public List<EquipmentSlot> getFills() { return _fills; }
	public int getID() { return _id; }
}