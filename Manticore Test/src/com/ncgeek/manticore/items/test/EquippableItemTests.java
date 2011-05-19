package com.ncgeek.manticore.items.test;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.ItemSlots;

public class EquippableItemTests {

	private static final ItemSlots SLOT = ItemSlots.Body;
	
	@Test
	public void testGetItemSlot() {
		EquippableItem ei = new EquippableItem();
		ei.setItemSlot(SLOT);
		assertEquals(SLOT, ei.getItemSlot());
	}

}
