package com.ncgeek.android.manticore.fragments;



import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.Manticore;
import com.ncgeek.android.manticore.widgets.StatView;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.util.Logger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class SkillListFragment extends Fragment {

	private static final String LOG_TAG = "SkillListFgmt";
	
	private ManticoreCharacter character;
	private ArrayAdapter<Stat> adapter;
	
	public SkillListFragment() {}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			Manticore m = (Manticore)activity;
			character = m.getCharacter();
		} catch(ClassCastException ccex) {
			Logger.error(LOG_TAG, "Fragment attached to an activity other than Manticore", ccex);
			throw ccex;
		}
	}
	
	@SuppressLint("NewApi") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.skillview, container, false);
		
		ListView lv = (ListView)v.findViewById(R.id.lvSkills);
		adapter = new ArrayAdapter<Stat>(getActivity(), R.layout.skillview_listitem, R.id.stat) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = convertView;
				if(convertView == null) {
					LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = li.inflate(R.layout.skillview_listitem, parent, false);
					v.setOnLongClickListener(showDetails);
				}
				
				Stat s = getItem(position);

				((StatView)v).setStat(s);
				
				return v;
			}
			
		};
		
		lv.setAdapter(adapter);
		
		adapter.addAll(character.getStats().getSkills());
		
		return v;
	}
	
	private View.OnLongClickListener showDetails = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			((Manticore)getActivity()).displayStatDetails(((StatView)v).getStat());
			return true;
		}
	};
}
