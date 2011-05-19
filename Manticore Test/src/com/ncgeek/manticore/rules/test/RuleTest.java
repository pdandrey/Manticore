package com.ncgeek.manticore.rules.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.Source;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;
import com.ncgeek.manticore.rules.Specific;

public class RuleTest {

	private Rule _rule;
	
	@Before
	public void setUp() {
		_rule = new Rule("Test Rule", RuleTypes.ARMOR, "INT-ID", "http://www.ncgeek.com/", "rules-legal");
		_rule.addSpecific(new Specific("short description", "This is a short description"));
	}

	@Test
	public void testGetName() {
		assertEquals("Test Rule", _rule.getName());
	}

	@Test
	public void testGetType() {
		assertEquals(RuleTypes.ARMOR, _rule.getType());
	}

	@Test
	public void testGetURL() {
		assertEquals("http://www.ncgeek.com/", _rule.getURL());
	}

	@Test
	public void testGetInternalID() {
		assertEquals("INT-ID", _rule.getInternalID());
	}

	@Test
	public void testGetLegality() {
		assertEquals("rules-legal", _rule.getLegality());
	}
	
	@Test
	public void testGetShortDescription() {
		List<Specific> specs = _rule.getSpecifics();
		assertEquals(1, specs.size());
		Specific desc = specs.get(0);
		assertEquals("short description", desc.getName());
		assertEquals("This is a short description", desc.getValue());
	}
	
	@Test
	public void testCompare() {
		Rule other = new Rule("Rule 2", RuleTypes.ARMOR, "INT-ID", "http://www.ncgeek.com/", "rules-legal");
		assertEquals(_rule.getName().compareTo(other.getName()), _rule.compareTo(other));
	}

	@Test
	public void testSources() {
		Source s1 = Source.forName("abc");
		Source s2 = Source.forName("xyz");
		_rule.addSource(s1);
		_rule.addSource(s2);
		
		List<Source> c = _rule.getSources();
		assertEquals(2, c.size());
		assertSame(s1, c.get(0));
		assertSame(s2, c.get(1));
	}
}
