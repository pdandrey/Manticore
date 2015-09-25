package com.ncgeek.android.manticore.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.util.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class ImageCacheRepository {
	
	private static final String LOG_TAG = "ImageCacheRepository";
	
	private Context context;
	private ManticoreDatabase db;
	private ManticorePreferences prefs;
	
	public ImageCacheRepository(Context context) {
		this.context = context;
	}
	
	public Bitmap getNoPortrait() {
		return BitmapFactory.decodeResource(context.getResources(), R.drawable.no_portrait);
	}
	
	public Bitmap get(URI uri) {
		Bitmap bitmap = null;
		db = new ManticoreDatabase(context);
		SQLiteDatabase conn = db.getReadableDatabase();
		
		Cursor c = conn.query("ImageCache", new String[] { "_id" }, "URI = ?", new String[] { uri.toString() }, null, null, null);
		if(c.moveToFirst()) {
			bitmap = loadFromCache(c.getInt(0));
		} else {
			bitmap = getAndCache(uri);
		}
		c.close();
		
		conn.close();
		db.close();
		
		return bitmap;
	}
	
	private Bitmap getAndCache(URI uri) {
		
		URL url = null;
		Bitmap ret = null;
		prefs = new ManticorePreferences(context);
		
		// do we have an official image?
		if(uri.getScheme().equals("id")) {
			// Try the Jullian server
			
			String id = uri.getSchemeSpecificPart();
			try {
				url = new URL(prefs.getJullianPortraitURL(id));
				//ret = downloadBitmap(url);
				
				if(ret == null) {
					// The jullian server does not have it.  Try NCGeek
					//url = new URL(String.format("http://www.ncgeek.com/Manticore/Portraits/%s.png", id));
					//ret = downloadBitmap(url);
					File file = new File(Utility.getExternalStorageDirectory("portraits"), id + ".png");
                    if(file.exists()) {
                        ret = BitmapFactory.decodeFile(file.toString());
                    } else {
                        Logger.debug(LOG_TAG, "Portrait %d does not exist", id);
                    }
				}
			} catch(MalformedURLException malUrlEx) {
				Logger.error(LOG_TAG, "Error while creating Jullian Portrait URL for %s", malUrlEx, uri);
			}
		} else {
			// assume it's a web file
			try {
				url = uri.toURL();
				ret = downloadBitmap(url); 
			} catch(MalformedURLException malUrlEx) {
				Logger.error(LOG_TAG, "Error while creating Jullian Portrait URL for %s", malUrlEx, uri);
			}
		}
		
		if(ret == null) {
			// We can't find the portrait
			// return the default portrait
			ret = getNoPortrait();
		} else {
			// cache the image
			cache(uri, ret);
		}
		
		return ret;
	}
	
	private void cache(URI uri, Bitmap bm) {
		SQLiteDatabase conn = db.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("URI", uri.toString());
		
		conn.beginTransaction();
		long id = conn.insert("ImageCache", null, values);
		
		File file = new File(Utility.getPortraitCacheDirectory(), String.format("%d.png", id));
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bm.compress(CompressFormat.PNG, 100, fos);
			fos.close();
			conn.setTransactionSuccessful();
		} catch(IOException ioex) {
			Logger.error(LOG_TAG, "error while caching %s", ioex, uri);
			conn.endTransaction();
		}
		conn.endTransaction();
		conn.close();
	}
	
	private Bitmap downloadBitmap(URL url) {
		try {
			InputStream is = url.openStream();
			Bitmap ret = BitmapFactory.decodeStream(is);
			is.close();
			return ret;
		} catch(FileNotFoundException fnfEx) {
			Logger.warn(LOG_TAG, "Could not find image %s", fnfEx, url.toString());
			return null;
		} catch(IOException ioex) {
			Logger.error(LOG_TAG, "Error loading url %s", ioex, url.toString());
			return null;
		} catch(Exception netEx) {
			Logger.error(LOG_TAG, "Error while trying to download portrait from %s", netEx, url);
			return null;
		}
	}
	
	private Bitmap loadFromCache(int cacheID) {
		File dir = new File(Utility.getPortraitCacheDirectory(), String.format("%d.png", cacheID));
		return BitmapFactory.decodeFile(dir.toString());
	}
}
