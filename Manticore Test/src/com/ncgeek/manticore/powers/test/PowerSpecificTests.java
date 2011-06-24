package com.ncgeek.manticore.powers.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.powers.PowerAttack;
import com.ncgeek.manticore.powers.PowerKeywords;
import com.ncgeek.manticore.powers.PowerSpecific;
import com.ncgeek.manticore.powers.PowerSpecificAttribute;
import com.ncgeek.manticore.powers.PowerUsages;

public class PowerSpecificTests {

	private PowerSpecific spec;
	private List<PowerSpecificAttribute> lstAttr;
	private EnumSet<PowerKeywords> setKeywords;
	private EnumSet<PowerKeywords> setAlt;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		lstAttr = mock(List.class);
		setKeywords = mock(EnumSet.class);
		setAlt = mock(EnumSet.class);
		spec = new PowerSpecific(lstAttr, setKeywords, setAlt);
	}

	@Test
	public void testIsSpecial() {
		assertFalse(spec.isSpecial());
		spec.setSpecial(true);
		assertTrue(spec.isSpecial());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testGetAttributes() {
		spec = new PowerSpecific();
		PowerSpecificAttribute attr = new PowerSpecificAttribute(null, null);
		spec.add(attr);
		List<PowerSpecificAttribute> lst = spec.getAttributes();
		assertEquals(1, lst.size());
		lst.add(null);
	}

	@Test
	public void testAddPowerSpecificAttribute() {
		PowerSpecificAttribute attr = new PowerSpecificAttribute(null, null);
		spec.add(attr);
		verify(lstAttr).add(attr);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetKeywords() {
		spec = new PowerSpecific();
		spec.add(PowerKeywords.Cold);
		Set<PowerKeywords> kw = spec.getKeywords();
		assertEquals(1, kw.size());
		assertTrue(kw.contains(PowerKeywords.Cold));
		kw.add(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testGetAlternativeKeywords() {
		spec = new PowerSpecific();
		spec.add(PowerKeywords.Cold, true);
		Set<PowerKeywords> kw = spec.getAlternativeKeywords();
		assertEquals(1, kw.size());
		assertTrue(kw.contains(PowerKeywords.Cold));
		kw.add(null);
	}
	
	@Test
	public void testGetAlternativeKeywords_NoAlts() {
		spec = new PowerSpecific();
		Set<PowerKeywords> kw = spec.getAlternativeKeywords();
		assertNull(kw);
	}

	@Test
	public void testAddPowerKeywords() {
		spec.add(PowerKeywords.Fire);
		verify(setKeywords).add(PowerKeywords.Fire);
		verify(setAlt, never()).add(PowerKeywords.Fire);
	}

	@Test
	public void testAddPowerKeywordsBoolean() {
		spec.add(PowerKeywords.Beast, true);
		verify(setKeywords, never()).add(PowerKeywords.Beast);
		verify(setAlt).add(PowerKeywords.Beast);
	}

	@Test
	public void testGetAttack() {
		assertNull(spec.getAttack());
		PowerAttack atk = mock(PowerAttack.class);
		spec.setAttack(atk);
		assertSame(atk, spec.getAttack());
	}

}
