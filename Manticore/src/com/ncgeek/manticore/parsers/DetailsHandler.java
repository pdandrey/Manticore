package com.ncgeek.manticore.parsers;

import org.xml.sax.Attributes;

import com.ncgeek.manticore.character.Alignment;
import com.ncgeek.manticore.character.Gender;
import com.ncgeek.manticore.character.PlayerCharacter;

public class DetailsHandler implements IElementHandler {
	
	private static final IElementHandler instance = new DetailsHandler();
	public static final IElementHandler getInstance() { return instance; }
	
	private enum Section { 
		name,
		Level,
		Player,
		Height,
		Weight,
		Gender,
		Age,
		Alignment,
		Company,
		Portrait,
		Experience,
		CarriedMoney,
		StoredMoney,
		Traits,
		Appearance,
		Companions,
		Notes
	}
	
	private DetailsHandler() {}
	private Section section;
	
	@Override
	public void startElement(PlayerCharacter pc, String name, Attributes attributes) {
		section = Section.valueOf(Section.class, name);
	}
	
	@Override
	public void endElement(PlayerCharacter pc, String name, String body) {
		if(body != null) {
			body = body.trim();
			if(body.length() == 0)
				body = null;
		}
		
		switch(section) {
			case name: 
				pc.setName(body);
				break;
				
			case Level:
				pc.setLevel(Integer.parseInt(body));
				break;
				
			case Player:
				pc.setPlayer(body);
				break;
				
			case Height:
				pc.setHeight(body);
				break;
				
			case Weight:
				pc.setWeight(body);
				break;
				
			case Gender:
				Gender g = body == null || body.equalsIgnoreCase("male") ? Gender.Male : Gender.Female;
				pc.setGender(g);
				break;
				
			case Age:
				if(body != null)
					pc.setAge(Integer.parseInt(body));
				break;
				
			case Alignment:
				pc.setAlignment(body == null ? Alignment.Unaligned : Alignment.forName(body));
				break;
				
			case Company:
				pc.setCompany(body);
				break;
				
			case Portrait:
				//pc.setPortrait(body);
				break;
				
			case Experience:
				if(body != null)
					pc.setExperience(Integer.parseInt(body));
				break;
				
			case CarriedMoney:
				if(body != null)
					pc.getMoneyCarried().setAmount(body);
				break;
				
			case StoredMoney:
				if(body != null)
					pc.getMoneyStored().setAmount(body);
				break;
				
			case Traits:
				pc.setTraits(body);
				break;
				
			case Appearance:
				pc.setApperance(body);
				break;
				
			case Companions:
				pc.setCompanions(body);
				break;
				
			case Notes:
				pc.setNotes(body);
				break;
		}
	}
}
