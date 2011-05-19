package com.ncgeek.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestbedTests {

	@Test
	public void testWhenOverwrites() {
		TestClass str = mock(TestClass.class);
		when(str.length()).thenReturn(0);
		when(str.length()).thenReturn(5);
		assertEquals(5, str.length());
	}
	
	@Test(expected=RuntimeException.class)
	public void testAfterException() {
		TestClass tc = new TestClass();
		TestClass mock = mock(TestClass.class);
		
		tc.error();
		
		// this doesn't get verified
		verify(mock).length();
	}
	
	@Test(expected=RuntimeException.class)
	public void testContainsEqualsAndLists() {
		ArrayList<String> lst = new ArrayList<String>();
		lst.add("hello");
		lst.add("world");
		lst.contains(new TestClass());
	}
	
	public class TestClass {
		public int length() { return -1; }
		public int error() { throw new RuntimeException("bad"); }
		public boolean equals(Object o) {
			throw new RuntimeException("equal called");
		}
	}
}
