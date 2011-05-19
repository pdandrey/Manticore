package com.ncgeek.manticore.character.inventory.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.ncgeek.manticore.character.inventory.EquipmentManager;
import com.ncgeek.manticore.character.inventory.EquipmentManagerEventArgs;
import com.ncgeek.manticore.character.inventory.EquipmentManagerEventType;
import com.ncgeek.manticore.character.inventory.EquipmentSlot;
import com.ncgeek.manticore.character.inventory.ItemStack;
import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.items.ItemSlots;

public class EquipmentManagerTests {

	private EquipmentManager _pack;
	private List<ItemStack> _lootList;
	private Map<EquipmentSlot, EquippableItem> _equippedMap;
	private Item _item;
	private EquippableItem _equipItem;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		_lootList = mock(List.class);
		_equippedMap = mock(Map.class);
		_pack = new EquipmentManager(_lootList, _equippedMap);
		_item = mockItem("Mock Item");
		_equipItem = mockEquippableItem("Mock Belt", ItemSlots.Waist);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddItem_NotPresent() {
		_lootList = new ArrayList();
		_pack = new EquipmentManager(_lootList, _equippedMap);
		
		_pack.add(_item);
		
		assertEquals(1, _lootList.size());
		ItemStack stack = _lootList.get(0);
		assertEquals(1, stack.getCount());
		assertSame(_item, stack.getItem());
		assertEquals(0, stack.getEquippedCount());
	}
	
