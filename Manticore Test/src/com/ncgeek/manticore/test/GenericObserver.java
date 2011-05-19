package com.ncgeek.manticore.test;

import java.util.Observable;
import java.util.Observer;

public class GenericObserver implements Observer {

	private Observable sender;
	private Object data;
	private boolean fired;
	
	public Observable getSender() { return sender; }
	public Object getData() { return data; }
	public boolean wasFired() { return fired; }
	public void reset() { 
		sender = null;
		data = null;
		fired = false;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		sender = arg0;
		data = arg1;
		fired = true;
	}

}
