package com.ncgeek.android.manticore.activities;

import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.adapters.FeatListAdapter;
import com.ncgeek.android.manticore.adapters.RitualListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class FeatView extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.featview);
        GridView gv = (GridView)findViewById(R.id.featview_grid);
        
        Bundle extras = getIntent().getExtras();
        String type = extras.getString("type");
        
        ArrayAdapter<?> adapter = null;
        
        if(type.equals("feat")) 
        	adapter = new FeatListAdapter(this, R.layout.feat_list_item, ManticoreStatus.getPC().getFeats());
        else if(type.equals("ritual")) {
        	adapter = new RitualListAdapter(this, R.layout.ritual_listitem, ManticoreStatus.getPC().getRituals());
        }
        
        gv.setAdapter(adapter);
        
        if(adapter instanceof AdapterView.OnItemClickListener) {
        	gv.setOnItemClickListener((AdapterView.OnItemClickListener)adapter);
        }
	}
}
