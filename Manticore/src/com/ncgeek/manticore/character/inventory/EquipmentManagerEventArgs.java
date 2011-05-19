package com.ncgeek.manticore.character.inventory;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.ncgeek.manticore.items.Item;

public class EquipmentManagerEventArgs {
	
	private EquipmentManagerEventType _type;
	private Item _item;
	private EnumSet<EquipmentSlot> _slot;
	
	public EquipmentManagerEventArgs(EquipmentManagerEventType type, Item item, EnumSet<EquipmentSlot> slots) {
		_type = type;
		_item = item;
		if(slots != null)
			_slot = EnumSet.copyOf(slots);
	}
	
	public EquipmentManagerEventType getType() { return _type; }
	public Item getItem() { return _item; }
	public Set<EquipmentSlot> getSlot() { return _slot == null ? null : Collections.unmodifiableSet(_slot); }
}
