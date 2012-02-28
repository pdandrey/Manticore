package com.ncgeek.android.manticore.actionbar;

import com.ncgeek.android.manticore.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.view.View;

public class ActionBarHelperHoneycomb extends ActionBarHelper {

	public ActionBarHelperHoneycomb(Activity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ActionBar.TabListener listener = new ActionBar.TabListener() {
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				ActionBarHelperHoneycomb.this.setChanged();
				ActionBarHelperHoneycomb.this.notifyObservers(tab.getPosition());
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
			}
		};
		
		getActivity().findViewById(R.id.actionbar).setVisibility(View.GONE);
		
		ActionBar bar = getActivity().getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled(false);

		for(ActionBarTab tab : ActionBarTab.values()) {
			bar.addTab(bar.newTab().setText(tab.toString()).setTag(tab).setTabListener(listener));
		}
		
		int position = 0;
		if(savedInstanceState != null)
			position = savedInstanceState.getInt("ActionBarPosition");
		
		bar.setSelectedNavigationItem(position);
		
		setChanged();
		notifyObservers(bar.getSelectedTab().getPosition());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("ActionBarPosition", getActivity().getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public void setSelectedTab(int position) {
		ActionBar bar = getActivity().getActionBar();
		bar.setSelectedNavigationItem(position);
	}
}
