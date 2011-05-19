package com.ncgeek.manticore.powers;

public class PowerAttack {

	private AttackTypes _type;
	private String _range1;
	private String _range2;
	private String _special;
	private String _center;
	
	public AttackTypes getType() { return _type; }
	public void setType(AttackTypes type) { _type = type; }
	
	public String getRange1() { return _range1; }
	public void setRange1(String range) { _range1 = range; }
	
	public String getRange2() { return _range2; }
	public void setRange2(String range) { _range2 = range; }
	
	public String getSpecial() { return _special; }
	public void setSpecial(String special) { _special = special; }
	
	public String getCenter() { return _center; }
	public void setCenter(String center) { _center = center; }
}
