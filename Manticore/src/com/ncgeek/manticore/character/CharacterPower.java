package com.ncgeek.manticore.character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ncgeek.manticore.Dice;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.powers.Power;
import com.ncgeek.manticore.powers.PowerUsages;

public class CharacterPower implements Serializable, IRest, Comparable<CharacterPower> {

	private static final long serialVersionUID = 1L;

	private Power power;
	private List<PowerWeapon> weapons;
	private boolean used;
	
	public CharacterPower(Power power) {
		this(power, new ArrayList<PowerWeapon>());
	}
	
	public CharacterPower(Power power, List<PowerWeapon> lstWeapons) {
		this.power = power;
		weapons = lstWeapons;
		used = false;
	}
	
	public Power getPower() { return power; }
	
	public List<PowerWeapon> getWeapons() {
		return Collections.unmodifiableList(weapons);
	}
	
	public void add(PowerWeapon weapon) {
		weapons.add(weapon);
	}
	
	public boolean isUsed() { return used; }
	
	public void use() {
		PowerUsages usage = power.getUsage();
		if(usage != null && !usage.equals(PowerUsages.AtWill))
			used = true;
	}
	
	@Override
	public void fullRest() {
		used = false;
	}

	@Override
	public void shortRest() {
		PowerUsages usage = power.getUsage();
		if(usage != null && usage.equals(PowerUsages.Encounter))
			used = false;
	}

	@Override
	public void milestone() {}
	
	@Override
	public String toString() {
		return power.getName();
	}
	
 	public static final class PowerWeapon implements Serializable {
		
		private static final long serialVersionUID = 1L;
		private EquippableItem weapon;
		private int attackBonus;
		private Dice damage;
		private Stat attackStat;
		private String defense;
		private List<String> hitComponents;
		private List<String> damageComponents;
		
		public PowerWeapon() {
			hitComponents = new ArrayList<String>();
			damageComponents = new ArrayList<String>();
		}

		public final EquippableItem getWeapon() {
			return weapon;
		}

		public final void setWeapon(EquippableItem weapon) {
			this.weapon = weapon;
		}

		public final int getAttackBonus() {
			return attackBonus;
		}

		public final void setAttackBonus(int attackBonus) {
			this.attackBonus = attackBonus;
		}

		public final Dice getDamage() {
			return damage;
		}

		public final void setDamage(Dice damage) {
			this.damage = damage;
		}

		public final Stat getAttackStat() {
			return attackStat;
		}

		public final void setAttackStat(Stat attackStat) {
			this.attackStat = attackStat;
		}

		public final String getDefense() {
			return defense;
		}

		public final void setDefense(String defense) {
			this.defense = defense;
		}
		
		public final List<String> getHitComponents() {
			return Collections.unmodifiableList(hitComponents);
		}
		
		public final List<String> getDamageComponents() {
			return Collections.unmodifiableList(damageComponents);
		}
		
		public void addHitComponent(String component) {
			hitComponents.add(component);
		}
		
		public void addDamageComponent(String component) {
			damageComponents.add(component);
		}
	}

	@Override
	public int compareTo(CharacterPower other) {
		return power.compareTo(other.power);
	}
}
