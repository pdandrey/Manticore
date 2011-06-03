package com.ncgeek.android.manticore.partial;

import java.util.ArrayList;
import java.util.Collections;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.adapters.SkillGridAdapter;
import com.ncgeek.android.manticore.widgets.SkillTableRow;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.Skill;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;

import android.content.Context;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TableLayout;

public class SkillPartial extends Partial {

	public SkillPartial(Context context) {
		super(context, R.layout.skillview);
	}

	@Override
	public void update() {
		PlayerCharacter pc = ManticoreStatus.getPC();
        View v = getView();
        
        ScrollView scroll = (ScrollView)v.findViewById(R.id.skillview_scroll);
        TableLayout tbl = (TableLayout)v.findViewById(R.id.skillview_tbl);
        
        ArrayList<String> lstSkills = new ArrayList<String>();
        for(Rule r : pc.getRules()) {
        	if(r.getType() == RuleTypes.SKILL) {
        		lstSkills.add(r.getName());
        	}
        }
        
        Collections.sort(lstSkills);
        boolean isFirst = true;
        GridView gv = (GridView)v.findViewById(R.id.skillview_grid);
        ManticorePreferences prefs = new ManticorePreferences(getContext());
        boolean useGrid = prefs.SkillsInGrid();
        
        ArrayList<Stat> lstStats = new ArrayList<Stat>();
        
        for(String skill : lstSkills) {
        	if(!useGrid) {
	        	if(!isFirst) {
		        	View vLine = new View(getContext());
		        	vLine.setBackgroundColor(Color.BLACK);
		        	ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 2);
		        	vLine.setLayoutParams(params);
		        	tbl.addView(vLine);
	        	} else {
	        		isFirst = false;
	        	}
	        	SkillTableRow str = new SkillTableRow(getContext());
	        	str.setSkill(new Skill(pc.getStats().get(skill)));
	        	tbl.addView(str);
        	} else {
        		lstStats.add(pc.getStats().get(skill));
        	}
        }
        
        if(useGrid) {
        	gv.setAdapter(new SkillGridAdapter(getContext(), lstStats));
        	scroll.setVisibility(View.GONE);
        	gv.setVisibility(View.VISIBLE);
        } else {
        	scroll.setVisibility(View.VISIBLE);
        	gv.setVisibility(View.GONE);
        }
	}

	@Override
	public int getContextMenuID() {
		return 0;
	}

	@Override
	public void setupContextMenu(Menu menu) {
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return false;
	}

}