	@Test
	public void testAddItem_AlreadyInPack() {
		Item i2 = mockItem("Mock Item");
		ItemStack stack = mockStack(_item, 1, 0);
		
		_pack.add(i2);
		
		verify(_lootList).indexOf(i2);
		verify(_lootList).get(0);
		verify(stack).increment();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddItem_Null() {
		_pack.add(null);
	}
	
	@Test
	public void testAddItem_Observer() {
		Observer observer = mock(Observer.class);
		_pack.addObserver(observer);
		
		when(_lootList.indexOf(_item)).thenReturn(-1);
		_pack.add(_item);
		
		ArgumentCaptor<EquipmentManagerEventArgs> captor = ArgumentCaptor.forClass(EquipmentManagerEventArgs.class);
		verify(observer).update(eq(_pack), captor.capture());
		
		EquipmentManagerEventArgs args = captor.getValue();
		assertSame(EquipmentManagerEventType.ItemAdded, args.getType());
		assertSame(_item, args.getItem());
		assertNull(args.getSlot());
	}
	
	@Test
	public void testRemoveItem_ToZero() {
		ItemStack stack = mockStack(_item, 1, 0);
		assertTrue(_pack.remove(_item));
		
		verify(stack).decrement();
		verify(_lootList, never()).remove(0);
		verify(_lootList, never()).remove(stack);
	}
	
	@Test
	public void testRemoveItem_PastZero() {
		ItemStack stack = mockStack(_item, 0, 0);
		assertFalse(_pack.remove(_item));
		verify(stack, never()).decrement();
		verify(stack).getCount();
	}
	
	@Test
	public void testRemoveItem_Observer() {
		Observer observer = mock(Observer.class);
		_pack.addObserver(observer);
		
		mockStack(_item, 2, 2);
		_pack.remove(_item);
		
		ArgumentCaptor<EquipmentManagerEventArgs> captor = ArgumentCaptor.forClass(EquipmentManagerEventArgs.class);
		verify(observer).update(eq(_pack), captor.capture());
		
		EquipmentManagerEventArgs args = captor.getValue();
		assertSame(EquipmentManagerEventType.ItemRemoved, args.getType());
		assertSame(_item, args.getItem());
		assertNull(args.getSlot());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEquip_ItemNotInPack() {
		when(_lootList.indexOf(_equipItem)).thenReturn(-1);
		
		_pack.equip(_equipItem);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEquip_ItemCountZero() {
		mockStack(_equipItem, 0, 3);
		_pack.equip(_equipItem);
	}
	
	@Test
	public void testEquip() {
		ItemStack stack = mockStack(_equipItem, 1, 2);
		_pack.equip(_equipItem);
		
		verify(_lootList).indexOf(_equipItem);
		verify(_lootList).get(2);
		verify(stack).equip();
		verify(_equippedMap).containsKey(EquipmentSlot.Waist);
		verify(_equippedMap).put(EquipmentSlot.Waist, _equipItem);
	}
	
	@Test
	public void testEquip_Observer() {
		Observer observer = mock(Observer.class);
		_pack.addObserver(observer);
		
		mockStack(_equipItem, 1, 1);
		
		_pack.equip(_equipItem);
		
		ArgumentCaptor<EquipmentManagerEventArgs> captor = ArgumentCaptor.forClass(EquipmentManagerEventArgs.class);
		verify(observer).update(eq(_pack), captor.capture());
		
		EquipmentManagerEventArgs args = captor.getValue();
		assertSame(EquipmentManagerEventType.ItemEquipped, args.getType());
		assertSame(_equipItem, args.getItem());
		assertTrue(args.getSlot().contains(EquipmentSlot.Waist));
	}
	
	@Test
	public void testEquip_Observer_MultiSlot() {
		Observer observer = mock(Observer.class);
		_pack.addObserver(observer);
		
		_equipItem = mockEquippableItem("Two Hands", ItemSlots.TwoHands);
		mockStack(_equipItem, 1, 1);
		
		_pack.equip(_equipItem);
		
		ArgumentCaptor<EquipmentManagerEventArgs> captor = ArgumentCaptor.forClass(EquipmentManagerEventArgs.class);
		verify(observer).update(eq(_pack), captor.capture());
		
		EquipmentManagerEventArgs args = captor.getValue();
		assertSame(EquipmentManagerEventType.ItemEquipped, args.getType());
		assertSame(_equipItem, args.getItem());
		assertTrue(args.getSlot().contains(EquipmentSlot.MainHand));
		assertTrue(args.getSlot().contains(EquipmentSlot.OffHand));
	}
	
	@Test
	public void testEquip_OverExisting() {
		ItemStack stackNewItem = mockStack(_equipItem, 1, 2);
		EquippableItem existingItem = mockEquippableItem("Existing Belt", ItemSlots.Waist);
		ItemStack stackExisting = mockStack(existingItem, 1, 1, 1);
		equipItem(existingItem, EquipmentSlot.Waist);
		
		_pack.equip(_equipItem);
		
		verify(_lootList).indexOf(_equipItem);
		verify(_lootList).get(2);
		verify(_equippedMap, times(2)).containsKey(EquipmentSlot.Waist);
		verify(_equippedMap, times(2)).get(EquipmentSlot.Waist);
		verify(_lootList).indexOf(existingItem);
		verify(_lootList).get(1);
		verify(stackExisting).unequip();
		verify(stackNewItem).equip();
		verify(_equippedMap).put(EquipmentSlot.Waist, _equipItem);
	}
	
	@Test
	public void testEquip_HeadAndNeck() {
		_equipItem = mockEquippableItem("Head and Neck Item", ItemSlots.HeadandNeck);
		ItemStack stack = mockStack(_equipItem, 1, 6);
		
		_pack.equip(_equipItem);
		
		verify(_lootList).indexOf(_equipItem);
		verify(_lootList).get(6);
		verify(_equippedMap).containsKey(EquipmentSlot.Head);
		verify(_equippedMap).containsKey(EquipmentSlot.Neck);
		verify(_equippedMap).put(EquipmentSlot.Head, _equipItem);
		verify(_equippedMap).put(EquipmentSlot.Neck, _equipItem);
		verify(stack).equip();
	}
	
	@Test
	public void testEquip_IntoOffhand() {
		EquippableItem main = mockEquippableItem("Dagger 1", ItemSlots.OneHand);
		EquippableItem off = mockEquippableItem("Dagger 2", ItemSlots.OneHand);
		
		mockStack(main, 1, 1, 0);
		ItemStack stackOff = mockStack(off, 1, 0, 1);
		
		equipItem(main, EquipmentSlot.MainHand);
		
		_pack.equip(off);
		
		verify(_lootList).indexOf(off);
		verify(_lootList).get(1);
		verify(_equippedMap).containsKey(EquipmentSlot.MainHand);
		verify(_equippedMap).containsKey(EquipmentSlot.OffHand);
		verify(_equippedMap).put(EquipmentSlot.OffHand, off);
		verify(stackOff).equip();
	}
	
	@Test
	public void testEquip_OverMainHand() {
		EquippableItem main = mockEquippableItem("Dagger 1", ItemSlots.OneHand);
		EquippableItem off = mockEquippableItem("Dagger 2", ItemSlots.OneHand);
		EquippableItem newMain = mockEquippableItem("Dagger 2", ItemSlots.OneHand);
		
		ItemStack oldStack = mockStack(main, 1, 1, 0);
		mockStack(off, 1, 1, 1);
		ItemStack newStack = mockStack(newMain, 1, 0, 2);
		
		equipItem(main, EquipmentSlot.MainHand);
		equipItem(off, EquipmentSlot.OffHand);
		
		_pack.equip(newMain);
		
		verify(_lootList).indexOf(newMain);
		verify(_lootList).get(2);
		verify(_equippedMap, times(2)).containsKey(EquipmentSlot.MainHand);
		verify(_equippedMap).containsKey(EquipmentSlot.OffHand);
		verify(_equippedMap).put(EquipmentSlot.MainHand, newMain);
		verify(newStack).equip();
		verify(oldStack).unequip();
	}
	
	@Test
	public void testEquip_TwoHandOverMain() {
		EquippableItem twoHand = mockEquippableItem("Two Hands", ItemSlots.TwoHands);
		EquippableItem oneHand = mockEquippableItem("One Hand", ItemSlots.OneHand);
		
		ItemStack stackTwo = mockStack(twoHand, 1, 0, 4);
		ItemStack stackOne = mockStack(oneHand, 1, 1, 1);
		
		equipItem(oneHand, EquipmentSlot.MainHand);
		
		_pack.equip(twoHand);
		
		verify(_lootList).indexOf(twoHand);
		verify(_lootList).get(4);
		verify(_equippedMap, times(2)).containsKey(EquipmentSlot.MainHand);
		verify(_equippedMap).containsKey(EquipmentSlot.OffHand);
		verify(_equippedMap, times(2)).get(EquipmentSlot.MainHand);
		verify(stackOne).unequip();
		verify(stackTwo).equip();
		verify(_equippedMap).put(EquipmentSlot.MainHand, twoHand);
		verify(_equippedMap).put(EquipmentSlot.OffHand, twoHand);
	}
	
	@Test
	public void testEquip_TwoHandOverMainAndOff() {
		EquippableItem twoHand = mockEquippableItem("Two Hands", ItemSlots.TwoHands);
		EquippableItem oneHand = mockEquippableItem("One Hand", ItemSlots.OneHand);
		EquippableItem offHand = mockEquippableItem("Off Hand", ItemSlots.OneHand);
		
		ItemStack stackTwo = mockStack(twoHand, 1, 0, 4);
		ItemStack stackOne = mockStack(oneHand, 1, 1, 1);
		ItemStack stackOff = mockStack(offHand, 1, 1, 12);
		
		equipItem(oneHand, EquipmentSlot.MainHand);
		equipItem(offHand, EquipmentSlot.OffHand);
		
		_pack.equip(twoHand);
		
		verify(_lootList).indexOf(twoHand);
		verify(_lootList).get(4);
		verify(_equippedMap, times(2)).containsKey(EquipmentSlot.MainHand);
		verify(_equippedMap, times(2)).containsKey(EquipmentSlot.OffHand);
		verify(_equippedMap, times(2)).get(EquipmentSlot.MainHand);
		verify(_equippedMap, times(2)).get(EquipmentSlot.OffHand);
		verify(stackOne).unequip();
		verify(stackOff).unequip();
		verify(stackTwo).equip();
		verify(_equippedMap).put(EquipmentSlot.MainHand, twoHand);
		verify(_equippedMap).put(EquipmentSlot.OffHand, twoHand);
	}
	
	@Test
	public void testEquipInSlot() {
		_equipItem = mockEquippableItem("Dagger", ItemSlots.OneHand);
		ItemStack stack = mockStack(_equipItem, 1, 0);
		
		_pack.equip(_equipItem, EquipmentSlot.OffHand);
		
		verify(_equippedMap).containsKey(EquipmentSlot.OffHand);
		verify(_equippedMap).put(EquipmentSlot.OffHand, _equipItem);
		verify(stack).equip();
	}
	
	@Test
	public void testEquipSlot_Observer() {
		Observer observer = mock(Observer.class);
		_pack.addObserver(observer);
		
		_equipItem = mockEquippableItem("dagger", ItemSlots.OneHand);
		mockStack(_equipItem, 1, 1);
		
		_pack.equip(_equipItem, EquipmentSlot.OffHand);
		
		ArgumentCaptor<EquipmentManagerEventArgs> captor = ArgumentCaptor.forClass(EquipmentManagerEventArgs.class);
		verify(observer).update(eq(_pack), captor.capture());
		
		EquipmentManagerEventArgs args = captor.getValue();
		assertSame(EquipmentManagerEventType.ItemEquipped, args.getType());
		assertSame(_equipItem, args.getItem());
		assertTrue(args.getSlot().contains(EquipmentSlot.OffHand));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEquipInSlot_BadSlot() {
		_equipItem = mockEquippableItem("Main Hand", ItemSlots.MainHand);
		mockStack(_equipItem, 1, 0);
		_pack.equip(_equipItem, EquipmentSlot.OffHand);
	}
	
	@Test
	public void testEquip_MultipleItemsIntoOff() {
		_equipItem = mockEquippableItem("Dagger", ItemSlots.OneHand);
		ItemStack stack = mockStack(_equipItem, 2, 1, 0);
		equipItem(_equipItem, EquipmentSlot.MainHand);
		
		_pack.equip(_equipItem);
		
		verify(_lootList).indexOf(_equipItem);
		verify(_lootList).get(0);
		verify(_equippedMap).containsKey(EquipmentSlot.MainHand);
		verify(_equippedMap).containsKey(EquipmentSlot.OffHand);
		verify(_equippedMap).put(EquipmentSlot.OffHand, _equipItem);
		verify(stack).equip();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEquip_AlreadyEquipped() {
		mockStack(_equipItem, 1, 1, 0);
		equipItem(_equipItem, EquipmentSlot.Waist);
		
		_pack.equip(_equipItem);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEquip_AlreadyEquippedStack() {
		mockStack(_equipItem, 1, 3, 0);
		equipItem(_equipItem, EquipmentSlot.Waist);
		
		_pack.equip(_equipItem);
	}
	
	@Test
	public void testUnequipItem() {
		ItemStack stack = mockStack(_equipItem, 1, 1, 0);
		equipItem(_equipItem, EquipmentSlot.Waist);
		
		_pack.unequip(_equipItem);
		
		verify(_equippedMap).remove(EquipmentSlot.Waist);
		verify(_lootList).indexOf(_equipItem);
		verify(_lootList).get(0);
		verify(stack).unequip();
	}
	
	@Test
	public void testUnequipItem_Observer() {
		Observer observer = mock(Observer.class);
		_pack.addObserver(observer);
		
		mockStack(_equipItem, 1, 1, 1);
		equipItem(_equipItem, EquipmentSlot.Waist);
		
		_pack.unequip(_equipItem);
		
		ArgumentCaptor<EquipmentManagerEventArgs> captor = ArgumentCaptor.forClass(EquipmentManagerEventArgs.class);
		verify(observer).update(eq(_pack), captor.capture());
		
		EquipmentManagerEventArgs args = captor.getValue();
		assertSame(EquipmentManagerEventType.ItemUnequipped, args.getType());
		assertSame(_equipItem, args.getItem());
		assertTrue(args.getSlot().contains(EquipmentSlot.Waist));
	}
	
	@Test
	public void testUnequipItem_Observer_Multislot() {
		Observer observer = mock(Observer.class);
		_pack.addObserver(observer);
		
		_equipItem = mockEquippableItem("Two Hands", ItemSlots.TwoHands);
		mockStack(_equipItem, 1, 1, 1);
		equipItem(_equipItem, EquipmentSlot.MainHand);
		equipItem(_equipItem, EquipmentSlot.OffHand);
		
		_pack.unequip(_equipItem);
		
		ArgumentCaptor<EquipmentManagerEventArgs> captor = ArgumentCaptor.forClass(EquipmentManagerEventArgs.class);
		verify(observer).update(eq(_pack), captor.capture());
		
		EquipmentManagerEventArgs args = captor.getValue();
		assertSame(EquipmentManagerEventType.ItemUnequipped, args.getType());
		assertSame(_equipItem, args.getItem());
		assertTrue(args.getSlot().contains(EquipmentSlot.MainHand));
		assertTrue(args.getSlot().contains(EquipmentSlot.OffHand));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnequipItem_NotEquipped() {
		mockStack(_equipItem, 1, 12);
		_pack.unequip(_equipItem);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnequipItem_NotInPack() {
		when(_lootList.indexOf(_equipItem)).thenReturn(-1);
		_pack.unequip(_equipItem);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnequipItem_StackZero() {
		mockStack(_equipItem, 0, 15);
		_pack.unequip(_equipItem);
	}
	
	@Test
	public void testUnequipItem_MultiSlot() {
		_equipItem = mockEquippableItem("Two Hands", ItemSlots.TwoHands);
		ItemStack stack = mockStack(_equipItem, 1, 1, 1);
		equipItem(_equipItem, EquipmentSlot.MainHand);
		equipItem(_equipItem, EquipmentSlot.OffHand);
		
		_pack.unequip(_equipItem);
		
		verify(_lootList).indexOf(_equipItem);
		verify(_lootList).get(1);
		verify(_equippedMap).remove(EquipmentSlot.MainHand);
		verify(_equippedMap).remove(EquipmentSlot.OffHand);
		verify(stack).unequip();
	}
	
	@Test
	public void testUnequipItem_OffSlot_SameStack() {
		_equipItem = mockEquippableItem("dagger", ItemSlots.OneHand);
		ItemStack stack = mockStack(_equipItem, 2, 2, 2);
		equipItem(_equipItem, EquipmentSlot.MainHand);
		equipItem(_equipItem, EquipmentSlot.OffHand);
		
		when(_equipItem.equals(_equipItem)).thenReturn(true);
		
		_pack.unequip(_equipItem);
		
		verify(_lootList).indexOf(_equipItem);
		verify(_lootList).get(2);
		verify(_equippedMap).get(EquipmentSlot.MainHand);
		verify(_equippedMap).get(EquipmentSlot.OffHand);
		verify(_equippedMap).remove(EquipmentSlot.OffHand);
		verify(_equippedMap, never()).remove(EquipmentSlot.MainHand);
		verify(stack).unequip();
	}
	
	@Test
	public void testUnequipItem_OffSlot_DifferentStack() {
		EquippableItem main = mockEquippableItem("Dagger", ItemSlots.OneHand);
		EquippableItem off = mockEquippableItem("Off Dagger", ItemSlots.OneHand);
		
		when(main.equals(main)).thenReturn(true);
		when(main.equals(off)).thenReturn(false);
		when(off.equals(off)).thenReturn(true);
		when(off.equals(main)).thenReturn(false);
		
		mockStack(main, 1, 1, 1);
		ItemStack offStack = mockStack(off, 1, 1, 2);
		
		equipItem(main, EquipmentSlot.MainHand);
		equipItem(off, EquipmentSlot.OffHand);
		
		_pack.unequip(off);
		
		verify(_lootList).indexOf(off);
		verify(_lootList).get(2);
		verify(_equippedMap).get(EquipmentSlot.MainHand);
		verify(_equippedMap).get(EquipmentSlot.OffHand);
		verify(_equippedMap).remove(EquipmentSlot.OffHand);
		verify(_equippedMap, never()).remove(EquipmentSlot.MainHand);
		verify(offStack).unequip();
	}
	
	@Test
	public void testUnequipSlot() {
		ItemStack stack = mockStack(_equipItem, 1, 1, 1);
		equipItem(_equipItem, EquipmentSlot.Waist);
		
		_pack.unequip(EquipmentSlot.Waist);
		
		verify(_equippedMap).containsKey(EquipmentSlot.Waist);
		verify(_equippedMap).get(EquipmentSlot.Waist);
		verify(_lootList).indexOf(_equipItem);
		verify(_lootList).get(1);
		verify(stack).unequip();
	}
	
	@Test
	public void testUnequipSlot_Observer() {
		Observer observer = mock(Observer.class);
		_pack.addObserver(observer);
		
		mockStack(_equipItem, 1, 1);
		equipItem(_equipItem, EquipmentSlot.Waist);
		
		_pack.unequip(EquipmentSlot.Waist);
		
		ArgumentCaptor<EquipmentManagerEventArgs> captor = ArgumentCaptor.forClass(EquipmentManagerEventArgs.class);
		verify(observer).update(eq(_pack), captor.capture());
		
		EquipmentManagerEventArgs args = captor.getValue();
		assertSame(EquipmentManagerEventType.ItemUnequipped, args.getType());
		assertSame(_equipItem, args.getItem());
		assertTrue(args.getSlot().contains(EquipmentSlot.Waist));
	}
	
	@Test
	public void testUnequipSlot_NotEquipped() {
		when(_equippedMap.containsKey(EquipmentSlot.Waist)).thenReturn(false);
		
		_pack.unequip(EquipmentSlot.Waist);
		
		verify(_equippedMap).containsKey(EquipmentSlot.Waist);
		verify(_equippedMap, never()).get(EquipmentSlot.Waist);
		verify(_lootList, never()).indexOf(any(Item.class));
		verify(_lootList, never()).get(anyInt());
	}
	
	@Test
	public void testUnequipSlot_TwoHanded() {
		_equipItem = mockEquippableItem("Two Hands", ItemSlots.TwoHands);
		ItemStack stack = mockStack(_equipItem, 1, 1, 1);
		equipItem(_equipItem, EquipmentSlot.MainHand);
		equipItem(_equipItem, EquipmentSlot.OffHand);
		
		_pack.unequip(EquipmentSlot.MainHand);
		
		verify(_equippedMap).containsKey(EquipmentSlot.MainHand);
		verify(_equippedMap).get(EquipmentSlot.MainHand);
		verify(_equippedMap).remove(EquipmentSlot.MainHand);
		verify(_lootList).indexOf(_equipItem);
		verify(_lootList).get(1);
		verify(_equippedMap).remove(EquipmentSlot.OffHand);
		verify(stack).unequip();
	}
	
	@Test
	public void testGetEquippedSlots_Single() {
		mockStack(_equipItem, 1, 1, 1);
		equipItem(_equipItem, EquipmentSlot.Waist);
		Set<EquipmentSlot> ret = _pack.getEquippedSlots(_equipItem);
		assertEquals("size", 1, ret.size());
		assertTrue("value", ret.contains(EquipmentSlot.Waist));
		verify(_equippedMap).get(EquipmentSlot.Waist);
	}
	
	@Test
	public void testGetEquippedSlots_MultipleItems() {
		_equipItem = mockEquippableItem("Dagger", ItemSlots.OneHand);
		equipItem(_equipItem, EquipmentSlot.MainHand);
		equipItem(_equipItem, EquipmentSlot.OffHand);
		mockStack(_equipItem, 2, 2, 2);
		
		Set<EquipmentSlot> ret = _pack.getEquippedSlots(_equipItem);
		
		assertEquals("size", 2, ret.size());
		assertTrue("main", ret.contains(EquipmentSlot.MainHand));
		assertTrue("off", ret.contains(EquipmentSlot.OffHand));
		
		verify(_equippedMap).get(EquipmentSlot.MainHand);
		verify(_equippedMap).get(EquipmentSlot.OffHand);
	}
	
	@Test
	public void testGetEquippedItem() {
		equipItem(_equipItem, EquipmentSlot.Waist);
		assertSame(_equipItem, _pack.getEquippedItem(EquipmentSlot.Waist));
		verify(_equippedMap).containsKey(EquipmentSlot.Waist);
		verify(_equippedMap).get(EquipmentSlot.Waist);
	}
	
	@Test
	public void testGetEquippedItem_NotEquipped() {
		when(_equippedMap.containsKey(EquipmentSlot.Head)).thenReturn(false);
		assertNull(_pack.getEquippedItem(EquipmentSlot.Head));
		verify(_equippedMap).containsKey(EquipmentSlot.Head);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSize_SingleItem() {
		ItemStack stack = mockStack(_item, 3, 0);
		
		Iterator<ItemStack> iterator = mock(Iterator.class);
		when(iterator.next()).thenReturn(stack);
		when(iterator.hasNext()).thenReturn(true, false);
		when(_lootList.iterator()).thenReturn(iterator);

		assertEquals(3, _pack.size());
		verify(stack).getCount();
		verify(_lootList).iterator();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSize_MultipleItems() {
		Item i3 = mockItem("Test Item 3");
		ItemStack stackItem = mockStack(_item, 2, 0);
		ItemStack stackEquip = mockStack(_equipItem, 1, 1, 1);
		ItemStack stack3 = mockStack(i3, 4, 2);
		
		Iterator<ItemStack> iterator = mock(Iterator.class);
		when(iterator.next()).thenReturn(stackItem, stackEquip, stack3);
		when(iterator.hasNext()).thenReturn(true, true, true, false);
		when(_lootList.iterator()).thenReturn(iterator);
		
		assertEquals(7, _pack.size());
		
		verify(stackItem).getCount();
		verify(stackEquip).getCount();
		verify(stack3).getCount();
		verify(_lootList).iterator();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEquip_NotEquippableGear() {
		_equipItem = mockEquippableItem("gear", null);
		
		mockStack(_equipItem, 1, 1);
		
		_pack.equip(_equipItem);
	}
	
	private EquippableItem mockEquippableItem(String name, ItemSlots slot) {
		EquippableItem ret = mock(EquippableItem.class);
		when(ret.getItemSlot()).thenReturn(slot);
		when(ret.getName()).thenReturn(name);
		when(ret.equals(ret)).thenReturn(true);
		
		if(slot != null) {
			for(EquipmentSlot es : slot.getFills())
				when(_equippedMap.containsKey(es)).thenReturn(false);
		}
		
		return ret;
	}
	
	private Item mockItem(String name) {
		Item ret = mock(Item.class);
		when(ret.getName()).thenReturn(name);
		return ret;
	}
	
	private ItemStack mockStack(Item item, int count, int index) {
		return mockStack(item, count, 0, index);
	}
	private ItemStack mockStack(Item item, int count, int equipCount, int index) {
		ItemStack stack = mock(ItemStack.class);
		when(stack.getItem()).thenReturn(item);
		when(stack.getCount()).thenReturn(count);
		if(item instanceof EquippableItem) {
			when(stack.getEquippedCount()).thenReturn(equipCount);
		} else
			when(stack.getEquippedCount()).thenThrow(new IllegalArgumentException());
		
		if(index >= 0) {
			when(_lootList.indexOf(item)).thenReturn(index);
			when(_lootList.get(index)).thenReturn(stack);
		}
		return stack;
	}
	
	private void equipItem(EquippableItem item, EquipmentSlot slot) {
		when(_equippedMap.containsKey(slot)).thenReturn(true);
		when(_equippedMap.get(slot)).thenReturn(item);
	}
}
