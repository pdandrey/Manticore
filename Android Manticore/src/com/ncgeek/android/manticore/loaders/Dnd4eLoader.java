package com.ncgeek.android.manticore.loaders;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.AsyncTaskLoader;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.parsers.CharacterParser;
import com.ncgeek.manticore.parsers.CharacterParserEventArgs;
import com.ncgeek.manticore.util.Logger;

public class Dnd4eLoader 
	extends AsyncTaskLoader<ManticoreCharacter>
	implements Observer
{
	private static final String LOG_TAG = "Dnd4eLoader";
	
	private File _file;
	private ManticoreCharacter _pc;
	private Handler _handler;
	
	public Dnd4eLoader(Context context, File file, Handler handle) {
		super(context);
		this._file = file;
		_handler = handle;
	}
	
	@Override
	public ManticoreCharacter loadInBackground() {
		ManticoreCharacter pc = new ManticoreCharacter();
		
		long start = System.currentTimeMillis();
		try {
			pc = new ManticoreCharacter();
			CharacterParser<ManticoreCharacter> parser = new CharacterParser<ManticoreCharacter>();
			parser.addObserver(this);
			parser.parse(_file, pc);
		} catch(Exception ex) {
			Logger.error(LOG_TAG, "Error parsing character", ex);
			return null;
		}
		long end = System.currentTimeMillis();
		long taken = end - start;
		Logger.info(LOG_TAG, String.format("DND4e File Loader finished in %d ms.", taken));
		
		pc.setPortrait(Utility.getPortrait(pc.getPortraitUri().toString(), new ManticorePreferences(getContext())));
		this.
		_pc = pc;
		return pc;
	}

	@Override
	protected void onReset() {
		super.onReset();
		_pc = null;
	}
	
	public final ManticoreCharacter getCharacter() { return _pc; }

	@Override
	public void update(Observable sender, Object data) {
		if(data instanceof CharacterParserEventArgs) {
			CharacterParserEventArgs args = (CharacterParserEventArgs)data;
			switch(args.getType()) {
				case SectionStart:
					Logger.verbose(LOG_TAG, String.format("Starting section %s", args.getSectionName()));
					Message msg = _handler.obtainMessage();
					msg.obj = args.getSectionName();
					_handler.sendMessage(msg);
					break;
			}
		}
	}

	@Override
	protected void onStartLoading() {
		if(_pc != null)
			deliverResult(_pc);
		else
			forceLoad();
	}
}
