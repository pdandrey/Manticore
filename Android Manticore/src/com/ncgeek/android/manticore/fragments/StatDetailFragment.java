package com.ncgeek.android.manticore.fragments;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StatDetailFragment extends Fragment {

	private Stat stat;
	private ArrayAdapter<Addition> adapter;
	
	public StatDetailFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.statdetails, container, false);
		
		ListView lv = (ListView)v.findViewById(R.id.lvStatModifiers);
		
		adapter = new ArrayAdapter<Addition>(getActivity(), R.layout.statdetails_listitem, R.id.tvName) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = convertView;
				if(convertView == null) {
					LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = li.inflate(R.layout.statdetails_listitem, parent, false);
				}
				
				Addition a = getItem(position);
				
				StringBuilder buf = new StringBuilder();

				int value = a.getValue();

				if(value < 0)
					buf.append(value);
				else
					buf.append("+" + value);

				String strValue = buf.toString();
				buf = new StringBuilder();
				
				String s = a.getType();
				if(s != null)
					buf.append(s + " bonus");

				s = a.getStatLink();
				if(s != null)
					buf.append("from " + s.replace(" [linked]", ""));

				s = a.getRequires();
				if(s != null)
				buf.append("requires " + s);

				s = a.getWearing();
				if(s != null)
				buf.append("when wearing " + s);

				s = a.getNotWearing();
				if(s != null)
				buf.append("when not wearing " + s);
				
				((TextView)v.findViewById(R.id.tvModifier)).setText(strValue);
				((TextView)v.findViewById(R.id.tvDescription)).setText(buf.toString());
				
				return v;
			}
			
		};
		
		lv.setAdapter(adapter);
		
		if(savedInstanceState != null) {
			stat = (Stat)savedInstanceState.get("stat");
			displayStat();
		}
		
		return v;
	}
	
	public void setStat(Stat stat) {
		this.stat = stat;
		displayStat();
	}
	
	private void displayStat() {
		Utility.setTextFromFormat(getView(), R.id.tvName, stat.getAliases().get(0), stat.getCalculatedValue());
		adapter.clear();
		for(Addition a : stat.getAppliedAdditions())
			adapter.add(a);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("stat", stat);
	}	
}
