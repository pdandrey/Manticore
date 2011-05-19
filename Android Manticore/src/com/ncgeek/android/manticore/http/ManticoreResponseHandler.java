package com.ncgeek.android.manticore.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;

public class ManticoreResponseHandler extends BasicResponseHandler {

	private String _location;
	
	public String getRedirectLocation() { return _location; }
	
	@Override
	public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
		_location = null;
		
		try {
			return super.handleResponse(response);
		} catch(HttpResponseException httpEx) {
			int code = httpEx.getStatusCode();
			
			if(code >= 300 && code < 400) {
				Header[] headers = response.getHeaders("location");
				
				if(headers.length > 0) 
					_location = headers[0].getValue();
			}
			
			throw httpEx;
		}
	}
}
