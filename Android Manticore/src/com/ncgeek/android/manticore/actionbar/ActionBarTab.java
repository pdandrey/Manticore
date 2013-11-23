package com.ncgeek.android.manticore.actionbar;

import com.ncgeek.android.manticore.fragments.CharacterStatsFragment;
import com.ncgeek.android.manticore.fragments.EmptyFragment;
import com.ncgeek.android.manticore.fragments.FeatListFragment;
import com.ncgeek.android.manticore.fragments.PowerListFragment;
import com.ncgeek.android.manticore.fragments.SkillListFragment;

import android.support.v4.app.Fragment;

public enum ActionBarTab {
	Stats(new CharacterStatsFragment()),
	Combat(new EmptyFragment()),
	Party(new EmptyFragment()),
	Inventory(new EmptyFragment()),
	Powers(new PowerListFragment()),
	Skills(new SkillListFragment()),
	Feats(new FeatListFragment());
	
	private Fragment fragment;
	
	ActionBarTab(Fragment fragment) {
		this.fragment = fragment;
	}
	
	public Fragment getFragment() { return fragment; }
}
