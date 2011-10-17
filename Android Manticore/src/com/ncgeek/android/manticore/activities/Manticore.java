package com.ncgeek.android.manticore.activities;

import java.io.File;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.fragments.CharacterStatusFragment;
import com.ncgeek.android.manticore.loaders.Dnd4eLoader;
import com.ncgeek.manticore.util.Logger;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class Manticore 
	extends FragmentActivity 
	implements LoaderManager.LoaderCallbacks<ManticoreCharacter> {

	private static final String LOG_TAG = "Activity - Manticore";
	
	private CharacterStatusFragment status;
	
	private static enum Loaders {
		Dnd4eLoader(0);
		
		public static Loaders getLoader(int id) {
			for(Loaders l : Loaders.values())
				if(l.getID() == id)
					return l;
			return null;
		}
		private int id;
		Loaders(int id) { this.id = id; }
		public int getID() { return id; }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manticore);
		
		ManticoreStatus.initialize(this);
		
		FragmentManager fm = getSupportFragmentManager();
		status = (CharacterStatusFragment)fm.findFragmentById(R.id.manticore_character);

		LoaderManager lm = getSupportLoaderManager();
		Bundle b = new Bundle();
		b.putString("file", "Alek2.dnd4e");
		lm.initLoader(Loaders.Dnd4eLoader.getID(), b, this);
		
		/*ManticoreCharacter pc = new ManticoreCharacter();
		Stat s = new Stat();
		Addition a = new Addition();
		a.setLevel(1);
		a.setValue(10);
		s.addAlias("Hit Points");
		s.addAddition(a);
		pc.getStats().add(s);
		
		s = new Stat();
		a = new Addition();
		a.setLevel(1);
		a.setValue(6);
		s.addAlias("Healing Surges");
		s.addAddition(a);
		pc.getStats().add(s);
		
		pc.setName("Alek Vier");
		pc.setLevel(2);
		pc.setRace("Human");
		pc.setHeroicClass("Psion");
		pc.setPortrait(Utility.getPortrait("http://www.ncgeek.com/10123017.png", new ManticorePreferences(this)));*/
	}

	@Override
	public Loader<ManticoreCharacter> onCreateLoader(int loaderID, Bundle args) {
		String file = args.getString("file");
		File dir = ManticoreStatus.getExternalStorageDirectory();
		File f = new File(dir, file);
		
		Loaders l = Loaders.getLoader(loaderID);
		
		
		Logger.info(LOG_TAG, String.format("Loader for %s created", l.toString()));
		return new Dnd4eLoader(this, f);
	}

	@Override
	public void onLoadFinished(Loader<ManticoreCharacter> loader, ManticoreCharacter data) {
		Logger.info(LOG_TAG, String.format("Character %s finished loading", data.getName()));
		status.setCharacter(data);
	}

	@Override
	public void onLoaderReset(Loader<ManticoreCharacter> loader) {
		Logger.info(LOG_TAG, "Loader reset");
	}
	
}
