package com.ncgeek.manticore.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Observable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.ncgeek.manticore.ICompendiumRepository;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.util.Logger;

public class CharacterParser extends Observable {

	static final String LOG_TAG = "Character Parser";
	
	private ICompendiumRepository repository;
	
	public CharacterParser() {
		this(null);
	}
	
	public CharacterParser(ICompendiumRepository repository) {
		this.repository = repository;
	}
	
	public void setRepository(ICompendiumRepository repos) {
		repository = repos;
	}
	
	public PlayerCharacter parse(String url) {
		try {
			InputStream in = new URL(url).openStream();
			PlayerCharacter pc = parse(in);
			in.close();
			return pc;
		} catch(IOException ioex) {
			Logger.error(LOG_TAG, "Error opening url " + url, ioex);
			return null;
		}
	}
	
	public PlayerCharacter parse(File file) {
		try {
			InputStream in = new FileInputStream(file);
			PlayerCharacter pc = parse(in);
			in.close();
			return pc;
		} catch(IOException ioex) {
			Logger.error(LOG_TAG, "Error opening file " + file.getName(), ioex);
			return null;
		}
	}
	
	private PlayerCharacter parse(InputStream in) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
		    SAXParser parser = factory.newSAXParser();
		    CharacterHandler handler = new CharacterHandler(this, repository);
		    parser.parse(in, handler);
		    return handler.getPlayerCharacter();
		} catch (Exception e) {
		    Logger.error(LOG_TAG, "Error parsing XML file", e);
		    return null;
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
