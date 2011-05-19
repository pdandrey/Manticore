package com.ncgeek.manticore.character.inventory;

import java.io.Serializable;

import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Item;

public class ItemStack implements Serializable {

	private static final long serialVersionUID = 1L;
	private Item _item;
	private int _count;
	private int _equipCount;
	
	public ItemStack(Item item) {
		if(item == null)
			throw new IllegalArgumentException("item cannot be null");
		_item = item;
		_count  = 1;
		_equipCount = 0;
	}
	
	public Item getItem() { return _item; }
	
	public int getCount() { return _count; }
	public void setCount(int count)  { 
		if(count < 0)
			throw new IllegalArgumentException("Cannot set count < 0");
		_count = count;
	}
	public void increment() {
		++_count;
	}
	public void decrement() {
		if(_count <= 0)
			throw new UnsupportedOperationException("There are no items to decriment");
		--_count;
	}
	
	public int getEquippedCount() { return _equipCount; }
	public void setEquippedCount(int count) { 
		if(!(_item instanceof EquippableItem))
			throw new UnsupportedOperationException("Item is not equippable");
		if(count < 0)
			throw new IllegalArgumentException("Cannot set equipped count < 0");
		if(count > _count)
			throw new IllegalArgumentException("No more items to equip");
		_equipCount = count;
	}
	public void equip() {
		setEquippedCount(_equipCount + 1);
	}
	public void unequip() {
		setEquippedCount(_equipCount - 1);
	}
	
	@Override
	public int hashCode() {
		return _item.hashCode();
	}
	
	@Override
	public String toString() {
		String equipped = "";
		if(_equipCount > 0)
			equipped = String.format(", %d equipped", _equipCount);
		return String.format("%s x%d%s", _item.getName(), _count, equipped);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null)
			return false;
		if(other instanceof ItemStack)
			return _item.equals(((ItemStack)other)._item);
		if(other instanceof Item)
			return _item.equals((Item)other);
		return false;
	}
}
