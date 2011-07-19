package com.ncgeek.android.manticore.threads;

import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.ncgeek.android.manticore.http.ManticoreHttpClient;
import com.ncgeek.android.manticore.http.ManticoreHttpResponse;
import com.ncgeek.manticore.util.Logger;

public class ResponseThread extends Thread {
	
	public static final int RESPONSE_ERROR = -1;
	
	private static final String LOG_TAG = "ResponseThread";
	private final String method;
	private final int what;
	private ManticoreHttpClient http;
	private final Handler handler;
	
	public ResponseThread(int what, String method, ManticoreHttpClient http, Handler handler) {
		this.method = method;
		this.http = http;
		this.handler = handler;
		this.what = what;
	}
	
	@Override public void run() {
		ManticoreHttpResponse response = null;
		
		try {
			response = http.get().get(0);
		} catch (InterruptedException intEx) {
			sendError(String.format("ResponseThread.%s was interupted", method), intEx);
		} catch (ExecutionException exeEx) {
			sendError(String.format("ResponseThread.%s failed", method), exeEx);
		}
		
		if(response == null) {
			Logger.error(LOG_TAG, "No response from server");
			sendError(String.format("No response from the server"), null);
		}
		else if(response.getStatusCode() == HttpStatus.SC_OK) {
			String result = response.getResponse();
			Logger.info(LOG_TAG, String.format("ResponseThread.%s Response: %s", method, result));
			Message msg = handler.obtainMessage(what, result);
			handler.sendMessage(msg);
		} else {
			sendError(String.format("Server returned status code %d", response.getStatusCode()), null);
		}
	}
	
	private void sendError(String message, Exception ex) {
		Message msg = handler.obtainMessage(RESPONSE_ERROR);
		msg.getData().putString("message", message);
		msg.obj = ex;
		handler.sendMessage(msg);
	}
}