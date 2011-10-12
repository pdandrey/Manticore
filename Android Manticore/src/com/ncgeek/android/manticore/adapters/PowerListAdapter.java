package com.ncgeek.android.manticore.adapters;

import java.util.List;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.PowerViewer;
import com.ncgeek.manticore.character.CharacterPower;
import com.ncgeek.manticore.powers.Power;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class PowerListAdapter 
	extends SectionedListAdapter<String, CharacterPower>
	implements AdapterView.OnItemClickListener {

	public PowerListAdapter(Context context) {
		super(context, R.layout.power_headeritem, R.layout.power_listitem, String.class, CharacterPower.class);
	}
	
	public void setPower(List<CharacterPower> lstPowers) {
		for(CharacterPower cp : lstPowers)
			add(cp.getPower().getUsage().toString(), cp);
	}
	
	private static final class ViewHolder implements ISectionedViewHolder {

		public static ViewHolder forHeader(TextView tvHeader) {
			ViewHolder vh = new ViewHolder();
			vh.tvHeader = tvHeader;
			return vh;
		}
		
		public static ViewHolder forItem(TextView tvName) {
			ViewHolder vh = new ViewHolder();
			vh.tvName = tvName;
			return vh;
		}
		
		private TextView tvHeader;
		private TextView tvName;
		
		private ViewHolder() {
			tvHeader = null;
			tvName = null;
		}
		
		public TextView getHeader() { return tvHeader; }
		public TextView getName() { return tvName; }
		
		@Override
		public boolean isHeader() {
			return tvHeader != null;
		}

		@Override
		public boolean isValue() {
			return tvName != null;
		}
		
	}

	@Override
	protected void fillView(ISectionedViewHolder holder, Object value) {
		ViewHolder vh = (ViewHolder)holder;
		if(holder.isHeader()) {
			vh.getHeader().setText((String)value);
		} else {
			CharacterPower cp = (CharacterPower)value;
			vh.getName().setText(cp.getPower().getName());
		}
	}

	@Override
	protected ISectionedViewHolder createViewHolder(final View view, final boolean isHeader) {
		ViewHolder vh = null;
		if(isHeader) {
			vh = ViewHolder.forHeader((TextView)view.findViewById(R.id.power_headeritem_tvHeader));
		} else {
			vh = ViewHolder.forItem((TextView)view.findViewById(R.id.power_listitem_tvName));
		}
		
		return vh;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CharacterPower p = (CharacterPower)getItem(position);
		Intent i = new Intent(view.getContext(), PowerViewer.class);
		i.putExtra("Power", p);
		i.putExtra("Power Index", position);
		view.getContext().startActivity(i);
	}
}
