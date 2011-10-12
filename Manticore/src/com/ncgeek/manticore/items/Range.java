package com.ncgeek.manticore.items;

import java.io.Serializable;

public class Range implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private byte _range1;
	private byte _range2;
	
	public Range(int range1, int range2) {
		if(range1 < 0 || range1 > Byte.MAX_VALUE)
			throw new IllegalArgumentException("Range 1 must be between 0 and " + Byte.MAX_VALUE);
		if(range2 < 0 || range2 > Byte.MAX_VALUE)
			throw new IllegalArgumentException("Range 2 must be between 0 and " + Byte.MAX_VALUE);
		
		_range1 = (byte)range1;
		_range2 = (byte)range2;
	}
	
	public int getRange1() { return _range1; }
	
	public int getRange2() { return _range2; }
	
	@Override
	public String toString() { return _range1 + "/" + _range2; }
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Range) {
			Range r = (Range)other;
			return _range1 == r._range1 && _range2 == r._range2;
		} else {
			return false;
		}
	}
}
