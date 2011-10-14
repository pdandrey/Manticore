package com.ncgeek.android.manticore.activities;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.fragments.CharacterStatusFragment;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class Manticore extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manticore);
		
		ManticoreStatus.initialize(this);
		
		FragmentManager fm = getSupportFragmentManager();
		CharacterStatusFragment cs = (CharacterStatusFragment)fm.findFragmentById(R.id.manticore_character);
		
		ManticoreCharacter pc = new ManticoreCharacter();
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
		pc.setPortrait(Utility.getPortrait("http://www.ncgeek.com/10123017.png", new ManticorePreferences(this)));
		
		cs.setCharacter(pc);
	}
	
}
