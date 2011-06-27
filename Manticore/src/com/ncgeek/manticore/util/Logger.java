package com.ncgeek.manticore.util;

public final class Logger {
	
	private static ILog _log;
	public static void setLogger(ILog log) { _log = log; }
	
	private Logger() {}
	
	public static void verbose(String tag, String msg) { verbose(tag, msg, null); }
	public static void verbose(String tag, String msg, Throwable tr) {
		log(LogPriority.Verbose, tag, msg, tr);
	}
	
	public static void info(String tag, String msg) { info(tag, msg, null); }
	public static void info(String tag, String msg, Throwable tr) { 
		log(LogPriority.Info, tag, msg, tr);
	}
	
	public static void debug(String tag, String msg) { debug(tag, msg, null); }
	public static void debug(String tag, String msg, Throwable tr) {
		log(LogPriority.Debug, tag, msg, tr);
	}
	
	public static void warn(String tag, String msg) { warn(tag, msg, null); }
	public static void warn(String tag, String msg, Throwable tr) {
		log(LogPriority.Warn, tag, msg, tr);
	}
	
	public static void error(String tag, String msg) { error(tag, msg, null); }
	public static void error(String tag, String msg, Throwable tr) {
		log(LogPriority.Error, tag, msg, tr);
	}
	
	private static void log(LogPriority priority, String tag, String msg, Throwable tr) {
		if(_log != null) {
			_log.log(priority, tag, msg, tr);
		}
	}
}
