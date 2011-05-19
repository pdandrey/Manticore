package com.ncgeek.android.manticore.threads;

import java.io.File;

import com.ncgeek.android.manticore.MessageTypes;
import com.ncgeek.android.manticore.parsers.CharacterParser;
import com.ncgeek.android.manticore.util.ThreadState;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.util.Logger;

import android.os.Handler;
import android.os.Message;

public class LoadCharacterThread extends Thread {
	
	private static final String LOG_TAG = "Load Character Thread";
	
	private static LoadCharacterThread instance;
	
	public static LoadCharacterThread getThread() { return instance; }
	
	public static LoadCharacterThread getThread(String file, Handler handler, CharacterParser parser) {
		return getThread(new File(file), handler, parser);
	}
	public static LoadCharacterThread getThread(File file, Handler handler, CharacterParser parser) { 
		
		if(instance != null && file.equals(instance.file))
			return instance;
		
		instance = new LoadCharacterThread(file, handler, parser);
		return instance; 
	}
	
	private Handler handler;
	private File file;
	private ThreadState state;
	private PlayerCharacter pc;
	private CharacterParser parser;
	
	private LoadCharacterThread(File file, Handler handler, CharacterParser parser) {
		this.file = file;
		this.handler = handler;
		state = ThreadState.Initial;
		this.parser = parser;
	}
	
	public void run() {
		state = ThreadState.Active;
		
		Logger.debug(LOG_TAG, "Starting character parse");
		
		long start = System.currentTimeMillis();
		try {
			pc = parser.parse(file);
			Message msg = handler.obtainMessage(MessageTypes.MESSAGE_FINISHED, pc);
			handler.sendMessage(msg);
		} catch(Exception ex) {
			Logger.error(LOG_TAG, "Error parsing character", ex);
			Message err = handler.obtainMessage(MessageTypes.MESSAGE_ERROR, ex);
			handler.sendMessage(err);
		}
		long end = System.currentTimeMillis();
		
		long taken = end - start;
		Message time = handler.obtainMessage(MessageTypes.MESSAGE_TIME_TAKEN, new Long(taken));
		handler.sendMessage(time);
		
		Logger.info(LOG_TAG, "Character parse thread finished in " + taken + " ms.");
		state = ThreadState.Finished;
	}
	
	public boolean hasStarted() { return state != ThreadState.Initial; }
	public boolean isRunning() { return state == ThreadState.Active; }
	public boolean isFinished() { return state == ThreadState.Finished; }
	public PlayerCharacter getCharacter() { return pc; }
	public void setHandler(Handler h) {
		handler = h; 
	}
}
