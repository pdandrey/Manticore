package com.ncgeek.android.manticore.threads;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.os.Handler;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.http.ManticoreHttpClient;
import com.ncgeek.android.manticore.partial.PartyPartial;
import com.ncgeek.manticore.util.Logger;

public class GetUpdatesThread extends Thread {
	
	private static final String LOG_TAG = "GetUpdatesThread";
	
	private boolean run;
	private final ArrayList<NameValuePair> lstParams;
	private final String url;
	private final ManticorePreferences prefs;
	private final Handler handler;
	
	public GetUpdatesThread(String characterToken, ManticorePreferences prefs, Handler handler) { 
		run = true; 
		lstParams = new ArrayList<NameValuePair>();
		lstParams.add(new BasicNameValuePair("token", characterToken));
		url = prefs.getJullianServer() + "GetEvents";
		this.prefs = prefs;
		this.handler = handler;
	}
	
	@Override
	public void run() {
		// do a delay to start with.
		try {
			Thread.sleep(15000);
		} catch(InterruptedException intEx) {
			Logger.debug(LOG_TAG, "Sleep was interrupted.", intEx);
		}
		
		while(run) {
			Logger.debug(LOG_TAG, "Getting Events");
			HttpPost post = null;
			try {
				post = ManticoreHttpClient.post(url, lstParams);
			} catch (UnsupportedEncodingException e) {
				Logger.error(LOG_TAG, "Error creating post", e);
			}
			
			if(post != null) {
				final ManticoreHttpClient http = new ManticoreHttpClient("Android Manticore/PartyService");
				http.execute(post);
				
				Thread t = new ResponseThread(PartyPartial.GET_UPDATES, "GetEvents", http, handler);
				t.start();
				try {
					t.join();
				} catch(InterruptedException intEx) {
					Logger.debug(LOG_TAG, "GetUpdates ResponseThread.join was interrupted.", intEx);
				}
			}
			try {
				Thread.sleep(prefs.getPartyPollInterval() * 1000);
			} catch(InterruptedException intEx) {
				Logger.debug(LOG_TAG, "Sleep was interrupted.", intEx);
			}
		}
	}
}