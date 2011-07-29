package com.ncgeek.android.manticore.http;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

import android.os.Environment;

import com.ncgeek.manticore.ICompendiumRepository;
import com.ncgeek.manticore.items.Armor;
import com.ncgeek.manticore.items.Gear;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.items.MagicItem;
import com.ncgeek.manticore.items.Weapon;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.util.Logger;

public class HttpCompendiumRepository implements ICompendiumRepository {
	
	private static final String LOG_TAG = "repository";
	
	private String _email;
	private String _password;
	
	private File dirBase;
	
	public HttpCompendiumRepository(String email, String password) {
		_email = email;
		_password = password;
		dirBase = new File(Environment.getExternalStorageDirectory(), "Manticore/cache");
		if(!dirBase.exists() && !dirBase.mkdirs())
			Logger.error(LOG_TAG, "Could not create " + dirBase.toString());
	}
	
	public void getCharacterList() {
		try {
			ManticoreCompendiumHttpClient repos = new ManticoreCompendiumHttpClient(_email, _password);
			
			final String REQUEST = "<s:Envelope xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\"><s:Header><a:Action s:mustUnderstand=\"1\">http://tempuri.org/IContentVaultService/GetAvailableContent</a:Action><a:MessageID>urn:uuid:%s</a:MessageID><a:ReplyTo><a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address></a:ReplyTo><a:To s:mustUnderstand=\"1\">http://ioun.wizards.com/ContentVault.svc</a:To></s:Header><s:Body><GetAvailableContent xmlns=\"http://tempuri.org/\"><contentType>0</contentType></GetAvailableContent></s:Body></s:Envelope>";
			final String REFERER = "http://media.wizards.com/downloads/dnd/CharacterBuilder/Client/223.241754/CharBuilder.xap";
			final String URL = "http://ioun.wizards.com/ContentVault.svc";
			final String CONTENT_TYPE = "application/soap+xml";
			String guid = UUID.randomUUID().toString();
			
			repos.execute(URL, String.format(REQUEST, guid), REFERER, CONTENT_TYPE);
			String xml = repos.get();
			
			int i = 0;
			i++;
			
		} catch(Exception ex) {
			Logger.error(LOG_TAG, "Error getting character list", ex);
		}
	}
	
	public Item getItem(Rule rule) {
		
		try {
			ManticoreCompendiumHttpClient repos = new ManticoreCompendiumHttpClient(_email, _password);
			repos.execute(rule.getURL());
			String item = repos.get();
			item = "" + item;
			
		} catch (Exception e) {
			Logger.error(LOG_TAG, "Error getting item", e);
		}
		return null;
	}
	
	public void cacheRule(Rule rule) {
		if(rule == null)
			throw new IllegalArgumentException("rule cannot be null");
		
		File dir = new File(dirBase, rule.getType().toString());
		if(!dir.exists() && !dir.mkdir())
		{
			Logger.error(LOG_TAG, "Could not create " + dir.toString());
			return;
		}
		
		try {
			String name = rule.getName().replaceAll("[\\s]", "_").replaceAll("[^\\w]", "");
			
			File html = new File(dir, name + ".html");
			if(!html.exists())
			{
				ManticoreCompendiumHttpClient repos = new ManticoreCompendiumHttpClient(_email, _password);
				repos.execute(rule.getURL());
				String item = repos.get();
				
				if(item == null) {
					Logger.error(LOG_TAG, "Could not download rule " + rule.toString());
					return;
				}
				
				//Charset ascii = Charset.forName("US-ASCII");
				//item = new String(ascii.encode(item).array());
				
				BufferedWriter br = new BufferedWriter(new FileWriter(html), 1024);
				
				if(br == null) {
					int i = 0;
					++i;
				}
				br.write(item);
				br.close();
			}
		} catch (Exception e) {
			Logger.error(LOG_TAG, "Error getting rule " + rule.toString(), e);
		}
	}

	@Override
	public Weapon getWeapon(Rule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Armor getArmor(Rule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(Item item) {
		throw new UnsupportedOperationException("Cannot add items to the web.");
	}

	@Override
	public void add(Weapon weapon) {
		throw new UnsupportedOperationException("Cannot add items to the web.");
	}

	@Override
	public void add(Armor armor) {
		throw new UnsupportedOperationException("Cannot add items to the web.");
	}

	@Override
	public MagicItem getMagicItem(Rule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(MagicItem magicItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Gear getGear(Rule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(Gear gear) {
		// TODO Auto-generated method stub
		
	}
}
