package com.ncgeek.manticore.character.inventory.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ncgeek.manticore.character.inventory.ItemStack;
import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Item;

public class ItemStackTests {

	private ItemStack stack;
	private Item _item;
	
	@Before
	public void setup() {
		_item = mock(EquippableItem.class);
		stack = new ItemStack(_item);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCount_NullItem() {
		new ItemStack(null);
	}
	
	@Test
	public void testCount_SafePath() {
		assertEquals("initial count", 1, stack.getCount());
		stack.setCount(5);
		assertEquals("setCount", 5, stack.getCount());
		stack.increment();
		assertEquals("increment", 6, stack.getCount());
		stack.decrement();
		assertEquals("decrement", 5, stack.getCount());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetCount_Negative() {
		stack.setCount(-1);
	}
	
	@Test
	public void testSetCount_Zero() {
		stack.setCount(0);
		assertEquals(0, stack.getCount());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testDecriment_PastZero() {
		stack.setCount(0);
		stack.decrement();
	}
	
	@Test
	public void testEquipped_SafePath() {
		assertEquals("default", 0, stack.getEquippedCount());
		stack.setCount(10);
		stack.setEquippedCount(3);
		assertEquals("setEquip", 3, stack.getEquippedCount());
		stack.equip();
		assertEquals("equip", 4, stack.getEquippedCount());
		stack.unequip();
		assertEquals("unequip", 3, stack.getEquippedCount());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetEquipped_MoreThanCount() {
		stack.setCount(1);
		stack.setEquippedCount(3);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEquip_MoreThanCount() {
		stack.setCount(2);
		stack.setEquippedCount(2);
		stack.equip();
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testEquip_NotEquippable() {
		stack = new ItemStack(mock(Item.class));
		stack.setCount(1);
		stack.equip();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnequip_NoneEquipped() {
		stack.unequip();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEquip_SetNegative() {
		stack.setEquippedCount(-1);
	}
	
	@Test
	public void testGetItem() {
		assertSame(_item, stack.getItem());
	}
	
	@Test
	@Ignore
	public void testHasCode() {
		// So it seems that we can't mock hashCode() :(
		when(_item.hashCode()).thenReturn(1234);
		assertEquals(1234, stack.hashCode());
		verify(_item).hashCode();
	}
	
	@Test
	public void testToString() {
		when(_item.getName()).thenReturn("Mock Item");
		assertEquals("Single Item", "Mock Item x1", stack.toString());
		
		stack.setCount(4);
		assertEquals("Multiple Items", "Mock Item x4", stack.toString());
		
		stack.setEquippedCount(1);
		assertEquals("Equipped", "Mock Item x4, 1 equipped", stack.toString());
		
		verify(_item, times(3)).getName();
	}
	
	@Test
	@Ignore
	public void testEquals() {
		// It seems that we can't mock equals() :(
		Item i2 = mock(EquippableItem.class);
		when(_item.equals(i2)).thenReturn(true);
		assertTrue(stack.equals(i2));
		verify(_item).equals(i2);
	}
}
