package com.ncgeek.android.manticore.widgets;

import java.util.Observable;
import java.util.Observer;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.character.HitPoints;

public class HitPointObserver implements Observer {

	private HitPoints hp;
	
	private Drawable hpNormal;
	private Drawable hpBloodied;
	private TextView tvHP;
	private TextView tvSurge;
	private ProgressBar barHP;
	private ProgressBar barSurge;
	
	public HitPointObserver(Drawable normal, Drawable bloodied, TextView hp, TextView surge, ProgressBar bhp, ProgressBar bs) {
		hpNormal = normal;
		hpBloodied = bloodied;
		tvHP = hp;
		tvSurge = surge;
		barHP = bhp;
		barSurge = bs;
	}
	
	@Override
	protected void finalize() throws Throwable {
		Log.d("HitPointObserver", "finalize()");
		if(hp != null) {
			hp.deleteObserver(this);
			hp = null;
		}
		super.finalize();
	}

	public void setup(ManticoreCharacter character) {
		if(hp != null) {
			hp.deleteObserver(this);
			hp = null;
		}
		
		barHP.setProgress(0);
		barHP.setMax(character.getHP().getMax());
		
		barSurge.setProgress(0);
		barSurge.setMax(character.getHP().getTotalSurges());
		
		character.getHP().addObserver(this);
		hp = character.getHP();
		update(character.getHP(), null);
	}
	
	@Override
	public void update(Observable observable, Object data) {			
		Utility.setTextFromFormat(tvHP, (Object)hp.getCurrent(), hp.getMax());
		Utility.setTextFromFormat(tvSurge, (Object)hp.getRemainingSurges(), hp.getTotalSurges());
		barHP.setProgress(hp.getCurrent());
		barSurge.setProgress(hp.getRemainingSurges());
		
		if(hp.isBloodied()) {
			barHP.setProgressDrawable(hpBloodied);
		} else {
			barHP.setProgressDrawable(hpNormal);
		}
	}
}
