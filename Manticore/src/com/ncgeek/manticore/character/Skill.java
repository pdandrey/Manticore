package com.ncgeek.manticore.character;

import java.io.Serializable;

import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.util.Logger;

public final class Skill implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int total;
	private int misc;
	private int trained;
	private int abilityMod;
	private String abilityModName;
	private int halfLevel;
	private int armorPenalty;
	
	public Skill(Stat skill) {
		name = skill.getAliases().get(0);
		total = skill.getCalculatedValue();
		
		for(Addition add : skill.getAdditions()) {
			if(add.getStat() != null) {
				String stat = add.getStat().getAliases().get(0);
				if(stat != null) {
					stat = stat.trim().toLowerCase();
					
					if(stat.equals("half-level")) {
						halfLevel = add.getValue();
					} else if(stat.endsWith("trained")) {
						trained = add.getValue();
					} else if(stat.endsWith("misc")) {
						misc = add.getValue();
					} else if(stat.equals("armor penalty")) {
						armorPenalty = add.getValue();
					} else if(add.getType() != null && add.getType().equalsIgnoreCase("Ability")) {
						abilityMod = add.getValue();
						abilityModName = add.getStatLink();
					} else {
						Logger.debug("Skill", String.format("Unknown stat add found: %s", stat.toString()));
					}
				}
			}
		}
	}
	
	public final String getName() { return name; }
	
	public final boolean isTrained() { return trained > 0; }
	
	public final int getArmorPenalty() { return armorPenalty; }

	public final int getTotal() {
		return total;
	}

	public final int getMisc() {
		return misc;
	}

	public final int getTrained() {
		return trained;
	}

	public final int getAbilityMod() {
		return abilityMod;
	}

	public final String getAbilityModName() {
		return abilityModName;
	}

	public final int getHalfLevel() {
		return halfLevel;
	}
}
