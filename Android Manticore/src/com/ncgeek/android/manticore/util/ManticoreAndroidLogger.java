package com.ncgeek.android.manticore.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.manticore.util.ILog;
import com.ncgeek.manticore.util.LogPriority;

public class ManticoreAndroidLogger implements ILog {

	private static final String LOG_TAG = "ManticoreAndroidLogger";
	
	private File _log;
	private ManticorePreferences prefs;
	
	public ManticoreAndroidLogger(ManticorePreferences prefs, File logDirectory) {
		this.prefs = prefs;
		
		if(logDirectory == null)
			return;
		
		Date now = new Date();
		SimpleDateFormat sdf= new SimpleDateFormat("MM-dd-yyyy");
		
		_log = new File(logDirectory, sdf.format(now) + ".log");
	}
	
	@Override
	public void log(LogPriority priority, String tag, String msg, Throwable tr) {
		switch(priority) {
			case Verbose:
				if(Log.isLoggable(tag, Log.VERBOSE)) {
					Log.v(tag, msg, tr);
					logToFile(priority, tag, msg, tr);
				}
				break;
				
			case Info:
				if(Log.isLoggable(tag, Log.INFO)) {
					Log.i(tag, msg, tr);
					logToFile(priority, tag, msg, tr);
				}
				break;
				
			case Warn:
				if(Log.isLoggable(tag, Log.WARN)) {
					Log.w(tag, msg, tr);
					logToFile(priority, tag, msg, tr);
				}
				break;
				
			case Debug:
				if(Log.isLoggable(tag, Log.DEBUG)) {
					Log.d(tag, msg, tr);
					logToFile(priority, tag, msg, tr);
				}
				break;
				
			case Error:
				if(Log.isLoggable(tag, Log.ERROR)) {
					Log.e(tag, msg, tr);
					logToFile(priority, tag, msg, tr);
				}
				break;
		}
	}

	private void logToFile(LogPriority priority, String tag, String msg, Throwable tr) {
		Date now = new Date();
		DateFormat sdf = DateFormat.getDateTimeInstance();
		
		if(_log == null || !prefs.logToFile())
			return;
		
		ensureLogDirectory();
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(_log, true));
			writer.write(String.format("%s\t%s\t%s\t%s\n", priority.toString(), sdf.format(now), tag, msg));
			
			while(tr != null) {
				writer.write("\t" + tr.getMessage() + "\n");
				
				for(StackTraceElement s : tr.getStackTrace()) {
					writer.write(String.format("\t at %s.%s :%d\n", s.getFileName(), s.getMethodName(), s.getLineNumber()));
				}
				
				tr = tr.getCause();
				
				if(tr != null) {
					writer.write("\t--- Caused by ---\n");
				}
			}
			
			writer.close();
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error writing log to file", e);
		}
		
	}

	private void ensureLogDirectory() {
		File logDirectory = _log.getParentFile();
		
		if(!logDirectory.exists()) {
			if(!logDirectory.mkdirs()) {
				Log.e(LOG_TAG, "Could not create directory " + logDirectory.toString());
			}
		}
	}
}
