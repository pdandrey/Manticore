package com.ncgeek.manticore.party;

public final class EventResponse {
	
	private final boolean success;
	private final String message;
	private final PartyEventType type;
	
	public EventResponse(PartyEventType type, boolean success, String message) {
		this.success = success;
		this.message = message;
		this.type = type;
	}

	public final boolean isSuccess() {
		return success;
	}

	public final String getMessage() {
		return message;
	}
	
	public final PartyEventType getType() { return type; }
}
