package com.ncgeek.manticore.character.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.ncgeek.manticore.character.Alignment;
import com.ncgeek.manticore.character.Feat;
import com.ncgeek.manticore.character.HitPoints;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.Gender;
import com.ncgeek.manticore.character.RuleEventArgs;
import com.ncgeek.manticore.character.RuleEventType;
import com.ncgeek.manticore.character.Wallet;
import com.ncgeek.manticore.character.inventory.EquipmentManager;
import com.ncgeek.manticore.character.stats.StatBlock;
import com.ncgeek.manticore.powers.Power;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;

public class CharacterTests {
	
	private PlayerCharacter character;
	private Wallet moneyCarried;
	private Wallet moneyStored;
	private List<Rule> rules;
	private List<Power> powers;
	private List<Feat> feats;
	private StatBlock stats;
	private EquipmentManager equipment;
	private HitPoints hp;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		moneyCarried = mock(Wallet.class);
		moneyStored = mock(Wallet.class);
		rules = mock(List.class);
		powers = mock(List.class);
		feats = mock(List.class);
		stats = mock(StatBlock.class);
		equipment = mock(EquipmentManager.class);
		hp = mock(HitPoints.class);
		character = new PlayerCharacter(moneyCarried, moneyStored, rules, powers, feats, stats, equipment, hp);
	}

	@Test
	public void testGetName_Initial() {
		assertNull(character.getName());
	}
	
	@Test
	public void testGetName() {
		final String name = "Test Name";
		character.setName(name);
		assertSame(name, character.getName());
	}

	@Test
	public void testGetLevel_Initial() {
		assertEquals(1, character.getLevel());
	}
	
	@Test
	public void testGetLevel() {
		final int level = 7;
		character.setLevel(level);
		assertEquals(level, character.getLevel());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetLevel_Zero() {
		final int level = 0;
		character.setLevel(level);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetLevel_LessZero() {
		int level = -4;
		character.setLevel(level);
	}

	@Test
	public void testGetPlayer_Initial() {
		assertNull(character.getPlayer());
	}
	
	@Test
	public void testGetPlayer() {
		final String player = "Test Player";
		character.setPlayer(player);
		assertSame(player, character.getPlayer());
	}

	@Test
	public void testGetHeight_Initial() {
		assertNull(character.getHeight());
	}
	
	@Test
	public void testGetHeight() {
		final String height = "5' 9\"";
		character.setHeight(height);
		assertSame(height, character.getHeight());
	}

	@Test
	public void testGetWeight_Initial() {
		assertNull(character.getWeight());
	}
	
	@Test
	public void testGetWeight() {
		final String weight = "184 lbs";
		character.setWeight(weight);
		assertSame(weight, character.getWeight());
	}

	@Test
	public void testGetGender_Initial() {
		assertNull(character.getGender());
	}
	
	@Test
	public void testGetGender() {
		final Gender gender = Gender.Male;
		character.setGender(gender);
		assertSame(gender, character.getGender());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetGender_Null() {
		character.setGender(null);
	}

	@Test
	public void testSetAlignment_Initial() {
		assertSame(Alignment.Unaligned, character.getAlignment());
	}
	
	@Test
	public void testSetAlignment() {
		final Alignment align = Alignment.LawfulGood;
		character.setAlignment(align);
		assertSame(align, character.getAlignment());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetAlignment_Null() {
		character.setAlignment(null);
	}

	@Test
	public void testGetAge_Initial() {
		assertEquals(0, character.getAge());
	}
	
	@Test
	public void testGetAge() {
		final int age = 27;
		character.setAge(age);
		assertEquals(age, character.getAge());
	}

	@Test
	public void testGetCompany_Initial() {
		assertNull(character.getCompany());
	}
	
	@Test
	public void testGetCompany() {
		final String company = "Test Company";
		character.setCompany(company);
		assertSame(company, character.getCompany());
	}

	@Test
	public void testGetPortrait_Initial() {
		assertNull(character.getPortrait());
	}
	
	@Test
	public void testGetPortrait() {
		final String portrait = "Test Portrait";
		character.setPortrait(portrait);
		assertSame(portrait, character.getPortrait());
	}

	@Test
	public void testGetExperience_Initial() {
		assertEquals(0, character.getExperience());
	}
	
	@Test
	public void testGetExperience() {
		final int exp = 250;
		character.setExperience(exp);
		assertEquals(exp, character.getExperience());
	}

	@Test
	public void testGetMoneyCarried() {
		assertSame(moneyCarried, character.getMoneyCarried());
		verifyZeroInteractions(moneyCarried);
	}

	@Test
	public void testGetMoneyStored() {
		assertSame(moneyStored, character.getMoneyStored());
		verifyZeroInteractions(moneyStored);
	}

	@Test
	public void testGetTraits_Initial() {
		assertNull(character.getTraits());
	}
	
	@Test
	public void testGetTraits() {
		final String traits = "Test Traits";
		character.setTraits(traits);
		assertSame(traits, character.getTraits());
	}

	@Test
	public void testGetApperance_Initial() {
		assertNull(character.getApperance());
	}
	
	@Test
	public void testGetApperance() {
		final String apperance = "Test Apperance";
		character.setApperance(apperance);
		assertSame(apperance, character.getApperance());
	}

	@Test
	public void testGetCompanions_Initial() {
		assertNull(character.getCompanions());
	}
	
	@Test
	public void testGetCompanions() {
		final String companions = "Test Group";
		character.setCompanions(companions);
		assertSame(companions, character.getCompanions());
	}

	@Test
	public void testGetNotes_Initial() {
		assertNull(character.getNotes());
	}
	
	@Test
	public void testGetNotes() {
		final String notes = "Test Notes";
		character.setNotes(notes);
		assertSame(notes, character.getNotes());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testGetRules_Add() {
		character.getRules().add(null);
	}
	
	@Test
	public void testAddRule() {
		Rule r = mock(Rule.class);
		when(r.getType()).thenReturn(RuleTypes.GEAR);
		ArgumentCaptor<RuleEventArgs> args = ArgumentCaptor.forClass(RuleEventArgs.class);
		character.add(r);
		verify(rules).add(r);
		verify(stats).update(eq(character), args.capture());
		
		assertSame(r, args.getValue().getRule());
		assertSame(RuleEventType.RuleAdded, args.getValue().getType());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddRule_Null() {
		Rule r = null;
		character.add(r);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testGetPowers_Add() {
		character.getPowers().add(null);
	}
	
	@Test
	public void testAddPower() {
		Power power = mock(Power.class);
		character.add(power);
		verify(powers).add(power);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddPower_Null() {
		Power power = null;
		character.add(power);
	}

	@Test
	public void testGetStats() {
		assertSame(stats, character.getStats());
	}

	@Test
	public void testGetEquipment() {
		assertSame(equipment, character.getEquipment());
	}

}
