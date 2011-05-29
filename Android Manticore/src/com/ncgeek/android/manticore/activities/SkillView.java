package com.ncgeek.android.manticore.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.widgets.SkillTableRow;
import com.ncgeek.android.manticore.widgets.StatView;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.Skill;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TextView;

public class SkillView extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skillview);
        
        PlayerCharacter pc = ManticoreStatus.getPC();
        
        ((TextView)findViewById(R.id.skillview_txtName)).setText(pc.getName());
        
        TableLayout tbl = (TableLayout)findViewById(R.id.skillview_tbl);
        
        ArrayList<String> lstSkills = new ArrayList<String>();
        for(Rule r : pc.getRules()) {
        	if(r.getType() == RuleTypes.SKILL) {
        		lstSkills.add(r.getName());
        	}
        }
        
        Collections.sort(lstSkills);
        boolean isFirst = true;
        GridView gv = (GridView)findViewById(R.id.skillview_grid);
        ManticorePreferences prefs = new ManticorePreferences(this);
        boolean useGrid = prefs.SkillsInGrid();
        
        ArrayList<Stat> lstStats = new ArrayList<Stat>();
        
        for(String skill : lstSkills) {
        	if(!useGrid) {
	        	if(!isFirst) {
		        	View v = new View(this);
		        	v.setBackgroundColor(Color.BLACK);
		        	ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 2);
		        	v.setLayoutParams(params);
		        	tbl.addView(v);
	        	} else {
	        		isFirst = false;
	        	}
	        	SkillTableRow str = new SkillTableRow(this);
	        	str.setSkill(new Skill(pc.getStats().get(skill)));
	        	tbl.addView(str);
        	} else {
        		lstStats.add(pc.getStats().get(skill));
        	}
        }
        
        if(useGrid) {
        	gv.setAdapter(new GridAdapter(this, lstStats));
        	tbl.setVisibility(View.GONE);
        	gv.setVisibility(View.VISIBLE);
        } else {
        	tbl.setVisibility(View.VISIBLE);
        	gv.setVisibility(View.GONE);
        }
	}
	
	private static class GridAdapter extends BaseAdapter {
		
		private List<Stat> stats;
		private Context context;
		
		public GridAdapter(Context context, List<Stat> stats) {
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
}
