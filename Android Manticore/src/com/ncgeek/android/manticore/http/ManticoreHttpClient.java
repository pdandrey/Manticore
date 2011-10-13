package com.ncgeek.android.manticore.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;

import com.ncgeek.manticore.util.Logger;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

public class ManticoreHttpClient extends AsyncTask<HttpUriRequest, Void, List<ManticoreHttpResponse>> {

	private static final String LOG_TAG = "ManticoreHttpClient";
	
	private AndroidHttpClient _http;
	private BasicCookieStore _cookies;
	private BasicHttpContext _context;
	private ManticoreResponseHandler _handler; 
	private String _userAgent;
	
	public ManticoreHttpClient(String userAgent) {
		_userAgent = userAgent;
	}
	
	public void setProxy(String host, int port) {
		HttpHost proxy = new HttpHost(host, port);
		_http.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}
	
	@Override
	protected List<ManticoreHttpResponse> doInBackground(HttpUriRequest... requests) {
		_http = AndroidHttpClient.newInstance(_userAgent);
		_cookies = new BasicCookieStore();
		_context = new BasicHttpContext();
		_context.setAttribute(ClientContext.COOKIE_STORE, _cookies);
		_handler = new ManticoreResponseHandler();
		
		ArrayList<ManticoreHttpResponse> ret = new ArrayList<ManticoreHttpResponse>(requests.length);
		
		for(HttpUriRequest request : requests) {
			ret.add(httpExecute(request));
		}
		
		_http.close();
		_http = null;
		return ret;
	}

	private ManticoreHttpResponse httpExecute(HttpUriRequest request) {
		int statusCode = 200;
		String ret = "";
		try {
			Logger.verbose(LOG_TAG, String.format("Executing %s %s", request.getMethod(), request.getURI().toString()));
			ret = _http.execute(request, _handler, _context);
		} catch(HttpResponseException httpEx) {
			// Bad status code
			statusCode = httpEx.getStatusCode();
			ret = httpEx.getLocalizedMessage();
			Logger.warn(LOG_TAG, String.format("Error executing %s %s", request.getMethod(), request.getURI().toString()), httpEx);
		} catch (IOException ioEx) {
			Logger.error(LOG_TAG, "Error loading page " + request.getURI().toString(), ioEx);
			return null;
		}
		
		return new ManticoreHttpResponse(statusCode, ret, _handler.getHeaders());
	}
	
	public static HttpPost post(String dest, List<NameValuePair> params) throws UnsupportedEncodingException {
		HttpPost post = new HttpPost(dest);
		//put.addHeader("Content-Type", "application/json; charset=utf-8");
		if(params != null) {
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
	    	post.setEntity(ent);
		}
		return post;
	}
	
	public static HttpPut put(String dest, List<NameValuePair> params) {
		HttpPut put = new HttpPut(dest);
		//put.addHeader("Content-Type", "application/json; charset=utf-8");
		if(params != null) {
			try {
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		    	put.setEntity(ent);
			} catch (UnsupportedEncodingException e) {
				Logger.error(LOG_TAG, "Cannot encode input fields", e);
			}
		}
		return put;
	}
	
	public static HttpGet get(String dest) {
		return new HttpGet(dest);
	}
}
