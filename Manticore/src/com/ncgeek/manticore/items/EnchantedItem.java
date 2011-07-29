package com.ncgeek.manticore.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ncgeek.manticore.Money;
import com.ncgeek.manticore.Source;

public class EnchantedItem extends EquippableItem {

	private final EquippableItem _item;
	private final MagicItem _enchantment;
	
	public EnchantedItem(EquippableItem item, MagicItem enchant) {
		if(item == null)
			throw new IllegalArgumentException("Item cannot be null");
		if(enchant == null)
			throw new IllegalArgumentException("Enchant cannot be null");
		
		_item = item;
		_enchantment = enchant;
	}
	
	public MagicItem getEnchantment() {
		return _enchantment;
	}
	
	public EquippableItem getItem() { return _item; }
	
	public void addSource(Source s) {
		throw new UnsupportedOperationException("Cannot set the source of an enchanted item.");
	}
	public String getDescription() {
		throw new UnsupportedOperationException("not implemented");
	}
	public ItemSlots getItemSlot() {
		return _item.getItemSlot();
	}
	public String getName() {
		return _enchantment.getName() + " " + _item.getName();
	}
	@Override
	public boolean isID(String id) {
		return _item.isID(id) || _enchantment.isID(id);
	}
	public Money getPrice() {
		return _item.getPrice().add(_enchantment.getPrice());
	}
	public List<Source> getSources() {
		List<Source> lst = new ArrayList<Source>(_item.getSources());
		lst.addAll(_enchantment.getSources());
		return Collections.unmodifiableList(lst);
	}
	public double getWeight() {
		return _item.getWeight() + _enchantment.getWeight();
	}
	public void setDescription(String description) {
		throw new UnsupportedOperationException("Cannot set the description of an enchanted item");
	}
	public void setItemSlot(ItemSlots slot) {
		throw new UnsupportedOperationException("Cannot set the item slot of an enchanted item");
	}
	public void setName(String name) {
		throw new UnsupportedOperationException("Cannot set the name of an enchanted item");
	}
	public void setPrice(Money price) {
		throw new UnsupportedOperationException("Cannot set the price of an enchanted item");
	}
	public void setWeight(double weight) {
		throw new UnsupportedOperationException("Cannot set the weight of an enchanted item");
	}	
}
