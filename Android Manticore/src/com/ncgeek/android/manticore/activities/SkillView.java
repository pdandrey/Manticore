package com.ncgeek.android.manticore.activities;

import java.util.ArrayList;
import java.util.Collections;

import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.widgets.SkillTableRow;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.Skill;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
        
        for(String skill : lstSkills) {
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
        }
	}
}
