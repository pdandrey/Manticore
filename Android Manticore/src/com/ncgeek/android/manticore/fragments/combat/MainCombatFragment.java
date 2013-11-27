package com.ncgeek.android.manticore.fragments.combat;



import com.ncgeek.android.manticore.R;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainCombatFragment extends BaseCombatFragment {

	private static final String LOG_TAG = "CombatFgmt";
	
	public MainCombatFragment() {
		setLayout(R.layout.combat_main);
	}

	@Override
	protected void hookupEvents(View v) {
		
		((Button)v.findViewById(R.id.btnHeal)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getParent().showHeal();
			}
		});
		
		((TextView)v.findViewById(R.id.tvActionPoints)).setText(String.format("%d", getCharacter().getActionPoints()));
	}
}
