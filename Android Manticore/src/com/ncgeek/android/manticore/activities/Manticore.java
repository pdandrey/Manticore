package com.ncgeek.android.manticore.activities;

import java.util.Observable;
import java.util.Observer;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.actionbar.ActionBarHelper;
import com.ncgeek.android.manticore.actionbar.ActionBarTab;
import com.ncgeek.manticore.util.Logger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class Manticore 
	extends FragmentActivity
	implements Observer {

	private static final String LOG_TAG = "Activity - Manticore";
	
	private ActionBarHelper actionBar;
	private ManticoreCharacter character;
	private ManticorePagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manticore);
		
		ManticoreStatus.initialize(this);
		
		if(savedInstanceState == null) 
			character = (ManticoreCharacter)getIntent().getSerializableExtra("character");
		else
			character = (ManticoreCharacter)savedInstanceState.getSerializable("character");
		
		adapter = new ManticorePagerAdapter(getSupportFragmentManager());
		
		ViewPager vp = ((ViewPager)findViewById(R.id.vpManticore));
		vp.setAdapter(adapter);
		vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) { 
				actionBar.setSelectedTab(position);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
			
			@Override
			public void onPageScrollStateChanged(int state) { }
		});
		
		actionBar = ActionBarHelper.createInstance(this);
		actionBar.addObserver(this);
		actionBar.onCreate(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		actionBar.onSaveInstanceState(outState);
		outState.putSerializable("character", character);
	}

	@Override
	public void update(Observable observable, Object data) {
		Logger.verbose(LOG_TAG, "Tab Selected: %s", data);
		int position = (Integer)data;
		((ViewPager)findViewById(R.id.vpManticore)).setCurrentItem(position);
	}
	
	public final ManticoreCharacter getCharacter() { return character; }
	
	private class ManticorePagerAdapter extends FragmentPagerAdapter {
		
		public ManticorePagerAdapter(FragmentManager fm) { 
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ActionBarTab.values()[position].getFragment();
		}

		@Override
		public int getCount() {
			return ActionBarTab.values().length;
		}
	}
}
