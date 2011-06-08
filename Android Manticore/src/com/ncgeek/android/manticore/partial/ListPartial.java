package com.ncgeek.android.manticore.partial;

import com.ncgeek.android.manticore.R;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class ListPartial extends Partial {

	public ListPartial(Context context, BaseAdapter adapter) {
		super(context, R.layout.featview);
		GridView gv = (GridView)getView().findViewById(R.id.featview_grid);
		gv.setAdapter(adapter);
		if(adapter instanceof AdapterView.OnItemClickListener) {
        	gv.setOnItemClickListener((AdapterView.OnItemClickListener)adapter);
        }
	}

	@Override
	public void update() {
	}

	@Override
	public int getContextMenuID() {
		return 0;
	}

	@Override
	public void setupContextMenu(Menu menu) {
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return false;
	}
}
