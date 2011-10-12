package com.ncgeek.manticore.items;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.ncgeek.manticore.Dice;

public class Weapon extends EquippableItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ItemSlots _additionalSlot; 
	private Dice _dice;
	private boolean _isTwoHanded;
	private int _profenciencyBonus;
	private Range _range;
	private WeaponCategories _category;
	private EnumSet<WeaponProperties> _properties;
	private EnumSet<WeaponGroups> _groups;
	
	public ItemSlots getAdditionalSlot() { return _additionalSlot; }
	public void setAdditionalSlot(ItemSlots slot) { _additionalSlot = slot; }
	
	public Dice getDice() { return _dice; }
	public void setDice(Dice dice) { _dice = dice; }
	
	public boolean isTwoHanded() { return _isTwoHanded; }
	public void setTwoHanded(boolean usesTwoHands) { _isTwoHanded = usesTwoHands;}
	
	public int getProficiencyBonus() { return _profenciencyBonus; }
	public void setProficiencyBonus(int bonus) { _profenciencyBonus = bonus; }
	
	public boolean isRangedWeapon() { return _range != null && _range.getRange1() > 0; }
	public Range getRange() { return _range; }
	public void setRange(Range range) { _range = range; }
	
	public WeaponCategories getCategory() { return _category; }
	public void setCategory(WeaponCategories category) { _category = category; }
	
	public boolean hasProperty(WeaponProperties property) { 
		return _properties != null && _properties.contains(property); 
	}
	public Set<WeaponProperties> getProperties() {
		if(_properties == null)
			return Collections.unmodifiableSet(EnumSet.noneOf(WeaponProperties.class));
		else
			return Collections.unmodifiableSet(_properties); 
	}
	public void addProperty(WeaponProperties property) { 
		if(_properties == null)
			_properties = EnumSet.noneOf(WeaponProperties.class);
		_properties.add(property);
	}
	
	public boolean isInGroup(WeaponGroups group) { 
		return _groups != null && _groups.contains(group);
	}
	public Set<WeaponGroups> getGroups() { 
		if(_groups == null)
			return Collections.unmodifiableSet(EnumSet.noneOf(WeaponGroups.class));
		else
			return Collections.unmodifiableSet(_groups); 
	}
	public void addGroup(WeaponGroups group) {
		if(group == null)
			throw new IllegalArgumentException("cannot add null group");
		if(_groups == null)
			_groups = EnumSet.noneOf(WeaponGroups.class);
		_groups.add(group);
	}
}
