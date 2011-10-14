package com.ncgeek.manticore.character;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import com.ncgeek.manticore.Ritual;
import com.ncgeek.manticore.Tier;
import com.ncgeek.manticore.character.inventory.EquipmentManager;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.character.stats.StatBlock;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;
import com.ncgeek.manticore.rules.Specific;

public class PlayerCharacter extends Observable implements Serializable, IRest {
	
	private static final long serialVersionUID = 2L;
	private String _id;
	private String _race;
	private String _heroicClass;
	private String _paragonPath;
	
	private String _name;
	private int _level;
	private String _player;
	private String _height;
	private String _weight;
	private Gender _gender;
	private Alignment _alignment;
	private int _age;
	private String _company;
	private URI _portraitUri;
	private int _exp;
	private Wallet _carried;
	private Wallet _stored;
	private String _traits;
	private String _appearance;
	private String _companions;
	private String _notes;
	
	private List<Rule> _rules;
	private List<CharacterPower> _powers;
	private List<Feat> _feats;
	private List<Ritual> _rituals;
	private StatBlock _stats;
	private EquipmentManager _equipment;
	private HitPoints _hp;
	private boolean _hpSet;
	private boolean _surgeSet;
	
	private int _actionPoints;
	
	public PlayerCharacter(Wallet moneyCarried, Wallet moneyStored, List<Rule> rules, List<CharacterPower> powers, List<Feat> feats, List<Ritual> rituals, StatBlock stats, EquipmentManager equipment, HitPoints hp) {
		_name = null;
		_level = 1;
		_player = null;
		_height = null;
		_weight = null;
		_gender = null;
		_alignment = Alignment.Unaligned;
		_age = 0;
		_company = null;
		_portraitUri = null;
		_exp = 0;
		_carried = moneyCarried;
		_stored = moneyStored;
		_traits = null;
		_appearance = null;
		_companions = null;
		_notes = null;
		_rules = rules;
		_powers = powers;
		_stats = stats;
		_equipment = equipment;
		_hp = hp;
		_hpSet = _surgeSet = false;
		_actionPoints = 1;
		_feats = feats;
		_rituals = rituals;
		
		_equipment.addObserver(_stats);
		//this.addObserver(_stats);
	}
	
	public PlayerCharacter() {
		this(new Wallet(), new Wallet(), new ArrayList<Rule>(), new ArrayList<CharacterPower>(), new ArrayList<Feat>(), null, new StatBlock(), new EquipmentManager(), new HitPoints());
	}
	
	public String getID() { return _id; }
	public void setID(String id) { _id = id; }
	
	public String getRace() { return _race; }
	public void setRace(String race) { _race = race; }
	
	public String getHeroicClass() { return _heroicClass; }
	public void setHeroicClass(String heroicClass) { _heroicClass = heroicClass; }
	
	public String getParagonPath() { return _paragonPath; }
	public void setParagonPath(String path) { _paragonPath = path; }
	
	public String getName() { return _name; }
	public void setName(String name) { _name = name; }
	
	public int getLevel() { return _level; }
	public void setLevel(int level) { 
		if(level <= 0)
			throw new IllegalArgumentException("Level cannot be <= 0");
		_level = level;
	}
	
	public String getPlayer() { return _player; }
	public void setPlayer(String player) { _player = player; }
	
	public String getHeight() { return _height; }
	public void setHeight(String height) { _height = height; }
	
	public String getWeight() { return _weight; }
	public void setWeight(String weight) { _weight = weight; }
	
	public Gender getGender() { return _gender; }
	public void setGender(Gender gender) { 
		if(gender == null)
			throw new IllegalArgumentException("Gender cannot be null");
		_gender = gender;
	}
	
	public Alignment getAlignment() { return _alignment; }
	public void setAlignment(Alignment align) { 
		if(align == null)
			throw new IllegalArgumentException("Alignment cannot be null");
		_alignment = align;
	}
	
	public int getAge() { return _age; }
	public void setAge(int age) { 
		if(age <= 0)
			throw new IllegalArgumentException("Age cannot be <= 0");
		_age = age;
	}
	
	public String getCompany() { return _company; }
	public void setCompany(String company) { _company = company; }
	
	public URI getPortraitUri() { return _portraitUri; }
	public void setPortraitUri(URI portraitUri) { _portraitUri = portraitUri; }
	
	public int getExperience() { return _exp; }
	public void setExperience(int exp) { 
		if(exp < 0)
			throw new IllegalArgumentException("Experiance cannot be < 0");
		_exp = exp;
	}
	
	public Wallet getMoneyCarried() { return _carried; }
	public Wallet getMoneyStored() { return _stored; }
	
	public String getTraits() { return _traits; }
	public void setTraits(String traits) { _traits = traits; }
	
