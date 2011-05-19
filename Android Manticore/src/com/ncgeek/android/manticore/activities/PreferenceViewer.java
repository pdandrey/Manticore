package com.ncgeek.android.manticore.activities;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PreferenceViewer extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferenceviewer);
        
        TextView txt = (TextView)findViewById(R.id.preferenceviewer_txt);
        
        ManticorePreferences prefs = new ManticorePreferences(this);
        StringBuilder buf = new StringBuilder();
        buf.append("UseDatabase: " + prefs.isDatabaseEnabled());
        buf.append("\nCopy Database: " + prefs.shouldCopyDatabase());
        buf.append("\nUse Calculated Stats: " + prefs.useCalculatedStats());
        buf.append("\nWrite logs to file: " + prefs.logToFile());
        
        txt.setText(buf);
    }
}
