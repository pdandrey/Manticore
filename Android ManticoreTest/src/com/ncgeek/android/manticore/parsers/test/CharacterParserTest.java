package com.ncgeek.android.manticore.parsers.test;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.util.ManticoreAndroidLogger;
import com.ncgeek.android.manticore.mock.MockHandler;
import com.ncgeek.android.manticore.mock.MockRepository;
import com.ncgeek.android.manticore.parsers.CharacterParser;
import com.ncgeek.android.manticore.test.TestLogger;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.util.Logger;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

public class CharacterParserTest extends InstrumentationTestCase {
	
	private MockRepository repos;
	private MockHandler handler;
	private CharacterParser parser;
	private AssetManager assets;
	private TestLogger logger;
	
	public CharacterParserTest() {

	}
	
	@Override
	public void setUp() {
		repos = new MockRepository();
		handler = new MockHandler();
		parser = new CharacterParser(repos, handler);
		
		assets = getInstrumentation().getContext().getAssets();
		
		logger = new TestLogger();
		ManticorePreferences prefs = new ManticorePreferences(getInstrumentation().getContext());
		ManticoreAndroidLogger log = new ManticoreAndroidLogger(prefs, null);
		Logger.setLogger(log);
	}
	
	@Override
	public void tearDown() {
		List<String> logs = logger.getLogs();
		for(String s : logs) {
			System.out.println(s);
		}
		System.out.flush();
	}
	
	public void testChase2() {
		PlayerCharacter pc = null;
		
		try {
			InputStream in = assets.open("Chase2.dnd4e");
			pc = parser.parse(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		assertNotNull(pc);
	}
}
