package com.ncgeek.manticore.character.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ncgeek.manticore.character.Wallet;

public class WalletTests {

	private Wallet wallet;
	
	@Before
	public void setUp() throws Exception {
		wallet = new Wallet();
	}

	@Test
	public void testSetAmountLong() {
		wallet.setAmount(1000);
		assertEquals(1000, wallet.getTotalCopper());
	}

	@Test
	public void testSetAmountString() {
		wallet.setAmount("1s 5c");
		assertEquals(15, wallet.getTotalCopper());
	}

	@Test
	public void testAddLong() {
		wallet.add(10);
		assertEquals(10, wallet.getTotalCopper());
	}

	@Test
	public void testAddString() {
		wallet.add("1g 3s 4c");
		assertEquals(134, wallet.getTotalCopper());
	}

	@Test
	public void testSubtractLong() {
		wallet.setAmount(100);
		wallet.subtract(25);
		assertEquals(75, wallet.getTotalCopper());
	}

	@Test
	public void testSubtractString() {
		wallet.setAmount(100);
		wallet.subtract("5s");
		assertEquals(50, wallet.getTotalCopper());
	}

}
