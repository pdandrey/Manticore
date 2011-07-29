package com.ncgeek.manticore.integrationtests;

import static org.junit.Assert.*;

import com.ncgeek.manticore.character.PlayerCharacter;

public class RitualTests extends BaseIntegrationTest {

	private int ritualCount;
	
	@Override
	protected void test(PlayerCharacter pc) {
		assertEquals(ritualCount, pc.getRituals().size());
	}

	@Override
	protected void setupCharacterValues() {
		String name = super.getFile().getName().toLowerCase();
		
		if(name.equals("alek2.dnd4e")) {
			ritualCount = 2;
		} else {
			ritualCount = 0;
		}
	}

}
