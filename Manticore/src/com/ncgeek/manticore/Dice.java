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
	
	public Dice(int count, int sides) {
		_sides = sides;
		_count = count;
	}
	
	public Dice(String dice) {
		Pattern p = Pattern.compile("(\\d+)d(\\d+)");
		Matcher m = p.matcher(dice);
		
		if(!m.matches())
			throw new IllegalArgumentException();
		
		_count = Integer.parseInt(m.group(1));
		_sides = Integer.parseInt(m.group(2));
	}
	
	public int getSides() { return _sides; }
	public int getCount() { return _count; }
	
	@Override
	public String toString() { return _count + "d" + _sides; }
	
	public int roll() { 
		if(_random == null)
			_random = new Random();
		
		int sum = 0;
		for(int i=0; i<_count; ++i) {
			int roll = _random.nextInt(_sides) + 1;
			sum += roll;
		}
		
		return sum;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Dice) {
			Dice d = (Dice)other;
			return _sides == d._sides && _count == d._count;
		} else {
			return false;
		}
	}
}
