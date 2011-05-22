package com.ncgeek.manticore.parsers;

import com.ncgeek.manticore.character.PlayerCharacter;

public final class CharacterParserEventArgs {
	
	private ParserEventType type;
	private String section;
	private PlayerCharacter pc;
	
	public CharacterParserEventArgs(String sectionName) {
		section = sectionName;
		type = ParserEventType.SectionStart;
	}
	
	public CharacterParserEventArgs(PlayerCharacter pc) {
		this.pc = pc;
		type = ParserEventType.DocumentFinished;
	}
	
	public ParserEventType getType() { return type; }
	
	public String getSectionName() { return section; }
	
	public PlayerCharacter getPlayerCharacter() { return pc; }
}
