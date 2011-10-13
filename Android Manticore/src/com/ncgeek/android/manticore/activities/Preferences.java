package com.ncgeek.android.manticore.activities;

import com.ncgeek.android.manticore.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new com.ncgeek.android.manticore.ManticorePreferences(this).getPartyPollInterval();
        addPreferencesFromResource(R.xml.preferences);
    }
}
