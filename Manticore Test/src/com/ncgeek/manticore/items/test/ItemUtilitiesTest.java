package com.ncgeek.manticore.items.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.ncgeek.manticore.Dice;
import com.ncgeek.manticore.Money;
import com.ncgeek.manticore.items.Armor;
import com.ncgeek.manticore.items.ArmorCategories;
import com.ncgeek.manticore.items.ArmorTypes;
import com.ncgeek.manticore.items.ItemSlots;
import com.ncgeek.manticore.items.ItemUtilities;
import com.ncgeek.manticore.items.Range;
import com.ncgeek.manticore.items.Weapon;
import com.ncgeek.manticore.items.WeaponCategories;
import com.ncgeek.manticore.items.WeaponGroups;
import com.ncgeek.manticore.items.WeaponProperties;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;
import com.ncgeek.manticore.rules.Specific;

public class ItemUtilitiesTest {

	@Test
	public void testArmorFromRule() {
		Rule r = getRule("Leather Armor", "laid", RuleTypes.ARMOR,
				"Weight", "15",
				"Armor Bonus", "2",
				"Check", "-",
				"Speed", "-",
				"Gold", "25",
				"Armor Type", "Light",
				"Item Slot", "Body",
				"Armor Category", "Leather");
		
		Armor a = ItemUtilities.armorFromRule(r);
		
		assertEquals("name", "Leather Armor", a.getName());
		assertEquals("id", "laid", a.getID());
		assertEquals("weight", 15.0, a.getWeight(), 0);
		assertEquals("price", new Money(2500), a.getPrice());
		assertEquals("bonus", 2, a.getBonus());
		assertEquals("check", 0, a.getCheckPenalty());
		assertEquals("speed", 0, a.getSpeedPenalty());
		assertEquals("type", ArmorTypes.Light, a.getArmorType());
		assertEquals("slot", ItemSlots.Body, a.getItemSlot());
		assertEquals("category", ArmorCategories.Leather, a.getArmorCategory());
		assertNull("special", a.getSpecial());
		assertEquals("min enh", 0, a.getMinEnhancementBonus());
	}

	@Test
	public void testWeaponFromRule() {
		Rule r = getRule("Bastard Sword", "InternalID", RuleTypes.WEAPON, 
				"Weight", "6",
				"Gold", "30",
				"Damage", "1d10",
				"Proficiency Bonus", "3",
				"Weapon Category", "Superior Melee",
				"Hands Required", "One-Handed",
				"Item Slot", "One-hand",
				"Group", "Heavy Blade",
				"Properties", "Versatile");
		
		Weapon w = ItemUtilities.weaponFromRule(r);
		
		assertEquals("name", "Bastard Sword", w.getName());
		assertEquals("id", "InternalID", w.getID());
		assertEquals("weight", 6.0, w.getWeight(), 0);
		assertEquals("price", new Money(3000), w.getPrice());
		assertEquals("damage", new Dice(1, 10), w.getDice());
		assertEquals("prof bonus", 3, w.getProficiencyBonus());
		assertEquals("category", WeaponCategories.SuperiorMelee, w.getCategory());
		assertEquals("slot", ItemSlots.OneHand, w.getItemSlot());
		assertEquals("range", new Range(0, 0), w.getRange());
		
		assertEquals("group size", 1, w.getGroups().size());
		assertTrue("group", w.getGroups().contains(WeaponGroups.HeavyBlade));
		assertEquals("prop size", 1, w.getProperties().size());
		assertTrue("prop", w.getProperties().contains(WeaponProperties.Versatile));
	}

	private Rule getRule(String name, String id, RuleTypes type, String...specifics) {
		Rule r = new Rule(name, type, id, null, null);
		
		if(specifics.length % 2 == 1)
			throw new RuntimeException("Invalid number of specifics");
		
		for(int i=0; i<specifics.length; i+=2) {
			Specific s = new Specific(specifics[i], specifics[i+1]);
			r.addSpecific(s);
		}
		
		return r;
	}
}
