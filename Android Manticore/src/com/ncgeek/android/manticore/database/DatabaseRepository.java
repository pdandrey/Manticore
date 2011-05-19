package com.ncgeek.android.manticore.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.manticore.Dice;
import com.ncgeek.manticore.ICompendiumRepository;
import com.ncgeek.manticore.Money;
import com.ncgeek.manticore.Source;
import com.ncgeek.manticore.items.Armor;
import com.ncgeek.manticore.items.ArmorCategories;
import com.ncgeek.manticore.items.ArmorTypes;
import com.ncgeek.manticore.items.Gear;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.items.ItemSlots;
import com.ncgeek.manticore.items.MagicItem;
import com.ncgeek.manticore.items.Range;
import com.ncgeek.manticore.items.Weapon;
import com.ncgeek.manticore.items.WeaponCategories;
import com.ncgeek.manticore.items.WeaponGroups;
import com.ncgeek.manticore.items.WeaponProperties;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.util.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseRepository extends SQLiteOpenHelper implements ICompendiumRepository {

	private static final String LOG_TAG = "DatabaseRepository";
	
	// The Android's default system path of your application database.
	///data/data/com.ncgeek.android.manticore/files
	private static String EXISTING = "/mnt/sdcard/Manticore/";
    private static String DB_PATH = "/data/data/com.ncgeek.android.manticore/databases/";
    private static String DB_NAME = "Manticore.db";
    private static int DB_VERSION = 1;
    
    private static final String[] SOURCE_COLUMNS = { "Name" };
    private static final String WHERE_CLAUSE = "InternalID = ?";
    
    private final ManticorePreferences prefs;
    
    private boolean needsCreate;
    private boolean needsUpgrade;
    
    private Context context;
	
	public DatabaseRepository(Context context) {
		 super(context, DB_NAME, null, DB_VERSION);
		 
		 prefs = new ManticorePreferences(context);
		 
		 needsCreate = false;
		 needsUpgrade = false;
		 this.context = context;
		 
		 getReadableDatabase().close();
		 prepDatabase();
	}
	
	private void prepDatabase() {
		
		if(needsCreate)
			copyFromAssets();
		else if(needsUpgrade)
			;	// will need to implement this when we upgrade the DB version
		
		File existing = new File(EXISTING + DB_NAME);
		
		if(existing.exists() && prefs.shouldCopyDatabase()) {
			Logger.info(LOG_TAG, "Copying Manticore.db");
			
			try {
				FileInputStream in = new FileInputStream(existing);
				copyDatabase(in);
				in.close();
				existing.delete();
				Logger.info(LOG_TAG, "Finished copying");
			} catch(IOException ex) {
				Logger.error(LOG_TAG, "Error while copying the database.");
			}
		}
	}
	
	private void copyFromAssets() {
		Logger.info(LOG_TAG, "Creating Manticore.db");
		
		try {
			InputStream in = context.getAssets().open("manticore_base.db");
			copyDatabase(in);
			in.close();
			Logger.info(LOG_TAG, "Finished creating Manticore.db");
		} catch(Exception ex) {
			Logger.error("LOG_TAG", "Error creating manticore db from assests");
		}
	}
	
	private void copyDatabase(InputStream in) throws IOException {
		File target = new File(DB_PATH + DB_NAME);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(target);
		
			byte[] buffer = new byte[2056];
			int length;
			while ((length = in.read(buffer))>0){
	    		out.write(buffer, 0, length);
			}
			out.flush();
		} finally {
			out.close();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		needsCreate = true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int currentVersion, int upgradeVersion) {
		needsUpgrade = true;
	}

	@Override
	public Item getItem(Rule rule) {
		if(!prefs.isDatabaseEnabled())
			return null;
		
		switch(rule.getType()) {
			case ARMOR:
				return getArmor(rule);
				
			case WEAPON:
				return getWeapon(rule);
				
			default:
				return null;
		}
	}

	@Override
	public Weapon getWeapon(Rule rule) {
		if(!prefs.isDatabaseEnabled())
			return null;
		
		final String[] columns = {
			"Name",
			"AdditionalSlot",
			"DamageDiceCount",
			"DamageDiceSides",
			"Price",
			"HandsRequired",
			"ItemSlot",
			"ProficiencyBonus",
			"Range1",
			"Range2",
			"WeaponCategory",
			"Weight"
		};
		
		final String[] whereArgs = { rule.getInternalID() };
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor tmpCursor = db.query("vWeapon", columns, WHERE_CLAUSE, whereArgs, null, null, null);
		
		if(tmpCursor == null) {
			db.close();
			return null;
		}
		
		CursorHelper c = new CursorHelper(tmpCursor);
		Weapon w = new Weapon();
		if(!c.moveToFirst()) {
			c.close();
			db.close();
			return null;
		}
		w.setName(c.getString("Name"));
		w.setID(whereArgs[0]);
		if(!c.isNull("AdditionalSlot"));
			w.setAdditionalSlot(ItemSlots.forName("AdditionalSlot"));
		w.setDice(new Dice(c.getInt("DamageDiceCount"), c.getInt("DamageDiceSides")));
		w.setPrice(new Money(c.getLong("Price")));
		w.setTwoHanded(c.getInt("HandsRequired") == 2);
		w.setItemSlot(ItemSlots.forName(c.getString("ItemSlot")));
		w.setProficiencyBonus(c.getInt("ProficiencyBonus"));
		w.setRange(new Range(c.getInt("Range1"), c.getInt("Range2")));
		w.setCategory(WeaponCategories.forName(c.getString("WeaponCategory")));
		w.setWeight(c.getDouble("Weight"));
		c.close();
		
		tmpCursor = db.query("vWeaponSources", SOURCE_COLUMNS, WHERE_CLAUSE, whereArgs, null, null, null);
		if(tmpCursor != null) {
			c = new CursorHelper(tmpCursor);
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				w.addSource(Source.forName(c.getString("Name")));
			}
			c.close();
		}
		tmpCursor = db.query("vWeaponGroups", SOURCE_COLUMNS, WHERE_CLAUSE, whereArgs, null, null, null);
		if(tmpCursor != null) {
			c = new CursorHelper(tmpCursor);
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				w.addGroup(WeaponGroups.forName(c.getString("Name")));
			}
			c.close();
		}
		tmpCursor = db.query("vWeaponProperties", SOURCE_COLUMNS, WHERE_CLAUSE, whereArgs, null, null, null);
		if(tmpCursor != null) {
			c = new CursorHelper(tmpCursor);
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				w.addProperty(WeaponProperties.forName(c.getString("Name")));
			}
			c.close();
		}
		db.close();
		
		return w;
	}

	@Override
	public Armor getArmor(Rule rule) {
		if(!prefs.isDatabaseEnabled())
			return null;
		
		Armor a = new Armor();
		
		SQLiteDatabase db = getReadableDatabase();
		
		String[] columns = {
				"InternalID",
				"Name",
				"ArmorBonus",
				"ArmorCategory",
				"ArmorType",
				"CheckPenalty",
				"Description",
				"Price",
				"ItemSlot",
				"MinimumEnhancementBonus",
				"Special",
				"SpeedPenalty",
				"Weight"
		};
		String[] whereArgs = {
				rule.getInternalID()
		};
		
		Cursor c = db.query("vArmor", columns, WHERE_CLAUSE, whereArgs, null, null, null);
		
		if(c == null) {
			db.close();
			return null;
		}
		
		if(!c.moveToFirst()) {
			c.close();
			db.close();
			return null;
		}
		
		a.setName(c.getString(c.getColumnIndex("Name")));
		a.setID(rule.getInternalID());
		a.setBonus(c.getInt(c.getColumnIndex("ArmorBonus")));
		a.setArmorCategory(ArmorCategories.forName(c.getString(c.getColumnIndex("ArmorCategory"))));
		a.setArmorType(ArmorTypes.forName(c.getString(c.getColumnIndex("ArmorType"))));
		a.setCheckPenalty(c.getInt(c.getColumnIndex("CheckPenalty")));
		a.setDescription(c.getString(c.getColumnIndex("Description")));
		a.setPrice(new Money(c.getLong(c.getColumnIndex("Price"))));
		a.setItemSlot(ItemSlots.forName(c.getString(c.getColumnIndex("ItemSlot"))));
		a.setMinEnhancementBonus(c.getInt(c.getColumnIndex("MinimumEnhancementBonus")));
		a.setSpecial(c.getString(c.getColumnIndex("Special")));
		a.setSpeedPenalty(c.getInt(c.getColumnIndex("SpeedPenalty")));
		a.setWeight(c.getDouble(c.getColumnIndex("Weight")));
		c.close();
		
		c = db.query("vArmorSources", SOURCE_COLUMNS, WHERE_CLAUSE, whereArgs, null, null, null);
		if(c != null) {
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				a.addSource(Source.forName(c.getString(c.getColumnIndex("Name"))));
			}
			c.close();
		}
		
		db.close();
		
		return a;
	}

	@Override
	public void add(Item item) {
		if(!prefs.isDatabaseEnabled())
			return;
		
		if(item instanceof Weapon)
			add((Weapon)item);
		else if(item instanceof Armor)
			add((Armor)item);
		else
			Logger.warn(LOG_TAG, "Don't know how to add item of type " + item.getClass().getSimpleName());
	}

	@Override
	public void add(Weapon weapon) {
		
		if(!prefs.isDatabaseEnabled())
			return;
		
		ContentValues weaponValues = new ContentValues(13);
		weaponValues.put("InternalID", weapon.getID());
		weaponValues.put("Name", weapon.getName());
		weaponValues.put("AdditionalSlotID", weapon.getAdditionalSlot() == null ? null : weapon.getAdditionalSlot().getID());
		weaponValues.put("DamageDiceCount", weapon.getDice().getCount());
		weaponValues.put("DamageDiceSides", weapon.getDice().getSides());
		weaponValues.put("Price", weapon.getPrice().getTotalCopper());
		weaponValues.put("HandsRequired", weapon.isTwoHanded() ? 2 : 1);
		weaponValues.put("ItemSlotID", weapon.getItemSlot().getID());
		weaponValues.put("ProficiencyBonus", weapon.getProficiencyBonus());
		weaponValues.put("Range1", weapon.getRange().getRange1());
		weaponValues.put("Range2", weapon.getRange().getRange2());
		weaponValues.put("WeaponCategoryID", weapon.getCategory().getID());
		weaponValues.put("Weight", weapon.getWeight());

		SQLiteDatabase db = getWritableDatabase();
		
		long id = db.insert("Weapon", null, weaponValues);
		
		if(id == -1) {
			Logger.error(LOG_TAG, "Error while inserting a new weapon");
		} else {
			ContentValues cv = null;
			for(WeaponProperties wp : weapon.getProperties()) {
				cv = new ContentValues();
				cv.put("WeaponID", id);
				cv.put("PropertyID", wp.getID());
				long wpID = db.insert("WeaponsXWeaponProperties", null, cv);
				if(wpID == -1) {
					Logger.error("LOG_TAG", "Error while inserting a new weaponxweaponproperty");
				}
			}
			for(WeaponGroups wg : weapon.getGroups()) {
				cv = new ContentValues();
				cv.put("WeaponID", id);
				cv.put("PropertyID", wg.getID());
				long wgID = db.insert("WeaponsXWeaponProperties", null, cv);
				if(wgID == -1) {
					Logger.error("LOG_TAG", "Error while inserting a new weaponxweapongroup");
				}
			}
		}
		
		db.close();
	}

	@Override
	public void add(Armor armor) {
		
		if(!prefs.isDatabaseEnabled())
			return;
		
		ContentValues values = new ContentValues(13);
		values.put("InternalID", armor.getID());
		values.put("Name", armor.getName());
		values.put("ArmorBonus", armor.getBonus());
		values.put("ArmorCategoryID", armor.getArmorCategory().getID());
		values.put("ArmorTypeID", armor.getArmorType().getID());
		values.put("CheckPenalty", armor.getCheckPenalty());
		values.put("Description", armor.getDescription());
		values.put("Price", armor.getPrice().getTotalCopper());
		values.put("ItemSlotID", armor.getItemSlot().getID());
		values.put("MinimumEnhancementBonus", armor.getMinEnhancementBonus());
		values.put("Special", armor.getSpecial());
		values.put("SpeedPenalty", armor.getSpeedPenalty());
		values.put("Weight", armor.getWeight());
		
		SQLiteDatabase db = getWritableDatabase();
		
		long id = db.insert("Armor", null, values);
		
		if(id == -1) {
			Logger.error(LOG_TAG, "Error while inserting a new armor");
		}
		
		db.close();
	}

	@Override
	public MagicItem getMagicItem(Rule rule) {
		Logger.error(LOG_TAG, "Need to read MagicItem from database");
		return null;
	}

	@Override
	public void add(MagicItem magicItem) {
		Logger.error(LOG_TAG, "Need to save Magic Item to database");
	}

	@Override
	public Gear getGear(Rule rule) {
		Logger.error(LOG_TAG, "Need to read Gear from database");
		return null;
	}

	@Override
	public void add(Gear gear) {
		Logger.error(LOG_TAG, "Need to save Gear to database");
	}

}
