package com.ncgeek.android.manticore.mock;

public interface IMockable {
	
	public IMockable returnWhen(Object returnValue, String method, Object...params);
	public IMockable throwWhen(Throwable tr, String method, Object...params);
	
}
