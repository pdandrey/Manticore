package com.ncgeek.android.manticore.fragments;

import java.util.Observable;
import java.util.Observer;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.widgets.LabelBar;
import com.ncgeek.manticore.character.HitPoints;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CharacterStatusFragment extends Fragment implements Observer {

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
		//return super.onCreateView(inflater, container, savedInstanceState);
		_view = inflater.inflate(R.layout.character_status, null);
		
		_barHP = (LabelBar)_view.findViewById(R.id.charactersheet_hpbar);
		_barSurge = (LabelBar)_view.findViewById(R.id.charactersheet_surgebar);
		
		_barHP.addChange(50, "Bloodied", Color.RED, getResources().getDrawable(R.drawable.hp_bar_bloodied));
		
		setupCharacter();
		return _view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		_pc.getHP().deleteObserver(this);
	}
	
	private void setupCharacter() {
		if(_pc == null || _view == null)
			return;
		
		ImageView iv = (ImageView)_view.findViewById(R.id.characterstatus_img);
		iv.setImageBitmap(_pc.getPortrait());
		
		TextView tv = (TextView)_view.findViewById(R.id.characterstatus_txtName);
		tv.setText(_pc.getName());
		
		tv = (TextView)_view.findViewById(R.id.characterstatus_txtClass);
		tv.setText(_pc.getHeroicClass());
		
		tv = (TextView)_view.findViewById(R.id.characterstatus_txtLevel);
		tv.setText(Integer.toString(_pc.getLevel()));
		
		tv = (TextView)_view.findViewById(R.id.characterstatus_txtRace);
		tv.setText(_pc.getRace());
		
		HitPoints hp = _pc.getHP();
		_barHP.setMax(hp.getMax());
		_barHP.setCurrent(hp.getCurrent());
		_barHP.setTemporary(hp.getTemp());
		
		_barSurge.setMax(hp.getTotalSurges());
		_barSurge.setCurrent(hp.getRemainingSurges());
		
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
