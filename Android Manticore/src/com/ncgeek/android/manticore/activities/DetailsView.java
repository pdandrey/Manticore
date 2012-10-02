package com.ncgeek.android.manticore.activities;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.fragments.StatDetailFragment;
import com.ncgeek.manticore.character.stats.Stat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class DetailsView extends FragmentActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        Bundle extras = this.getIntent().getExtras();
        
        Stat item = (Stat)extras.getSerializable("item");
        
        setContentView(R.layout.detailview_full);
        FragmentManager fm = getSupportFragmentManager();

        StatDetailFragment fgmt = (StatDetailFragment)fm.findFragmentByTag("StatDetail");
        fgmt.setStat(item);
	 }
	
	}
