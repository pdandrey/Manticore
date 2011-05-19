package com.ncgeek.android.manticore.parsers;

import java.util.List;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.ElementListener;

import com.ncgeek.manticore.ICompendiumRepository;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.inventory.EquipmentManager;
import com.ncgeek.manticore.items.Armor;
import com.ncgeek.manticore.items.EnchantedItem;
import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Gear;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.items.ItemUtilities;
import com.ncgeek.manticore.items.MagicItem;
import com.ncgeek.manticore.items.Weapon;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.util.Logger;

class LootElementListener implements ElementListener {

	private static final String LOG_TAG = "Loot Element Listener";
	
	private final EquipmentManager mgr;
	private final ICompendiumRepository repository;
	private final RulesElementElementListener reListener;
	
	private int count;
	private int equipCount;
	
	public LootElementListener(Element loot, PlayerCharacter pc, ICompendiumRepository repos) {
		mgr = pc.getEquipment();
		this.repository = repos;
		
		loot.setElementListener(this);
		Element re = loot.getChild("RulesElement");
		
		reListener = new RulesElementElementListener(re);
	}
	
	@Override
	public void start(Attributes attrs) {
		count = Integer.parseInt(attrs.getValue("count"));
		equipCount = Integer.parseInt(attrs.getValue("equip-count"));
	}

	@Override
	public void end() {
		List<Rule> lstRules = reListener.getRules();
		Rule r = lstRules.get(0);
		
		Logger.info(LOG_TAG, String.format("Getting item for rule: ID=%s, name=%s, type=%s", r.getInternalID(), r.getName(), r.getType().toString()));
		
		Item current = repository.getItem(r);
		
		if(current == null) {
			// parse the rule
			current = parseRule(r);
		}
		
		if(lstRules.size() > 1) {
			// Magic Item Enchant
			r = lstRules.get(1);
			Logger.info(LOG_TAG, String.format("Getting item enchant for rule: ID=%s, name=%s, type=%s", r.getInternalID(), r.getName(), r.getType().toString()));
			
			MagicItem enchant = repository.getMagicItem(r);
			if(enchant == null)
				enchant = parseMagicItem(r);
			current = new EnchantedItem((EquippableItem)current, enchant);
		}
		
		mgr.add(current, count);
		if(equipCount > 0) {
			for(int i=0; i<equipCount; ++i) {
				mgr.equip((EquippableItem)current);
			}
		}
		count = 0;
		equipCount = 0;
		reListener.reset();
	}
	
	private Item parseRule(Rule r) {
		switch(r.getType()) {
			case WEAPON:
				return parseWeapon(r);
				
			case ARMOR:
				return parseArmor(r);
				
			case MAGIC_ITEM:
				return parseMagicItem(r);
				
			case GEAR:
				return parseGear(r);
				
			default:
				Logger.warn(LOG_TAG, "No parsing for RulesElement type " + r.getType().toString());
				return null;
		}
	}
	
	private Gear parseGear(Rule r) {
		Gear g = repository.getGear(r);
		if(g == null) {
			g = ItemUtilities.gearFromRule(r);
			repository.add(g);
		}
		return g;
	}

	private MagicItem parseMagicItem(Rule r) {
		MagicItem mi = repository.getMagicItem(r);
		if(mi == null) {
			mi = ItemUtilities.magicItemFromRule(r);
			repository.add(mi);
		}
		return mi;
	}
	
	private Weapon parseWeapon(Rule r) {
		Weapon w = repository.getWeapon(r);
		if(w == null) {
			w = ItemUtilities.weaponFromRule(r);
			repository.add(w);
		}
		return w;
	}
	
	private Armor parseArmor(Rule r) {
		Armor a = repository.getArmor(r); 
		if(a == null) {
			a = ItemUtilities.armorFromRule(r);
			repository.add(a);
		}
		return a;
	}

}