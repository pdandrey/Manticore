package com.ncgeek.android.manticore.activities;

import java.io.Serializable;
import java.util.List;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.Ritual;
import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailsView extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
        super.onCreate(savedInstanceState);
        
        Bundle extras = this.getIntent().getExtras();
        
        Serializable item = extras.getSerializable("item");
        
        if(item instanceof Stat)
        	displayStat((Stat)item);
        else if(item instanceof Ritual)
        	displayRitual((Ritual)item);
        
	 }
	
	private void displayRitual(Ritual r) {
		setContentView(R.layout.ritualdetails);
		
		setText(R.id.ritualdetails_tvName, r.getName());
		setText(R.id.ritualdetails_tvTypeAndCategory, String.format("%s (%s)", r.getType(), r.getCategory()));
		setText(R.id.ritualdetails_tvFlavor, r.getFlavor());
		setText(R.id.ritualdetails_tvComponentCost, r.getComponentCost());
		setText(R.id.ritualdetails_tvDescription, r.getDescription());
		setText(R.id.ritualdetails_tvDuration, r.getDuration());
		setText(R.id.ritualdetails_tvKeySkill, r.getKeySkills());
		setText(R.id.ritualdetails_tvLevel, r.getLevel() + "");
		setText(R.id.ritualdetails_tvMarketPrice, r.getMarketPrice().toString());
		setText(R.id.ritualdetails_tvTime, r.getTime());
		
		String prereq = r.getPrerequisite();
		if(prereq == null || prereq.trim().length() == 0) {
			findViewById(R.id.ritualdetails_trPrerequisite).setVisibility(View.GONE);
		} else {
			setText(R.id.ritualdetails_tvPrerequisite, r.getPrerequisite());
		}
		
	}
	
	private void setText(int id, String text) {
		TextView tv = (TextView)findViewById(id);
		tv.setText(text);
	}

	private void displayStat(Stat s) {
		setContentView(R.layout.stat_breakdown);
		
		LinearLayout layoutApplied = (LinearLayout)findViewById(R.id.statbreakdown_layoutApplied);
        LinearLayout layoutNotApplied = (LinearLayout)findViewById(R.id.statbreakdown_layoutNotApplied);
        
		TextView tv = (TextView)findViewById(R.id.statbreakdown_txtStat);
        tv.setText(String.format("%s %d Breakdown", s.getAliases().get(0), s.getCalculatedValue()));
        
        ImageView iv = (ImageView)findViewById(R.id.statbreakdown_img);
        iv.setImageResource(Utility.getStatIcon(s.getAliases().get(0)));
        
        List<Addition> applied = s.getAppliedAdditions();
        
        for(Addition a : applied) {
        	layoutApplied.addView(createView(a, true));
        }
        
        List<Addition> all = s.getAdditions();
        if(all.size() > applied.size()) {
        	for(Addition a : all) {
        		if(!applied.contains(a)) {
        			layoutNotApplied.addView(createView(a, false));
        		}
        	}
        }
	}
	
	private View createView(Addition a, boolean getAppliedValue) {
		TextView tv = new TextView(this);
		StringBuilder buf = new StringBuilder();
		
		int value = a.getValue();
		if(!getAppliedValue)
			value = a.getAbsoluteValue();
		
		if(value < 0)
			buf.append(value);
		else
			buf.append("+" + value);
		
		String s = a.getType();
		if(s != null)
			buf.append(" " + s + " bonus");
		
		s = a.getStatLink();
		if(s != null)
			buf.append(" from " + s.replace(" [linked]", ""));
		
		s = a.getRequires();
		if(s != null)
			buf.append(" requires " + s);
		
		s = a.getWearing();
		if(s != null)
			buf.append(" when wearing " + s);
		
		s = a.getNotWearing();
		if(s != null)
			buf.append(" when not wearing " + s);
		
    	tv.setText(buf.toString());
    	return tv;
	}
}
