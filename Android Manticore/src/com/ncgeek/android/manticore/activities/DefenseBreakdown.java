package com.ncgeek.android.manticore.activities;

import java.util.ArrayList;
import java.util.List;

import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DefenseBreakdown extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.defense_breakdown);
        
        Bundle extras = this.getIntent().getExtras();
        String defense = extras.getString("defense");
        
        LinearLayout layoutApplied = (LinearLayout)findViewById(R.id.defensebreakdown_layoutApplied);
        LinearLayout layoutNotApplied = (LinearLayout)findViewById(R.id.defensebreakdown_layoutNotApplied);
        
        PlayerCharacter pc = ManticoreStatus.getPC();
        Stat s = pc.getStats().get(defense);
        
        TextView tv = (TextView)findViewById(R.id.defensebreakdown_txtDefense);
        tv.setText(String.format("%s %d Breakdown", defense, s.getCalculatedValue()));
        
        List<Addition> applied = s.getAppliedAdditions();
        
        for(Addition a : applied) {
        	layoutApplied.addView(createView(a));
        }
        
        List<Addition> all = s.getAdditions();
        if(all.size() > applied.size()) {
        	for(Addition a : all) {
        		if(!applied.contains(a)) {
        			layoutNotApplied.addView(createView(a));
        		}
        	}
        }
	 }
	
	private View createView(Addition a) {
		TextView tv = new TextView(this);
		StringBuilder buf = new StringBuilder();
		
		int value = a.getValue();
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
