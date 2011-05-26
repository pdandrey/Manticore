package com.ncgeek.manticore.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.ncgeek.manticore.Source;

public class Rule implements Comparable<Rule> {

	private String _name;
	private RuleTypes _type;
	private String _url;
	private String _internalID;
	private String _legality;
	private Date _revisionDate;
	private List<Source> _sources;
	private List<Specific> _specifics;
	private String _body;
	
	public Rule(String name, RuleTypes type, String internalID, Date revisionDate) {
		throw new UnsupportedOperationException("Not Implemented");
	}
	
	public Rule(String name, RuleTypes type, String internalID, String url, String legality) {
		_name = name;
		_type = type;
		_internalID = internalID;
		_url = url;
		_legality = legality;
	}
	
	public void addSpecific(Specific s) {
		if(_specifics == null)
			_specifics = new ArrayList<Specific>();
		_specifics.add(s);
	}
	
	public List<Specific> getSpecifics() {
		if(_specifics == null) 
			return Collections.emptyList();
		else
			return Collections.unmodifiableList(_specifics);
	}
	
	public List<Specific> getSpecifics(String name) {
		if(_specifics == null)
			return Collections.emptyList();
		
		ArrayList<Specific> lst = new ArrayList<Specific>();
		for(Specific s : _specifics) {
			if(s.getName().equalsIgnoreCase(name)) {
				lst.add(s);
			}
		}
		
		return Collections.unmodifiableList(lst);
	}
	
	public boolean hasSpecifics() {
		return _specifics != null && _specifics.size() > 0;
	}
	
	public void addSource(Source s) {
		if(_sources == null)
			_sources = new ArrayList<Source>();
		_sources.add(s);
	}
	
	public List<Source> getSources() {
		return Collections.unmodifiableList(_sources);
	}
	
	public String getName() { return _name; }
	
	public RuleTypes getType() { return _type; }
	
	public String getURL() { return _url; }
	
	public String getInternalID() { return _internalID; }
	
	public String getLegality() { return _legality; }
	
	public Date getRevisionDate() { return _revisionDate; }
	
	public String getBody() { return _body; }
	public void setBody(String body) { _body = body; }
	
	public Specific getSpecific(String name) {
		if(_specifics == null)
			return null;
		
		for(Specific s : _specifics) {
			if(s.getName().equalsIgnoreCase(name)) {
				return s;
			}
		}
		return null;
	}

	@Override
	public int compareTo(Rule other) {
		return _name.compareTo(other.getName());
	}
	
	@Override
	public String toString() {
		return _name + " (" + _type.toString() + ")";
	}
	
	public boolean equals(Object other) {
		if(this == other)
			return true;
		
		if(other != null && other instanceof Rule) {
			Rule r = (Rule)other;
			return _name.equals(r._name)
				&& ((_type == null && r._type == null) || _type.equals(r._type))
				&& ((_url == null && r._url == null) || _url.equals(r._url))
				&& _internalID.equals(r._internalID);
		}
		
		return false;
	}
}
