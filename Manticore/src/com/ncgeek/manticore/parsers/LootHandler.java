package com.ncgeek.manticore.parsers;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.ncgeek.manticore.ICompendiumRepository;
import com.ncgeek.manticore.Ritual;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.inventory.EquipmentManager;
import com.ncgeek.manticore.items.EnchantedItem;
import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.items.ItemUtilities;
import com.ncgeek.manticore.items.MagicItem;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;
import com.ncgeek.manticore.util.Logger;

public class LootHandler implements IElementHandler {

	private static final String LOG_TAG = "LootHandler";
	
	private static final IElementHandler instance = new LootHandler();
	public static final IElementHandler getInstance() { return instance; }
	
	private int count;
	private int equipped;
	private RulesElementHandler ruleHandler;
	private ICompendiumRepository repository;
	private Item current;
	
	private ArrayList<Item> items;
	
	private LootHandler() {
		count = 0;
		equipped = 0;
		ruleHandler = (RulesElementHandler)RulesElementHandler.getInstance();
		items = new ArrayList<Item>(2);
	}
	
	public void setRepository(ICompendiumRepository repos) {
		this.repository = repos;
	}

	@Override
	public void startElement(PlayerCharacter pc, String name, Attributes attributes) {
		Logger.verbose(LOG_TAG, String.format("<%s>", name));
		if(name.equals("loot")) {
			count = Integer.parseInt(attributes.getValue("count"));
			equipped = Integer.parseInt(attributes.getValue("equip-count"));
			current = null;
			items.clear();
		} else if(current == null) {
			if(count == 0)
				return;
			ruleHandler.startElement(pc, name, attributes);
			if(name.equals("RulesElement") && repository != null) {
				current = repository.getItem(ruleHandler.getRule());
				if(current != null)
					items.add(current);
			}
		}
	}

	@Override
	public void endElement(PlayerCharacter pc, String name, String body) {
		Logger.verbose(LOG_TAG, String.format("</%s>", name));
		if(count == 0)
			return;
		
		if(name.equals("loot")) {
			
			if(items.size() == 0)
				return;
			
			current = items.get(0);
			
			if(items.size() > 1)
				current = new EnchantedItem((EquippableItem)current, (MagicItem)items.get(1));
			
			Logger.verbose(LOG_TAG, String.format("Adding %s %s", current.getClass().getSimpleName(), current.getName()));
			
			EquipmentManager mgr = pc.getEquipment();
			mgr.add(current, count);
			
			for(int i=0; i<equipped; ++i) {
				mgr.equip((EquippableItem)current);
			}
			items.clear();
		} else if(current == null) {
			ruleHandler.endElement(pc, name, body);
			
			if(name.equals("RulesElement")) {
				Rule rule = ruleHandler.getRule();
				if(rule.getType() == RuleTypes.RITUAL) {
					pc.add(Ritual.fromRule(rule));
					return;
				}
				current = ItemUtilities.itemFromRule(ruleHandler.getRule());
				items.add(current);
			}
		}
		
		if(name.equals("RulesElement"))
			current = null;
	}
}
