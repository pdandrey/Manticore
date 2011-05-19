package com.ncgeek.android.manticore.parsers;

import org.xml.sax.Attributes;

import com.ncgeek.android.manticore.MessageTypes;

import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;

public final class SectionStartListener implements StartElementListener {

	private final Message msg;
	private final Handler handler;
	
	public SectionStartListener(String sectionName, Handler handler) {
		msg = handler.obtainMessage(MessageTypes.MESSAGE_LOADING_SECTION_NAME, sectionName);
		this.handler = handler;
	}
	
	@Override
	public void start(Attributes attrs) {
		handler.sendMessage(msg);
	}

}