	public String getApperance() { return _appearance; }
	public void setApperance(String apperance) { _appearance = apperance; }
	
	public String getCompanions() { return _companions; }
	public void setCompanions(String companions) { _companions = companions; }
	
	public String getNotes() { return _notes; }
	public void setNotes(String notes) { _notes = notes; }
	
	public int getActionPoints() {
		return _actionPoints;
	}
	
	public void useActionPoint() {
		if(_actionPoints == 0)
			throw new UnsupportedOperationException("No action points available");
		--_actionPoints;
	}
	
	public HitPoints getHP() {
		if(!_hpSet) {
			Stat s = _stats.get("Hit Points");
			if(s == null)
				throw new UnsupportedOperationException("Unknown Hitpoints");
			_hp.setMax(s.getCalculatedValue());
			_hpSet = true;
		}
		
		if(!_surgeSet) {
			Stat s = _stats.get("Healing Surges");
			if(s == null)
				throw new UnsupportedOperationException("Unknown Healing Surges");
			_hp.setTotalSurges(s.getCalculatedValue());
			_surgeSet = true;
		}
		
		return _hp; 
	}
	
	public List<Feat> getFeats() {
		return Collections.unmodifiableList(_feats);
	}
	
	public List<Ritual> getRituals() {
		if(_rituals == null)
			return Collections.emptyList();
		else
			return Collections.unmodifiableList(_rituals);
	}
	
	public void add(Ritual ritual) {
		if(_rituals == null)
			_rituals = new ArrayList<Ritual>(2);
		int index = Collections.binarySearch(_rituals, ritual);
		if(index< 0) {
			index = -index - 1;
			_rituals.add(index, ritual);
		}
	}
	
	public List<Rule> getRules() {
		 return Collections.unmodifiableList(_rules);
	}
	
	public void add(Rule rule) {
		if(rule == null)
			throw new IllegalArgumentException("Rule cannot be null");
		
		if(_rules.contains(rule))
			throw new RuntimeException("Duplicate Rule");
		
		if(rule.getType() == RuleTypes.FEAT) {
			Feat f = getFeat(rule);
			int index = Collections.binarySearch(_feats, f);
			if(index < 0) {
				index = -index - 1;
				_feats.add(index, f);
			}
			return;
		}
		
		_rules.add(rule);
		
		switch(rule.getType()) {
			case GENDER:
				Gender g = Gender.Male;
				if(rule.getName().equalsIgnoreCase("female"))
					g = Gender.Female;
				this.setGender(g);
				break;
				
			case ALIGNMENT:
				setAlignment(Alignment.forName(rule.getName()));
				break;
				
			case RACE:
				setRace(rule.getName());
				break;
				
			case CLASS:
				setHeroicClass(rule.getName());
				break;
		}
		
		//this.setChanged();
		//this.notifyObservers(new RuleEventArgs(RuleEventType.RuleAdded, rule));
		_stats.update(this, new RuleEventArgs(RuleEventType.RuleAdded, rule));
	}
	
	private Feat getFeat(Rule rule) {
		Feat f = new Feat();
		
		f.setName(rule.getName());
		f.setDescription(rule.getBody());
		
		for(Specific s : rule.getSpecifics()) {
			if(s.getName().equals("Tier")) {
				f.setTier(Tier.forName("Heroic"));
			}
            else if(s.getName().equals("Short Description")) {
            	f.setShortDescription(s.getValue());
            }
            else if(s.getName().equals("Special")) {
            	f.setSpecial(s.getValue());
            }
            else if(s.getName().equals("Type")) {
            	f.setType(s.getValue());
            }
            else if(s.getName().equals("Associated Power Info")) {
            	f.setAssociatedPowerInfo(s.getValue());
            }
            else if(s.getName().equals("Associated Powers")) {
            	f.setAssociatedPowers(s.getValue());
            }
		}
		
		return f;
	}

	public List<CharacterPower> getPowers() {
		return Collections.unmodifiableList(_powers);
	}
	
	public void add(CharacterPower power) {
		if(power == null)
			throw new IllegalArgumentException("Power cannot be null");
		
		_powers.add(power);
	}
	
	public StatBlock getStats() { return _stats; }
	
	public EquipmentManager getEquipment() { return _equipment; }

	@Override
	public void fullRest() {
		_hp.fullRest();
		_actionPoints = 1;
		
		for(IRest r : _powers)
			r.fullRest();
	}

	@Override
	public void shortRest() {
		_hp.shortRest();
		for(IRest r : _powers)
			r.shortRest();
	}

	@Override
	public void milestone() {
		++_actionPoints;
		for(IRest r : _powers)
			r.milestone();
	}
}
