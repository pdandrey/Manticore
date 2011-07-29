package com.ncgeek.android.manticore.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;

public class ManticoreResponseHandler extends BasicResponseHandler {

	private String _location;
	private Header[] headers;
	
	public String getRedirectLocation() { return _location; }
	public Header[] getHeaders() { return headers; }
	
	@Override
	public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
		_location = null;
		headers = response.getAllHeaders();
		
		try {
			String ret = super.handleResponse(response);
			return ret;
		} catch(HttpResponseException httpEx) {
			int code = httpEx.getStatusCode();
			
			if(code >= 300 && code < 400) {
				Header[] h = response.getHeaders("location");
				
				if(h.length > 0) 
					_location = h[0].getValue();
			}
			
			throw httpEx;
		}
	}
}
