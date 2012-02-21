package com.ncgeek.android.manticore.actionbar;

import java.util.Observable;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

public abstract class ActionBarHelper extends Observable {

	public static ActionBarHelper createInstance(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return new ActionBarHelperICS(activity);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return new ActionBarHelperHoneycomb(activity);
        } else {
            return new ActionBarHelperBase(activity);
        }
	}
	
	private Activity activity;
	
	public ActionBarHelper(Activity activity) {
		this.activity = activity;
	}
	
	public final Activity getActivity() { return activity; }
	
	public void onCreate(Bundle savedInstanceState) {}
	public boolean onCreateOptionsMenu(Menu menu) { return true; }
	public void onSaveInstanceState(Bundle outState) {}
}
