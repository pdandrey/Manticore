package com.ncgeek.manticore.character;

import java.io.Serializable;

import com.ncgeek.manticore.Money;

public class Wallet extends Money implements Serializable {

	private static final long serialVersionUID = 1L;

	public Wallet() {
		super(0);
	}
	
	public Wallet(long total) {
		super(total);
	}
	
	public Wallet(String money) {
		super(money);
	}
	
	public void setAmount(long amount) {
		_total = amount;
	}
	
	public void setAmount(String amount) {
		_total = parseAmount(amount);
	}
	
	public void add(long amount) {
		_total += amount;
	}
	
	public void add(String amount) {
		_total += parseAmount(amount);
	}
	
	public void subtract(long amount) {
		_total -= amount;
	}
	
	public void subtract(String amount) {
		_total -= parseAmount(amount);
	}
}
