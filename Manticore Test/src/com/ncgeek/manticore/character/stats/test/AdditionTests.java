package com.ncgeek.manticore.character.stats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ncgeek.manticore.character.RuleEventArgs;
import com.ncgeek.manticore.character.RuleEventType;
import com.ncgeek.manticore.character.inventory.EquipmentManagerEventArgs;
import com.ncgeek.manticore.character.inventory.EquipmentManagerEventType;
import com.ncgeek.manticore.character.inventory.EquipmentSlot;
import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.character.stats.UnlinkedStatException;
import com.ncgeek.manticore.items.Armor;
import com.ncgeek.manticore.items.ArmorTypes;
import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Gear;
import com.ncgeek.manticore.items.ItemSlots;
import com.ncgeek.manticore.items.Weapon;
import com.ncgeek.manticore.items.WeaponGroups;
import com.ncgeek.manticore.rules.Rule;

public class AdditionTests {

	private Addition _addition;
	private Map<String,String> _map;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		_map = (Map<String,String>)mock(Map.class);
		_addition = new Addition(_map);
	}

	@Test
	public void testGetValue() {
		assertEquals(0, _addition.getValue());
		_addition.setValue(5);
		assertEquals(5, _addition.getValue());
	}

	@Test
	public void testGetLevel() {
		assertNull(_addition.getLevel());
		_addition.setLevel(2);
		assertEquals(new Integer(2), _addition.getLevel());
	}

	@Test
	public void testSetStatLinkString() {
		assertNull(_addition.getStatLink());
		final String stat = "Dex";
		
		_addition.setStatLink(stat);
		assertSame(stat, _addition.getStatLink());
	}

	@Test
	public void testSetStatLinkStat() {
		final String name = "Con";
		Stat s = mock(Stat.class);
		when(s.equals(name)).thenReturn(true);
		
		_addition.setStatLink(name);
		_addition.setStatLink(s);
		
		verify(s).equals(name);
		
		assertEquals("Con [linked]", _addition.getStatLink());
	}
	
	@Test
	public void testGetValue_LinkedStat() {
		final String name = "misc";
		Stat s = mock(Stat.class);
		when(s.equals(name)).thenReturn(true);
		when(s.getCalculatedValue()).thenReturn(5);
		
		_addition.setStatLink(name);
		_addition.setStatLink(s);
		_addition.setValue(1);
		
		assertEquals(5, _addition.getValue());
		
		verify(s).getCalculatedValue();
		verify(s, never()).getAbsoluteValue();
	}

	@Test
	public void testIsAbilityModified() {
		assertFalse(_addition.isAbilityModified());
		_addition.setAbilityModified(true);
		assertTrue(_addition.isAbilityModified());
	}
	
	@Test
	public void testGetValue_AbilityModified() {
		Stat s = mock(Stat.class);
		final String name = "stat";
		when(s.equals(name)).thenReturn(true);
		when(s.getModifier()).thenReturn(2);
		
		_addition.setStatLink(name);
		_addition.setStatLink(s);
		_addition.setAbilityModified(true);
		
		assertEquals(2, _addition.getValue());
		
		verify(s).getModifier();
		verify(s, never()).getCalculatedValue();
		verify(s, never()).getAbsoluteValue();
	}
	
	@Test(expected=UnlinkedStatException.class)
	public void testGetValue_AbilityModified_NotLinked() {
		final String name = "link";
		
		_addition.setStatLink(name);
		_addition.setAbilityModified(true);
		
		_addition.getValue();
	}

	@Test
	public void testGetType() {
		final String type = "type";
		assertNull(_addition.getType());
		_addition.setType(type);
		assertSame(type, _addition.getType());
	}

	@Test
	public void testGetWearing() {
		final String wearing = "wear";
		assertNull(_addition.getWearing());
		assertTrue(_addition.shouldApply());
		_addition.setWearing(wearing);
		assertSame(wearing, _addition.getWearing());
		assertFalse(_addition.shouldApply());
	}

	@Test
	public void testIsNotWearing() {
		assertFalse(_addition.isNotWearing());
		_addition.setNotWearing("armor:");
		assertTrue(_addition.isNotWearing());
	}

	@Test
	public void testGetRequires() {
		final String requires = "req";
		assertNull(_addition.getRequires());
		assertTrue(_addition.shouldApply());
		_addition.setRequires(requires);
		assertSame(requires, _addition.getRequires());
		//assertFalse(_addition.shouldApply());
	}

	@Test
	public void testGetAdditionalAttributeNames() {
		HashSet<String> set = new HashSet<String>();
		set.add("one");
		set.add("two");
		
		when(_map.keySet()).thenReturn(set);
		
		Set<String> lst = _addition.getAdditionalAttributeNames();
		assertTrue(lst.containsAll(set));
		assertEquals(set.size(), lst.size());
		
		verify(_map).keySet();
	}

	@Test
	public void testGetAdditionalAttribute() {
		
		when(_map.containsKey("one")).thenReturn(true);
		when(_map.containsKey("two")).thenReturn(true);
		when(_map.get("one")).thenReturn("1");
		when(_map.get("two")).thenReturn("2");
		
		assertEquals("1", _addition.getAdditionalAttribute("one"));
		assertEquals("2", _addition.getAdditionalAttribute("two"));
		
		verify(_map).containsKey("one");
		verify(_map).containsKey("two");
		verify(_map).get("one");
		verify(_map).get("two");
	}

	@Test
	public void testSetAdditionalAttribute() {
		_addition.setAdditionalAttribute("one", "1");
		_addition.setAdditionalAttribute("two", "2");
		
		verify(_map).put("one", "1");
		verify(_map).put("two", "2");
	}
	
	@Test
	public void testSetAdditionalAttribute_NullMap() {
		_addition = new Addition(null);
		
		_addition.setAdditionalAttribute("one", "1");
		
		assertEquals("1", _addition.getAdditionalAttribute("one"));
		assertEquals(1, _addition.getAdditionalAttributeNames().size());
	}

	@Test
	public void testGetAdditionalAttributes() {
		HashMap<String,String> map = new HashMap<String,String>();
		_addition = new Addition(map);
		
		_addition.setAdditionalAttribute("a", "a");
		
		Map<String,String> attrs = _addition.getAdditionalAttributes();
		assertEquals(map, attrs);
	}

	@Test
	public void testProcessRule() {
		_addition.setRequires("rule");
		_addition.setValue(4);
		
		Rule r = mock(Rule.class);
		when(r.getName()).thenReturn("rule");
		
		assertTrue(_addition.process(new RuleEventArgs(RuleEventType.RuleAdded, r)));
		
		assertTrue(_addition.shouldApply());
		assertTrue(_addition.ruleFound());
		assertEquals(4, _addition.getValue());
		verify(r).getName();
	}
	
	@Test
	public void testProcessRule_NotFound() {
		_addition.setRequires("rule");
		_addition.setValue(4);
		
		Rule r = mock(Rule.class);
		when(r.getName()).thenReturn("not the rule");
		
		assertFalse(_addition.process(new RuleEventArgs(RuleEventType.RuleAdded, r)));
		
		//assertFalse(_addition.shouldApply());
		assertFalse(_addition.ruleFound());
		assertEquals(0, _addition.getValue());
		verify(r).getName();
	}
	
	@Test
	public void testProcessRule_NegativeRuleFound() {
		_addition.setRequires("!rule");
		_addition.setValue(-3);
		
		Rule r = mock(Rule.class);
		when(r.getName()).thenReturn("rule");
		
		_addition.process(new RuleEventArgs(RuleEventType.RuleAdded, r));
		
		verify(r).getName();
		assertFalse(_addition.shouldApply());
		assertTrue(_addition.ruleFound());
		assertEquals(0, _addition.getValue());
	}
	
	@Test
	public void testProcessRule_NegativeRuleNotFound() {
		_addition.setRequires("!rule");
		_addition.setValue(-3);
		
		Rule r = mock(Rule.class);
		when(r.getName()).thenReturn("different rule");
		
		_addition.process(new RuleEventArgs(RuleEventType.RuleAdded, r));
		
		verify(r).getName();
		assertTrue(_addition.shouldApply());
		assertFalse(_addition.ruleFound());
		assertEquals(-3, _addition.getValue());
	}
	
	@Test
	public void testProcessRule_RuleRemoved() {
		_addition.setRequires("rule");
		_addition.setValue(4);
		
		Rule r = mock(Rule.class);
		when(r.getName()).thenReturn("rule");
		
		_addition.process(new RuleEventArgs(RuleEventType.RuleAdded, r));
		_addition.process(new RuleEventArgs(RuleEventType.RuleRemoved, r));
		
		assertFalse(_addition.shouldApply());
		assertFalse(_addition.ruleFound());
		assertEquals(0, _addition.getValue());
		verify(r, times(2)).getName();
	}
	
	@Test
	public void testProcessNegativeRule_RuleRemoved() {
		_addition.setRequires("!rule");
		_addition.setValue(-3);
		
		Rule r = mock(Rule.class);
		when(r.getName()).thenReturn("rule");
		
		_addition.process(new RuleEventArgs(RuleEventType.RuleAdded, r));
		_addition.process(new RuleEventArgs(RuleEventType.RuleRemoved, r));
		
		verify(r, times(2)).getName();
		assertTrue(_addition.shouldApply());
		assertFalse(_addition.ruleFound());
		assertEquals(-3, _addition.getValue());
	}

	@Test
	public void testProcessEquipment_ArmorNoSub() {
		//<statadd type="Unarmored Defense" Level="1" wearing="armor:" value="-2" charelem="1cdf8de0" /> 
		_addition.setValue(-2);
		_addition.setWearing("armor:");
		
		Armor item = mock(Armor.class);
		
		assertTrue(_addition.process(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, EnumSet.of(EquipmentSlot.Body))));
		
		assertTrue(_addition.shouldApply());
		assertTrue(_addition.itemFound());
		assertEquals(-2, _addition.getValue());
		
		verifyZeroInteractions(item);
	}
	
	@Test
	public void testProcessEquipment_ArmorNoSub_NotWearing() {
		//<statadd type="Unarmored Defense" Level="1" wearing="armor:" value="-2" charelem="1cdf8de0" /> 
		_addition.setValue(2);
		_addition.setNotWearing("armor:");
		
		Armor item = mock(Armor.class);
		
		_addition.process(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, EnumSet.of(EquipmentSlot.Body)));
		
		assertFalse(_addition.shouldApply());
		assertTrue(_addition.itemFound());
		assertEquals(0, _addition.getValue());
		
		verifyZeroInteractions(item);
	}
	
	@Test
	public void testProcessEquipment_ArmorSub() {
		//<statadd type="Ability" Level="1" not-wearing="armor:heavy" value="1" statlink="Dexterity" abilmod="true" 
		_addition.setValue(-2);
		_addition.setWearing("armor:heavy");
		
		Armor item = mock(Armor.class);
		when(item.getArmorType()).thenReturn(ArmorTypes.Heavy);
		
		_addition.process(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, EnumSet.of(EquipmentSlot.Body)));
		
		assertTrue(_addition.shouldApply());
		assertTrue(_addition.itemFound());
		assertEquals(-2, _addition.getValue());
		
		verify(item).getArmorType();
	}
	
	@Test
	public void testProcessEquipment_ArmorSub_NotWearing() {
		//<statadd type="Ability" Level="1" not-wearing="armor:heavy" value="1" statlink="Dexterity" abilmod="true" 
		_addition.setValue(-2);
		_addition.setNotWearing("armor:heavy");
		
		Armor item = mock(Armor.class);
		when(item.getArmorType()).thenReturn(ArmorTypes.Heavy);
		
		_addition.process(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, EnumSet.of(EquipmentSlot.Body)));
		
		assertFalse(_addition.shouldApply());
		assertTrue(_addition.itemFound());
		assertEquals(0, _addition.getValue());
		
		verify(item).getArmorType();
	}
	
	@Test
	@Ignore
	public void testProcessEquipment_DefensiveNoSub() {
		fail("Not Yet Implemented... because I have no idea what this is supposed to do yet.");
	}
	
	@Test
	public void testProcessEquipment_ImplementNoSub() {
		
		_addition.setValue(1);
		_addition.setWearing("implement");
		
		Gear item = mock(Gear.class);
		when(item.getName()).thenReturn("Rod Implement");
		
		_addition.process(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, EnumSet.of(EquipmentSlot.OffHand)));
		
		assertTrue(_addition.shouldApply());
		assertTrue(_addition.itemFound());
		assertEquals(1, _addition.getValue());
	}
	
	@Test
	public void testProcessEquipment_ImplementSub() {
		
		_addition.setValue(1);
		_addition.setWearing("implement:rod");
		
		Gear item = mock(Gear.class);
		when(item.getName()).thenReturn("Rod Implement");
		
		_addition.process(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, EnumSet.of(EquipmentSlot.OffHand)));
		
		assertTrue(_addition.shouldApply());
		assertTrue(_addition.itemFound());
		assertEquals(1, _addition.getValue());
		
		verify(item).getName();
	}
	
	@Test
	public void testProcessEquipment_ImplementSub_WrongItem() {
		
		_addition.setValue(1);
		_addition.setWearing("implement:orb");
		
		Gear item = mock(Gear.class);
		when(item.getName()).thenReturn("Rod Implement");
		
		_addition.process(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, EnumSet.of(EquipmentSlot.OffHand)));
		
		assertFalse(_addition.shouldApply());
		assertFalse(_addition.itemFound());
		assertEquals(0, _addition.getValue());
		
		verify(item).getName();
	}
	
	@Test
	public void testProcessEquipment_Slot() {
		_addition.setValue(1);
		_addition.setWearing("slot:off hand");
		
		EquippableItem item = mock(EquippableItem.class);
		when(item.getItemSlot()).thenReturn(ItemSlots.OneHand);
		
		_addition.process(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, EnumSet.of(EquipmentSlot.OffHand)));
		
		assertTrue(_addition.shouldApply());
		assertTrue(_addition.itemFound());
		assertEquals(1, _addition.getValue());
	}
	
	@Test
	public void testProcessEquipment_NotWearingSlot() {
		_addition.setValue(3);
		_addition.setNotWearing("slot:off hand");
		
		EquippableItem item = mock(EquippableItem.class);
		when(item.getItemSlot()).thenReturn(ItemSlots.OneHand);
		
		_addition.process(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, EnumSet.of(EquipmentSlot.OffHand)));
		
		assertFalse(_addition.shouldApply());
		assertTrue(_addition.itemFound());
		assertEquals(0, _addition.getValue());
	}
	
	@Test
	public void testProcessEquipment_Weapon() {
		_addition.setValue(3);
		_addition.setWearing("weapon:light blade");
		
		Weapon item = mock(Weapon.class);
		when(item.getGroups()).thenReturn(EnumSet.of(WeaponGroups.LightBlade));
		
		_addition.process(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, EnumSet.of(EquipmentSlot.MainHand)));
		
		assertTrue(_addition.shouldApply());
		assertTrue(_addition.itemFound());
		assertEquals(3, _addition.getValue());
		
		verify(item).getGroups();
	}
	
	@Test
	public void testProcessEquipment_WeaponNotWearing() {
		_addition.setValue(3);
		_addition.setNotWearing("weapon:heavy blade");
		
		Weapon item = mock(Weapon.class);
		when(item.getGroups()).thenReturn(EnumSet.of(WeaponGroups.HeavyBlade));
		
		_addition.process(new EquipmentManagerEventArgs(EquipmentManagerEventType.ItemEquipped, item, EnumSet.of(EquipmentSlot.MainHand)));
		
		assertFalse(_addition.shouldApply());
		assertTrue(_addition.itemFound());
		assertEquals(0, _addition.getValue());
		
		verify(item).getGroups();
	}
}
