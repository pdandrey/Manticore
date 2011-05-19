package com.ncgeek.android.manticore.threads;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.ncgeek.android.manticore.MessageTypes;
import com.ncgeek.android.manticore.adapters.CharacterFileInfo;
import com.ncgeek.android.manticore.util.ThreadState;

public class LoadFileListThread extends Thread {
	
	//private static LoadFileListThread instance;
	private static LoadFileListThread internal;
	private static LoadFileListThread external;
	
	public static LoadFileListThread getInternal(Handler handler, File directory) {
		if(internal != null) {
			return internal;
		}
		
		internal = new LoadFileListThread(handler, directory);
		return internal;
	}
	public static LoadFileListThread getExternal(Handler handler, File directory) {
		if(external != null) {
			return external;
		}
		
		external = new LoadFileListThread(handler, directory);
		return external;
	}

	private ThreadState state;
	private Handler handler;
	private File[] files;
	private List<CharacterFileInfo> lst;
	
	private static FilenameFilter filenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String filename) {
			return filename.endsWith(".dnd4e");
		}
	};
	
	private LoadFileListThread(Handler handler, File directory) {
		this.handler = handler;
		files = directory.listFiles(filenameFilter);
		lst = new ArrayList<CharacterFileInfo>();
		state = ThreadState.Initial;
	}
	
	public void run() {
		
		if(state == ThreadState.Finished)
			return;
		
		if(state != ThreadState.Initial)
			throw new UnsupportedOperationException("Thread has already been started");
		
		state = ThreadState.Active;
		
		Message initial = handler.obtainMessage(MessageTypes.MESSAGE_TOTAL);
		initial.arg1 = files.length;
		handler.sendMessage(initial);
		
		for(int i = 0; state == ThreadState.Active && i<files.length; ++i) {
			Message msg = handler.obtainMessage(MessageTypes.MESSAGE_CURRENT);
			msg.arg1 = i;
			handler.sendMessage(msg);
			
			File f = files[i];
			lst.add(new CharacterFileInfo(f));
		}
		
		Collections.sort(lst, CharacterFileInfo.NAME_COMPARE);
		
		state = ThreadState.Finished;
		Message finished = handler.obtainMessage(MessageTypes.MESSAGE_FINISHED, getCharacterInfo());
		handler.sendMessage(finished);
	}
	
	public int size() { return files.length; }
	public boolean hasStarted() { return state != ThreadState.Initial; }
	public boolean isRunning() { return state == ThreadState.Active; }
	public boolean isFinished() { return state == ThreadState.Finished; }
	public void setHandler(Handler h) {
		handler = h; 
	}
	
	public List<CharacterFileInfo> getCharacterInfo() {
		return Collections.unmodifiableList(lst);
	}
}
