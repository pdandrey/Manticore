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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class MainCombatFragment extends BaseCombatFragment {

	private static final String LOG_TAG = "CombatFgmt";
	
	public MainCombatFragment() {
		setLayout(R.layout.combat_main);
	}

	@Override
	protected void hookupEvents(View v) {
		
		((Button)v.findViewById(R.id.btnHeal)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getParent().showHeal();
			}
		});
	}
}
