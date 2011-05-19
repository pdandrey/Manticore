package com.ncgeek.manticore.character.stats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;

public class StatTests {

	private Stat _stat;
	private List<String> _lstNames;
	private List<Addition> _lstAdds;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		_lstNames = mock(List.class);
		_lstAdds = mock(List.class);
		_stat = new Stat(_lstNames, _lstAdds);
	}

	@Test
	public void testAddAlias_Single() {
		final String alias = "ali";
		
		when(_lstNames.size()).thenReturn(0);
		
		_stat.addAlias(alias);
		
		verify(_lstNames).add(0, alias);
	}
	
	@Test
	public void testAddAlias_Multiple() {
		final String a1 = "alias";
		final String a2 = "ali";
		
		when(_lstNames.size()).thenReturn(0, 0, 1);
		when(_lstNames.get(0)).thenReturn(a1);
		
		_stat.addAlias(a1);
		_stat.addAlias(a2);
		
		verify(_lstNames).add(0, a1);
		verify(_lstNames).add(0, a2);
	}
	
	@Test
	public void testAddAlias_Multiple2() {
		final String a1 = "alias";
		final String a2 = "ali";
		
		when(_lstNames.size()).thenReturn(0, 0, 1);
		when(_lstNames.get(0)).thenReturn(a2);
		
		_stat.addAlias(a2);
		_stat.addAlias(a1);
		
		verify(_lstNames).add(0, a2);
		verify(_lstNames).add(1, a1);
	}
	
	@Test
	public void testAddAlias_Duplicate() {
		final String a = "alias";
		
		when(_lstNames.size()).thenReturn(1);
		when(_lstNames.get(0)).thenReturn(a);
		
		_stat.addAlias(a);
		
		verify(_lstNames, never()).add(anyInt(), eq(a));
	}

	@Test
	public void testGetAliases() {
		List<String> mock = new ArrayList<String>();
		mock.add("a1");
		mock.add("a2");
		_stat = new Stat(mock, null);
		
		List<String> lst = _stat.getAliases();
		
		assertEquals(2, lst.size());
		assertTrue(lst.contains("a1"));
		assertTrue(lst.contains("a2"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddAlias_Null() {
		_stat.addAlias(null);
	}
	
	@Test(expected=IllegalArgumentException.class) 
	public void testAddAlias_Empty() {
		_stat.addAlias("");
	}

	@Test
	public void testAddAddition() {
		Addition a = mock(Addition.class);
		
		_stat.addAddition(a);
		
		verify(_lstAdds).contains(a);
		verify(_lstAdds).add(a);
	}

	@Test
	public void testGetAdditions() {
		Addition a1 = mock(Addition.class);
		Addition a2 = mock(Addition.class);
		
		ArrayList<Addition> lstAdd = new ArrayList<Addition>();
		lstAdd.add(a1);
		lstAdd.add(a2);
		_stat = new Stat(null, lstAdd);
		
		List<Addition> lst = _stat.getAdditions();
		
		assertTrue(lst.contains(a1));
		assertTrue(lst.contains(a2));
		assertEquals(2, lst.size());
	}

	@Test
	public void testGetAppliedAdditions_Basic() {
		Addition a1 = mockAddition(true);
		Addition a2 = mockAddition(true);
		Addition a3 = mockAddition(false);
		
		mockAdditionIterator(a1, a2, a3);
		
		List<Addition> lst = _stat.getAppliedAdditions();
		
		assertEquals(2, lst.size());
		assertTrue(lst.contains(a1));
		assertTrue(lst.contains(a2));
		assertFalse(lst.contains(a3));
	}
	
	@Test
	public void testGetAppliedAdditions_Type() {
		Addition a1 = mockAddition(true);
		Addition a2 = mockAddition("type", 1, true);
		Addition a3 = mockAddition("type", 3, true);
		
		mockAdditionIterator(a1, a2, a3);
		
		List<Addition> lst = _stat.getAppliedAdditions();
		
		assertEquals(2, lst.size());
		assertTrue(lst.contains(a1));
		assertFalse(lst.contains(a2));
		assertTrue(lst.contains(a3));
	}

	@Test
	public void testAbsoluteValue() {
		assertEquals(0, _stat.getAbsoluteValue());
		_stat.setAbsoluteValue(5);
		assertEquals(5, _stat.getAbsoluteValue());
	}

	@Test
	public void testGetCalculatedValue() {
		Addition a = mockAddition(null, 1, true);
		Addition a2 = mockAddition(null, 3, false);
		Addition a3 = mockAddition(null, 1, true);
		mockAdditionIterator(a, a2, a3);
		
		_stat.setAbsoluteValue(4);
		assertEquals(2, _stat.getCalculatedValue());
	}

	@Test
	public void testGetModifier() {
		
		int[] values = new int[] { 7, 8, 10, 13, 14, 16, 18, 20 };
		int[] mods = new int[] { -2, -1, 0, 1, 2, 3, 4, 5};
		
		for(int i=0; i<values.length; ++i) {
			_stat.setAbsoluteValue(values[i]);
			assertEquals("mod(" + values[i] + ")", mods[i], _stat.getModifier());
		}
	}

	@Test
	public void testEqualsString() {
		mockNameIterator("Dexterity", "dex");
		
		assertTrue(_stat.equals("DEX"));
	}

	@Test
	public void testEqualsString_False() {
		mockNameIterator("Dexterity", "dex");
		
		assertFalse(_stat.equals("con"));
	}
	
	@SuppressWarnings("unchecked")
	private void mockAdditionIterator(Addition...adds) {
		Iterator<Addition> i = mock(Iterator.class);
		
		List<Boolean> lstHasNext = new ArrayList<Boolean>(Collections.nCopies(adds.length-1, true));
		lstHasNext.add(false);
		when(i.hasNext()).thenReturn(true, lstHasNext.toArray(new Boolean[0]));
		when(i.next()).thenReturn(adds[0], Arrays.copyOfRange(adds, 1, adds.length));
		
		when(_lstAdds.iterator()).thenReturn(i);
	}
	
	@SuppressWarnings("unchecked")
	private void mockNameIterator(String...adds) {
		Iterator<String> i = mock(Iterator.class);
		
		List<Boolean> lstHasNext = new ArrayList<Boolean>(Collections.nCopies(adds.length-1, true));
		lstHasNext.add(false);
		when(i.hasNext()).thenReturn(true, lstHasNext.toArray(new Boolean[0]));
		when(i.next()).thenReturn(adds[0], Arrays.copyOfRange(adds, 1, adds.length));
		
		when(_lstNames.iterator()).thenReturn(i);
	}
	
	private Addition mockAddition(boolean shouldApply) {
		return mockAddition(null, 0, shouldApply);
	}
	
	private Addition mockAddition(String type, int value, boolean shouldApply) {
		Addition a = mock(Addition.class);
		when(a.getType()).thenReturn(type);
		when(a.shouldApply()).thenReturn(shouldApply);
		when(a.getValue()).thenReturn(shouldApply ? value : 0);
		
		return a;
	}
}
