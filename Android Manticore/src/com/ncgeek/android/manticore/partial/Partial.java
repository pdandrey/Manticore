package com.ncgeek.android.manticore.partial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public abstract class Partial {
	
	private View view;
	private Context context;
	private LayoutInflater inflater;
	
	public Partial(Context context, int resID) {
		this.context = context;
		inflate(resID);
	}
	
	public final View getView() { return view; }
	protected final void setView(View view) { this.view = view; }
	
	protected final Context getContext() { return context; }
	private final LayoutInflater getLayoutInflater() {
		if(inflater == null)
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater;
	}
	
	private final void inflate(int resID) {
		view = getLayoutInflater().inflate(resID, null);
	}
	
	public abstract void update();
	
	public abstract int getContextMenuID();
	
	public abstract void setupContextMenu(Menu menu);
	
	public abstract boolean onContextItemSelected(MenuItem item);
	
	public void onPause() {}
	
	public void onStart() {}
}
