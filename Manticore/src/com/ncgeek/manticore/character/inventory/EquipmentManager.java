package com.ncgeek.manticore.character.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Item;

public class EquipmentManager extends Observable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<ItemStack> _items;
	private Map<EquipmentSlot, EquippableItem> _equippedItems;
	
	public EquipmentManager(List<ItemStack> list, Map<EquipmentSlot,EquippableItem> map) {
		_items = list;
		_equippedItems = map;
	}
	
	public EquipmentManager() {
		this(new ArrayList<ItemStack>(), new EnumMap<EquipmentSlot, EquippableItem>(EquipmentSlot.class));
	}
	
	public void add(Item item) {
		add(item, 1);
	}
	public void add(Item item, int count) {
		if(item == null)
			throw new IllegalArgumentException("Cannot add a null item");
		
		int index = _items.indexOf(item);
		if(index == -1) {
			ItemStack stack = new ItemStack(item);
			stack.setCount(count);
			_items.add(stack);
		} else {
			_items.get(index).increment();;
		}
		
		setChanged();
		notifyObservers(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemAdded, item, null));
	}
	
	public boolean remove(Item item) {
		int index = _items.indexOf(item);
		if(index == -1)
			return false;
		ItemStack stack = _items.get(index);
		if(stack.getCount() > 0) {
			stack.decrement();
			setChanged();
			
			notifyObservers(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemRemoved, item, null));
			
			return true;
		} else {
			return false;
		}
	}
	
	public void equip(EquippableItem item) {
		if(item == null)
			throw new IllegalArgumentException("Cannot equip a null item");
		
		int index = _items.indexOf(item);
		
		if(index == -1)
			throw new IllegalArgumentException("Cannot equip an item that is not in the pack");
		
		ItemStack stack = _items.get(index);
		item = (EquippableItem)stack.getItem();
		
		if(stack.getCount() == 0)
			throw new IllegalArgumentException("Cannot equip an item that is not in the pack");
		
		// make sure that we have one to equip
		if(stack.getCount() == stack.getEquippedCount())
			throw new IllegalArgumentException("Do not have any in the stack to equip");
		
		if(item.getItemSlot() == null)
			throw new IllegalArgumentException("That item is not equippable: " + item.getName());
		
		EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
		// Does the item fill all the item slots? (two-handed weapon, etc)
		if(item.getItemSlot().fillsAll()) {
			for(EquipmentSlot es : item.getItemSlot().getFills()) {
				// Is there something already there?
				if(_equippedItems.containsKey(es)) {
					if(_equippedItems.get(es) == stack.getItem())
						throw new IllegalArgumentException("Item is already equipped: " + item.getName());
					unequip(es);
				}
				_equippedItems.put(es, item);
				slots.add(es);
			}
		} else {
			boolean emptySlotFound = false;
			for(EquipmentSlot es : item.getItemSlot().getFills()) {
				// Is there something already there?
				if(!_equippedItems.containsKey(es)) {
					_equippedItems.put(es, item);
					slots.add(es);
					emptySlotFound = true;
					break;
				}
			}
			if(!emptySlotFound) {
				EquipmentSlot es = item.getItemSlot().getFills().get(0);
				unequip(es);
				_equippedItems.put(es, item);
				slots.add(es);
			}
		}
		
		stack.equip();
		
		setChanged();
		notifyObservers(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, slots));
	}
	
	public void equip(EquippableItem item, EquipmentSlot slot) {
		if(item == null)
			throw new IllegalArgumentException("Cannot equip a null item");
		
		int index = _items.indexOf(item);
		
		if(index == -1)
			throw new IllegalArgumentException("Cannot equip an item that is not in the pack");
		
		ItemStack stack = _items.get(index);
		item = (EquippableItem)stack.getItem();
		
		if(stack.getCount() == 0)
			throw new IllegalArgumentException("Cannot equip an item that is not in the pack");
		
		// make sure that we have one to equip
		if(stack.getCount() == stack.getEquippedCount())
			throw new IllegalArgumentException("Do not have any in the stack to equip");
		
		// Make sure that the item can go into the specified slot
		if(!item.getItemSlot().getFills().contains(slot))
			throw new IllegalArgumentException("Cannot equip into that slot");
		
		EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
		
		// Does the item fill all the item slots? (two-handed weapon, etc)
		if(item.getItemSlot().fillsAll()) {
			for(EquipmentSlot es : item.getItemSlot().getFills()) {
				// Is there something already there?
				if(_equippedItems.containsKey(es)) {
					if(_equippedItems.get(es) == stack.getItem())
						throw new IllegalArgumentException("Item is already equipped");
					unequip(es);
				}
				_equippedItems.put(es, item);
				slots.add(es);
			}
		} else {			
			// Is there something already there?
			if(_equippedItems.containsKey(slot)) {
				unequip(slot);
			}
			_equippedItems.put(slot, item);
			slots.add(slot);
		}
		
		stack.equip();
		setChanged();
		notifyObservers(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, slots));
	}
	
	public void unequip(EquippableItem item) {
		if(item == null)
			throw new IllegalArgumentException("null item");
		
		int index = _items.indexOf(item);
		if(index == -1)
			throw new IllegalArgumentException("Item is not in pack");
		
		ItemStack stack = _items.get(index);
		item = (EquippableItem)stack.getItem();
		if(stack.getEquippedCount() == 0)
			throw new IllegalArgumentException("Item is not equipped");
		
		EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
		// does the item fill multiple slots?
		if(item.getItemSlot().fillsAll()) {
			// yes, so unequip them
			for(EquipmentSlot es : item.getItemSlot().getFills()) {
				_equippedItems.remove(es);
				slots.add(es);
			}
		} else {
			// no, find the first item and unequip it
			EquipmentSlot slot = null;
			for(EquipmentSlot es : item.getItemSlot().getFills()) {
				if(_equippedItems.get(es).equals(item)) {
					slot = es;
				}	
			}
			if(slot != null) {
				_equippedItems.remove(slot);
				slots.add(slot);
			}
		}
		stack.unequip();
		
		setChanged();
		notifyObservers(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemUnequipped, item, slots));
	}
	
	public void unequip(EquipmentSlot slot) {
		if(slot == null)
			throw new IllegalArgumentException("slot to unequip cannot be null");
		
		if(!_equippedItems.containsKey(slot))
			return;
		
		EquippableItem item = _equippedItems.get(slot);
		
		EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
		if(!item.getItemSlot().fillsAll()) {
			// unequip from a single slot
			_equippedItems.remove(slot);
			slots.add(slot);
		} else {
			// unequip from all slots
			for(EquipmentSlot es : item.getItemSlot().getFills()) {
				_equippedItems.remove(es);
				slots.add(es);
			}
		}
		
		_items.get(_items.indexOf(item)).unequip();
		
		setChanged();
		notifyObservers(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemUnequipped, item, slots));
	}
	
	public Set<EquipmentSlot> getEquippedSlots(EquippableItem item) {
		EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
		
		if(item == null)
			throw new IllegalArgumentException("null item");
		
		int index = _items.indexOf(item);
		if(index == -1)
			throw new IllegalArgumentException("Item not in list");
		
		ItemStack stack = _items.get(index);
		item = (EquippableItem)stack.getItem();
		
		if(stack.getEquippedCount() == 0)
			throw new IllegalArgumentException("item not equipped");
		
		for(EquipmentSlot es : item.getItemSlot().getFills()) {
			if(item.equals(_equippedItems.get(es)))
				slots.add(es);
		}
		
		return Collections.unmodifiableSet(slots);
	}
	
	public EquippableItem getEquippedItem(EquipmentSlot slot) {
		if(slot == null)
			throw new IllegalArgumentException("slot is null");
		
		if(!_equippedItems.containsKey(slot))
			return null;
		else
			return _equippedItems.get(slot);
	}
	
	public int size() {
		int size = 0;
		for(ItemStack s : _items)
			size += s.getCount();
		return size;
	}
	
	public List<ItemStack> getItems() {
		return Collections.unmodifiableList(_items);
	}
	
	public Map<EquipmentSlot,EquippableItem> getEquippedItems() {
		return Collections.unmodifiableMap(_equippedItems);
	}
}
