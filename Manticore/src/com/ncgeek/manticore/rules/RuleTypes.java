package com.ncgeek.manticore.rules;

public enum RuleTypes {
	ABILITY_INCREASE_LEVEL_14("Ability Increase (Level 14)"),
	ABILITY_INCREASE_LEVEL_18("Ability Increase (Level 18)"),
	ABILITY_INCREASE_LEVEL_24("Ability Increase (Level 24)"),
	ABILITY_INCREASE_LEVEL_28("Ability Increase (Level 28)"),
	ABILITY_INCREASE_LEVEL_4("Ability Increase (Level 4)"),
	ABILITY_INCREASE_LEVEL_8("Ability Increase (Level 8)"),
	ABILITY_SCORE("Ability Score"),
	ALIGNMENT("Alignment"),
	ARMOR("Armor"),
	ASSOCIATE("Associate"),
	BACKGROUND("Background"),
	BACKGROUND_ASSOCIATION("Background Association"),
	BACKGROUND_CHOICE("Background Choice"),
	BUILD("Build"),
	BUILD_SUGGESTIONS("Build Suggestions"),
	CATEGORY("Category"),
	CLASS("Class"),
	CLASS_FEATURE("Class Feature"),
	COMPANION("Companion"),
	COMPANION_ABILITY_INCREASE_LEVEL_14("Companion Ability Increase (Level 14)"),
	COMPANION_ABILITY_INCREASE_LEVEL_18("Companion Ability Increase (Level 18)"),
	COMPANION_ABILITY_INCREASE_LEVEL_24("Companion Ability Increase (Level 24)"),
	COMPANION_ABILITY_INCREASE_LEVEL_28("Companion Ability Increase (Level 28)"),
	COMPANION_ABILITY_INCREASE_LEVEL_4("Companion Ability Increase (Level 4)"),
	COMPANION_ABILITY_INCREASE_LEVEL_8("Companion Ability Increase (Level 8)"),
	COUNTSASCLASS("CountsAsClass"),
	COUNTSASFEATURE("CountsAsFeature"),
	COUNTSASPACT("CountsAsPact"),
	COUNTSASRACE("CountsAsRace"),
	DEITY("Deity"),
	DOMAIN("Domain"),
	EPIC_DESTINY("Epic Destiny"),
	FAMILIAR("Familiar"),
	FEAT("Feat"),
	GEAR("Gear"),
	GENDER("Gender"),
	GOD_FRAGMENT("God Fragment"),
	GRANTS("Grants"),
	HYBRID_CLASS("Hybrid Class"),
	INFORMATION("Information"),
	INTERNAL("Internal"),
	ITEM_SET("Item Set"),
	ITEM_SET_BENEFIT("Item Set Benefit"),
	LANGUAGE("Language"),
	LEVEL("Level"),
	LEVEL1RULES("Level1Rules"),
	MAGIC_ITEM("Magic Item"),
	MONEY("Money"),
	MULTICLASS("Multiclass"),
	PARAGON_PATH("Paragon Path"),
	POWER("Power"),
	POWER_SOURCE("Power Source"),
	PROFICIENCY("Proficiency"),
	PSEUDO_CLASS("Pseudo Class"),
	RACE("Race"),
	RACE_ABILITY_BONUS("Race Ability Bonus"),
	RACIAL_TRAIT("Racial Trait"),
	RITUAL("Ritual"),
	RITUAL_SCROLL("Ritual Scroll"),
	ROLE("Role"),
	SIZE("Size"),
	SKILL("Skill"),
	SKILL_TRAINING("Skill Training"),
	SKILL_USAGE("Skill Usage"),
	SOURCE("source"),
	SUPERIOR_IMPLEMENT("Superior Implement"),
	THEME("Theme"),
	TIER("Tier"),
	TRAIT_PACKAGE("Trait Package"),
	VISION("Vision"),
	WEAPON("Weapon"),
	WEAPON_GROUP("Weapon Group"),
	WEAPON_PROPERTY("Weapon Property");
	
	public static RuleTypes forName(String name) {
		for(RuleTypes rt : RuleTypes.values()) {
			if(rt._name.equals(name))
				return rt;
		}
		return null;
	}
	
	private String _name;
	RuleTypes(String name) {
		_name = name;
	}
	
	public String getName() {
		return _name;
	}
}
