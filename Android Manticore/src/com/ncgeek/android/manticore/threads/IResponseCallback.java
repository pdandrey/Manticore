package com.ncgeek.android.manticore.threads;

import org.json.JSONException;
import org.json.JSONObject;

public interface IResponseCallback {
	public void callback(JSONObject json) throws JSONException;
	public void error(String message, Exception ex);
}