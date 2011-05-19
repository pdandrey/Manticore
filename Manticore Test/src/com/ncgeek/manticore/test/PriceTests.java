package com.ncgeek.manticore.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.Money;

public class PriceTests {

	private Money _price;
	
	private static final int TOTAL_PRICE = 4236789;
	private static final int COPPER_EXPECTED = 9;
	private static final int SILVER_EXPECTED = 8;
	private static final int GOLD_EXPECTED = 67;
	private static final int PLATINUM_EXPECTED = 23;
	private static final int ASTRAL_EXPECTED = 4;
	private static final String STRING_EXPECTED = "4a 23p 67g 8s 9c";
	
	@Before
	public void setUp() throws Exception {
		_price = new Money(TOTAL_PRICE);
	}

	@Test
	public void testPriceInt() {
		assertEquals(TOTAL_PRICE, _price.getTotalCopper());
	}

	@Test
	public void testPriceString() {
		Money p = new Money("2a 3p 87g 2s 1c");
		assertEquals(2038721, p.getTotalCopper());
	}

	@Test
	public void testGetCopper() {
		assertEquals(COPPER_EXPECTED, _price.getCopper());
	}

	@Test
	public void testGetSilver() {
		assertEquals(SILVER_EXPECTED, _price.getSilver());
	}

	@Test
	public void testGetGold() {
		assertEquals(GOLD_EXPECTED, _price.getGold());
	}

	@Test
	public void testGetPlatinum() {
		assertEquals(PLATINUM_EXPECTED, _price.getPlatinum());
	}

	@Test
	public void testGetAstral() {
		assertEquals(ASTRAL_EXPECTED, _price.getAstral());
	}

	@Test
	public void testToString() {
		assertEquals(STRING_EXPECTED, _price.toString());
	}
	
	@Test
	public void testPartialToString1() {
		Money p = new Money(5);
		assertEquals("5c", p.toString());
	}

	@Test
	public void testPartialToString2() {
		Money p = new Money(20);
		assertEquals("2s", p.toString());
	}
	
	@Test
	public void testPartialToString3() {
		Money p = new Money(301);
		assertEquals("3g 1c", p.toString());
	}
	
	@Test
	public void testPartialToString4() {
		Money p = new Money(7000213);
		assertEquals("7a 2g 1s 3c", p.toString());
	}
	
	@Test
	public void testCompareTo() {
		Money p1 = new Money(23);
		Money p2 = new Money(30);
		
		assertEquals(30-23, p2.compareTo(p1));
		assertEquals(23-30, p1.compareTo(p2));
	}
}
