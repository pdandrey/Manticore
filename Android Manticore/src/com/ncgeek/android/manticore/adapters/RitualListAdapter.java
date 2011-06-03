package com.ncgeek.android.manticore.adapters;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.DetailsView;
import com.ncgeek.manticore.Ritual;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

public class RitualListAdapter 
	extends DelayedArrayAdapter<Ritual> 
	implements AdapterView.OnItemClickListener {

	private static class ViewHolder {
		TextView tvName;
	}
	
	public RitualListAdapter(Context context, int resID) {
		super(context, resID);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if(convertView == null) {
			convertView = getInflater().inflate(getResourceID(), parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView)convertView.findViewById(R.id.rituallistitem_tvName);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Ritual r = getItem(position);
		holder.tvName.setText(r.getName());
		
		return convertView;
	}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Ritual r = getItem(position);
			Intent i = new Intent(getContext(), DetailsView.class);
			i.putExtra("item", r);
			getContext().startActivity(i);
		}

}
