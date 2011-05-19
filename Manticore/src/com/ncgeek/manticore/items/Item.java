package com.ncgeek.manticore.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ncgeek.manticore.Money;
import com.ncgeek.manticore.Source;
import com.ncgeek.manticore.character.inventory.ItemStack;

public abstract class Item implements Comparable<Item> {

	public static final Comparator<Item> PriceComparator = new Comparator<Item>() {
		@Override
		public int compare(Item item1, Item item2) {
			return item1._price.compareTo(item2._price);
		}
	};
	
	private String _name;
	private String _id;
	private String _desc;
	private Money _price;
	private List<Source> _sources;
	private double _weight;
	
	public Item(List<Source> lst) {
		_sources = lst;
	}
	
	public Item() {
		this(new ArrayList<Source>(1));
	}
	
	public String getID() { return _id; }
	public void setID(String id) { _id = id; }
	
	public String getName() { return _name; }
	public void setName(String name) { _name = name; }
	
	public String getDescription() { return _desc; }
	public void setDescription(String description) { _desc = description; }
	
	public Money getPrice() { return _price; }
	public void setPrice(Money price) { _price = price; }
	
	public List<Source> getSources() {
		return Collections.unmodifiableList(_sources);
	}
	public void addSource(Source s) {
		_sources.add(s);
	}
	
	public double getWeight() { return _weight; }

	public void setWeight(double weight) {
		if(weight < 0)
			throw new IllegalArgumentException("Weight cannot be less than 0");
		
		_weight = weight;
	}
	
	@Override
	public int compareTo(Item other) {
		return _name.compareTo(other._name);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o instanceof ItemStack)
			return equals(((ItemStack)o).getItem());
		if(o instanceof Item)
			return equals((Item)o);
		return false;
	}

	public boolean equals(Item i) {
		return this == i 
				|| (
						getName().equals(i.getName())
						&& ((getDescription() == null && i.getDescription() == null) || getDescription().equals(i.getDescription()))
						&& getPrice().equals(i.getPrice())
						&& getWeight() == i.getWeight()
						&& getSources().size() == i.getSources().size()
						&& getSources().containsAll(i.getSources())
					);
	}
}
