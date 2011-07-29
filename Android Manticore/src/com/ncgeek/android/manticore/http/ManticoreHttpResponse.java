package com.ncgeek.android.manticore.http;

import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

public final class ManticoreHttpResponse {

	private static final int[] REDIRECT_CODES = {
		HttpStatus.SC_MOVED_PERMANENTLY, 	// 301
		HttpStatus.SC_MOVED_TEMPORARILY, 	// 302
		HttpStatus.SC_SEE_OTHER,			// 303
		HttpStatus.SC_TEMPORARY_REDIRECT	// 307
	};
	
	private int statusCode;
	private String response;
	private String redirect;
	private Header[] headers;
	
	public ManticoreHttpResponse(int status, String response, Header[] headers) {
		this.statusCode = status;
		this.response = response;
		this.headers = headers;
		
		if(headers != null && Arrays.binarySearch(REDIRECT_CODES, status) >= 0) {
			for(Header h : headers) {
				if(h.getName().equals("location")) {
					redirect = h.getValue();
					break;
				}
			}
		}
	}

	public final int getStatusCode() {
		return statusCode;
	}

	public final String getResponse() {
		return response;
	}

	public final Header[] getHeaders() {
		return headers;
	}
	
	public final String getRedirect() {
		return redirect;
	}
}
