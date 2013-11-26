package com.ncgeek.android.manticore.fragments.combat;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.activities.Manticore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseCombatFragment extends Fragment {

	private ManticoreCharacter character;
	private int layout;
	
	public BaseCombatFragment() {}
	
	protected final ManticoreCharacter getCharacter() { return character; }
	protected final void setLayout(int layout) { this.layout = layout; }
	protected final CombatFragment getParent() { return (CombatFragment)getParentFragment(); }
	
	protected abstract void hookupEvents(View v);
	
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
		View v = inflater.inflate(layout, container, false);	
		hookupEvents(v);
		return v;
	}
}
