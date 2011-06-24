package com.ncgeek.manticore;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ncgeek.manticore.util.Logger;

public class Dice {
	
	private static final String LOG_TAG = "Manticore.Dice";
	private static Random _random = null;
	
	public static void setRandom(Random r) {
		Logger.warn(LOG_TAG, "Setting new Random for Dice");
		_random = r;
	}
	
	private int _sides;
	private int _count;
	private int _modifier;
	
	public Dice(int count, int sides) {
		this(count, sides, 0);
	}
	
	public Dice(int count, int sides, int modifier) {
		_sides = sides;
		_count = count;
		_modifier = modifier;
	}
	
	public Dice(String dice) {
		Pattern p = Pattern.compile("(\\d+)d(\\d+)(\\s*(\\+|\\-)\\s*(\\d+))?");
		Matcher m = p.matcher(dice);
		
		if(!m.matches())
			throw new IllegalArgumentException();
		
		_count = Integer.parseInt(m.group(1));
		_sides = Integer.parseInt(m.group(2));
		String mod = m.group(5);
		if(mod != null) {
			_modifier = Integer.parseInt(mod);
			if(m.group(4).equals("-"))
				_modifier = -_modifier;
		}
	}
	
	public int getSides() { return _sides; }
	public int getCount() { return _count; }
	public int getModifier() { return _modifier; }
	
	@Override
	public String toString() { 
		String sign = _modifier < 0 ? "-" : "+";
		if(_modifier != 0)
			return String.format("%dd%d %s %d", _count, _sides, sign, Math.abs(_modifier));
		else
			return String.format("%dd%d", _count, _sides); 
	}
	
	public int roll() { 
		if(_random == null)
			_random = new Random();
		
		int sum = 0;
		for(int i=0; i<_count; ++i) {
			int roll = _random.nextInt(_sides) + 1;
			sum += roll;
		}
		
		return sum + _modifier;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Dice) {
			Dice d = (Dice)other;
			return _sides == d._sides && _count == d._count && _modifier == d._modifier;
		} else {
			return false;
		}
	}
}
