package com.ncgeek.manticore.parsers;

import org.xml.sax.Attributes;

import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.inventory.EquipmentManager;
import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.items.ItemUtilities;

public class LootHandler implements IElementHandler {
	
	private static final IElementHandler instance = new LootHandler();
	public static final IElementHandler getInstance() { return instance; }
	
	private int count;
	private int equipped;
	private RulesElementHandler ruleHandler;
	
	private LootHandler() {
		count = 0;
		equipped = 0;
		ruleHandler = (RulesElementHandler)RulesElementHandler.getInstance();
	}

	@Override
	public void startElement(PlayerCharacter pc, String name, Attributes attributes) {
		if(name.equals("loot")) {
			count = Integer.parseInt(attributes.getValue("count"));
			equipped = Integer.parseInt(attributes.getValue("equip-count"));
		} else {
			ruleHandler.startElement(pc, name, attributes);
		}
	}

	@Override
	public void endElement(PlayerCharacter pc, String name, String body) {
		if(name.equals("loot")) {
			Item item = ItemUtilities.itemFromRule(ruleHandler.getRule());
			EquipmentManager mgr = pc.getEquipment();
			mgr.add(item, count);
			
			for(int i=0; i<equipped; ++i) {
				mgr.equip((EquippableItem)item);
			}
		} else {
			ruleHandler.endElement(pc, name, body);
		}
	}
}
