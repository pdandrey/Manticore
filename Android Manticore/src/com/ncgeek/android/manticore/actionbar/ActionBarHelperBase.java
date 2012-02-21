package com.ncgeek.android.manticore.actionbar;

import com.ncgeek.android.manticore.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Gallery;

public class ActionBarHelperBase extends ActionBarHelper {

	private ActionBarAdapter adapter;
	
	public ActionBarHelperBase(Activity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Gallery gallery = (Gallery)getActivity().findViewById(R.id.actionbar);
		int position = 0;
		if(savedInstanceState != null)
			position = savedInstanceState.getInt("ActionBarPosition");
		
		adapter = new ActionBarAdapter(getActivity(), position);
		
		for(ActionBarTab tab : ActionBarTab.values()) {
			adapter.add(tab);
		}
		
		gallery.setAdapter(adapter); 
		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.setSelection(position, view);
				ActionBarHelperBase.this.setChanged();
				ActionBarHelperBase.this.notifyObservers(adapter.getItem(position));
			}
		});
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("ActionBarPosition", adapter.selectedPosition);
	}

	private static class ActionBarAdapter extends ArrayAdapter<ActionBarTab> {
		private int selectedPosition;
		private View selectedView;
		
		public ActionBarAdapter(Activity activity, int position) {
			super(activity, R.layout.actionbar_item);
			setSelection(position);
		}
		public void setSelection(int position) {
			selectedPosition = position;
			selectedView = null;
		}
		
		public void setSelection(int position, View v) {
			if(selectedView != null)
				selectedView.setBackgroundResource(R.drawable.actionbar_item_unselected);
			selectedPosition = position;
			selectedView = v;
			v.setBackgroundResource(R.drawable.actionbar_item_selected);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			if(position == selectedPosition) {
				v.setBackgroundResource(R.drawable.actionbar_item_selected);
				selectedView = v;
			}
			return v;
		}
	}
}
