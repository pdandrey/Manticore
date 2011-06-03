package com.ncgeek.android.manticore.partial;

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
		
		updateActionPoints();
	}
	
	private void updateActionPoints() {
		((TextView)getView().findViewById(R.id.partialstats_tvActionPoints)).setText(ManticoreStatus.getPC().getActionPoints() + "");
	}
	
	private void setStat(int id, Stat stat) {
		((StatView)getView().findViewById(id)).setStat(stat);
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
