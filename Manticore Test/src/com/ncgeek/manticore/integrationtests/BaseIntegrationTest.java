package com.ncgeek.manticore.integrationtests;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.parsers.CharacterParser;
import com.ncgeek.manticore.test.TestLogger;
import com.ncgeek.manticore.util.Logger;

public abstract class BaseIntegrationTest {

	private CharacterParser parser;
	private File directory;
	private ManticoreDatabase db;
	private File file;
	
	@Before
	public void setup() {
		directory = new File(".\\docs\\");
		db = new ManticoreDatabase(new File("c:\\users\\walynkyle\\documents\\my dropbox\\manticore\\manticore.db"));
		parser = new CharacterParser(db);
		file = null;
		Logger.setLogger(new TestLogger());
	}
	
	@After
	public void cleanup() {
		db.close();
	}
	
	@Test
	public void testChase() {
		file = new File(directory, "chase2.dnd4e");
		setupCharacterValues();
		test(parser.parse(file));
	}
	
	@Test
	public void testPynder() {
		file = new File(directory, "pynder2.dnd4e");
		setupCharacterValues();
		test(parser.parse(file));
	}
	
	@Test
	public void testAlek() {
		file = new File(directory, "alek2.dnd4e");
		setupCharacterValues();
		test(parser.parse(file));
	}
	
	@Test
	public void testChristof() {
		file = new File(directory, "christof2.dnd4e");
		setupCharacterValues();
		test(parser.parse(file));
	}
	
	@Test
	public void testGreyson() {
		file = new File(directory, "greyson2.dnd4e");
		setupCharacterValues();
		test(parser.parse(file));
	}
	
	protected final File getFile() { return file; }
	
	protected abstract void test(PlayerCharacter pc);
	
	protected abstract void setupCharacterValues();
}
