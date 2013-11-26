package com.ncgeek.android.manticore.widgets;

import java.util.ArrayList;
import java.util.List;

import com.ncgeek.android.manticore.R;
import com.ncgeek.manticore.character.stats.Stat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
public class StatView extends LinearLayout {

	private static final String LOG_TAG = "StatView";
	
	public enum StatType { 
		Stat(0), Ability(1), Skill(2); 
		public static StatType forValue(int value) {
			for(StatType st : StatType.values())
				if(st.value == value)
					return st;
			return null;
		}
		private int value;
		StatType(int v) { value = v; }
		public int getValue() { return value; }
	}
	
	private Stat stat;
	private StatType type;
	private boolean setNameFromStat;
	
	public StatView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public StatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public StatView(Context context) {
		super(context);
		init(context, null);
	}

	public Stat getStat() { return stat; }
	public void setStat(Stat stat) {
		this.stat = stat;
		
		TextView tv = (TextView)findViewById(R.id.tvValue);
		tv.setText(String.format("%d", stat.getCalculatedValue()));
		
		if(setNameFromStat) {
			tv = (TextView)findViewById(R.id.tvName);
			tv.setText(stat.getAliases().get(0));
		}
		
		switch(type) {
			case Ability:
				displayAbility();
				break;
				
			case Skill:
				displaySkill();
				break;
				
			case Stat:
				break;
		}
	}

	public void init(Context context, AttributeSet attrs) {
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatView);
		List<TextView> lstTextViews = new ArrayList<TextView>();
		List<Integer> lstIds = new ArrayList<Integer>();
		
		if(!a.hasValue(R.styleable.StatView_type)) {
			throw new RuntimeException("Missing type attribute");
		}
		
		type = StatType.forValue(a.getInt(R.styleable.StatView_type, 0));
		
		int layout = R.layout.statview;
		
		lstIds.add(R.id.tvName);
		lstIds.add(R.id.tvValue);
		
		switch(type) {
			case Ability:
				layout = R.layout.abilityview;
				lstIds.add(R.id.tvModifier);
				break;
				
			case Skill:
				layout = R.layout.stat_skillview;
				lstIds.add(R.id.tvModifier);
				break;
				
			case Stat:
				layout = R.layout.statview;
				int orientation = a.getInt(R.styleable.StatView_android_orientation, LinearLayout.VERTICAL);
				setOrientation(orientation);				
				
				int gravity = a.getInt(R.styleable.StatView_android_gravity, Gravity.CENTER_HORIZONTAL);
				setGravity(gravity);
				break;
		}
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(layout, this, true);
		
		for(int id : lstIds) {
			lstTextViews.add((TextView)findViewById(id));
		}
		
		Integer color = null;
		if(a.hasValue(R.styleable.StatView_android_textColor))
			color = a.getColor(R.styleable.StatView_android_textColor, android.graphics.Color.WHITE);
		
		TextView tvName = (TextView)findViewById(R.id.tvName);
		setNameFromStat = !a.hasValue(R.styleable.StatView_android_text);
		if(!setNameFromStat)
			tvName.setText(a.getString(R.styleable.StatView_android_text));
		else
			tvName.setText(type.toString());
		
		TextView tvVal = (TextView)findViewById(R.id.tvValue);
		tvVal.setText("##");
		
		if(color != null) {
			for(TextView tv : lstTextViews)
				tv.setTextColor(color);
		}
		
		if(a.hasValue(R.styleable.StatView_android_textSize)) {
			float size = a.getDimension(R.styleable.StatView_android_textSize, 0);
			for(TextView tv : lstTextViews)
				tv.setTextSize(size);
		}
		
		a.recycle();
	}
	
	private void displayAbility() {
		TextView tv = (TextView)findViewById(R.id.tvModifier);
		String sign = "";
		if(stat.getModifier() >= 0)
			sign = "+";
		tv.setText(String.format("%s%d", sign, stat.getModifier()));
	}
	
	private void displaySkill() {
		TextView tv = (TextView)findViewById(R.id.tvModifier);
		String sign = "";
		Stat mod = stat.getAbilityModifier();
		
		if(mod.getModifier() >= 0)
			sign = "+";
		String trained = "";
		if(stat.isTrained())
			trained = " Trn";
		tv.setText(String.format("%s%s%d", mod.getAliases().get(0), sign, mod.getModifier()));		
	}
}
