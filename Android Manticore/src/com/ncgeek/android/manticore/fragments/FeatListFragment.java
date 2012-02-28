package com.ncgeek.android.manticore.fragments;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.Manticore;
import com.ncgeek.manticore.character.Feat;
import com.ncgeek.manticore.util.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FeatListFragment extends ListFragment {

	private static final String LOG_TAG = "Feat List";
	
	private ArrayAdapter<Feat> adapter;
	
	public FeatListFragment() {}
	
	@Override
	public void onAttach(Activity activity) {
		try {
			Manticore m = (Manticore)activity;
		} catch(ClassCastException ccex) {
			Logger.error(LOG_TAG, "Activity is not a Manticore activity.", ccex);
			throw ccex;
		}
		super.onAttach(activity);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ManticoreCharacter mc = ((Manticore)getActivity()).getCharacter();
		adapter = new ArrayAdapter<Feat>(getActivity(), R.layout.feat_list_item, R.id.tvName, mc.getFeats()) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				Feat f = getItem(position);
				TextView tv = (TextView)v.findViewById(R.id.tvName);
				tv.setText(f.getName());
				
				((TextView)v.findViewById(R.id.tvTier)).setText(f.getTier().toString());
				
				((TextView)v.findViewById(R.id.tvDescription)).setText(f.getDescription());
				return v;
			}
			
		};
		setListAdapter(adapter);
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
}
