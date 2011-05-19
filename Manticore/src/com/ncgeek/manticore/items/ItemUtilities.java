package com.ncgeek.manticore.items;

import java.util.List;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ncgeek.manticore.Dice;
import com.ncgeek.manticore.Money;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.Specific;

public final class ItemUtilities {

	private ItemUtilities() {}
	
	public static Armor armorFromRule(Rule rule) {
		if(!rule.hasSpecifics())
			throw new IllegalArgumentException("Cannot create armor from rule without specifics");
		
		Armor a = new Armor();
		fillItem(a, rule);
		
		a.setBonus(getSpecificInt(rule, "Armor Bonus"));
		a.setArmorCategory(ArmorCategories.forName(getSpecificString(rule, "Armor Category")));
		a.setArmorType(ArmorTypes.forName(getSpecificString(rule, "Armor Type")));
		a.setCheckPenalty(getSpecificInt(rule, "Check", 0));
		a.setItemSlot(ItemSlots.forName(getSpecificString(rule, "Item Slot")));
		a.setMinEnhancementBonus(getSpecificInt(rule, "Minimum Enhancement Bonus", 0));
		a.setSpecial(getSpecificString(rule, "Special"));
		
		return a;
	}
	
	public static Weapon weaponFromRule(Rule rule) {
		if(!rule.hasSpecifics())
			throw new IllegalArgumentException("Cannot create weapon from rule without specifics");
		
		Weapon w = new Weapon();
		fillItem(w, rule);
		
		w.setAdditionalSlot(ItemSlots.forName(getSpecificString(rule, "Additional Slot")));
		w.setDice(new Dice(getSpecificString(rule, "Damage")));
		w.setTwoHanded(getSpecificString(rule, "Hands Required").equals("One-Handed") ? false : true);
		w.setItemSlot(ItemSlots.forName(getSpecificString(rule, "Item Slot")));
		w.setProficiencyBonus(getSpecificInt(rule, "Proficiency Bonus"));
		
		String range = getSpecificString(rule, "Range");
		if(range == null || range.length() == 0) 
			range = "0/0";
		
		String[] rangeParts = range.split("/");
		w.setRange(new Range(Integer.parseInt(rangeParts[0]), Integer.parseInt(rangeParts[1])));
	
		w.setCategory(WeaponCategories.forName(getSpecificString(rule, "Weapon Category")));
		
		
		String[] groups = getSpecificString(rule, "Group").split(",");
		for(String g : groups) {
			g = g.trim();
			w.addGroup(WeaponGroups.forName(g));
		}
		
		String[] properties = getSpecificString(rule, "Properties").split(",");
		for(String p : properties) {
			p = p.trim();
			if(p.length() > 0)
				w.addProperty(WeaponProperties.forName(p));
		}
		
		return w;
	}
	
	public static MagicItem magicItemFromRule(Rule rule) {
		
		MagicItem mi = new MagicItem();
		fillItem(mi, rule);
		
		mi.setFlavor(getSpecificString(rule, "Flavor"));
		mi.setRarity(MagicItemRarity.forName(getSpecificString(rule, "_Rarity")));
		mi.setType(MagicItemType.forName(getSpecificString(rule, "Magic Item Type")));
		mi.setLevel(getSpecificInt(rule, "Level"));
		mi.setPowers(getSpecificString(rule, "Power"));
		mi.setEnhancement(getSpecificString(rule, "Enhancement"));
		mi.setCritical(getSpecificString(rule, "Critical"));
		mi.setItemSlot(ItemSlots.forName(getSpecificString(rule, "Item Slot")));
		
		for(Specific s : rule.getSpecifics("Property")) {
			mi.add(s.getValue());
		}
		
		boolean isArmor = true;
		
		String target = getSpecificString(rule, "Armor");
		if(target == null || target.length() == 0) {
			isArmor = false;
			target = getSpecificString(rule, "Weapon");
		}
		
		if(target != null && target.length() > 0) {
			Pattern regexRestrict = Pattern.compile("([^\\(]*)(?:\\((.*?)\\))?");
			
			String [] targets = target.split(",");
			for(String t : targets) {
				String proper = properCase(t);
				Matcher m = regexRestrict.matcher(proper);
				if(m.find()) {
					String targetValue = m.group(1);
					String subtarget = null;
					if(m.groupCount() > 2) {
						m.group(2);
					}
					mi.add(new MagicItemTarget(targetValue, subtarget, isArmor));
				}
			}
		}
		
		List<String> skipNames = Arrays.asList(new String[] {
				"_Rarity",
				"Magic Item Type",
				"Level",
				"Gold",
				"Power",
				"Enhancement",
				"Property",
				"Critical",
				"Item Slot",
				"Weapon",
				"Armor"});
		
		for(Specific s : rule.getSpecifics()) {
			if(!skipNames.contains(s.getName())) {
				mi.add(new MagicItemAttribute(s.getName(), s.getValue()));
			}
		}
		
		return mi;
	}
	
