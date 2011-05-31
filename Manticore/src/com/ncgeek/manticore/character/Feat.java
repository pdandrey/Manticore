package com.ncgeek.manticore.character;

import com.ncgeek.manticore.Tier;

public class Feat {
	
	private String name;
	private Tier tier;
	private String Prerequisites;
	private String shortDescription;
	private String type;
	private String associatedPowers;
	private String associatedPowerInfo;
	private String special;
	private String description;
	
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final Tier getTier() {
		return tier;
	}
	public final void setTier(Tier tier) {
		this.tier = tier;
	}
	public final String getPrerequisites() {
		return Prerequisites;
	}
	public final void setPrerequisites(String prerequisites) {
		Prerequisites = prerequisites;
	}
	public final String getShortDescription() {
		return shortDescription;
	}
	public final void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public final String getType() {
		return type;
	}
	public final void setType(String type) {
		this.type = type;
	}
	public final String getAssociatedPowers() {
		return associatedPowers;
	}
	public final void setAssociatedPowers(String associatedPowers) {
		this.associatedPowers = associatedPowers;
	}
	public final String getAssociatedPowerInfo() {
		return associatedPowerInfo;
	}
	public final void setAssociatedPowerInfo(String associatedPowerInfo) {
		this.associatedPowerInfo = associatedPowerInfo;
	}
	public final String getSpecial() {
		return special;
	}
	public final void setSpecial(String special) {
		this.special = special;
	}
	public final String getDescription() {
		return description;
	}
	public final void setDescription(String description) {
		this.description = description;
	}
	
}
