package com.ncgeek.android.manticore;

import java.util.Map;

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
	
	public final boolean SkillsInGrid() {
		return prefs.getBoolean("SkillsInGrid", true);
	}
	
	public final String CharacterBuilderVersion() {
		return prefs.getString("CharBuilderVersion", "223.241754");
	}
	
	public final int getPartyPollInterval() {
		try {
			return Integer.parseInt(prefs.getString("PartyPollInterval", "30"));
		} catch(ClassCastException ccex) {
			int val = prefs.getInt("PartyPollInterval", 30);
			Editor edit = prefs.edit();
			edit.putString("PartyPollInterval", val + "");
			edit.commit();
			return val;
		} catch(NumberFormatException nfex) {
			return 30;
		}
	}
	
	public final String getJullianServer() {
		String url = prefs.getString("JullianServer", "http://10.0.2.2/JullianServer/");
		if(!url.endsWith("/")) {
			url += "/";
			Editor edit = prefs.edit();
			edit.putString("JullianServer", url);
			edit.commit();
		}
		return url;
	}
	
	public final String getJullianServerPartyUrl(String action) {
		return String.format("%sParty/%s", getJullianServer(), action);
	}
	
	public final Map<String,?> getAll() {
		return prefs.getAll();
	}
}
