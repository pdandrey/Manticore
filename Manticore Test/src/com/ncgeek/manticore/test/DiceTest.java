package com.ncgeek.manticore.test;


import java.util.Random;

import org.junit.After;
import org.junit.Test;

import com.ncgeek.manticore.Dice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DiceTest {
	
	@After
	public void cleanup() {
		Dice.setRandom(null);
	}

	@Test
	public void testNewDiceIntInt() {
		final int count = 1;
		final int sides = 8;
		Dice d = new Dice(count, sides);
		assertEquals("count", count, d.getCount());
		assertEquals("sides", sides, d.getSides());
		assertEquals("modifier", 0, d.getModifier());
	}
	
	@Test
	public void testNewDiceString() {
		final String dice = "2d4";
		final int count = 2;
		final int sides = 4;
		Dice d = new Dice(dice);
		assertEquals("count", count, d.getCount());
		assertEquals("sides", sides, d.getSides());
		assertEquals("modifier", 0, d.getModifier());
	}
	
	@Test
	public void testNewDiceStringWithModifier() {
		final String dice = "2d4+3";
		final int count = 2;
		final int sides = 4;
		final int modifier = 3;
		Dice d = new Dice(dice);
		assertEquals("count", count, d.getCount());
		assertEquals("sides", sides, d.getSides());
		assertEquals("modifier", modifier, d.getModifier());
	}
	
	@Test
	public void testNewDiceStringWithModifierWithSpaces() {
		final String dice = "2d4 + 3";
		final int count = 2;
		final int sides = 4;
		final int modifier = 3;
		Dice d = new Dice(dice);
		assertEquals("count", count, d.getCount());
		assertEquals("sides", sides, d.getSides());
		assertEquals("modifier", modifier, d.getModifier());
	}
	
	@Test
	public void testNewDiceStringWithNegModifier() {
		final String dice = "2d4-3";
		final int count = 2;
		final int sides = 4;
		final int modifier = -3;
		Dice d = new Dice(dice);
		assertEquals("count", count, d.getCount());
		assertEquals("sides", sides, d.getSides());
		assertEquals("modifier", modifier, d.getModifier());
	}
	
	@Test(expected=IllegalArgumentException.class) 
	public void testNewDiceInvalidString() {
		new Dice("Invalid Dice String");
	}
	
	@Test
	public void testToString() {
		final int count = 3;
		final int sides = 12;
		final String expected = "3d12";
		Dice d = new Dice(count, sides);
		assertEquals(expected, d.toString());
	}
	
	@Test
	public void testToStringWithModifier() {
		final int count = 2;
		final int sides = 8;
		final int modifier = 2;
		final String expected = "2d8 + 2";
		Dice d = new Dice(count, sides, modifier);
		assertEquals(expected, d.toString());
	}
	
	@Test
	public void testToStringWithNegModifier() {
		final int count = 1;
		final int sides = 6;
		final int modifier = -1;
		final String expected = "1d6 - 1";
		Dice d = new Dice(count, sides, modifier);
		assertEquals(expected, d.toString());
	}
	
	@Test
	public void testRollSingle() {
		final int roll = 3;
		final int sides = 6;
		final int count = 1;
		Random r = mock(Random.class);
		when(r.nextInt(sides)).thenReturn(roll-1);
		Dice.setRandom(r);
		
		Dice d = new Dice(count, sides);
		assertEquals(roll, d.roll());
		
		verify(r).nextInt(sides);
	}
	
	@Test
	public void testRollMulti() {
		int sides = 6;
		int count = 3;
		
		Random r = mock(Random.class);
		when(r.nextInt(sides)).thenReturn(0, 1, 2);
		Dice.setRandom(r);
		
		Dice d = new Dice(count, sides);
		assertEquals(6, d.roll());
		
		verify(r, times(3)).nextInt(sides);
	}
	
	@Test
	public void testRollModifier() {
		int sides = 6;
		int count = 3;
		int modifier = 2;
		
		Random r = mock(Random.class);
		when(r.nextInt(sides)).thenReturn(0, 1, 2);
		Dice.setRandom(r);
		
		Dice d = new Dice(count, sides, modifier);
		assertEquals(8, d.roll());
		
		verify(r, times(3)).nextInt(sides);
	}
}
