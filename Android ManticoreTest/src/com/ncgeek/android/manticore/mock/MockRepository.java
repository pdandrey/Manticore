package com.ncgeek.android.manticore.mock;

import com.ncgeek.manticore.ICompendiumRepository;
import com.ncgeek.manticore.items.Armor;
import com.ncgeek.manticore.items.Gear;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.items.MagicItem;
import com.ncgeek.manticore.items.Weapon;
import com.ncgeek.manticore.rules.Rule;

public class MockRepository implements ICompendiumRepository, IMockable {

	private MockCounter counter;
	
	public MockRepository() {
		counter = new MockCounter();
	}
	
	@Override
	public Item getItem(Rule rule) {
		return (Item)counter.call("getItem", rule);
	}

	@Override
	public Weapon getWeapon(Rule rule) {
		return (Weapon)counter.call("getWeapon", rule);
	}

	@Override
	public Armor getArmor(Rule rule) {
		return (Armor)counter.call("getArmor", rule);
	}

	@Override
	public void add(Item item) {
		counter.call("add", item);
	}

	@Override
	public void add(Weapon weapon) {
		counter.call("add", weapon);
	}

	@Override
	public void add(Armor armor) {
		counter.call("add", armor);
	}

	@Override
	public IMockable returnWhen(Object returnValue, String method, Object... params) {
		counter.returnWhen(returnValue, method, params);
		return this;
	}

	@Override
	public IMockable throwWhen(Throwable tr, String method, Object... params) {
		counter.throwWhen(tr, method, params);
		return this;
	}

	@Override
	public MagicItem getMagicItem(Rule rule) {
		return (MagicItem)counter.call("getMagicItem", rule);
	}

	@Override
	public void add(MagicItem magicItem) {
		counter.call("add", magicItem);
	}

	@Override
	public Gear getGear(Rule rule) {
		return (Gear)counter.call("getGear", rule);
	}

	@Override
	public void add(Gear gear) {
		counter.call("add", gear);
	}

}
