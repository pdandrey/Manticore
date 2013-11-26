package com.ncgeek.android.manticore.fragments.combat;



import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.Manticore;
import com.ncgeek.android.manticore.widgets.CharacterStatus;
import com.ncgeek.android.manticore.widgets.StatView;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.util.Logger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class CombatFragment extends Fragment {

	private static final String LOG_TAG = "CombatFgmt";
	
	private ManticoreCharacter character;
	private MainCombatFragment fgmtCombat;
	private HealCombatFragment fgmtHeal;
	
	public CombatFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null)
			character = (ManticoreCharacter)savedInstanceState.get("character");
		else
			character = ((Manticore)getActivity()).getCharacter();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("character", character);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.combat, container, false);
		((CharacterStatus)v.findViewById(R.id.characterstatus)).setCharacter(character);
		
		showMain();
		
		return v;
	}
	
	public void showMain() {
		if(fgmtCombat == null)
			fgmtCombat = new MainCombatFragment();
		
		FragmentManager mgr = getChildFragmentManager();
		
//		Fragment f = mgr.findFragmentByTag("combat_main");
//		if(f != null)
//			return;
		
		FragmentTransaction trans = mgr.beginTransaction();
		trans.replace(R.id.fgmtCombat, fgmtCombat, "combat_main");
		trans.commit();
	}
	
	public void showDamage() {
		
	}
	
	public void showHeal() {
		FragmentManager mgr = getChildFragmentManager();
		
//		Fragment f = mgr.findFragmentByTag("combat_heal");
//		if(f != null)
//			return;
		
		if(fgmtHeal == null)
			fgmtHeal = new HealCombatFragment();
		
		FragmentTransaction trans = mgr.beginTransaction();
		trans.addToBackStack("Heal");
		trans.replace(R.id.fgmtCombat, fgmtHeal, "combat_heal");
		trans.commit();
	}
	
	public void startTurn() {
		
	}
}
