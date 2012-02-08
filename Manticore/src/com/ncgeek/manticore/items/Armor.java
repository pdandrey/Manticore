package com.ncgeek.manticore.items;

import java.io.Serializable;

public class Armor extends EquippableItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int _checkPenalty;
	private int _speedPenalty;
	private ArmorTypes _armorType;
	private ArmorCategories _armorCategory;
	private int _bonus;
	private int _minEnhancementBonus;
	private String _special;
	
	public int getCheckPenalty() { return _checkPenalty; }
	public void setCheckPenalty(int penalty) { _checkPenalty = penalty; }
	
	public int getSpeedPenalty() { return _speedPenalty; }
	public void setSpeedPenalty(int penalty) { _speedPenalty = penalty; }
	
	public ArmorTypes getArmorType() { return _armorType; }
	public void setArmorType(ArmorTypes type) { 
		if(type == null)
			throw new IllegalArgumentException("Type cannot be null");
		_armorType = type;
	}
	
	public ArmorCategories getArmorCategory() { return _armorCategory; }
	public void setArmorCategory(ArmorCategories category) { _armorCategory = category; }
	
	public int getBonus() { return _bonus; }
	public void setBonus(int bonus) { _bonus = bonus; }
	
	public String getSpecial() { return _special; }
	public void setSpecial(String special) { _special = special; }
	
	public int getMinEnhancementBonus() { return _minEnhancementBonus; }
	public void setMinEnhancementBonus(int min) { 
		if(min < 0)
			throw new IllegalArgumentException("Min cannot be less than 0");
		_minEnhancementBonus = min;
	}
}
