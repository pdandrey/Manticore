package com.ncgeek.android.manticore.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;

import com.ncgeek.manticore.util.Logger;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

class ManticoreCompendiumHttpClient extends AsyncTask<String, Void, String> {

	private static final String USER_AGENT = "mozilla/5.0 (linux; u; android 2.2; en-us; sprint apa9292kt build/frf91) applewebkit/533.1 (khtml, like gecko) version/4.0 mobile safari/533.1";
	private static final String LOGIN_PAGE = "login.aspx";
	private static final String LOG_TAG = "http";
	
	private static AndroidHttpClient _http;
	private static BasicCookieStore _cookies;
	private static BasicHttpContext _context;
	private static ManticoreResponseHandler _handler; 
	
	static {
		_http = AndroidHttpClient.newInstance(USER_AGENT);
		_cookies = new BasicCookieStore();
		_context = new BasicHttpContext();
		_context.setAttribute(ClientContext.COOKIE_STORE, _cookies);
		_handler = new ManticoreResponseHandler();
	}
	
	public static void setProxy(String host, int port) {
		HttpHost proxy = new HttpHost(host, port);
		_http.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}
	
	private String _email;
	private String _password;
	private int _statusCode;
	
	public ManticoreCompendiumHttpClient(String email, String password) {
		_email = email;
		_password = password;
	}
	
	public int getStatusCode() { return _statusCode; }
	
	@Override
	protected String doInBackground(String... params) {
		if(params.length > 4)
			throw new IllegalArgumentException("Usage: <url> <data> <referer> <content type>");
		
		URI url = null;
		try {
			url = new URI(params[0]);
		} catch(URISyntaxException urlEx) {
			throw new IllegalArgumentException("Invalid URL", urlEx);
		}
		
		HttpUriRequest request = new HttpGet(url);
		
		if(params.length >= 2) {
			String referer = null;
			if(params.length >= 3)
				referer = params[2];
			request = getPost(url, referer, params[1]);
		}
		
		if(params.length >= 4) {
			request.addHeader("Content-Type", params[3]);
		}
		
		String ret = httpExecute(request, true);
		
		return ret;
	}

	private String httpExecute(HttpUriRequest request, boolean autoLogin) {
		_statusCode = 0;
		
		try {
			String ret = _http.execute(request, _handler, _context);
			return ret;
		} catch(HttpResponseException httpEx) {
			// Bad status code
			switch(httpEx.getStatusCode()) {
				case HttpStatus.SC_MOVED_PERMANENTLY: 	// 301
				case HttpStatus.SC_MOVED_TEMPORARILY: 	// 302
				case HttpStatus.SC_SEE_OTHER:			// 303
				case HttpStatus.SC_TEMPORARY_REDIRECT:	// 307
					String location = _handler.getRedirectLocation();
					if(autoLogin && location.toLowerCase().contains(LOGIN_PAGE)) {
						handleLogin(location);
						return httpExecute(request, false);
					} else {
						HttpGet get = new HttpGet(location);
						get.addHeader("Referer", location);
						return httpExecute(get, autoLogin);
					}
					
				default:
					_statusCode = httpEx.getStatusCode();
					Logger.warn(LOG_TAG, "Unhandled Status Code: " + _statusCode, httpEx);
					return null;
			}
		} catch (IOException ioEx) {
			Logger.error(LOG_TAG, "Error loading page " + request.getURI().toString(), ioEx);
			return null;
		}
	}
	
	private void handleLogin(String location) {
		HttpGet getLogin = new HttpGet(location);
		String pageLogin = httpExecute(getLogin, false);
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	Pattern regex = Pattern.compile("\\<input.*?name=\\\"(.*?)\\\".(?:.*?value=\\\"(.*?)\\\")?.*?>");
    	Matcher m = regex.matcher(pageLogin);
    	
    	while(m.find()) {
    		String name = m.group(1);
    		String value = m.group(2);
    		if(name.equals("email"))
    			value = _email;
    		else if(name.equals("password"))
    			value = _password;
	        params.add(new BasicNameValuePair(name, value));
    	}
    	
    	HttpPost postLogin = getPost(location, location, params);
    	httpExecute(postLogin, false);
	}
	
	private HttpPost getPost(String location, String referer, List<NameValuePair> params) {
		HttpPost ret = new HttpPost(location);
		
		if(referer != null)
			ret.addHeader("Referer", referer);
		
		if(params != null) {
			try {
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.ASCII);
		    	ret.setEntity(ent);
			} catch (UnsupportedEncodingException e) {
				Logger.error(LOG_TAG, "Cannot encode input fields", e);
			}
		}
		
		return ret;
	}
	
	private HttpPost getPost(URI location, String referer, String data) {
		HttpPost ret = new HttpPost(location);
		
		if(referer != null)
			ret.addHeader("Referer", referer);
		
		if(data != null) {
			
			try {
				ret.setEntity(new StringEntity(data));
			} catch (UnsupportedEncodingException e) {
				Logger.error(LOG_TAG, "Cannot encode input fields", e);
			}
		}
		
		return ret;
	}
}
