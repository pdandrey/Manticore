package com.ncgeek.manticore.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.Source;

public class SourceTest {

	private Source _source;
	private static final String SOURCE_NAME = "My Source";
	private static final String SOURCE_NAME_2 = "My Source 2";
	
	@Before
	public void setupTests() throws Exception {
		_source = Source.forName(SOURCE_NAME);
	}
	
	@Test
	public void testForName() {
		assertNotNull(_source);
	}

	@Test
	public void testGetName() {
		assertEquals(SOURCE_NAME, _source.getName());
	}
	
	@Test
	public void testForSameNameReturnsSameObject() {
		Source s2 = Source.forName(SOURCE_NAME);
		assertSame(_source, s2);
	}
	
	@Test
	public void testForDifferentNameReturnsDifferentObjects() {
		Source s2 = Source.forName(SOURCE_NAME_2);
		assertNotSame(_source, s2);
	}
}
