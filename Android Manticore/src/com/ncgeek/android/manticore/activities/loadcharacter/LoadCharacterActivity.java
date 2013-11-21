package com.ncgeek.android.manticore.activities.loadcharacter;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.Manticore;
import com.ncgeek.android.manticore.activities.PreferenceViewer;
import com.ncgeek.android.manticore.activities.Preferences;
import com.ncgeek.android.manticore.data.model.CharacterModel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class LoadCharacterActivity 
	extends FragmentActivity 
	implements ICharacterListCallback {

	@SuppressWarnings("unused")
	private static final String LOG_TAG = "LoadCharacterActivity";
	
	private ImportCharacterFragment fgmtImport;
	private CharacterInfoFragment fgmtInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManticoreStatus.initialize(this);
		setContentView(R.layout.loadcharacter);
		
		FragmentManager mgr = getSupportFragmentManager();
		fgmtImport =(ImportCharacterFragment)mgr.findFragmentByTag("Import");
		if(fgmtImport == null)
				fgmtImport = new ImportCharacterFragment();
		
		fgmtInfo = (CharacterInfoFragment)mgr.findFragmentByTag("Info");
		if(fgmtInfo == null)
			fgmtInfo = new CharacterInfoFragment();
		
		if(savedInstanceState != null) {
			findViewById(R.id.loadcharacter_frame).setVisibility(savedInstanceState.getInt("infoVisibility"));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("infoVisibility", findViewById(R.id.loadcharacter_frame).getVisibility());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.loadcharacter, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent i = null;
	    switch(item.getItemId()) {
		    case R.id.loadcharacter_mnuPreferences:
		        i = new Intent(this, Preferences.class);
		        startActivity(i);
		        return true;
		    case R.id.loadcharacter_mnuViewPrefs:
		    	i = new Intent(this, PreferenceViewer.class);
		    	startActivity(i);
		    	return true;
		    	
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onCharacterSelected(CharacterModel character) {
		FragmentManager mgr = getSupportFragmentManager();
		
		if(mgr.findFragmentByTag("Info") == null) {
			FragmentTransaction trans = mgr.beginTransaction();
			trans.replace(R.id.loadcharacter_frame, fgmtInfo, "Info");
			trans.commit();
		}
		fgmtInfo.displayCharacter(character);
		
		findViewById(R.id.loadcharacter_frame).setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onImportCharacter() {
		FragmentManager mgr = getSupportFragmentManager();
		
		if(mgr.findFragmentByTag("Import") == null) {
			FragmentTransaction trans = mgr.beginTransaction();
			trans.replace(R.id.loadcharacter_frame, fgmtImport, "Import");
			trans.commit();
		} else {
			fgmtImport.fillData();
		}
		
		findViewById(R.id.loadcharacter_frame).setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onCharacterLoaded(ManticoreCharacter character) {
		Intent i = new Intent(this, Manticore.class);
		i.putExtra("character", (Parcelable)character);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.setAction(Intent.ACTION_VIEW);
		
		if(getIntent().getCategories().contains(Intent.CATEGORY_LAUNCHER)) {
			// We started up, so we need to start the main activity
			startActivity(i);
		} else {
			// Hopefully we were called with startActivityForResult(), so let's set the result
			setResult(RESULT_OK, i);
		}
		
		finish();
	}	
}
