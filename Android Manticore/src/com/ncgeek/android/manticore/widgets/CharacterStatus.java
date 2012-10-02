package com.ncgeek.android.manticore.widgets;

import java.util.Observer;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.util.Utility;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CharacterStatus extends RelativeLayout { 
	
	private Observer hpObserver;
	private ManticoreCharacter ch;
	
	public CharacterStatus(Context context) {
		super(context);
		init();
	}

	public CharacterStatus(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public CharacterStatus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.character_status, this);
		TextView tvName = (TextView)findViewById(R.id.tvName);
		TextView tvLevel = (TextView)findViewById(R.id.tvLevel);
		TextView tvRaceClass = (TextView)findViewById(R.id.tvRaceAndClass);
		TextView tvHP = (TextView)findViewById(R.id.tvHP);
		TextView tvSurge = (TextView)findViewById(R.id.tvSurges);
		
		if(!isInEditMode()) {
			Typeface tfMorpheus = Typeface.createFromAsset(getContext().getAssets(), "morpheus.ttf");
			Typeface tfCentaur = Typeface.createFromAsset(getContext().getAssets(), "centaur.ttf");
			tvName.setTypeface(tfMorpheus);
			tvLevel.setTypeface(tfCentaur);
			tvRaceClass.setTypeface(tfCentaur);
			tvHP.setTypeface(tfCentaur);
			tvSurge.setTypeface(tfCentaur);
		}
		
		ProgressBar barHP = (ProgressBar)findViewById(R.id.barHP);
		ProgressBar barSurge = (ProgressBar)findViewById(R.id.barSurges);
		
		Drawable hpNormal = getResources().getDrawable(R.drawable.hp_bar);
		Drawable hpBloodied = getResources().getDrawable(R.drawable.hp_bar_bloodied);
		
		if(!isInEditMode()) {
			hpObserver = new HitPointObserver(hpNormal, hpBloodied, tvHP, tvSurge, barHP, barSurge);
		
			this.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					ch.getHP().takeDamage(5);
					return true;
				}
			});
		}
	}
	
	public void setCharacter(ManticoreCharacter character) {
		Utility.setText(this, R.id.tvName, character.getName());
		Utility.setTextFromFormat(this, R.id.tvLevel, character.getLevel());
		Utility.setTextFromFormat(this, R.id.tvRaceAndClass,
				character.getRace(),
				character.getHeroicClass(),
				character.getParagonPath() == null ? "" : character.getParagonPath(),
				""//character.getEpicClass() == null ? "" : character.getEpicClass()
				);
		
		if(!isInEditMode())
			((HitPointObserver)hpObserver).setup(character);
		
		ImageView iv = (ImageView)findViewById(R.id.ivPortrait);
		iv.setImageBitmap(character.getPortrait());
		ch = character;
	}
}
