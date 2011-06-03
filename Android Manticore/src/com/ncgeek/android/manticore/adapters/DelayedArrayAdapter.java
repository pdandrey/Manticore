package com.ncgeek.android.manticore.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

public abstract class DelayedArrayAdapter<T> extends ArrayAdapter<T> {

	private final int resourceID;
	private final LayoutInflater inflater;
	
	public DelayedArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resourceID = textViewResourceId;
	}

	public void add(List<T> items) {
		for(T i : items)
			super.add(i);
	}
	
	public int getResourceID() {
		return resourceID;
	}
	
	public LayoutInflater getInflater() {
		return inflater;
	}
}
