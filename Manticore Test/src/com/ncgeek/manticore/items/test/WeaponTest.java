package com.ncgeek.manticore.items.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.Dice;
import com.ncgeek.manticore.items.ItemSlots;
import com.ncgeek.manticore.items.Range;
import com.ncgeek.manticore.items.Weapon;
import com.ncgeek.manticore.items.WeaponCategories;
import com.ncgeek.manticore.items.WeaponGroups;
import com.ncgeek.manticore.items.WeaponProperties;

public class WeaponTest {

	private Weapon _weapon;
	
	@Before
	public void setUp() throws Exception {
		_weapon = new Weapon();
	}

	@Test
	public void testGetAdditionalSlot() {
		assertNull(_weapon.getAdditionalSlot());
		_weapon.setAdditionalSlot(ItemSlots.Hands);
		assertEquals(ItemSlots.Hands, _weapon.getAdditionalSlot());
	}

	@Test
	public void testGetDice() {
		Dice d = mock(Dice.class);
		_weapon.setDice(d);
		assertSame(d, _weapon.getDice());
	}

	@Test
	public void testIsTwoHanded() {
		assertFalse(_weapon.isTwoHanded());
		_weapon.setTwoHanded(true);
		assertTrue(_weapon.isTwoHanded());
		_weapon.setTwoHanded(false);
		assertFalse(_weapon.isTwoHanded());
	}

	@Test
	public void testGetProficiencyBonus() {
		_weapon.setProficiencyBonus(4);
		assertEquals(4, _weapon.getProficiencyBonus());
	}

	@Test
	public void testIsRangedWeapon() {
		assertFalse(_weapon.isRangedWeapon());
		
		Range r = mock(Range.class);
		when(r.getRange1()).thenReturn(5, 0);
		
		_weapon.setRange(r);
		assertTrue(_weapon.isRangedWeapon());
		assertFalse(_weapon.isRangedWeapon());
		
		verify(r, times(2)).getRange1();
	}

	@Test
	public void testGetRange() {
		Range range = mock(Range.class);
		_weapon.setRange(range);
		assertSame(range, _weapon.getRange());
	}

	@Test
	public void testGetCategory() {
		_weapon.setCategory(WeaponCategories.SimpleMelee);
		assertSame(WeaponCategories.SimpleMelee, _weapon.getCategory());
	}

	@Test
	public void testHasProperty() {
		_weapon.addProperty(WeaponProperties.Brutal);
		_weapon.addProperty(WeaponProperties.HighCrit);
		assertTrue(_weapon.hasProperty(WeaponProperties.HighCrit));
		assertFalse(_weapon.hasProperty(WeaponProperties.Reach));
	}

	@Test
	public void testGetProperties() {
		WeaponProperties[] props = new WeaponProperties[] { WeaponProperties.LightThrown, WeaponProperties.OffHand, WeaponProperties.Small };
		for(WeaponProperties wp : props)
			_weapon.addProperty(wp);
		Set<WeaponProperties> set = _weapon.getProperties();
		
		for(WeaponProperties wp : props)
			assertTrue(set.contains(wp));
		
		assertFalse(set.contains(WeaponProperties.Brutal));
	}

	@Test
	public void testIsInGroup() {
		_weapon.addGroup(WeaponGroups.Polearm);
		assertTrue(_weapon.isInGroup(WeaponGroups.Polearm));
		assertFalse(_weapon.isInGroup(WeaponGroups.Axe));
	}

	@Test
	public void testGetGroups() {
		WeaponGroups[] groups = new WeaponGroups[] { WeaponGroups.Polearm, WeaponGroups.Axe };
		for(WeaponGroups g : groups)
			_weapon.addGroup(g);
		Set<WeaponGroups> set = _weapon.getGroups();
		for(WeaponGroups g : groups)
			assertTrue(set.contains(g));
		assertFalse(set.contains(WeaponGroups.Bow));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testUnmodifibleProperties() {
		Set<WeaponProperties> set = _weapon.getProperties();
		set.add(WeaponProperties.Stout);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testUnmodifibleGroups() {
		Set<WeaponGroups> set = _weapon.getGroups();
		set.add(WeaponGroups.Axe);
	}
}
