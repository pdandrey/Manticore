package com.ncgeek.manticore.util;

public interface ILog {
	public void log(LogPriority priority, String tag, String message, Throwable tr);
}
