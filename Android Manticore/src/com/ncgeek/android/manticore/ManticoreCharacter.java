package com.ncgeek.android.manticore;

import android.graphics.Bitmap;

import com.ncgeek.manticore.character.PlayerCharacter;

public class ManticoreCharacter extends PlayerCharacter {

	private static final long serialVersionUID = 1L;

	private Bitmap _portrait;
	
	public Bitmap getPortrait() { return _portrait; }
	public void setPortrait(Bitmap portrait) { _portrait = portrait; }
}
