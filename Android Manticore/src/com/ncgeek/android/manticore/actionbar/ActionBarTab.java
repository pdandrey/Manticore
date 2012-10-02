package com.ncgeek.android.manticore.actionbar;

import com.ncgeek.android.manticore.fragments.CharacterStatsFragment;
import com.ncgeek.android.manticore.fragments.EmptyFragment;
import com.ncgeek.android.manticore.fragments.FeatListFragment;
import com.ncgeek.android.manticore.fragments.PowerListFragment;

import android.support.v4.app.Fragment;

public enum ActionBarTab {
	Stats(new CharacterStatsFragment()),
	Skills(new EmptyFragment()),
	Feats(new FeatListFragment()),
	Inventory(new EmptyFragment()),
	Powers(new PowerListFragment()),
	Party(new EmptyFragment()),
	Combat(new EmptyFragment());
	
	private Fragment fragment;
	
	ActionBarTab(Fragment fragment) {
		this.fragment = fragment;
	}
	
	public Fragment getFragment() { return fragment; }
}
