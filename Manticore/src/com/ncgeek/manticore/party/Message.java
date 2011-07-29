package com.ncgeek.manticore.party;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

	private static final long serialVersionUID = 2L;
	private final PartyMember from;
	private final String message;
	private final Date timestamp;
	
	public Message(PartyMember from, String msg, Date time) {
		message = msg;
		this.from = from;
		timestamp = time;
	}
	
	public final PartyMember getFrom() { 
		return from;
	}
	
	public final String getMessage() { return message; }
	
	public final Date getTimestamp() { return timestamp; }
}
