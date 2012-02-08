package com.ncgeek.manticore.items;

import java.io.Serializable;

public final class MagicItemTarget implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String _target;
	private String _subtarget;
	private boolean _isArmor;
	
	public MagicItemTarget(String target, String subtarget, boolean isArmor) {
		_target = target;
		_subtarget = subtarget;
		_isArmor = isArmor;
	}
	
	public String getTarget() { return _target; }
	
	public String getSubtarget() { return _subtarget; }
	
	public boolean isArmor() { return _isArmor; }
	
	public boolean isWeapon() { return !_isArmor; }
}
