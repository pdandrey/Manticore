package com.ncgeek.android.manticore.partial;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.CharacterSheet;
import com.ncgeek.android.manticore.widgets.StatView;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.character.stats.StatBlock;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class StatPartial extends Partial {
	
	public StatPartial(CharacterSheet context) {
		super(context, R.layout.partial_stats);
		
		View v = getView().findViewById(R.id.partialstats_llActionPoints);
        v.setOnClickListener(context.getContextMenuClickListener());
        context.registerForContextMenu(v);
	}
	
	@Override
	public void update() {
		StatBlock stats = ManticoreStatus.getPC().getStats();
		
		setStat(R.id.partialstats_svDefenseAC, stats.get("AC"));
		setStat(R.id.partialstats_svDefenseFort, stats.get("Fortitude"));
		setStat(R.id.partialstats_svDefenseReflex, stats.get("Reflex"));
		setStat(R.id.partialstats_svDefenseWill, stats.get("Will"));
		setStat(R.id.partialstats_svStatInitiative, stats.get("Initiative"));
		setStat(R.id.partialstats_svStatSpeed, stats.get("Speed"));
		
		ManticorePreferences prefs = new ManticorePreferences(getContext());
		setAbility(R.id.partialstats_txtStr, R.id.partialstats_txtStrMod, stats.get("Strength"), prefs.useCalculatedStats());
		setAbility(R.id.partialstats_txtCon, R.id.partialstats_txtConMod, stats.get("Con"), prefs.useCalculatedStats());
		setAbility(R.id.partialstats_txtDex, R.id.partialstats_txtDexMod, stats.get("Dex"), prefs.useCalculatedStats());
		setAbility(R.id.partialstats_txtInt, R.id.partialstats_txtIntMod, stats.get("Int"), prefs.useCalculatedStats());
		setAbility(R.id.partialstats_txtWis, R.id.partialstats_txtWisMod, stats.get("Wis"), prefs.useCalculatedStats());
		setAbility(R.id.partialstats_txtCha, R.id.partialstats_txtChaMod, stats.get("Cha"), prefs.useCalculatedStats());
		
		updateActionPoints();
	}
	
	private void updateActionPoints() {
		((TextView)getView().findViewById(R.id.partialstats_tvActionPoints)).setText(ManticoreStatus.getPC().getActionPoints() + "");
	}
	
	private void setStat(int id, Stat stat) {
		((StatView)getView().findViewById(id)).setStat(stat);
	}
	
	private void setAbility(int id, int modId, Stat ability, boolean useCalculated) {
		View v = getView();
		TextView tvMain = (TextView)v.findViewById(id);
		TextView tvMod = (TextView)v.findViewById(modId);
		
		if(useCalculated) {
			tvMain.setText(ability.getCalculatedValue() + "");
		} else {
			tvMain.setText(ability.getAbsoluteValue() + "");
		}
		int mod = ability.getModifier();
		String sMod = "+" + mod;
		if(mod < 0)
			sMod = mod + "";
		tvMod.setText(sMod);
	}

	@Override
	public int getContextMenuID() {
		return R.menu.charactersheet_actionpoints;
	}

	@Override
	public void setupContextMenu(Menu menu) {
		menu.setGroupEnabled(R.id.charactersheet_mnugrpActionPoints, ManticoreStatus.getPC().getActionPoints() > 0);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		PlayerCharacter pc = ManticoreStatus.getPC();
		switch(item.getItemId()) {
		case R.id.charactersheet_mnuUseActionPoint:
			pc.useActionPoint();
			updateActionPoints();
			return true;
	
		case R.id.charactersheet_mnuMilestone:
			pc.milestone();
			updateActionPoints();
			return true;
		}
		return false;
	}
}
