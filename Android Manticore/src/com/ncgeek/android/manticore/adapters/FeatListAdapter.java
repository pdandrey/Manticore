package com.ncgeek.android.manticore.adapters;

import java.util.List;

import com.ncgeek.android.manticore.R;
import com.ncgeek.manticore.character.Feat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FeatListAdapter extends ArrayAdapter<Feat> {

	private static class ViewHolder {
		TextView tvName;
		TextView tvTier;
		TextView tvDesc;
	}
	
	private List<Feat> feats;
	private int resourceID;
	private LayoutInflater inflater;
	
	public FeatListAdapter(Context context, int resID, List<Feat> feats) {
		super(context, resID, feats);
		this.feats = feats;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resourceID = resID;
	}
	
		@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if(convertView == null) {
			convertView = inflater.inflate(resourceID, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView)convertView.findViewById(R.id.featlistitem_tvName);
            holder.tvTier = (TextView)convertView.findViewById(R.id.featlistitem_tvTier);
            holder.tvDesc = (TextView)convertView.findViewById(R.id.featlistitem_tvDescription);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Feat f = feats.get(position);
		
		holder.tvName.setText(f.getName());
		holder.tvTier.setText(String.format("(%s)", f.getTier().toString()));
		holder.tvDesc.setText(f.getDescription());
		
		return convertView;
	}

}
