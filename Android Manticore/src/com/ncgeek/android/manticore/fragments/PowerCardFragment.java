package com.ncgeek.android.manticore.fragments;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.android.manticore.widgets.VerticalLabelView;
import com.ncgeek.manticore.character.CharacterPower;
import com.ncgeek.manticore.powers.Power;
import com.ncgeek.manticore.util.Logger;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PowerCardFragment extends Fragment {

	private static final String LOG_TAG = "Power Card";
	
	private CharacterPower power;
	private TextView tvName;
	private TextView tvKeywords;
	private TextView tvRangeIcon;
	private TextView tvFlavor;
	private TextView tvRange;
	private TextView tvActionType;
	private TextView tvTarget;
	private TextView tvToHit;
	private LinearLayout llSpecifics;
	private ImageView ivHeader;
	private FrameLayout frmHeader;
	private VerticalLabelView vlvLabel;
	
	public PowerCardFragment() {}
	
	public void setPower(CharacterPower power) {
		this.power = power;
	}
	
	public CharacterPower getPower() { return power; }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null)
			setPower((CharacterPower)savedInstanceState.getSerializable("Power"));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.powercard, container, false);
		
		Typeface tfMorpheus = Typeface.createFromAsset(getActivity().getAssets(), "morpheus.ttf");
		Typeface tfIcons = Typeface.createFromAsset(getActivity().getAssets(), "D&D 4e Icons v3.ttf");
		
		tvName = (TextView)v.findViewById(R.id.powerview_tvName);
		tvName.setTypeface(tfMorpheus);
		
		tvKeywords = (TextView)v.findViewById(R.id.powerview_tvKeywords);
		tvKeywords.setTypeface(tfMorpheus);
		
		tvRangeIcon = (TextView)v.findViewById(R.id.powerview_tvRangeIcon);
		tvRangeIcon.setTypeface(tfIcons);
		
		tvFlavor = (TextView)v.findViewById(R.id.powerview_tvFlavor);
		
		tvActionType = (TextView)v.findViewById(R.id.powerview_tvActionType);
		
		tvRange = (TextView)v.findViewById(R.id.powerview_tvRange);
		
		tvTarget = (TextView)v.findViewById(R.id.powerview_tvTarget);
		
		tvToHit = (TextView)v.findViewById(R.id.powerview_tvToHit);
		
		llSpecifics = (LinearLayout)v.findViewById(R.id.powerview_llSpecifics);
		
		ivHeader = (ImageView)v.findViewById(R.id.powerview_ivHeader);
		
		frmHeader = (FrameLayout)v.findViewById(R.id.powerview_frmHeader);
		
		vlvLabel = (VerticalLabelView)v.findViewById(R.id.powerview_vlvPowerTypeAndLevel);
		
		displayPower();
		
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("Power", power);
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
		
		setVisibility(tvRange, tvTarget, tvToHit);
		
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
		
		vlvLabel.setText(p.getDisplay());
		
		for(Power.Specific spec : p.getSpecifics()) {
			TextView tv = new TextView(getActivity());
			tv.setTextColor(Color.BLACK);
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
					tvRangeIcon.setText(" ");
					Logger.error(LOG_TAG, "Unhandled Power Attack Type of " + p.getAttackType().getName());
					break;
			}
		}
	}
	
	private void setVisibility(TextView...views) {
		for(TextView v : views) {
			int visiblity = View.VISIBLE;
			if(v.getText().toString().trim().length() == 0)
				visiblity = View.GONE;
			v.setVisibility(visiblity);
		}
	}
}
