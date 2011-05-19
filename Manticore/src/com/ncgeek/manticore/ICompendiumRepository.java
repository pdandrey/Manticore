package com.ncgeek.manticore;

import com.ncgeek.manticore.items.Armor;
import com.ncgeek.manticore.items.Gear;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.items.MagicItem;
import com.ncgeek.manticore.items.Weapon;
import com.ncgeek.manticore.rules.Rule;

public interface ICompendiumRepository {

	public Item getItem(Rule rule);
	public Weapon getWeapon(Rule rule);
	public Armor getArmor(Rule rule);
	public MagicItem getMagicItem(Rule rule);
	public Gear getGear(Rule rule);
	
	public void add(Item item);
	public void add(Weapon weapon);
	public void add(Armor armor);
	public void add(MagicItem magicItem);
	public void add(Gear gear);
}
