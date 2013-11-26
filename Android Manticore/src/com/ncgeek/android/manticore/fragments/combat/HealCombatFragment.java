package com.ncgeek.android.manticore.fragments.combat;

import com.ncgeek.android.manticore.R;
import com.ncgeek.manticore.util.Logger;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class HealCombatFragment extends BaseCombatFragment {

	private static final String LOG_TAG = "CombatHealFgmt";
	
	public HealCombatFragment() {
		super.setLayout(R.layout.combat_heal);
	}

	@Override
	protected void hookupEvents(final View view) {
		((Button)view.findViewById(R.id.btnOk)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox chkSurge = (CheckBox)view.findViewById(R.id.chkUseSurge);
				EditText txt = (EditText)view.findViewById(R.id.txtAdditional);
				
				int heal = 0;
				
				try {
					heal = Integer.parseInt(txt.getText().toString());
				} catch(Exception ex) {}
				
				if(chkSurge.isChecked())
					getCharacter().getHP().useSurge(heal);
				else if(heal > 0)
					getCharacter().getHP().heal(heal);
				
				chkSurge.setChecked(false);
				txt.setText("");
				
				getParent().showMain();
			}
		});
	}
	
	
	
	
}