	public static Gear gearFromRule(Rule rule) {
		Gear g = new Gear();
		fillItem(g, rule);
		
		g.setCount(getSpecificInt(rule, "count"));
		g.setCategory(GearCategory.forName(getSpecificString(rule, "Category")));
		g.setItemSlot(ItemSlots.forName(getSpecificString(rule, "Item Slot")));
		
		return g;
	}
	
	private static void fillItem(Item item, Rule rule) {
		if(!rule.hasSpecifics())
			throw new IllegalArgumentException("Cannot fill item from rule without specifics");
		
		item.setID(rule.getInternalID());
		item.setName(rule.getName());
		
		String gold = getSpecificString(rule, "Gold", "0");
		String silver = getSpecificString(rule, "Silver", "0");
		String copper = getSpecificString(rule, "Copper", "0");
		item.setPrice(new Money(String.format("%sg %ss %sc", gold, silver, copper)));
		
		Double weight = getSpecificDouble(rule, "Weight");
		if(weight != null)
			item.setWeight(weight);
		
		String desc = getSpecificString(rule, "Description");
		
		if(desc == null || desc.trim().length() == 0)
			desc = rule.getBody();
		if(desc != null && desc.trim().length() > 0)
			item.setDescription(desc.trim());
	}
	
	private static String getSpecificString(Rule r, String name) { return getSpecificString(r, name, null); }
	private static String getSpecificString(Rule r, String name, String defaultValue) {
		Specific s = r.getSpecific(name);
		if(s == null)
			return defaultValue;
		else
			return s.getValue();
	}
	
	private static Integer getSpecificInt(Rule r, String name) { return getSpecificInt(r, name, null); }
	private static Integer getSpecificInt(Rule r, String name, Integer defaultValue) {
		Specific s = r.getSpecific(name);
		if(s == null)
			return defaultValue;
		else {
			try {
				return new Integer(s.getValue());
			} catch(NumberFormatException ex) {
				return defaultValue;
			}
		}
	}
	
	private static Double getSpecificDouble(Rule r, String name) { return getSpecificDouble(r, name, null); }
	private static Double getSpecificDouble(Rule r, String name, Double defaultValue) {
		Specific s = r.getSpecific(name);
		if(s == null)
			return defaultValue;
		else {
			try {
				return new Double(s.getValue());
			} catch(NumberFormatException ex) {
				return defaultValue;
			}
		}
	}
	
	static String properCase(String s) {
		Pattern p = Pattern.compile("(^|\\W)([a-z])");
		Matcher m = p.matcher(s.toLowerCase());
		StringBuffer sb = new StringBuffer(s.length());
		while(m.find()) {
			m.appendReplacement(sb, m.group(1) + m.group(2).toUpperCase() );
		}
		m.appendTail(sb);
		return sb.toString();
	}

	
}
