package com.ncgeek.manticore.util;

public final class Logger {
	
	private static ILog _log;
	public static void setLogger(ILog log) { _log = log; }
	
	private Logger() {}
	
	public static void verbose(String tag, String msg, Object ... args) { verbose(tag, msg, null, args); }
	public static void verbose(String tag, String msg, Throwable tr, Object...args) {
		log(LogPriority.Verbose, tag, msg, tr, args);
	}
	
	public static void info(String tag, String msg, Object ... args) { info(tag, msg, null, args); }
	public static void info(String tag, String msg, Throwable tr, Object ... args) { 
		log(LogPriority.Info, tag, msg, tr, args);
	}
	
	public static void debug(String tag, String msg, Object ... args) { debug(tag, msg, null,args); }
	public static void debug(String tag, String msg, Throwable tr, Object ... args) {
		log(LogPriority.Debug, tag, msg, tr,args);
	}
	
	public static void warn(String tag, String msg, Object ... args) { warn(tag, msg, null,args); }
	public static void warn(String tag, String msg, Throwable tr, Object ... args) {
		log(LogPriority.Warn, tag, msg, tr,args);
	}
	
	public static void error(String tag, String msg, Object ... args) { error(tag, msg, null,args); }
	public static void error(String tag, String msg, Throwable tr, Object ... args) {
		log(LogPriority.Error, tag, msg, tr,args);
	}
	
	private static void log(LogPriority priority, String tag, String msg, Throwable tr, Object...args) {
		if(args != null && args.length > 0)
			msg = String.format(msg, args);
		
		if(_log != null) {
			_log.log(priority, tag, msg, tr);
		}
	}
}
