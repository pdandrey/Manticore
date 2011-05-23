package com.ncgeek.manticore.integrationtests;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ncgeek.manticore.Dice;
import com.ncgeek.manticore.ICompendiumRepository;
import com.ncgeek.manticore.Money;
import com.ncgeek.manticore.Source;
import com.ncgeek.manticore.items.Armor;
import com.ncgeek.manticore.items.ArmorCategories;
import com.ncgeek.manticore.items.ArmorTypes;
import com.ncgeek.manticore.items.Gear;
import com.ncgeek.manticore.items.GearCategory;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.items.ItemSlots;
import com.ncgeek.manticore.items.MagicItem;
import com.ncgeek.manticore.items.Range;
import com.ncgeek.manticore.items.Weapon;
import com.ncgeek.manticore.items.WeaponCategories;
import com.ncgeek.manticore.items.WeaponGroups;
import com.ncgeek.manticore.items.WeaponProperties;
import com.ncgeek.manticore.rules.Rule;

public class ManticoreDatabase implements ICompendiumRepository {

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch(ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private Connection connection;
	
	public ManticoreDatabase(File db) {
		try { 
			connection = DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath().replace('\\', '/'));
		} catch(SQLException sqlex) {
			throw new RuntimeException(sqlex);
		}
	}
	
	public void close() {
		if(connection != null) {
			try {
				connection.close();
			} catch(Exception ex) {
				throw new RuntimeException(ex);
			}
			connection = null;
		}
	}
	
	public Armor getArmor(String internalID) {
		try {
			ResultSet rs = execute("SELECT * FROM vArmor WHERE InternalID = '" + internalID + "'");
			
			if(!rs.next())
				return null;
			
			Armor a = new Armor();
			a.setName(rs.getString("Name"));
			a.setBonus(rs.getInt("ArmorBonus"));
			a.setArmorCategory(ArmorCategories.forName(rs.getString("ArmorCategory")));
			a.setArmorType(ArmorTypes.forName(rs.getString("ArmorType")));
			a.setCheckPenalty(rs.getInt("CheckPenalty"));
			a.setDescription(rs.getString("Description"));
			a.setPrice(new Money(rs.getLong("Price")));
			a.setItemSlot(ItemSlots.forName(rs.getString("ItemSlot")));
			a.setSpecial(rs.getString("Special"));
			a.setWeight(rs.getDouble("Weight"));
			
			rs.close();
			rs = null;
			
			rs = execute("SELECT * FROM vArmorSources WHERE InternalID = '" + internalID + "'");
			while(rs.next()) {
				a.addSource(Source.forName(rs.getString("Name")));
			}
			
			return a;
		} catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public Weapon getWeapon(String id) {
		try {
			ResultSet rs = execute("SELECT * FROM vWeapon WHERE InternalID = '" + id + "'");
			
			if(!rs.next())
				return null;
			
			Weapon w = new Weapon();
			w.setID(id);
			w.setName(rs.getString("Name"));
			w.setAdditionalSlot(ItemSlots.forName(rs.getString("AdditionalSlot")));
			w.setDice(new Dice(rs.getInt("DamageDiceCount"), rs.getInt("DamageDiceSides")));
			w.setPrice(new Money(rs.getLong("Price")));
			w.setTwoHanded(rs.getInt("HandsRequired") == 2);
			w.setItemSlot(ItemSlots.forName(rs.getString("ItemSlot")));
			w.setProficiencyBonus(rs.getInt("ProficiencyBonus"));
			w.setRange(new Range(rs.getInt("Range1"), rs.getInt("Range2")));
			w.setCategory(WeaponCategories.forName(rs.getString("WeaponCategory")));
			w.setWeight(rs.getDouble("Weight"));
			
			rs.close();
			rs = null;
			
			rs = execute("SELECT * FROM vWeaponSources WHERE InternalID = '" + id + "'");
			while(rs.next()) {
				w.addSource(Source.forName(rs.getString("Name")));
			}
			rs.close();
			rs = null;
			
			rs = execute("SELECT * FROM vWeaponGroups WHERE InternalID = '" + id + "'");
			while(rs.next()) {
				w.addGroup(WeaponGroups.forName(rs.getString("Name")));
			}
			rs.close();
			rs = null;
			
			rs = execute("SELECT * FROM vWeaponProperties WHERE InternalID = '" + id + "'");
			while(rs.next()) {
				w.addProperty(WeaponProperties.forName(rs.getString("Name")));
			}
			rs.close();
			rs = null;
			
			return w;
		} catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private ResultSet execute(String sql) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.setQueryTimeout(10);
		return stmt.executeQuery(sql);
	}

	@Override
	public Item getItem(Rule rule) {
		switch(rule.getType()) {
			case WEAPON: return getWeapon(rule);
			case ARMOR: return getArmor(rule);
			case MAGIC_ITEM: return getMagicItem(rule);
			default: return null;
		}
	}

	@Override
	public Weapon getWeapon(Rule rule) {
		return getWeapon(rule.getInternalID());
	}

	@Override
	public Armor getArmor(Rule rule) {
		return getArmor(rule.getInternalID());
	}

	@Override
	public MagicItem getMagicItem(Rule rule) {
		return null;
	}

	@Override
	public Gear getGear(Rule rule) {
		String internalID = rule.getInternalID();
		
		try {
			ResultSet rs = execute("SELECT * FROM vGear WHERE InternalID = '" + internalID + "'");
			
			if(!rs.next())
				return null;
			
			Gear g = new Gear();
			g.setName(rs.getString("Name"));
			g.setDescription(rs.getString("Description"));
			g.setPrice(new Money(rs.getLong("Price")));
			if(rs.getString("ItemSlot") != null)
				g.setItemSlot(ItemSlots.forName(rs.getString("ItemSlot")));
			g.setWeight(rs.getDouble("Weight"));
			g.setCategory(GearCategory.forName(rs.getString("GearCategory")));
			g.setCount(rs.getInt("ItemCount"));
			
			rs.close();
			rs = null;
			
			rs = execute("SELECT * FROM vGearSources WHERE InternalID = '" + internalID + "'");
			while(rs.next()) {
				g.addSource(Source.forName(rs.getString("Name")));
			}
			
			return g;
		} catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void add(Item item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(Weapon weapon) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(Armor armor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(MagicItem magicItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(Gear gear) {
		// TODO Auto-generated method stub
		
	}
}
