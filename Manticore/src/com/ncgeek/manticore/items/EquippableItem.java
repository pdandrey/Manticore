package com.ncgeek.manticore.items;

public class EquippableItem extends Item {
	
	private ItemSlots _itemSlot;
	
	public ItemSlots getItemSlot() { return _itemSlot;}
	public void setItemSlot(ItemSlots slot) { 
		_itemSlot = slot; 
	}
}
