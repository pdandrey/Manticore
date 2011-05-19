package com.ncgeek.manticore.items.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ncgeek.manticore.items.Range;

public class RangeTest {

	@Test
	public void testRangeIntInt() {
		final int RANGE1 = 10;
		final int RANGE2 = 20;
		
		Range r = new Range(RANGE1, RANGE2);
		assertEquals("range1", RANGE1, r.getRange1());
		assertEquals("range2", RANGE2, r.getRange2());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRange1TooBig() {
		new Range(1000, 10);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRange2TooBig() {
		new Range(10, 1000);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRange1LessZero() {
		new Range(-1, 5);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRange2LessZero() {
		new Range(5, -1);
	}

	@Test
	public void testToString() {
		final int RANGE1 = 2;
		final int RANGE2 = 5;
		final String EXPECTED = "2/5";
		
		Range r = new Range(RANGE1, RANGE2);
		assertEquals(EXPECTED, r.toString());
	}

}
