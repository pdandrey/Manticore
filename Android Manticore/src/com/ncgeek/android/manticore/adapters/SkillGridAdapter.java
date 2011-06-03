package com.ncgeek.android.manticore.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ncgeek.android.manticore.widgets.StatView;
import com.ncgeek.manticore.character.stats.Stat;

public class SkillGridAdapter extends BaseAdapter {
	
	private List<Stat> stats;
	private Context context;
	
	public SkillGridAdapter(Context context, List<Stat> stats) {
		this.stats = stats;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return stats.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		StatView sv = null;
		
		if(convertView == null) {
			sv = new StatView(context);
			convertView = sv;
		} else {
			sv = (StatView)convertView;
		}
		
		sv.setStat(stats.get(position));
		
		return sv;
	}
}