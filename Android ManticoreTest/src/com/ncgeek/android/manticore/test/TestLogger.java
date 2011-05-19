package com.ncgeek.android.manticore.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ncgeek.manticore.util.ILog;
import com.ncgeek.manticore.util.LogPriority;

public class TestLogger implements ILog {

	private ArrayList<String> lst;
	
	public TestLogger() {
		lst = new ArrayList<String>();
	}
	
	@Override
	public void log(LogPriority priority, String tag, String message, Throwable tr) {
		String s = String.format("%s\t%s\t%s", priority.toString(), tag, message);
		lst.add(s);
		System.out.println(s);
		
		if(tr != null)
			throw new RuntimeException(message, tr);
	}
	
	public List<String> getLogs() {
		return Collections.unmodifiableList(lst);
	}

}
