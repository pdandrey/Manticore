package com.ncgeek.manticore;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Money implements Comparable<Money>, Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final long COPPER_PER_GOLD = 100;
    private static final long COPPER_PER_SILVER = 10;
    private static final long COPPER_PER_PLAT = 10000;
    private static final long COPPER_PER_ASTRAL = 1000000;
	
	protected long _total;
	
	public Money(long price) {
		_total = price;
	}
	
	public Money(String price) {
		_total = parseAmount(price);
	}
	
	protected long parseAmount(String amount) {
		Pattern p = Pattern.compile("(\\d+)([apgsc])");
		Matcher m = p.matcher(amount);
		
		long sum = 0;
		
		while(m.find()) {
			long lAmount = Long.parseLong(m.group(1));
			long multiplier = 1;
			switch(m.group(2).charAt(0)) {
				case 'a': multiplier = COPPER_PER_ASTRAL; break;
				case 'p': multiplier = COPPER_PER_PLAT; break;
				case 'g': multiplier = COPPER_PER_GOLD; break;
				case 's': multiplier = COPPER_PER_SILVER; break;
			}
			sum += (lAmount * multiplier);
		}
		
		return sum;
	}
	
	public long getTotalCopper() { return _total; }
	public int getCopper() { return (int)(_total % COPPER_PER_SILVER); }
	public int getSilver() { return (int)(_total % COPPER_PER_GOLD / COPPER_PER_SILVER); }
	public int getGold() { return (int)(_total % COPPER_PER_PLAT / COPPER_PER_GOLD); }
	public int getPlatinum() { return (int)(_total % COPPER_PER_ASTRAL / COPPER_PER_PLAT); }
	public int getAstral() { return (int)(_total / COPPER_PER_ASTRAL); }
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		
		buf.append(getPriceString(getAstral(), "a"));
		buf.append(getPriceString(getPlatinum(), "p"));
		buf.append(getPriceString(getGold(), "g"));
		buf.append(getPriceString(getSilver(), "s"));
		buf.append(getPriceString(getCopper(), "c"));
		
		return buf.toString().trim();
	}
	
	private String getPriceString(int price, String suffix) {
		if(price == 0)
			return "";
		else
			return String.format("%d%s ", price, suffix);
	}

	@Override
	public int compareTo(Money arg0) {
		return (int)(_total - arg0._total);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Money)
			return _total == ((Money)other)._total;
		else
			return false;
	}
	
	public Money add(Money other) {
		return new Money(_total + other._total);
	}
}
