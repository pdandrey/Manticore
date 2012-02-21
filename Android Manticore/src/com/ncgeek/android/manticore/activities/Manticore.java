package com.ncgeek.android.manticore.activities;

import java.util.Observable;
import java.util.Observer;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.actionbar.ActionBarHelper;
import com.ncgeek.android.manticore.actionbar.ActionBarTab;
import com.ncgeek.android.manticore.fragments.CharacterStatusFragment;
import com.ncgeek.android.manticore.fragments.PowerListFragment;
import com.ncgeek.manticore.util.Logger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Manticore 
	extends FragmentActivity
	implements Observer {

	@SuppressWarnings("unused")
	private static final String LOG_TAG = "Activity - Manticore";
	
	private CharacterStatusFragment status;
	private ActionBarHelper actionBar;
	private ManticoreCharacter character;
	private Fragment current;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manticore);
		
		ManticoreStatus.initialize(this);
		
		FragmentManager fm = getSupportFragmentManager();
		//status = (CharacterStatusFragment)fm.findFragmentById(R.id.manticore_character);

		if(savedInstanceState == null) 
			character = (ManticoreCharacter)getIntent().getSerializableExtra("character");
		else
			character = (ManticoreCharacter)savedInstanceState.getSerializable("character");
		//status.setCharacter(character);
	
		actionBar = ActionBarHelper.createInstance(this);
		actionBar.onCreate(savedInstanceState);
		actionBar.addObserver(this);
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
		ActionBarTab tab = (ActionBarTab)data;
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		
		switch(tab) {
			case Powers:
				PowerListFragment plf = (PowerListFragment)fm.findFragmentByTag("Powers");
				if(plf == null)
					plf = new PowerListFragment();
				current = plf;
				trans.add(R.id.frmManticore, plf, "Powers");
				break;
			default:
				if(current != null)
					trans.remove(current);
				current = null;
				break;
		}
		
		trans.commit();
	}
	
	public final ManticoreCharacter getCharacter() { return character; }
}
