package com.ncgeek.manticore.integrationtests;

import static org.junit.Assert.*;

import com.ncgeek.manticore.character.PlayerCharacter;


public class PortraitTest extends BaseIntegrationTest {

	private int id;
	
	@Override
	protected void setupCharacterValues() {
		String file = getFile().getName().toLowerCase().replace(".dnd4e", "");
		
		if(file.equals("chase2")) {
			id = 10022317;
		} else if(file.equals("alek2")) {
			id = 10123017;
		} else if(file.equals("christof2")) {
			id = 10000017;
		} else if(file.equals("greyson2")) {
			id = 10022317;
		} else if(file.equals("pynder2")) {
			id = 10322617;
		}
	}
	
	@Override
	protected void test(PlayerCharacter pc) {
		final String format = "http://media.wizards.com/downloads/dnd/CharacterBuilder/Client/223.241754/CDNContent/Portraits/%d.png";
		assertEquals(String.format(format, id), pc.getPortrait());
	}

	
}
