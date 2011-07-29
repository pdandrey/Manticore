package com.ncgeek.android.manticore.adapters;

import java.util.List;

import com.ncgeek.android.manticore.R;
import com.ncgeek.manticore.party.Message;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PartyMessageAdapter extends ArrayAdapter<Message> {

	private static class ViewHolder {
		public ImageView img;
		public TextView chatter;
		public TextView timestamp;
	}
	
	private boolean showPortraits;
	private final LayoutInflater inflater;
	
	public PartyMessageAdapter(Context context) {
		super(context, R.layout.partychat_listitem);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setShowPortraits(boolean show) { showPortraits = show; }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.partychat_listitem, parent, false);
			holder = new ViewHolder();
			holder.img = (ImageView)convertView.findViewById(R.id.partychat_listitem_img);
			holder.chatter = (TextView)convertView.findViewById(R.id.partychat_listitem_tvChatter);
			holder.timestamp = (TextView)convertView.findViewById(R.id.partychat_listitem_tvTimestamp);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Message msg = getItem(position);
		
		if(showPortraits) {
			holder.img.setVisibility(View.VISIBLE);
			if(msg.getFrom().getPortrait() != null)
				holder.img.setImageBitmap((Bitmap)msg.getFrom().getPortrait());
		} else {
			holder.img.setVisibility(View.GONE);
		}
		
		holder.timestamp.setText(msg.getTimestamp().toString());
		
		SpannableStringBuilder buf = new SpannableStringBuilder();
		buf.append(msg.getFrom().getName());
		buf.append(": ");
		StyleSpan spanBold = new StyleSpan(Typeface.BOLD);
		buf.setSpan(spanBold, 0, buf.length()-1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		
		buf.append(msg.getMessage());
		
		holder.chatter.setText(buf);
		
		return convertView;
	}
}
