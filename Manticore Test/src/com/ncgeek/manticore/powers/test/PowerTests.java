package com.ncgeek.manticore.powers.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.ncgeek.manticore.Source;
import com.ncgeek.manticore.powers.Power;
import com.ncgeek.manticore.powers.PowerActions;
import com.ncgeek.manticore.powers.PowerSpecific;
import com.ncgeek.manticore.powers.PowerTypes;

public class PowerTests {

	private Power power;
	private List<Source> lstSources;
	private List<Power.Specific> lstPowerSpecifics;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		lstSources = mock(List.class);
		lstPowerSpecifics = mock(List.class);
		power = new Power(lstPowerSpecifics);
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
		power.addSpecific("Flavor", flavor);
		assertEquals(flavor, power.getFlavor());
	}

	@Test
	public void testGetAction() {
		final PowerActions action = PowerActions.MoveAction;
		power.addSpecific("Action Type", action.getName());
		assertSame(action, power.getAction());
	}

	@Test
	public void testGetType() {
		final PowerTypes type = PowerTypes.Attack;
		power.addSpecific("Power Type", type.getName());
		assertSame(type, power.getPowerType());
	}

	@Test
	public void testGetDisplay() {
		final String display = "test display";
		power.addSpecific("Display", display);
		assertEquals(display, power.getDisplay());
	}

	@Test
	public void testGetLevel() {
		final int level = 7;
		power.addSpecific("Level", level + "");
		assertEquals(level, power.getLevel());
	}

	@Ignore
	@Test(expected = UnsupportedOperationException.class)
	public void testGetSources() {
//		power = new Power();
//		power.add(Source.forName("test source"));
//		List<Source> lst = power.getSources();
//		assertEquals(1, lst.size());
//		lst.add(null);
		fail("Not Implemented");
	}

	@Ignore
	@Test
	public void testAddSource() {
//		Source s = Source.forName("test source");
//		power.add(s);
//		verify(lstSources).add(s);
		fail("Not Implemented");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetSpecifics() {
		power = new Power();
		power.addSpecific("Test", "Value");
		List<Power.Specific> lst = power.getSpecifics();
		assertEquals(1, lst.size());
		lst.add(null);
	}

	@Test
	public void testAddPowerSpecific() {
		power.addSpecific("Test", "Value");
		ArgumentCaptor<Power.Specific> ac = ArgumentCaptor.forClass(Power.Specific.class);
		verify(lstPowerSpecifics).add(ac.capture());
		assertEquals("Name", "Test", ac.getValue().getName());
		assertEquals("Value", "Value", ac.getValue().getValue());
	}

}
