package com.ncgeek.android.manticore.adapters;

import com.ncgeek.android.manticore.R;
import com.ncgeek.manticore.character.Feat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FeatListAdapter extends DelayedArrayAdapter<Feat> {

	private static class ViewHolder {
		TextView tvName;
		TextView tvTier;
		TextView tvDesc;
	}
	
	
	
	public FeatListAdapter(Context context, int resID) {
		super(context, resID);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if(convertView == null) {
			convertView = getInflater().inflate(getResourceID(), parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView)convertView.findViewById(R.id.featlistitem_tvName);
            holder.tvTier = (TextView)convertView.findViewById(R.id.featlistitem_tvTier);
            holder.tvDesc = (TextView)convertView.findViewById(R.id.featlistitem_tvDescription);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Feat f = getItem(position);
		
		holder.tvName.setText(f.getName());
		holder.tvTier.setText(String.format("(%s)", f.getTier().toString()));
		holder.tvDesc.setText(f.getDescription());
		
		return convertView;
	}

}
