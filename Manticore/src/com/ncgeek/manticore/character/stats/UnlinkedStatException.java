package com.ncgeek.manticore.character.stats;

public class UnlinkedStatException extends RuntimeException {
	private static final long serialVersionUID = -3456917005611190275L;

	public UnlinkedStatException() {
		super();
	}
	
	public UnlinkedStatException(String message) {
		super(message);
	}
}
