package com.ncgeek.android.manticore.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.ncgeek.manticore.party.Message;

public class ChatListAdapter extends ArrayAdapter<Message> {

	public ChatListAdapter(Context context, int resource, int textViewResourceId, List<Message> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public ChatListAdapter(Context context, int resource, int textViewResourceId, Message[] objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public ChatListAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	public ChatListAdapter(Context context, int textViewResourceId, List<Message> objects) {
		super(context, textViewResourceId, objects);
	}

	public ChatListAdapter(Context context, int textViewResourceId, Message[] objects) {
		super(context, textViewResourceId, objects);
	}

	public ChatListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

}
