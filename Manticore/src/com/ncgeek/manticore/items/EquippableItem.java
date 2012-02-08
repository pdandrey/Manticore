package com.ncgeek.manticore.items;

import java.io.Serializable;

public class EquippableItem extends Item implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ItemSlots _itemSlot;
	
	public ItemSlots getItemSlot() { return _itemSlot;}
	public void setItemSlot(ItemSlots slot) { 
		_itemSlot = slot; 
	}
}
