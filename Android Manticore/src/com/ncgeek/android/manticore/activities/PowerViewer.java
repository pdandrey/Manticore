package com.ncgeek.android.manticore.activities;

import java.util.List;
import java.util.Set;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.character.CharacterPower;
import com.ncgeek.manticore.powers.Power;
import com.ncgeek.manticore.powers.PowerAttack;
import com.ncgeek.manticore.powers.PowerKeywords;
import com.ncgeek.manticore.powers.PowerSpecific;
import com.ncgeek.manticore.powers.PowerSpecificAttribute;
import com.ncgeek.manticore.util.Logger;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PowerViewer extends Activity {

	private static final String LOG_TAG = "Power Viewer";
	
	private CharacterPower power;
	private int idxPower;
	private TextView tvName;
	private TextView tvKeywords;
	private TextView tvRangeIcon;
	private TextView tvFlavor;
	private TextView tvRange;
	private TextView tvActionType;
	private TextView tvTarget;
	private TextView tvToHit;
	//private TableLayout tblSpecifics;
	private LinearLayout llSpecifics;
	private ImageView ivHeader;
	private FrameLayout frmHeader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.powerview);
		
		power = (CharacterPower)getIntent().getSerializableExtra("Power");
		idxPower = getIntent().getIntExtra("Power Index", 0);
		
		Typeface tfMorpheus = Typeface.createFromAsset(getAssets(), "morpheus.ttf");
		Typeface tfIcons = Typeface.createFromAsset(getAssets(), "D&D 4e Icons v3.ttf");
		
		tvName = (TextView)findViewById(R.id.powerview_tvName);
		tvName.setTypeface(tfMorpheus);
		
		tvKeywords = (TextView)findViewById(R.id.powerview_tvKeywords);
		tvKeywords.setTypeface(tfMorpheus);
		
		tvRangeIcon = (TextView)findViewById(R.id.powerview_tvRangeIcon);
		tvRangeIcon.setTypeface(tfIcons);
		
		tvFlavor = (TextView)findViewById(R.id.powerview_tvFlavor);
		
		tvActionType = (TextView)findViewById(R.id.powerview_tvActionType);
		
		tvRange = (TextView)findViewById(R.id.powerview_tvRange);
		
		tvTarget = (TextView)findViewById(R.id.powerview_tvTarget);
		
		tvToHit = (TextView)findViewById(R.id.powerview_tvToHit);
		
		//tblSpecifics = (TableLayout)findViewById(R.id.powerview_tblSpecifics);
		llSpecifics = (LinearLayout)findViewById(R.id.powerview_llSpecifics);
		
		ivHeader = (ImageView)findViewById(R.id.powerview_ivHeader);
		
		frmHeader = (FrameLayout)findViewById(R.id.powerview_frmHeader);
		
		displayPower();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		power = (CharacterPower)savedInstanceState.getSerializable("Power");
		idxPower = savedInstanceState.getInt("Power Index");
		
		displayPower();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putSerializable("Power", power);
		outState.putInt("Power Index", idxPower);
	}
	
	private void displayPower() {
		Power p = power.getPower();
		tvActionType.setText(p.getAction().getName());
		tvFlavor.setText(p.getFlavor());
		tvKeywords.setText(p.getKeywords());
		tvName.setText(p.getName());
		tvRange.setText(p.getRange());
		tvTarget.setText(p.getTarget());
		tvToHit.setText(p.getToHit());
		
		switch(p.getUsage()) {
			case AtWill: 
				ivHeader.setImageResource(R.drawable.power_header_atwill_flare); 
				frmHeader.setBackgroundColor(Color.parseColor("#2f6e47"));
				break;
			case Daily: 
				ivHeader.setImageResource(R.drawable.power_header_daily_flare); 
				frmHeader.setBackgroundColor(Color.parseColor("#1e3c45"));
				break;
			case Encounter: 
				ivHeader.setImageResource(R.drawable.power_header_encounter_flare); 
				frmHeader.setBackgroundColor(Color.parseColor("#610101"));
				break;
		}
		
		for(Power.Specific spec : p.getSpecifics()) {
			TextView tv = new TextView(this);
			SpannableStringBuilder buf = new SpannableStringBuilder();
			buf.append(spec.getName());
			buf.append(": ");
			buf.append(spec.getValue());
			buf.setSpan(new StyleSpan(Typeface.BOLD), 0, spec.getName().length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			tv.setText(buf);
			tv.setPadding(Utility.dpToPx(10 * spec.getLevel()), 0, 0, 0);
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			layout.leftMargin = Utility.dpToPx(10 * spec.getLevel());
			//tv.setLayoutParams(layout);
			
			llSpecifics.addView(tv);
			
			/*
			TableRow tr = new TableRow(this);
			TextView tvLabel = new TextView(this);
			tvLabel.setText(spec.getName());
			tr.addView(tvLabel);
			
			TableRow.LayoutParams layout = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			layout.leftMargin = Utility.dpToPx(10 * spec.getLevel());
			tvLabel.setLayoutParams(layout);
			
			TextView tvText = new TextView(this);
			tvText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			tvText.setText(spec.getValue());
			tr.addView(tvText);
			
			tblSpecifics.addView(tr);*/
		}
	
		if(p.getAttackType() != null) {
			switch(p.getAttackType()) {
				case AreaBurst:
					tvRangeIcon.setText("a");
					break;
					
				case Melee:
					tvRangeIcon.setText("m");
					break;
					
				case Ranged:
					tvRangeIcon.setText("r");
					break;
					
				default:
					Logger.error(LOG_TAG, "Unhandled Power Attack Type of " + p.getAttackType().getName());
					break;
			}
		}
	}
}
