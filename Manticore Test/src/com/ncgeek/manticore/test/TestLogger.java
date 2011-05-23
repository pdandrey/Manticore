package com.ncgeek.manticore.test;

import com.ncgeek.manticore.util.ILog;
import com.ncgeek.manticore.util.LogPriority;

public class TestLogger implements ILog {

	@Override
	public void log(LogPriority priority, String tag, String message, Throwable tr) {
		String s = String.format("%s\t%s\t%s", priority.toString(), tag, message);
		System.out.println(s);
		
		if(tr != null)
			throw new RuntimeException(message, tr);
	}

}
