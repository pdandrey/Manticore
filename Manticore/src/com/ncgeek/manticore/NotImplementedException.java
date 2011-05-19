package com.ncgeek.manticore;

public class NotImplementedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotImplementedException() {
		this("Not Yet Implemented");
	}

	public NotImplementedException(String arg0) {
		super(arg0);
	}

	public NotImplementedException(Throwable arg0) {
		this("Not Yet Implemented", arg0);
	}

	public NotImplementedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
