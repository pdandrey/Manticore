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
	}
	
	@Test
	public void testNewDiceString() {
		final String dice = "2d4";
		final int count = 2;
		final int sides = 4;
		Dice d = new Dice(dice);
		assertEquals("count", count, d.getCount());
		assertEquals("sides", sides, d.getSides());
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
}
