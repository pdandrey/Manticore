package com.ncgeek.android.manticore.util;

import java.util.Collection;
import java.util.HashMap;

import com.ncgeek.android.manticore.R;
import com.ncgeek.manticore.items.ArmorCategories;
import com.ncgeek.manticore.items.WeaponGroups;

import android.os.Environment;

public final class Utility {

	private static final HashMap<String,Integer> STAT_ICONS = new HashMap<String, Integer>();
	private static final HashMap<WeaponGroups,Integer> WEAPON_ICONS = new HashMap<WeaponGroups, Integer>();
	private static final HashMap<ArmorCategories,Integer> ARMOR_ICONS = new HashMap<ArmorCategories, Integer>();
	
	static {
		STAT_ICONS.put("ac", R.drawable.i_stat_ac);
		STAT_ICONS.put("acrobatics", R.drawable.i_stat_acrobatics);
		STAT_ICONS.put("arcana", R.drawable.i_stat_arcana);
		STAT_ICONS.put("athletics", R.drawable.i_stat_athletics);
		STAT_ICONS.put("bluff", R.drawable.i_stat_bluff);
		STAT_ICONS.put("cha", R.drawable.i_stat_cha);
		STAT_ICONS.put("con", R.drawable.i_stat_con);
		STAT_ICONS.put("dex", R.drawable.i_stat_dex);
		STAT_ICONS.put("diplomacy", R.drawable.i_stat_diplomacy);
		STAT_ICONS.put("dungeoneering", R.drawable.i_stat_dungeoneering);
		STAT_ICONS.put("endurance", R.drawable.i_stat_endurance);
		STAT_ICONS.put("fortitude", R.drawable.i_stat_fortitude);
		STAT_ICONS.put("heal", R.drawable.i_stat_heal);
		STAT_ICONS.put("history", R.drawable.i_stat_history);
		STAT_ICONS.put("initiative", R.drawable.i_stat_initiative);
		STAT_ICONS.put("insight", R.drawable.i_stat_insight);
		STAT_ICONS.put("int", R.drawable.i_stat_int);
		STAT_ICONS.put("intimidate", R.drawable.i_stat_intimidate);
		STAT_ICONS.put("nature", R.drawable.i_stat_nature);
		STAT_ICONS.put("perception", R.drawable.i_stat_perception);
		STAT_ICONS.put("reflex", R.drawable.i_stat_reflex);
		STAT_ICONS.put("religion", R.drawable.i_stat_religion);
		STAT_ICONS.put("speed", R.drawable.i_stat_speed);
		STAT_ICONS.put("stealth", R.drawable.i_stat_stealth);
		STAT_ICONS.put("str", R.drawable.i_stat_str);
		STAT_ICONS.put("streetwise", R.drawable.i_stat_streetwise);
		STAT_ICONS.put("thievery", R.drawable.i_stat_thievery);
		STAT_ICONS.put("will", R.drawable.i_stat_will);
		STAT_ICONS.put("wis", R.drawable.i_stat_wis);
		
		ARMOR_ICONS.put(ArmorCategories.Chain, R.drawable.i_armor_chain);
		ARMOR_ICONS.put(ArmorCategories.Cloth, R.drawable.i_armor_cloth);
		ARMOR_ICONS.put(ArmorCategories.HeavyShields, R.drawable.i_armor_heavy_shield);
		ARMOR_ICONS.put(ArmorCategories.Hide, R.drawable.i_armor_hide);
		ARMOR_ICONS.put(ArmorCategories.Leather, R.drawable.i_armor_leather);
		ARMOR_ICONS.put(ArmorCategories.LightShields, R.drawable.i_armor_light_shield);
		ARMOR_ICONS.put(ArmorCategories.Plate, R.drawable.i_armor_plate);
		ARMOR_ICONS.put(ArmorCategories.Scale, R.drawable.i_armor_scale);
		
		WEAPON_ICONS.put(WeaponGroups.Axe, R.drawable.i_weapon_axe);
		WEAPON_ICONS.put(WeaponGroups.Blowgun, R.drawable.i_weapon_blowgun);
		WEAPON_ICONS.put(WeaponGroups.Bow, R.drawable.i_weapon_bow);
		WEAPON_ICONS.put(WeaponGroups.Crossbow, R.drawable.i_weapon_crossbow);
		WEAPON_ICONS.put(WeaponGroups.Flail, R.drawable.i_weapon_flail);
		//WEAPON_ICONS.put(WeaponGroups.Garrote, R.drawable.i_weapon_);
		WEAPON_ICONS.put(WeaponGroups.Hammer, R.drawable.i_weapon_hammer);
		WEAPON_ICONS.put(WeaponGroups.HeavyBlade, R.drawable.i_weapon_heavy_blade);
		WEAPON_ICONS.put(WeaponGroups.LightBlade, R.drawable.i_weapon_light_blade);
		WEAPON_ICONS.put(WeaponGroups.Mace, R.drawable.i_weapon_mace);
		//WEAPON_ICONS.put(WeaponGroups.Pick, R.drawable.i_weapon_);
		WEAPON_ICONS.put(WeaponGroups.Polearm, R.drawable.i_weapon_polearm);
		//WEAPON_ICONS.put(WeaponGroups.Sling, R.drawable.i_weapon_);
		WEAPON_ICONS.put(WeaponGroups.Spear, R.drawable.i_weapon_spear);
		WEAPON_ICONS.put(WeaponGroups.Staff, R.drawable.i_weapon_staff);
		WEAPON_ICONS.put(WeaponGroups.Unarmed, R.drawable.i_weapon_unarmed);
	}

	public static boolean isExternalAvailable() {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    return true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    return true;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    return false;
		}
	}
	
	public static boolean isExternalWritable() {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    return true;
		} else {
			return false;
		}
	}
	
	private static int density;
	public static void setDensityDpi(int density) {
		Utility.density = density;
	}
	public static int getDensityDpi() { return density; }
	
	public static int dpToPx(int dp) {
		return  dp * (density / 160);
	}

	
	public static final int getStatIcon(String stat) {
		stat = stat.toLowerCase().trim();
		if(STAT_ICONS.containsKey(stat))
			return STAT_ICONS.get(stat);
		else
			return 0;
	}

	public static final int getIcon(ArmorCategories armor) {
		if(ARMOR_ICONS.containsKey(armor))
			return ARMOR_ICONS.get(armor);
		else
			return 0;
	}
	
	public static final int getIcon(Collection<WeaponGroups> groups) {
		for(WeaponGroups g : groups) {
			if(WEAPON_ICONS.containsKey(g))
				return WEAPON_ICONS.get(g);
		}
		return 0;
	}
	
	
}
