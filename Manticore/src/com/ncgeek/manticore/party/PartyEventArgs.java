package com.ncgeek.manticore.party;

public final class PartyEventArgs {

	private final PartyEventType type;
	private final Object data;
	
	public PartyEventArgs(PartyEventType type, Object data) {
		this.type = type;
		this.data = data;
	}

	public final PartyEventType getType() {
		return type;
	}

	public final Object getData() {
		return data;
	}
}
