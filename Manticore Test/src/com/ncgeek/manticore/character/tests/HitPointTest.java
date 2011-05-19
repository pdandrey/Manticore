package com.ncgeek.manticore.character.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.character.HitPoints;

public class HitPointTest {

	private HitPoints hp;
	
	private final static int MAX_HP = 50;
	private final static int TOTAL_SURGES = 9;
	private final static int SURGE_VALUE = MAX_HP / 4;
	private final static int BLOODIED_VALUE = MAX_HP / 2;
	
	@Before
	public void setUp() throws Exception {
		hp = new HitPoints();
		hp.setMax(MAX_HP);
		hp.setTotalSurges(TOTAL_SURGES);
	}

	@Test
	public void testSetTemp_Under() {
		hp.setTemp(4);
		hp.setTemp(1);
		assertEquals(4, hp.getTemp());
	}
	
	@Test
	public void testSetTemp_Over() {
		hp.setTemp(4);
		hp.setTemp(11);
		assertEquals(11, hp.getTemp());
	}

	@Test
	public void testFullRest() {
		hp.setCurrent(25);
		hp.setTemp(5);
		hp.expendSurge();
		hp.expendSurge();
		hp.failDeathSave();
		
		hp.fullRest();
		
		assertEquals("max", MAX_HP, hp.getMax());
		assertEquals("current", MAX_HP, hp.getCurrent());
		assertEquals("temp", 0, hp.getTemp());
		assertEquals("total surges", TOTAL_SURGES, hp.getTotalSurges());
		assertEquals("remaining surges", TOTAL_SURGES, hp.getRemainingSurges());
		assertEquals("death saves", 0, hp.getDeathSaves());
	}
	
	@Test
	public void testShortRest() {
		hp.setCurrent(25);
		hp.setTemp(5);
		hp.expendSurge();
		hp.expendSurge();
		hp.failDeathSave();
		
		hp.shortRest();
		
		assertEquals("max", MAX_HP, hp.getMax());
		assertEquals("current", MAX_HP - 25, hp.getCurrent());
		assertEquals("temp", 0, hp.getTemp());
		assertEquals("total surges", TOTAL_SURGES, hp.getTotalSurges());
		assertEquals("remaining surges", TOTAL_SURGES - 2, hp.getRemainingSurges());
		assertEquals("death saves", 0, hp.getDeathSaves());
	}

	@Test
	public void testTakeDamage() {
		hp.takeDamage(10);
		assertEquals(40, hp.getCurrent());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTakeDamage_Negative() {
		hp.takeDamage(-5);
	}
	
	@Test
	public void testTakeDamage_PartialTemp() {
		hp.setTemp(10);
		hp.takeDamage(7);
		assertEquals("current", 50, hp.getCurrent());
		assertEquals("temp", 3, hp.getTemp());
	}
	
	@Test
	public void testTakeDamage_AllTemp() {
		hp.setTemp(5);
		hp.takeDamage(7);
		assertEquals("current", 48, hp.getCurrent());
		assertEquals("temp", 0, hp.getTemp());
	}

	@Test
	public void testHeal() {
		hp.takeDamage(20);
		hp.heal(3);
		assertEquals(33, hp.getCurrent());
	}
	
	@Test
	public void testHeal_Overheal() {
		hp.takeDamage(5);
		hp.heal(10);
		assertEquals(MAX_HP, hp.getCurrent());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testHeal_Negative() {
		hp.heal(-5);
	}
	
	@Test
	public void testHeal_FromDead() {
		hp.takeDamage(55);
		hp.failDeathSave();
		hp.heal(3);
		assertEquals(3, hp.getCurrent());
		assertEquals(0, hp.getDeathSaves());
	}

	@Test
	public void testUseSurge() {
		hp.takeDamage(20);
		hp.useSurge(3);
		assertEquals(30 + SURGE_VALUE + 3, hp.getCurrent());
		assertEquals(TOTAL_SURGES - 1, hp.getRemainingSurges());
	}

	@Test
	public void testExpendSurge() {
		hp.takeDamage(20);
		hp.expendSurge();
		assertEquals(30, hp.getCurrent());
		assertEquals(TOTAL_SURGES - 1, hp.getRemainingSurges());
	}
	
	@Test
	public void testGetBloodiedValue() {
		assertEquals(BLOODIED_VALUE, hp.getBloodiedValue());
	}
	
	@Test
	public void testIsBloodied() {
		assertFalse(hp.isBloodied());
		hp.takeDamage(24);
		assertFalse(hp.isBloodied());
		hp.takeDamage(1);
		assertTrue(hp.isBloodied());
	}
	
	@Test
	public void testGetSurgeValue() {
		assertEquals(SURGE_VALUE, hp.getSurgeValue());
	}
	
	@Test
	public void testFailDeathSave() {
		hp.failDeathSave();
		assertEquals(1, hp.getDeathSaves());
		hp.failDeathSave();
		assertEquals(2, hp.getDeathSaves());
		hp.failDeathSave();
		hp.failDeathSave();
		assertEquals(3, hp.getDeathSaves());
	}
	
	@Test
	public void testIsBleedingOut() {
		assertFalse(hp.isBleedingOut());
		hp.takeDamage(49);
		assertFalse(hp.isBleedingOut());
		hp.takeDamage(1);
		assertFalse(hp.isBleedingOut());
		hp.takeDamage(1);
		assertTrue(hp.isBleedingOut());
	}
	
	@Test
	public void testIsDead() {
		assertFalse(hp.isDead());
		hp.failDeathSave();
		assertFalse(hp.isDead());
		hp.failDeathSave();
		assertFalse(hp.isDead());
		hp.failDeathSave();
		assertTrue(hp.isDead());
	}
}
