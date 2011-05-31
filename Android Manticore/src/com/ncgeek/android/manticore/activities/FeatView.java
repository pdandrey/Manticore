package com.ncgeek.android.manticore.activities;

import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.adapters.FeatListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class FeatView extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.featview);
        GridView gv = (GridView)findViewById(R.id.featview_grid);
        gv.setAdapter(new FeatListAdapter(this, R.layout.feat_list_item, ManticoreStatus.getPC().getFeats()));
	}
}
