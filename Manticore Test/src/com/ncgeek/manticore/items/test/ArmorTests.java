package com.ncgeek.manticore.items.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.items.Armor;
import com.ncgeek.manticore.items.ArmorCategories;
import com.ncgeek.manticore.items.ArmorTypes;

public class ArmorTests {

	private static final ArmorCategories CATEGORY = ArmorCategories.Cloth;
	private static final ArmorTypes TYPE = ArmorTypes.Light;
	private static final int BONUS = 2;
	private static final int CHECK_PENALTY = -1;
	private static final String SPECIAL = null;
	private static final int SPEED_PENALTY = -2;
	private static final int MIN_ENH_BONUS = 4;
	
	private Armor _armor;
	
	@Before
	public void setUp() throws Exception {
		_armor = new Armor();
	}

	@Test
	public void testGetCheckPenalty() {
		_armor.setCheckPenalty(CHECK_PENALTY);
		assertEquals(CHECK_PENALTY, _armor.getCheckPenalty());
	}

	@Test
	public void testGetSpeedPenalty() {
		_armor.setSpeedPenalty(SPEED_PENALTY);
		assertEquals(SPEED_PENALTY, _armor.getSpeedPenalty());
	}

	@Test
	public void testGetArmorType() {
		_armor.setArmorType(TYPE);
		assertEquals(TYPE, _armor.getArmorType());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetArmorTypeNull() {
		_armor.setArmorType(null);
	}
	
	@Test
	public void testSetItemSlotNull() {
		_armor.setItemSlot(null);
		assertNull(_armor.getItemSlot());
	}

	@Test
	public void testGetArmorCategory() {
		_armor.setArmorCategory(CATEGORY);
		assertEquals(CATEGORY, _armor.getArmorCategory());
	}

	@Test
	public void testSetArmorCategoryNull() {
		_armor.setArmorCategory(null);
		assertNull(_armor.getArmorCategory());
	}
	
	@Test
	public void testGetBonus() {
		_armor.setBonus(BONUS);
		assertEquals(BONUS, _armor.getBonus());
	}

	@Test
	public void testGetSpecial() {
		_armor.setSpecial(SPECIAL);	
		assertEquals(SPECIAL, _armor.getSpecial());
	}

	@Test
	public void testGetMinEnhancementBonus() {
		_armor.setMinEnhancementBonus(MIN_ENH_BONUS);
		assertEquals(MIN_ENH_BONUS, _armor.getMinEnhancementBonus());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetMinEnhancementBonusLessZero() {
		_armor.setMinEnhancementBonus(-1);
	}
}
