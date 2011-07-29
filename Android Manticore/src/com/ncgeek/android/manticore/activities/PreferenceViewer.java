package com.ncgeek.android.manticore.activities;

import java.util.Map;

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
        
        Map<String,?> all = prefs.getAll();
        for(String key : all.keySet()) {
        	Object val = all.get(key);
        	buf.append(String.format("[%s] %s: %s\n", val.getClass().getSimpleName(), key, val.toString()));
        }
        
        txt.setText(buf);
    }
}
