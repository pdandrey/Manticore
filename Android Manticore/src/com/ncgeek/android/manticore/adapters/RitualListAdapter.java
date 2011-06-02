package com.ncgeek.android.manticore.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.DetailsView;
import com.ncgeek.manticore.Ritual;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RitualListAdapter extends ArrayAdapter<Ritual> implements AdapterView.OnItemClickListener {

	private static class ViewHolder {
		TextView tvName;
	}
	
	private static final Comparator<Ritual> RITUAL_COMPARE =  new Comparator<Ritual>() {
		@Override
		public int compare(Ritual r1, Ritual r2) {
			return r1.getName().compareToIgnoreCase(r2.getName());
		}
    };
	
	private List<Ritual> rituals;
	private int resourceID;
	private LayoutInflater inflater;
	private Context context;
	
	public RitualListAdapter(Context context, int resID, List<Ritual> rituals) {
		super(context, resID, rituals);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resourceID = resID;
        this.context = context;
        ArrayList<Ritual> lst = new ArrayList<Ritual>(rituals);
        Collections.sort(lst,RITUAL_COMPARE);
        this.rituals = lst;
	}
	
		@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if(convertView == null) {
			convertView = inflater.inflate(resourceID, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView)convertView.findViewById(R.id.rituallistitem_tvName);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Ritual r = rituals.get(position);
		holder.tvName.setText(r.getName());
		
		return convertView;
	}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Ritual r = rituals.get(position);
			Intent i = new Intent(context, DetailsView.class);
			i.putExtra("item", r);
			context.startActivity(i);
		}

}
