package com.ncgeek.manticore.rules.test;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.rules.Specific;

public class SpecificTest {

	private Specific _specific;
	
	private static final String SPECIFIC_NAME = "My Name";
	private static final String SPECIFIC_VALUE = "My Value";
	
	@Before
	public void setUp() throws Exception {
		_specific = new Specific(SPECIFIC_NAME, SPECIFIC_VALUE);
	}
	
	@Test
	public void testGetName() {
		assertEquals(SPECIFIC_NAME, _specific.getName());
	}
	
	@Test
	public void testGetValue() {
		assertEquals(SPECIFIC_VALUE, _specific.getValue());
	}

}
