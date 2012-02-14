package com.ncgeek.android.manticore.fragments;

import java.text.MessageFormat;
import java.util.Observable;
import java.util.Observer;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.android.manticore.widgets.LabelBar;
import com.ncgeek.manticore.character.HitPoints;
import com.ncgeek.manticore.util.Logger;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.renderscript.Font.Style;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CharacterStatusFragment extends Fragment implements Observer {

	private static final String LOG_TAG = "CharacterStatusFragment";
	
	private ManticoreCharacter _pc;
	
	private LabelBar _barHP;
	private LabelBar _barSurge;
	private View _view;
	
	public CharacterStatusFragment() { }
	public CharacterStatusFragment(ManticoreCharacter pc) {
		this();
		setCharacter(pc);
	}
	
	public void setCharacter(ManticoreCharacter pc) {
		if(_pc != null)
			_pc.deleteObserver(this);
		
		_pc = pc;
		setupCharacter();
		_pc.getHP().addObserver(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		_view = inflater.inflate(R.layout.character_status, container, false);
		
		_barHP = (LabelBar)_view.findViewById(R.id.hpbar);
		_barSurge = (LabelBar)_view.findViewById(R.id.surgebar);
		
		_barHP.addChange(50, "Bloodied", Color.RED, getResources().getDrawable(R.drawable.hp_bar_bloodied));
		
		Typeface fontMorphus = Typeface.createFromAsset(getActivity().getAssets(), "morpheus.ttf");
		TextView tv = (TextView)_view.findViewById(R.id.tvName);
		tv.setTypeface(fontMorphus);
		
		Typeface fontCentaur = Typeface.createFromAsset(getActivity().getAssets(), "centaur.ttf");
		tv = (TextView)_view.findViewById(R.id.tvLevel);
		tv.setTypeface(fontCentaur);
		
		tv = (TextView)_view.findViewById(R.id.tvRaceAndClass);
		tv.setTypeface(fontCentaur);
		
		setupCharacter();
		return _view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		_pc.getHP().deleteObserver(this);
		Logger.debug(LOG_TAG, "onDestroy");
	}
	
	private void setupCharacter() {
		if(_pc == null || _view == null)
			return;
		
		ImageView iv = (ImageView)_view.findViewById(R.id.ivPortrait);
		iv.setImageBitmap(_pc.getPortrait());
		
		Utility.setText(_view, R.id.tvName, _pc.getName());
		Utility.setTextFromFormat(_view, R.id.tvLevel, _pc.getLevel());
		Utility.setTextFromFormat(_view, R.id.tvRaceAndClass,
			_pc.getRace(),
			_pc.getHeroicClass(),
			_pc.getParagonPath() == null ? "" : _pc.getParagonPath(),
			""//_pc.getEpicClass() == null ? "" : _pc.getEpicClass()
			);
		
		HitPoints hp = _pc.getHP();
		_barHP.set(hp.getCurrent(), hp.getTemp(), hp.getMax());
		_barSurge.set(hp.getRemainingSurges(), 0, hp.getTotalSurges());
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if(_pc == null || _view == null)
			return;
		
		HitPoints hp = _pc.getHP();
		_barHP.setMax(hp.getMax());
		_barHP.setCurrent(hp.getCurrent());
		_barHP.setTemporary(hp.getTemp());
		
		_barSurge.setMax(hp.getTotalSurges());
		_barSurge.setCurrent(hp.getRemainingSurges());
	}
}
