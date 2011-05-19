package com.ncgeek.manticore.items.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.Money;
import com.ncgeek.manticore.Source;
import com.ncgeek.manticore.items.Item;

public class ItemTest {

	private static final String ITEM_NAME = "Test Item";
	private static final String ITEM_NAME_2 = "Test Item2";
	private static final String ITEM_DESC = "Item Desc";
	private static final double ITEM_WEIGHT = 25;
	private static final Source ITEM_SOURCE = Source.forName("Item Source");
	
	private Item _item;
	
	@Before
	public void setUp() throws Exception {
		_item = new Item(){};
		_item.setName(ITEM_NAME);
		_item.setDescription(ITEM_DESC);
		_item.setWeight(ITEM_WEIGHT);
		_item.addSource(ITEM_SOURCE);
	}

	@Test
	public void testGetName() {
		assertEquals(ITEM_NAME, _item.getName());
	}

	@Test
	public void testGetDescription() {
		assertEquals(ITEM_DESC, _item.getDescription());
	}

	@Test
	public void testCompareTo() {
		Item i2 = new Item() {};
		i2.setName(ITEM_NAME_2);
		int actual = _item.compareTo(i2);
		int expected = ITEM_NAME.compareTo(ITEM_NAME_2);
		assertEquals(expected, actual);
	}
	
	@Test 
	public void testGetCost() {
		Money p = mock(Money.class);
		_item.setPrice(p);
		assertSame(p, _item.getPrice());
	}
	
	@Test
	public void testCostCompare() {
		Money p1 = mock(Money.class);
		Money p2 = mock(Money.class);
		
		when(p1.compareTo(p2)).thenReturn(-3);
		
		_item.setPrice(p1);
		Item i2 = new Item(){};
		i2.setPrice(p2);
		
		int actual = Item.PriceComparator.compare(_item, i2);
		int expected = -3;
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetWeight() {
		assertEquals(ITEM_WEIGHT, _item.getWeight(), 0.0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetWeightBelowZero() {
		_item.setWeight(-1);
	}
}
