package com.ncgeek.manticore.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MagicItem extends EquippableItem {
	
	private String _flavor;
	private MagicItemRarity _rarity;
	private ItemType _type;
	private int _level;
	private int _gold;
	private String _powers;
	private String _enhancement;
	private String _critical;
	private ItemSlots _slot;
	
	private List<String> _properties;
	private List<MagicItemTarget> _targets;
	private List<MagicItemAttribute> _attributes;
	
	public MagicItem(List<String> properties, List<MagicItemTarget> targets, List<MagicItemAttribute> attrs) {
		_properties = properties;
		_targets = targets;
		_attributes = attrs;
	}
	
	public MagicItem() {
		this(null, null, null);
	}

	public String getFlavor() {
		return _flavor;
	}

	public void setFlavor(String flavor) {
		_flavor = flavor;
	}

	public MagicItemRarity getRarity() {
		return _rarity;
	}

	public void setRarity(MagicItemRarity rarity) {
		_rarity = rarity;
	}

	public ItemType getType() {
		return _type;
	}

	public void setType(ItemType type) {
		_type = type;
	}

	public int getLevel() {
		return _level;
	}

	public void setLevel(int level) {
		_level = level;
	}

	public int getGold() {
		return _gold;
	}

	public void setGold(int gold) {
		_gold = gold;
	}

	public String getPowers() {
		return _powers;
	}

	public void setPowers(String powers) {
		_powers = powers;
	}

	public String getEnhancement() {
		return _enhancement;
	}

	public void setEnhancement(String enhancement) {
		_enhancement = enhancement;
	}

	public String getCritical() {
		return _critical;
	}

	public void setCritical(String critical) {
		_critical = critical;
	}

	public ItemSlots getSlot() {
		return _slot;
	}

	public void setSlot(ItemSlots slot) {
		_slot = slot;
	}

	public List<String> getProperties() {
		return Collections.unmodifiableList(_properties);
	}
	
	public void add(String property) {
		if(_properties == null)
			_properties = new ArrayList<String>();
		_properties.add(property);
	}

	public List<MagicItemTarget> getTargets() {
		return Collections.unmodifiableList(_targets);
	}
	
	public void add(MagicItemTarget target) {
		if(_targets == null)
			_targets = new ArrayList<MagicItemTarget>();
		_targets.add(target);
	}

	public void add(MagicItemAttribute magicItemAttribute) {
		if(_attributes == null)
			_attributes = new ArrayList<MagicItemAttribute>();
		_attributes.add(magicItemAttribute);
	}
	
	public List<MagicItemAttribute> getAttributes() {
		if(_attributes == null)
			return Collections.emptyList();
		else
			return Collections.unmodifiableList(_attributes);
	}
}
