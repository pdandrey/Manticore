package com.ncgeek.manticore.party;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import com.ncgeek.manticore.character.HitPoints;

public class PartyMember extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String name;
	private final HitPoints hp;
	private final String id;
	private final List<Message> lstMessages;
	private transient Object portrait;
	private final String portraitURL;
	private int unreadMessageCount;
	
	public PartyMember(String id, String name, String portraitURL) {
		this.name = name;
		this.hp = new HitPoints();
		this.portraitURL = portraitURL;
		this.id = id;
		lstMessages = new ArrayList<Message>();
		unreadMessageCount = 0;
	}
	public PartyMember(String id, String name) {
		this.name = name;
		this.hp = new HitPoints();
		portraitURL = null;
		this.id = id;
		lstMessages = new ArrayList<Message>();
		unreadMessageCount = 0;
	}
	
	public final String getName() { return name; }
	public final HitPoints getHP() { return hp; }
	public final String getID() { return id; }
	public final Object getPortrait() { return portrait; }
	public final void setPortrait(Object bitmap) { portrait = bitmap; }
	public final String getPortraitUrl() { return portraitURL; }
	
	public final List<Message> getMessages() {
		return Collections.unmodifiableList(lstMessages);
	}
	
	public final int getUnreadMessageCount() { return unreadMessageCount; }
	public final boolean hasUnreadMessages() { return unreadMessageCount > 0; }
	
	void sendMessageTo(PartyMember from, String message, Date timestamp) {
		Message msg = new Message(from, message, timestamp);
		lstMessages.add(msg);
		setChanged();
		notifyObservers(msg);
	}
	
	Message receiveMessageFrom(String message, Date timestamp) {
		Message msg = new Message(this, message, timestamp);
		lstMessages.add(msg);
		++unreadMessageCount;
		setChanged();
		notifyObservers(msg);
		return msg;
	}
	
	public final void setMessagesRead() {
		unreadMessageCount = 0;
		setChanged();
		notifyObservers();
	}
}
