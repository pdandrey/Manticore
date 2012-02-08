package com.ncgeek.android.manticore.activities;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.fragments.CharacterStatusFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class Manticore 
	extends FragmentActivity  {

	@SuppressWarnings("unused")
	private static final String LOG_TAG = "Activity - Manticore";
	
	private CharacterStatusFragment status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manticore);
		
		ManticoreStatus.initialize(this);
		
		FragmentManager fm = getSupportFragmentManager();
		status = (CharacterStatusFragment)fm.findFragmentById(R.id.manticore_character);

		status.setCharacter((ManticoreCharacter)getIntent().getSerializableExtra("character"));
	}
	
}
