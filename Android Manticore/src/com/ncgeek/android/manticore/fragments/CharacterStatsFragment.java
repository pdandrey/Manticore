package com.ncgeek.android.manticore.fragments;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.Manticore;
import com.ncgeek.android.manticore.widgets.CharacterStatus;
import com.ncgeek.android.manticore.widgets.StatView;
import com.ncgeek.manticore.character.stats.Stat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CharacterStatsFragment extends Fragment {
	
	private ManticoreCharacter character;
	
	public CharacterStatsFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null)
			character = (ManticoreCharacter)savedInstanceState.get("character");
		else
			character = ((Manticore)getActivity()).getCharacter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.characterstats, container, false);
		
		((CharacterStatus)v.findViewById(R.id.characterstatus)).setCharacter(character);
		
		setStat(v, R.id.statAC, "AC");
		setStat(v, R.id.statFort, "Fortitude");
		setStat(v, R.id.statWill, "Will");
		setStat(v, R.id.statReflex, "Reflex");
		setStat(v, R.id.statStr, "str");
		setStat(v, R.id.statCon, "con");
		setStat(v, R.id.statDex, "dex");
		setStat(v, R.id.statInt, "int");
		setStat(v, R.id.statWis, "wis");
		setStat(v, R.id.statCha, "cha");
		
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("character", character);
	}
	
	private void setStat(View v, int id, String statName) {
		StatView sv = (StatView)v.findViewById(id);
		Stat s = character.getStats().get(statName);
		sv.setStat(s);
		sv.setOnLongClickListener(showDetails);
	}
	
	private View.OnLongClickListener showDetails = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			((Manticore)getActivity()).displayStatDetails(((StatView)v).getStat());
			return true;
		}
	};
}
