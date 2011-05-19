package com.ncgeek.android.manticore.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.xml.sax.Attributes;

import com.ncgeek.android.manticore.database.DatabaseRepository;
import com.ncgeek.manticore.ICompendiumRepository;
import com.ncgeek.manticore.character.Alignment;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;
import com.ncgeek.manticore.util.Logger;

import android.content.Context;
import android.os.Handler;
import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

public class CharacterParser {
	
	static final String LOG_TAG = "Character Parser";
	
	private final ICompendiumRepository db;
	private final Handler handler;
	
	public CharacterParser(ICompendiumRepository repos, Handler handler) {
		db = repos;
		this.handler = handler;
	}
	
	public CharacterParser(Context context, Handler handler) {
		db = new DatabaseRepository(context);
		this.handler = handler;
	}
	
	public PlayerCharacter parse(File f) {
		Logger.info(LOG_TAG, "Starting parse of " + f.toString());
		try {
			FileInputStream fis = new FileInputStream(f);
			PlayerCharacter ret = parse(fis);
			fis.close();
			return ret;
		} catch(Exception ex) {
			Logger.error(LOG_TAG, "Error parsing character file", ex);
			return null;
		}
	}
	
	public PlayerCharacter parse(InputStream in) {
		final PlayerCharacter pc = new PlayerCharacter();
		
		RootElement root = new RootElement("D20Character");
		Element characterSheet = root.getChild("CharacterSheet");
		Element details = characterSheet.getChild("Details");
		
		details.setStartElementListener(new SectionStartListener("Details", handler));
		
		details.getChild("name").setEndTextElementListener(new EndTextElementListener() {
			@Override public void end(String body) { pc.setName(body.trim()); }
		});
		
		details.getChild("Level").setEndTextElementListener(new EndTextElementListener() {
			@Override public void end(String body) { pc.setLevel(Integer.parseInt(body.trim())); }
		});
		
		details.getChild("Player").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) { pc.setPlayer(body.trim()); }
		});
		
		details.getChild("Height").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) { pc.setHeight(body.trim()); }
		});
		
		details.getChild("Weight").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) { pc.setWeight(body.trim()); }
		});
		
		details.getChild("Age").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) { pc.setAge(Integer.parseInt(body.trim())); }
		});
		
		details.getChild("Experience").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) { 
				body = body.trim();
				if(body.length() > 0)
					pc.setExperience(Integer.parseInt(body.trim())); 
			}
		});
		
		details.getChild("CarriedMoney").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) { pc.getMoneyCarried().setAmount(body.trim()); }
		});
		
		details.getChild("StoredMoney").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) { pc.getMoneyStored().setAmount(body.trim()); }
		});
		
		details.getChild("Alignment").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) { 
				if(body != null) {
					body = body.trim();
					if(body.length() > 0) {
						pc.setAlignment(Alignment.forName(body));
					}
				}
			}
		});
		
		Element statBlock = characterSheet.getChild("StatBlock");
		statBlock.setStartElementListener(new SectionStartListener("Rules", handler));

		Element stat = statBlock.getChild("Stat");
		
		@SuppressWarnings("unused")
		StatElementListener statListener = new StatElementListener(stat, pc);
				
		Element rulesElementTally = characterSheet.getChild("RulesElementTally");
		rulesElementTally.setStartElementListener(new SectionStartListener("Rules", handler));
			
		rulesElementTally.getChild("RulesElement").setStartElementListener(new StartElementListener() {
			public void start(Attributes attrs) {
				pc.add(parseRule(attrs));
			}
		});
		
		Element lootTally = characterSheet.getChild("LootTally");
		lootTally.setStartElementListener(new SectionStartListener("Loot", handler));
		Element loot = lootTally.getChild("loot");
		
		@SuppressWarnings("unused")
		LootElementListener lootListener = new LootElementListener(loot, pc, db);
		

		try {
			Xml.parse(in, Xml.Encoding.UTF_8, root.getContentHandler());
			return pc;
		} catch(Exception ex) {
			Logger.error(LOG_TAG, "Error parsing XML", ex);
			return null;
		}
	
	}
	
	private Rule parseRule(Attributes attrs) {
		String name = attrs.getValue("name");
		RuleTypes type = RuleTypes.forName(attrs.getValue("type"));
		String internalID = attrs.getValue("internal-id");
		String url = attrs.getValue("url");
		String legal = attrs.getValue("legality");
		return new Rule(name, type, internalID, url, legal);
	}
}
