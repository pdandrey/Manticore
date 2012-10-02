package com.ncgeek.android.manticore.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.fragments.PowerCardFragment;
import com.ncgeek.manticore.character.CharacterPower;
import com.ncgeek.manticore.util.Logger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class PowerViewer extends FragmentActivity {

	private static final String LOG_TAG = "Power Viewer";
	
	private PowerPagerAdapter adapter;
	private List<CharacterPower> cp;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.powerview);
		
		int position;
		
		if(savedInstanceState != null) {
			cp = (List<CharacterPower>)savedInstanceState.get("Powers");
			position = -1;
		} else {
			cp = (List<CharacterPower>)getIntent().getExtras().get("Powers");
			position = getIntent().getExtras().getInt("Position");
		}
		
		adapter = new PowerPagerAdapter(getSupportFragmentManager(), cp);
		ViewPager vp = (ViewPager)findViewById(R.id.vpPowers);
		vp.setAdapter(adapter);
		
		if(position != -1)
			vp.setCurrentItem(position);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("Powers", (Serializable)cp);
	}
	
	private static class PowerPagerAdapter extends FragmentPagerAdapter {

		private List<PowerCardFragment> lstPowers;
		
		public PowerPagerAdapter(FragmentManager fm, List<CharacterPower> lst) {
			super(fm);
			lstPowers = new ArrayList<PowerCardFragment>(lst.size());
			for(CharacterPower cp : lst) {
				PowerCardFragment pcf = new PowerCardFragment();
				pcf.setPower(cp);
				lstPowers.add(pcf);
			}
		}

		@Override
		public Fragment getItem(int position) {
			return lstPowers.get(position);
		}

		@Override
		public int getCount() {
			return lstPowers.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return lstPowers.get(position).getPower().getPower().getName();
		}
		
	}
}
