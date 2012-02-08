package com.ncgeek.android.manticore.data;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

public class CursorHelper {

	private Cursor cursor;
	
	public CursorHelper(Cursor c) {
		if(c == null)
			throw new IllegalArgumentException("Cursor cannot be null");
		cursor = c;
	}

	public void close() {
		cursor.close();
	}

	public void copyStringToBuffer(int arg0, CharArrayBuffer arg1) {
		cursor.copyStringToBuffer(arg0, arg1);
	}

	public void deactivate() {
		cursor.deactivate();
	}

	public byte[] getBlob(int arg0) {
		return cursor.getBlob(arg0);
	}

	public int getColumnCount() {
		return cursor.getColumnCount();
	}

	public int getColumnIndex(String arg0) {
		return cursor.getColumnIndex(arg0);
	}

	public int getColumnIndexOrThrow(String arg0)
			throws IllegalArgumentException {
		return cursor.getColumnIndexOrThrow(arg0);
	}

	public String getColumnName(int arg0) {
		return cursor.getColumnName(arg0);
	}

	public String[] getColumnNames() {
		return cursor.getColumnNames();
	}

	public int getCount() {
		return cursor.getCount();
	}

	public double getDouble(int arg0) {
		return cursor.getDouble(arg0);
	}
	
	public double getDouble(String columnName) {
		return cursor.getDouble(cursor.getColumnIndex(columnName));
	}

	public Bundle getExtras() {
		return cursor.getExtras();
	}

	public float getFloat(int arg0) {
		return cursor.getFloat(arg0);
	}
	
	public float getFloat(String columnName) {
		return cursor.getFloat(cursor.getColumnIndex(columnName));
	}

	public int getInt(int arg0) {
		return cursor.getInt(arg0);
	}
	
	public int getInt(String columnName) {
		return cursor.getInt(cursor.getColumnIndex(columnName));
	}

	public long getLong(int arg0) {
		return cursor.getLong(arg0);
	}
	
	public long getLong(String columnName) {
		return cursor.getLong(cursor.getColumnIndex(columnName));
	}

	public int getPosition() {
		return cursor.getPosition();
	}

	public short getShort(int arg0) {
		return cursor.getShort(arg0);
	}
	
	public short getShort(String columnName) {
		return cursor.getShort(cursor.getColumnIndex(columnName));
	}

	public String getString(int arg0) {
		return cursor.getString(arg0);
	}
	
	public String getString(String columnName) {
		return cursor.getString(cursor.getColumnIndex(columnName));
	}

	public boolean getWantsAllOnMoveCalls() {
		return cursor.getWantsAllOnMoveCalls();
	}

	public boolean isAfterLast() {
		return cursor.isAfterLast();
	}

	public boolean isBeforeFirst() {
		return cursor.isBeforeFirst();
	}

	public boolean isClosed() {
		return cursor.isClosed();
	}

	public boolean isFirst() {
		return cursor.isFirst();
	}

	public boolean isLast() {
		return cursor.isLast();
	}

	public boolean isNull(int arg0) {
		return cursor.isNull(arg0);
	}
	
	public boolean isNull(String columnName) {
		return cursor.isNull(cursor.getColumnIndex(columnName));
	}

	public boolean move(int arg0) {
		return cursor.move(arg0);
	}

	public boolean moveToFirst() {
		return cursor.moveToFirst();
	}

	public boolean moveToLast() {
		return cursor.moveToLast();
	}

	public boolean moveToNext() {
		return cursor.moveToNext();
	}

	public boolean moveToPosition(int arg0) {
		return cursor.moveToPosition(arg0);
	}

	public boolean moveToPrevious() {
		return cursor.moveToPrevious();
	}

	public void registerContentObserver(ContentObserver arg0) {
		cursor.registerContentObserver(arg0);
	}

	public void registerDataSetObserver(DataSetObserver arg0) {
		cursor.registerDataSetObserver(arg0);
	}

	public boolean requery() {
		return cursor.requery();
	}

	public Bundle respond(Bundle arg0) {
		return cursor.respond(arg0);
	}

	public void setNotificationUri(ContentResolver arg0, Uri arg1) {
		cursor.setNotificationUri(arg0, arg1);
	}

	public void unregisterContentObserver(ContentObserver arg0) {
		cursor.unregisterContentObserver(arg0);
	}

	public void unregisterDataSetObserver(DataSetObserver arg0) {
		cursor.unregisterDataSetObserver(arg0);
	}
}
