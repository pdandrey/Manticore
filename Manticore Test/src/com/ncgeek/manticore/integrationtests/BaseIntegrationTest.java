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
//		File dbFile = new File("c:\\users\\walynkyle\\documents\\my dropbox\\manticore\\manticore.db");
//		if(dbFile.exists())
//			db = new ManticoreDatabase(dbFile);
		parser = new CharacterParser(db);
		file = null;
		Logger.setLogger(new TestLogger());
	}
	
	@After
	public void cleanup() {
		if(db != null)
			db.close();
	}
	
	@Test
	public void testChase() {
		Logger.verbose("BaseIntegerationTest", "Starting Chase");
		file = new File(directory, "chase2.dnd4e");
		setupCharacterValues();
		test(parser.parse(file));
	}
	
	@Test
	public void testPynder() {
		Logger.verbose("BaseIntegerationTest", "Starting Pynder");
		file = new File(directory, "pynder2.dnd4e");
		setupCharacterValues();
		test(parser.parse(file));
	}
	
	@Test
	public void testAlek() {
		Logger.verbose("BaseIntegerationTest", "Starting Alek");
		file = new File(directory, "alek2.dnd4e");
		setupCharacterValues();
		test(parser.parse(file));
	}
	
	@Test
	public void testChristof() {
		Logger.verbose("BaseIntegerationTest", "Starting Christof");
		file = new File(directory, "christof2.dnd4e");
		setupCharacterValues();
		test(parser.parse(file));
	}
	
	@Test
	public void testGreyson() {
		Logger.verbose("BaseIntegerationTest", "Starting Greyson");
		file = new File(directory, "greyson2.dnd4e");
		setupCharacterValues();
		test(parser.parse(file));
	}
	
	protected final File getFile() { return file; }
	
	protected abstract void test(PlayerCharacter pc);
	
	protected abstract void setupCharacterValues();
}
