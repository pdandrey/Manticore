package com.ncgeek.manticore.powers.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.Source;
import com.ncgeek.manticore.powers.Power;
import com.ncgeek.manticore.powers.PowerActions;
import com.ncgeek.manticore.powers.PowerSpecific;
import com.ncgeek.manticore.powers.PowerTypes;

public class PowerTests {

	private Power power;
	private List<Source> lstSources;
	private List<PowerSpecific> lstPowerSpecifics;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		lstSources = mock(List.class);
		lstPowerSpecifics = mock(List.class);
		power = new Power(lstSources, lstPowerSpecifics);
	}

	@Test
	public void testGetName() {
		final String name = "test name";
		power.setName(name);
		assertEquals(name, power.getName());
	}

	@Test
	public void testGetFlavor() {
		final String flavor = "test flavor";
		power.setFlavor(flavor);
		assertEquals(flavor, power.getFlavor());
	}

	@Test
	public void testGetAction() {
		final PowerActions action = PowerActions.MoveAction;
		power.setAction(action);
		assertSame(action, power.getAction());
	}

	@Test
	public void testGetType() {
		final PowerTypes type = PowerTypes.Attack;
		power.setType(type);
		assertSame(type, power.getType());
	}

	@Test
	public void testGetDisplay() {
		final String display = "test display";
		power.setDisplay(display);
		assertEquals(display, power.getDisplay());
	}

	@Test
	public void testGetLevel() {
		final int level = 7;
		power.setLevel(level);
		assertEquals(level, power.getLevel());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetSources() {
		power = new Power();
		power.add(Source.forName("test source"));
		List<Source> lst = power.getSources();
		assertEquals(1, lst.size());
		lst.add(null);
	}

	@Test
	public void testAddSource() {
		Source s = Source.forName("test source");
		power.add(s);
		verify(lstSources).add(s);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetSpecifics() {
		power = new Power();
		PowerSpecific ps = mock(PowerSpecific.class);
		power.add(ps);
		List<PowerSpecific> lst = power.getSpecifics();
		assertEquals(1, lst.size());
		lst.add(null);
	}

	@Test
	public void testAddPowerSpecific() {
		PowerSpecific ps = mock(PowerSpecific.class);
		power.add(ps);
		verify(lstPowerSpecifics).add(ps);
	}

}
