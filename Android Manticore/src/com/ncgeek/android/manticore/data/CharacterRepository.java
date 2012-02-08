package com.ncgeek.android.manticore.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.data.model.CharacterModel;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.util.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

public class CharacterRepository {

	private static final String LOG_TAG = "CharacterRepository";
	
	private ManticoreDatabase helper;
	private Context context;
	
	public CharacterRepository(Context context) {
		helper = new ManticoreDatabase(context);
		this.context = context;
	}
	
	public ArrayList<CharacterModel> get() {
		final String[] columns = {
			"_id",
			"Name",
			"Race",
			"HeroicClass",
			"ParagonClass",
			"EpicClass",
			"Level",
			"PortraitURL",
			"ImportedOn",
			"UpdatedOn"
		};
		
		ManticorePreferences prefs = new ManticorePreferences(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		CursorHelper cursor = new CursorHelper(db.query("Character", columns, null, null, null, null, "Name ASC"));
		
		ArrayList<CharacterModel> lst = new ArrayList<CharacterModel>();
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			CharacterModel cm = new CharacterModel();
			cm.setId(cursor.getInt("_id"));
			cm.setName(cursor.getString("Name"));
			cm.setRace(cursor.getString("Race"));
			cm.setHeroicClass(cursor.getString("HeroicClass"));
			cm.setParagonClass(cursor.getString("ParagonClass"));
			cm.setEpicClass(cursor.getString("EpicClass"));
			cm.setLevel(cursor.getInt("Level"));
			String portraitURL = cursor.getString("PortraitURL");
			String imported = cursor.getString("ImportedOn");
			
			cm.setPortrait(Utility.getPortrait(portraitURL, prefs));
			Time t = new Time();
			t.parse(imported);
			cm.setImportedOn(t);
			
			if(!cursor.isNull("UpdatedOn")) {
				String updated = cursor.getString("UpdatedOn");
				t = new Time();
				t.parse(updated);
				cm.setUpdatedOn(t);
			} else {
				cm.setUpdatedOn(null);
			}
			
			
			lst.add(cm);
		}
		
		cursor.close();
		db.close();
		
		return lst;
	}
	
	public boolean contains(String characterName) {
		boolean ret = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT COUNT(_id) FROM Character WHERE Name = ?", new String[] { characterName });
		ret = c.moveToFirst() && c.getInt(0) > 0;
		c.close();
		db.close();
		return ret;
	}
	
	public int getID(String characterName) {
		int ret = -1;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT _id FROM Character WHERE Name = ?", new String[] { characterName });
		if(c.moveToFirst())
			ret = c.getInt(0);
		c.close();
		db.close();
		return ret;
	}

	public void insert(ManticoreCharacter character) {
		
		ContentValues values = getValues(character);
		
		SQLiteDatabase db = helper.getWritableDatabase();
		long id = db.insert("Character", null, values);
		db.close();
		
		if(id == -1) {
			Logger.error(LOG_TAG, String.format("Error inserting character %s.  -1 returned on insert", character.getName()));
		} else {
			character.setID((int)id);
			writeCharacterFile(character);
		}
	}
	
	public void update(ManticoreCharacter character) {
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = getValues(character);
		
		int rows = db.update("Character", values, "_id = ?", new String[] { character.getID() + "" });
		if(rows != 1) {
			Logger.error(LOG_TAG, String.format("Error updating character %s with id %d.  %d rows updated", character.getName(), character.getID(), rows));
		} else {
			writeCharacterFile(character);
		}
		
		db.close();
	}
	
	private void writeCharacterFile(ManticoreCharacter character) {
		File dir = Utility.getExternalStorageDirectory("Characters/");
		File file = new File(dir, character.getID() + ".ser");
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(character);
			oos.close();
		} catch(IOException ioex) {
			Logger.error(LOG_TAG, String.format("Error serializing character %s with id %d.", character.getName(), character.getID()), ioex);
		}
	}
	
	private ContentValues getValues(ManticoreCharacter character) {
		ContentValues values = new ContentValues();
		values.put("Name", character.getName());
		values.put("Race", character.getRace());
		values.put("HeroicClass", character.getHeroicClass());
		values.put("ParagonClass", character.getParagonPath());
		values.put("EpicClass", (String)null);
		values.put("Level", character.getLevel());
		values.put("PortraitURL", character.getPortraitUri().toString());
		
		Time t = new Time();
		t.setToNow();
		values.put("UpdatedOn", t.format2445());
		return values;
	}
	
	public ManticoreCharacter load(CharacterModel character) {
		File dir = Utility.getExternalStorageDirectory("Characters/");
		File file = new File(dir, character.getId() + ".ser");
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			ManticoreCharacter mc = (ManticoreCharacter)ois.readObject();
			ois.close();
			return mc;
		} catch(IOException ioex) {
			Logger.error(LOG_TAG, String.format("IO Error: Error loading serialized character %s with id %d.", character.getName(), character.getId()), ioex);
			return null;
		} catch(ClassNotFoundException cnfex) {
			Logger.error(LOG_TAG, String.format("Class Not Found Error: Error loading serialized character %s with id %d.", character.getName(), character.getId()), cnfex);
			return null;
		}
	}
}
