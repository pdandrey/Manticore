package com.ncgeek.manticore.character.stats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.ncgeek.manticore.character.RuleEventArgs;
import com.ncgeek.manticore.character.inventory.EquipmentManagerEventArgs;
import com.ncgeek.manticore.character.inventory.EquipmentManagerEventType;
import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.character.stats.StatBlock;
import com.ncgeek.manticore.items.EquippableItem;

public class StatBlockTests {

	private Map<String, Stat> _mapStats;
	private Map<String, List<Addition>> _mapLinks;
	private StatBlock _block;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		_mapStats = (Map<String,Stat>)mock(Map.class);
		_mapLinks = (Map<String,List<Addition>>)mock(Map.class);
		_block = new StatBlock(_mapStats, _mapLinks);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAdd() {
		Stat s = mockStat("Charisma", "CHA");
		_block.add(s);
		
		verify(s).getAdditions();
		verify(s).getAliases();
		
		verify(_mapStats).put("charisma", s);
		verify(_mapStats).put("cha", s);
		verify(_mapLinks, never()).put(anyString(), any(List.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAdd_NeedsLink() {
		Stat s = mockStat("Arcana");
		Addition a = mockAddition("int");
		s.addAddition(a);
		
		_block.add(s);
		
		verify(s).getAdditions();
		verify(s).getAliases();
		
		verify(_mapStats).put("arcana", s);
		
		ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
		verify(_mapLinks).put(eq("int"), captor.capture());
		
		List<Addition> lst = captor.getValue();
		assertEquals(1, lst.size());
		assertSame(a, lst.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAdd_LinkCreated() {
		Stat s = mockStat("Arcana");
		Stat iStat = mockStat("Int");
		
		Addition a = mockAddition("int");
		s.addAddition(a);
		
		when(_mapStats.containsKey("int")).thenReturn(true);
		when(_mapStats.get("int")).thenReturn(iStat);
		
		_block.add(s);
		
		verify(s).getAdditions();
		verify(s).getAliases();
		
		verify(_mapStats).put("arcana", s);
		
		verify(_mapLinks, never()).put(eq("int"), any(List.class));
		verify(a).setStatLink(iStat);
	}
	
	@Test
	public void testAdd_AutoLink() {
		Stat s = mockStat("Arcana");
		Stat iStat = mockStat("Int");
		
		Addition a = mockAddition("int");
		s.addAddition(a);
		
		List<Addition> lst = spy(new ArrayList<Addition>());
		lst.add(a);
		
		when(_mapLinks.containsKey("int")).thenReturn(true);
		when(_mapLinks.get("int")).thenReturn(lst);
		
		_block.add(iStat);
		
		verify(iStat).getAliases();
		verify(_mapLinks).containsKey("int");
		verify(_mapLinks).get("int");
		verify(a).setStatLink(iStat);
		
		verify(_mapStats).put("int", iStat);
		
		verify(_mapLinks).remove("int");
	}

	@Test
	public void testGet() {
		Stat s = mockStat("int", "intel");
		
		when(_mapStats.get("int")).thenReturn(s);
		when(_mapStats.get("intel")).thenReturn(s);
		
		assertSame(s, _block.get("int"));
		assertSame(s, _block.get("intel"));
		
		
		verify(_mapStats).get("int");
		verify(_mapStats).get("intel");
	}
	
	@Test
	public void testGet_NotAdded() {
		assertNull(_block.get("no such stat"));
		verify(_mapStats).get("no such stat");
	}

	@Test
	public void testGetStats() {
		Stat s1 = mockStat("s1");
		Stat s2 = mockStat("s2");
		
		HashMap<String,Stat> map = new HashMap<String,Stat>();
		map.put("s1", s1);
		map.put("s2", s2);
		
		_block = new StatBlock(map, null);
		
		Collection<Stat> lst = _block.getStats();
		
		assertEquals(2, lst.size());
		assertTrue(lst.contains(s1));
		assertTrue(lst.contains(s2));
	}

	@Test
	public void testUpdate_Equipment() {
		EquipmentManagerEventArgs args = mock(EquipmentManagerEventArgs.class);
		Stat s = mockStat("stat");
		Addition a1 = mockAddition(null);
		Addition a2 = mockAddition(null);
		
		Set<String> set = new HashSet<String>();
		set.add("stat");
		set.add("st");
		
		when(_mapStats.keySet()).thenReturn(set);
		when(_mapStats.get("stat")).thenReturn(s);
		when(_mapStats.get("st")).thenReturn(s);
		
		s.addAddition(a1);
		s.addAddition(a2);
		
		_block.update(null, args);
		
		verify(a1).process(args);
		verify(a2).process(args);
	}
	
	@Test
	public void testUpdate_Rule() {
		RuleEventArgs args = mock(RuleEventArgs.class);
		Stat s = mockStat("stat", "st");
		Addition a1 = mockAddition(null);
		Addition a2 = mockAddition(null);
		
		Set<String> set = new HashSet<String>();
		set.add("stat");
		set.add("st");
		
		when(_mapStats.keySet()).thenReturn(set);
		when(_mapStats.get("stat")).thenReturn(s);
		when(_mapStats.get("st")).thenReturn(s);
		
		s.addAddition(a1);
		s.addAddition(a2);
		
		_block.update(null, args);
		
		verify(a1).process(args);
		verify(a2).process(args);
	}

	private Stat mockStat(String...aliases) {
		Stat s = spy(new Stat());
		for(String str : aliases)
			s.addAlias(str);
		return s;
	}
	
	private Addition mockAddition(String name) {
		Addition a = mock(Addition.class);
		if(name != null)
			when(a.getStatLink()).thenReturn(name);
		return a;
	}
}
