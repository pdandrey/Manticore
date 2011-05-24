package com.ncgeek.android.manticore;

import com.ncgeek.android.manticore.util.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public final class ManticorePreferences {

	private SharedPreferences prefs;
	
	public ManticorePreferences(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public final boolean isDatabaseEnabled() {
		return prefs.getBoolean("UseDatabase", true);
	}

	public final boolean useCalculatedStats() {
		return prefs.getBoolean("UseCalculatedStats", true);
	}
	
	public final boolean cacheImages() {
		return prefs.getBoolean("CacheImages", true);
	}
	
	public final boolean shouldCopyDatabase() {
		boolean copy = prefs.getBoolean("CopyDatabase", true);
		boolean db = isDatabaseEnabled();
		
		if(!db && copy) {
			copy = false;
			Editor e = prefs.edit();
			e.putBoolean("CopyDatabase", copy);
			e.commit();
		}
		
		return copy;
	}
	
	public final boolean logToFile() {
		return Utility.isExternalWritable() && prefs.getBoolean("FileLogs", true);
	}
}
