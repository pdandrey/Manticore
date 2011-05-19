package com.ncgeek.manticore.integrationtests;

import static org.junit.Assert.*;

import com.ncgeek.manticore.character.Alignment;
import com.ncgeek.manticore.character.Gender;
import com.ncgeek.manticore.character.PlayerCharacter;

public class SetPCPropertiesFromStatsRules extends BaseIntegrationTest {
	
	private Gender gender;
	private Alignment align;
	private int hp;
	private int surges;
	private String _class;
	private String race;
	
	@Override
	protected void setupCharacterValues() {
		String file = getFile().getName().toLowerCase().replace(".dnd4e", "");
		
		gender = null;
		align = null;
		hp = -1;
		surges = -1;
		_class = null;
		race = null;
		
		if(file.equals("chase")) {
			gender = Gender.Male;
			align = Alignment.Good;
			hp = 43;
			surges = 11;
			_class = "Swordmage";
			race = "Human";
		} else if(file.equals("alek")) {
			gender = Gender.Male;
			align = Alignment.Good;
			hp = 30;
			surges = 8;
			_class = "Psion";
			race = "Human";
		} else if(file.equals("christof")) {
			gender = Gender.Male;
			align = Alignment.Good;
			hp = 31;
			surges = 8;
			_class = "Rogue";
			race = "Human";
		} else if(file.equals("greyson")) {
			gender = Gender.Male;
			align = Alignment.Unaligned;
			hp = 43;
			surges = 11;
			_class = "Swordmage";
			race = "Human";
		} else if(file.equals("pynder")) {
			gender = Gender.Male;
			align = Alignment.Unaligned;
			hp = 30;
			surges = 10;
			_class = "Warlock";
			race = "Human";
		}
	}

	@Override
	protected void test(PlayerCharacter pc) {
		assertEquals("Gender", gender, pc.getGender());
		assertEquals("Alignment", align, pc.getAlignment());
		assertEquals("hp", hp, pc.getHP().getMax());
		assertEquals("surge", surges, pc.getHP().getTotalSurges());
		assertEquals("class", _class, pc.getHeroicClass());
		assertEquals("race", race, pc.getRace());
	}

}
