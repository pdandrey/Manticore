package com.ncgeek.manticore.items;

public class Gear extends EquippableItem {
	
	private GearCategory _category;
	private int _count;
	
	public GearCategory getCategory() { return _category; }
	public void setCategory(GearCategory category) { _category = category; }
	
	public int getCount() { return _count; }
	public void setCount(int count) { 
		if(count <= 0)
			throw new IllegalArgumentException("Count cannot be <= 0");
		_count = count;
	}
}
