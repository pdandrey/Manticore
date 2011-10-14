package com.ncgeek.manticore.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Observable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.ncgeek.manticore.character.PlayerCharacter;

public class CharacterParser<T extends PlayerCharacter> extends Observable {

	static final String LOG_TAG = "Character Parser";
	
	public CharacterParser() {
		
	}
	
	public void parse(String url, T character) throws IOException {
		InputStream in = new URL(url).openStream();
		parse(in, character);
		in.close();
	}
	
	public void parse(File file, T character) throws IOException {
		InputStream in = new FileInputStream(file);
		parse(in, character);
		in.close();
	}
	
	private void parse(InputStream in, T character) throws IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
	    SAXParser parser = null;
	    try {
		    parser = factory.newSAXParser();
		    CharacterHandler handler = new CharacterHandler(this, character);
		    parser.parse(in, handler);
	    } catch(Exception ex) {
	    	throw new IOException(String.format("Error parsing character: %s", ex.getMessage()), ex);
	    }
	}
	
	void sectionStart(String name) {
		super.setChanged();
		notifyObservers(new CharacterParserEventArgs(name));
	}
	
	void finished(PlayerCharacter pc) {
		super.setChanged();
		notifyObservers(new CharacterParserEventArgs(pc));
	}
}
