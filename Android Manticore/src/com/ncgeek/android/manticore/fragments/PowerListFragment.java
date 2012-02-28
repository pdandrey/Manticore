package com.ncgeek.android.manticore.fragments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.Manticore;
import com.ncgeek.android.manticore.activities.PowerViewer;
import com.ncgeek.manticore.character.CharacterPower;
import com.ncgeek.manticore.powers.Power;
import com.ncgeek.manticore.powers.PowerUsages;
import com.ncgeek.manticore.util.Logger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class PowerListFragment extends Fragment {

	private static final String LOG_TAG = "PowerListFragment";
	
	private ManticoreCharacter character;
	private HashMap<PowerUsages,List<CharacterPower>> powers;
	private ArrayList<PowerUsages> groups;
	
	public PowerListFragment() {}

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		groups = new ArrayList<PowerUsages>();
		groups.add(PowerUsages.AtWill);
		groups.add(PowerUsages.Encounter);
		groups.add(PowerUsages.Daily);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.powerlist, container, false);
		
		ExpandableListView elv = (ExpandableListView)v.findViewById(R.id.lstAbilities);
		elv.setAdapter(adapter);
		
		elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				CharacterPower cp = (CharacterPower)adapter.getChild(groupPosition, childPosition);
				Intent i = new Intent();
				i.setClass(getActivity(), PowerViewer.class);
				i.putExtra("Powers", (Serializable)adapter.getGroup(groupPosition));
				i.putExtra("Position", childPosition);
				getActivity().startActivity(i);
				return true;
			}
		});
		
		setupPowers();
		
		return v;
	}
	
	private void setupPowers() {
		if(character == null) {
			character = ((Manticore)getActivity()).getCharacter();
			if(character == null)
				return;
		}
		
		powers = new HashMap<PowerUsages, List<CharacterPower>>();
		
		for(CharacterPower p : character.getPowers()) {
			PowerUsages usage = p.getPower().getUsage();
			if(!powers.containsKey(usage))
				powers.put(usage, new ArrayList<CharacterPower>());
			powers.get(usage).add(p);
		}
		
		Collections.sort(powers.get(PowerUsages.AtWill));
		Collections.sort(powers.get(PowerUsages.Encounter));
		Collections.sort(powers.get(PowerUsages.Daily));
	}
	
	private static class ChildViewHolder {
		public TextView tvRangeIcon;
		public TextView tvName;
	}
	
	BaseExpandableListAdapter adapter = new BaseExpandableListAdapter() {
		
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		@Override
		public boolean hasStableIds() {
			return false;
		}
		
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.power_header, parent, false);
			}
			PowerUsages usage = groups.get(groupPosition);
			TextView tv = (TextView)convertView;
			switch(usage) {
				case AtWill:
					convertView.setBackgroundResource(R.drawable.atwill_header);
					tv.setText("At-Will Powers");
					break;
					
				case Encounter:
					convertView.setBackgroundResource(R.drawable.encounter_header);
					tv.setText("Encounter Powers");
					break;
					
				case Daily:
					convertView.setBackgroundResource(R.drawable.daily_header);
					tv.setText("Daily Powers");
					break;
			}
			
			tv.setPadding(100, 0, 0, 0);
			return tv;
		}
		
		@Override
		public long getGroupId(int groupPosition) {
			return groups.get(groupPosition).getID();
		}
		
		@Override
		public int getGroupCount() {
			return groups.size();
		}
		
		@Override
		public List<CharacterPower> getGroup(int groupPosition) {
			PowerUsages group = groups.get(groupPosition);
			return powers.get(group);
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			return getGroup(groupPosition).size();
		}
		
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder holder = null;
			
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.power_listitem, parent, false);
				holder = new ChildViewHolder();
				holder.tvRangeIcon = (TextView)convertView.findViewById(R.id.tvRangeIcon);
				holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
				convertView.setTag(holder);
				
				Typeface tfIcons = Typeface.createFromAsset(getActivity().getAssets(), "D&D 4e Icons v3.ttf");
				holder.tvRangeIcon.setTypeface(tfIcons);
				
			} else {
				holder = (ChildViewHolder)convertView.getTag();
			}
			
			Power p = getChild(groupPosition, childPosition).getPower();
			holder.tvName.setText(p.getName());
			
			if(p.getAttackType() == null) {
				holder.tvRangeIcon.setText(" ");
			} else {
				switch(p.getAttackType()) {
					case AreaBurst:
						holder.tvRangeIcon.setText("a");
						break;
						
					case Melee:
						holder.tvRangeIcon.setText("m");
						break;
						
					case Ranged:
						holder.tvRangeIcon.setText("r");
						break;
						
					default:
						Logger.warn(LOG_TAG, "Unhandled Power Attack Type of " + p.getAttackType().getName());
						holder.tvRangeIcon.setText(" ");
						break;
				}
			}
			
			return convertView;
		}
		
		
		
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}
		
		@Override
		public CharacterPower getChild(int groupPosition, int childPosition) {
			return getGroup(groupPosition).get(childPosition);
		}
	};

	
}
