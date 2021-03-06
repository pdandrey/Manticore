package com.ncgeek.android.manticore;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ncgeek.android.manticore.util.ManticoreAndroidLogger;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.party.Party;
import com.ncgeek.manticore.util.Logger;

public final class ManticoreStatus {
	
	private final static String LOG_TAG = "Manticore Status";
	
	private static ManticoreStatus INSTANCE; 
	
	public static PlayerCharacter getPC() { return INSTANCE._pc; }
	public static void setPC(PlayerCharacter pc) { INSTANCE._pc = pc; }
	
	public static File getPCFile() { return INSTANCE._filePC; }
	public static void setPCFile(File file) { INSTANCE._filePC = file; }
	
	public static void setParty(Party party) { INSTANCE.party = party; }
	public static Party getParty() { return INSTANCE.party; }
	
	public static File getExternalStorageDirectory() { return INSTANCE._externalDirectory; }
	
	public static void initialize(Context context) { 
		INSTANCE = new ManticoreStatus(context);
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager mgr = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE));
		mgr.getDefaultDisplay().getMetrics(metrics);
		Utility.setDensityDpi(metrics.densityDpi);
	}
	
	private PlayerCharacter _pc;
	private File _filePC;
	private ManticoreAndroidLogger _logger;
	private File _externalDirectory;
	private ManticorePreferences prefs;
	private Party party;
	
	private ManticoreStatus(Context context) {
		setupExternalDirectory();
		
		prefs = new ManticorePreferences(context);
		
		File logDir = null;
		if(_externalDirectory != null)
			logDir = new File(_externalDirectory, "Logs/");
		_logger = new ManticoreAndroidLogger(prefs, logDir);
		Logger.setLogger(_logger);
	}
	
	private void setupExternalDirectory() {
		if(!Utility.isExternalAvailable())
			return;
		
		_externalDirectory = new File(Environment.getExternalStorageDirectory(), "Manticore/");
		
		if(!_externalDirectory.exists()) {
			try {
				if(!_externalDirectory.mkdir()) {
					throw new IOException("Could not create the Manticore/ directory on the SDCard.");
				}
			} catch(Exception ex) {
				Logger.error(LOG_TAG, "Error creating the Manticore/ directory on the SDCard", ex);
			}
		} 
	}
}
