package com.ncgeek.manticore.integrationtests;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ncgeek.manticore.character.Alignment;
import com.ncgeek.manticore.character.Gender;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.inventory.EquipmentManager;
import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;
import com.ncgeek.manticore.rules.Specific;

public class PlayerCharacterParser {
	
	private SAXBuilder builder;
	private ManticoreDatabase db;
	
	public PlayerCharacterParser(ManticoreDatabase db) {
		builder = new SAXBuilder();
		this.db = db;
	}
	
	public PlayerCharacter parse(String filename) {
		try {
			return parse(builder.build(filename));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public PlayerCharacter parse(File file) {
		try {
			return parse(builder.build(file));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private PlayerCharacter parse(Document doc) {
		PlayerCharacter pc = new PlayerCharacter();
		
		Element root = doc.getRootElement();
		Element charsheet = root.getChild("CharacterSheet");
		
		getDetails(pc, charsheet);
		getStats(pc, charsheet);
		getRules(pc, charsheet);
		getItems(pc, charsheet);
		
		return pc;
	}
	
	public void getItems(PlayerCharacter pc, Element charsheet) {
		EquipmentManager mgr = pc.getEquipment();
		
		for(Object oLoot : charsheet.getChild("LootTally").getChildren("loot")) {
			Element loot = (Element)oLoot;
			int count = Integer.parseInt(loot.getAttributeValue("count"));
			int equip = Integer.parseInt(loot.getAttributeValue("equip-count"));
			
			Item i = null;
			
			for(Object oRE : loot.getChildren("RulesElement")) {
				Element re = (Element)oRE;
				RuleTypes type = RuleTypes.forName(re.getAttributeValue("type"));
				String internalID = re.getAttributeValue("internal-id");
				
				switch(type) {
					case ARMOR:
						i = db.getArmor(internalID);
						break;
						
					case WEAPON:
						i = db.getWeapon(internalID);
						break;
						
				}
			}
			
			if(i != null) {
				mgr.add(i, count);
				for(int j=0; j<equip; ++j) {
					mgr.equip((EquippableItem)i);
				}
			}
		}
	}
	
	private void getRules(PlayerCharacter pc, Element charsheet) {
		Element tally = charsheet.getChild("RulesElementTally");
		
		for(Object ore : tally.getChildren("RulesElement")) {
			Element re = (Element)ore;
			
			String name = re.getAttributeValue("name");
			RuleTypes type = RuleTypes.forName(re.getAttributeValue("type"));
			String internalID = re.getAttributeValue("internal-id");
			String url = re.getAttributeValue("url");
			String legality = re.getAttributeValue("legality");
			
			Rule r = new Rule(name, type, internalID, url, legality);
			
			for(Object ospec : re.getChildren("specific")) {
				Element spec = (Element)ospec;
				String sname = spec.getAttributeValue("name");
				String value = spec.getText();
				r.addSpecific(new Specific(sname, value));
			}
			
			pc.add(r);
		}
	}
	
	private void getStats(PlayerCharacter pc, Element charsheet) {
		Element xmlStatBlock = charsheet.getChild("StatBlock");
		
		@SuppressWarnings("rawtypes")
		List lstNodes = xmlStatBlock.getChildren();
		
		for(Object oNode : lstNodes) {
			Element eStat = (Element)oNode;
			
			@SuppressWarnings("rawtypes")
			List lst = eStat.getChildren();
			Stat s = new Stat();
			s.setAbsoluteValue(Integer.parseInt(eStat.getAttributeValue("value")));
			
			for(Object oChild : lst) {
				Element eChild = (Element)oChild;
				
				String name = eChild.getName();
				if(name.equalsIgnoreCase("alias")) {
					s.addAlias(eChild.getAttributeValue("name"));
				} else {
					Addition add = new Addition();
					
					String abilmod = eChild.getAttributeValue("abilmod");
					add.setAbilityModified(abilmod != null && abilmod.equalsIgnoreCase("true"));
					
					String notWearing = eChild.getAttributeValue("not-wearing");
					if(notWearing != null)
						add.setNotWearing(notWearing);
					
					String requires = eChild.getAttributeValue("requires");
					if(requires != null)
						add.setRequires(requires);
					
					add.setStatLink(eChild.getAttributeValue("statlink"));
					add.setType(eChild.getAttributeValue("type"));
					add.setValue(Integer.parseInt(eChild.getAttributeValue("value")));
					
					String wearing = eChild.getAttributeValue("wearing");
					if(wearing != null)
						add.setWearing(wearing);
					
					String lvl = eChild.getAttributeValue("Level");
					if(lvl != null) {
						add.setLevel(new Integer(lvl));
					}
					
					String condition = eChild.getAttributeValue("conditional");
					if(condition != null) {
						add.setConditional(condition);
					}
					
					Set<String> known = new HashSet<String>(Arrays.asList(new String[]{"abilmod", "Level", "not-wearing", "requires", "statlink", "type", "wearing"}));
					for(Object oAttr : eChild.getAttributes()) {
						Attribute attr = (Attribute)oAttr;
						String attrName = attr.getName();
						if(!known.contains(attrName))
							add.setAdditionalAttribute(attrName, attr.getValue());
					}
					s.addAddition(add);
				}
			}
			
			pc.getStats().add(s);
		}
	}
	
	private void getDetails(PlayerCharacter pc, Element charsheet) {
		Element details = charsheet.getChild("Details");
		
		for(Object oDetail : details.getChildren()) {
			Element e = (Element)oDetail;
			
			DetailNode type = DetailNode.valueOf(e.getName().trim());
			String value = e.getValue().trim();
			
			switch(type) {
				case name:
					pc.setName(value);
					break;
					
				case Level:
					pc.setLevel(Integer.parseInt(value)); 
					break;
					
				case Player:
					pc.setPlayer(value);
					break;
					
				case Height:
					pc.setHeight(value);
					break;
					
				case Weight:
					pc.setWeight(value);
					break;
					
				case Gender:
					Gender g = Gender.Male;
					if(value.equalsIgnoreCase("female"))
						g = Gender.Female;
					pc.setGender(g);
					break;
					
				case Age:
					pc.setAge(Integer.parseInt(value));
					break;
					
				case Alignment:
					if(value.length() > 0)
						pc.setAlignment(Alignment.forName(value));
					break;
					
				case Company:
					pc.setCompany(value);
					break;
					
				case Experience:
					if(value != null && value.trim().length() > 0)
						pc.setExperience(Integer.parseInt(value));
					break;
					
				case CarriedMoney:
					pc.getMoneyCarried().setAmount(value);
					break;
					
				case StoredMoney:
					pc.getMoneyStored().setAmount(value);
					break;
					
				case Traits:
					pc.setTraits(value);
					break;
					
				case Appearance:
					pc.setApperance(value);
					break;
					
				case Portrait:
					pc.setPortrait(value);
					break;
					
				case Companions:
					pc.setCompanions(value);
					break;
					
				case Notes:
					pc.setNotes(value);
					break;
			}
		}
	}
	
	private enum DetailNode {
		name, Level, Player, Height, Weight, Gender, Age, Alignment, Company, Portrait, Experience, 
		CarriedMoney, StoredMoney, Traits, Appearance, Companions, Notes
	}
}
