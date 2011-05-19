package com.ncgeek.manticore.items.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.items.Gear;
import com.ncgeek.manticore.items.GearCategory;
import com.ncgeek.manticore.items.ItemSlots;

public class GearTest {

	private Gear _gear;
	
	@Before
	public void setUp() throws Exception {
		_gear = new Gear();
	}

	@Test
	public void testGetCategory() {
		final GearCategory gc = GearCategory.Food;
		_gear.setCategory(gc);
		assertSame(gc, _gear.getCategory());
	}

	@Test
	public void testGetCount() {
		final int count = 30;
		_gear.setCount(count);
		assertEquals(count, _gear.getCount());
	}

	@Test(expected= IllegalArgumentException.class)
	public void testSetCountZero() {
		_gear.setCount(0);
	}
	
	@Test(expected= IllegalArgumentException.class)
	public void testSetCountLessZero() {
		_gear.setCount(-1);
	}
	
	@Test
	public void testGetSlot() {
		final ItemSlots slot = ItemSlots.OneHand;
		_gear.setItemSlot(slot);
		assertSame(slot, _gear.getItemSlot());
	}

}
