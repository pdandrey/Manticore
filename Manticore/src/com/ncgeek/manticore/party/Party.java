package com.ncgeek.manticore.party;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ncgeek.manticore.character.HitPoints;
import com.ncgeek.manticore.util.Logger;

public class Party extends Observable {
	
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'");
	
	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	private PartyMember me;
	private final List<PartyMember> lstMembers;
	private final List<Message> lstChat;
	
	public Party(String joinJson, Object portraitBitmap) {
		lstMembers = new ArrayList<PartyMember>();
		lstChat = new ArrayList<Message>();
		parseJullianEvent(joinJson);
		me.setPortrait(portraitBitmap);
	}
	
	public final String getToken() {
		return me.getID();
	}
	
	public EventResponse parseJullianEvent(String event) {
		try {
			return parseJullianEvent(new JSONObject(event));
			
		} catch(JSONException jsonEx) {
			Logger.error("Party", "Error while parsing json", jsonEx);
			return new EventResponse(null, false, "Error parsing server result");
		}
	}
	
	private EventResponse parseJullianEvent(JSONObject json) throws JSONException {
		PartyEventType type = null;
		boolean success = json.getBoolean("Success");
		String message = json.getString("Message");
		type = PartyEventType.valueOf(json.getString("EventName"));
		Date timestamp = null; 
		
		try {
			timestamp = dateFormat.parse(json.getString("Timestamp"));
		} catch(Exception ex) {
			timestamp = Calendar.getInstance().getTime();
		}
		
		switch(type) {
			case PartyJoined:
				me = new PartyMember(json.getString("Token"), "Me");
				JSONArray party = json.getJSONArray("PartyMembers");
				for(int i=0; i<party.length(); ++i) {
					addMember(parsePartyMember(party.getJSONObject(i)));
				}
				return new EventResponse(type, success, message);
				
			case PartyChat:
				chat(json.getString("FromID"), message, timestamp);
				return new EventResponse(type, success, "Party chatter");
				
			case Message:
				receiveMessageFrom(json.getString("FromID"), message, timestamp);
				return new EventResponse(type, success, "Message received");
				
			case GetEvents:
				JSONArray events = json.getJSONArray("Events");
				for(int i=0; i<events.length(); ++i) {
					parseJullianEvent(events.getJSONObject(i));
				}
				return new EventResponse(type, success, "Updates received");
				
			case CharacterJoined:
				addMember(parsePartyMember(json.getJSONObject("Character")));
				return null;
				
			case CharacterLeft:
				removeMember(getPartyMember(json.getString("CharacterID")));
				return null;
				
			case CharacterUpdate:
				/*{"ID":"71c4407d-7149-4a30-a319-acc901f11d41",
				 * "HP":15,
				 * "TempHP":0,
				 * "Surges":8,
				 * "DeathSaves":0,
				 * "Success":false,
				 * "Message":null,
				 * "Timestamp":"2011-07-13 18:02:56Z",
				 * "EventName":"CharacterUpdate"}
				 */
				String id = json.getString("ID");
				int currHP = json.getInt("HP");
				int temp = json.getInt("TempHP");
				int surge = json.getInt("Surges");
				int death = json.getInt("DeathSaves");
				
				PartyMember pm = getPartyMember(id);
				HitPoints hp = pm.getHP();
				hp.setCurrent(currHP, temp, surge, death);
				return null;
				
			default:
				Logger.warn("Party", String.format("Unhandled event %s", type));
				return null;
		}
	}
	
	public void addMember(PartyMember member) {
		lstMembers.add(member);
		setChanged();
		notifyObservers(new PartyEventArgs(PartyEventType.CharacterJoined, member));
	}
	
	public void removeMember(PartyMember member) {
		lstMembers.remove(member);
		setChanged();
		notifyObservers(new PartyEventArgs(PartyEventType.CharacterLeft, member));
	}
	
	public List<PartyMember> getMembers() {
		return Collections.unmodifiableList(lstMembers);
	}
	
	public void chat(String message) {
		chat(getToken(), message, Calendar.getInstance().getTime());
	}
	
	public void chat(String fromID, String message, Date timestamp) {
		PartyMember pm = getPartyMember(fromID);
		Message msg = new Message(pm, message, timestamp);
		lstChat.add(msg);
		setChanged();
		notifyObservers(new PartyEventArgs(PartyEventType.PartyChat, msg));
	}
	
	public List<Message> getChat() {
		return Collections.unmodifiableList(lstChat);
	}
	
	public void sendMessageTo(String toID, String message) {
		PartyMember pm = getPartyMember(toID);
		pm.sendMessageTo(me, message, Calendar.getInstance().getTime());
	}
	
	public void receiveMessageFrom(String fromID, String message, Date timestamp) {
		PartyMember pm = getPartyMember(fromID);
		Message msg = pm.receiveMessageFrom(message, timestamp);
		setChanged();
		notifyObservers(new PartyEventArgs(PartyEventType.Message, msg));
	}
	
	public PartyMember getPartyMember(String id) {
		if(id.equals(me.getID()))
			return me;
		
		for(PartyMember pm : lstMembers) {
			if(pm.getID().equals(id)) {
				return pm;
			}
		}
		return null;
	}
	
	private PartyMember parsePartyMember(JSONObject jpc) throws JSONException {
		/*{
		 * "ID":"89a6bdf9-a245-45c0-b1c4-1a5c59f7ddef",
		 * "Name":"Lance",
		 * "MaxHP":10,
		 * "CurrentHP":10,
		 * "TempHP":0,
		 * "DeathSaves":0,
		 * "MaxSurges":4,
		 * "CurrentSurges":4,
		 * "Portrait":null
		 * }*/
		
		String name = jpc.getString("Name");
		String id = jpc.getString("ID");
		String url = jpc.getString("Portrait");
		
		PartyMember pc = new PartyMember(id, name, url);
		HitPoints hp = pc.getHP();
		hp.setMax(jpc.getInt("MaxHP"));
		hp.setCurrent(jpc.getInt("CurrentHP"));
		hp.setTemp(jpc.getInt("TempHP"));
		hp.setDeathSaves(jpc.getInt("DeathSaves"));
		hp.setTotalSurges(jpc.getInt("MaxSurges"));
		hp.setRemainingSurges(jpc.getInt("CurrentSurges"));
		
		return pc;
	}
}
