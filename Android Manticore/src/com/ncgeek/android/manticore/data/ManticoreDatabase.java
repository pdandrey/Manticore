package com.ncgeek.android.manticore.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.ncgeek.manticore.util.Logger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ManticoreDatabase extends SQLiteOpenHelper {

	private static final String LOG_TAG = "DatabaseRepository";
	
	private static String DB_NAME = "Manticore.db";
    private static int DB_VERSION = 1;
    
    private Context context;
	
	public ManticoreDatabase(Context context) {
		 super(context, DB_NAME, null, DB_VERSION);
		 this.context = context;
		 
		 // Get a writable database to go ahead and force an
		 // onCreate or onUpgrade
		 getWritableDatabase().close();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("DatabaseSchema.sql")));
			String line = null;
			StringBuilder buf = new StringBuilder();
			while((line = br.readLine()) != null) {
				if(!line.trim().startsWith("--")) {
					buf.append(" ");
					buf.append(line.trim());
					buf.append("\n");
				} else {
					// we have a new command, execute and clear
					if(buf.toString().trim().length() > 0)
						db.execSQL(buf.toString());
					buf = new StringBuilder();
				}
			}
			
			if(buf.toString().trim().length() > 0)
				db.execSQL(buf.toString());
			br.close();
		} catch (IOException e) {
			Logger.error(LOG_TAG, "Error creating database", e);
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int currentVersion, int upgradeVersion) {
		// Not implemented
		Logger.error(LOG_TAG, "Attempted to upgrade database from %d to %d", currentVersion, upgradeVersion);
	}

	

}
