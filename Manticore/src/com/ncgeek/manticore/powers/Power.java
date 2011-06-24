package com.ncgeek.manticore.powers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ncgeek.manticore.Source;

public class Power implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String _name;
	private String _flavor;
	private PowerActions _action;
	private PowerTypes _type;
	private String _display;
	private int _level;
	private PowerUsages _usage;
	
	private List<Source> _sources;
	private List<PowerSpecific> _specifics;
	
	public Power(List<Source> lstSources, List<PowerSpecific> lstSpecs) {
		_sources = lstSources;
		_specifics = lstSpecs;
	}
	
	public Power() {
		this(new ArrayList<Source>(), new ArrayList<PowerSpecific>());
	}
	
	public String getName() { return _name; }
	public void setName(String name) { _name = name; }
	
	public String getFlavor() { return _flavor; }
	public void setFlavor(String flavor) { _flavor = flavor; }
	
	public PowerActions getAction() { return _action; }
	public void setAction(PowerActions action) { _action = action; }
	
	public PowerTypes getType() { return _type; }
	public void setType(PowerTypes type) { _type = type; }
	
	public String getDisplay() { return _display; }
	public void setDisplay(String display) { _display = display; }
	
	public int getLevel() { return _level; }
	public void setLevel(int level) { _level = level; }
	
	public PowerUsages getUsage() { return _usage; }
	public void setUsage(PowerUsages usage) { _usage = usage; }
	
	public List<Source> getSources() {
		return Collections.unmodifiableList(_sources);
	}
	
	public void add(Source s) {
		_sources.add(s);
	}
	
	public List<PowerSpecific> getSpecifics() {
		return Collections.unmodifiableList(_specifics);
	}
	
	public void add(PowerSpecific ps) {
		_specifics.add(ps);
	}
}
