package com.ncgeek.android.manticore.mock;

import android.os.Handler;
import android.os.Message;

public class MockHandler extends Handler implements IMockable {

	private MockCounter counter;
	
	public MockHandler() {
		counter = new MockCounter();
	}
	
	@Override
	public void dispatchMessage(Message msg) {
		counter.call("dispatchMessage", msg);
	}

	@Override
	public void handleMessage(Message msg) {
		counter.call("handleMessage", msg);
	}

	@Override
	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
		Object o = counter.call("sendMessageAtTime", msg, uptimeMillis);
		if(o == null)
			return true;
		else
			return (boolean)(Boolean)o;
	}

	@Override
	public IMockable returnWhen(Object returnValue, String method,
			Object... params) {
		counter.returnWhen(returnValue, method, params);
		return this;
	}

	@Override
	public IMockable throwWhen(Throwable tr, String method, Object... params) {
		counter.throwWhen(tr, method, params);
		return this;
	}

}
