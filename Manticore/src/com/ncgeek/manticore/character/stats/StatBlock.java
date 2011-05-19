package com.ncgeek.manticore.character.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.ncgeek.manticore.character.RuleEventArgs;
import com.ncgeek.manticore.character.inventory.EquipmentManagerEventArgs;

public class StatBlock extends Observable implements Observer {
	
	private Map<String, Stat> _statMap;
	private Map<String, List<Addition>> _linksNeeded;
	
	public StatBlock(Map<String,Stat> statmap, Map<String, List<Addition>> linkmap) {
		_statMap = statmap;
		_linksNeeded = linkmap;
	}
	public StatBlock() {
		this(new HashMap<String,Stat>(), new HashMap<String, List<Addition>>());
	}
	
	public void add(Stat s) {
		for(String name : s.getAliases()) {
			name = name.toLowerCase();
			if(!_statMap.containsKey(name)) {
				_statMap.put(name, s);
				
				if(_linksNeeded.containsKey(name)) {
					for(Addition a : _linksNeeded.get(name)) {
						a.setStatLink(s);
					}
					_linksNeeded.remove(name);
				}
			}
		}
		
		for(Addition add : s.getAdditions()) {
			if(add.getStatLink() != null) {
				String statName = add.getStatLink().toLowerCase();
				if(_statMap.containsKey(statName)) {
					add.setStatLink(_statMap.get(statName));
				} else {
					if(!_linksNeeded.containsKey(statName)) {
						List<Addition> lst = new ArrayList<Addition>();
						_linksNeeded.put(statName, lst);
						lst.add(add);
					} else {
						_linksNeeded.get(statName).add(add);
					}
					
				}
			}
		}
	}
	
	public Stat get(String name) {
		return _statMap.get(name.toLowerCase());
	}
	
	public Collection<Stat> getStats() {
		return Collections.unmodifiableCollection(_statMap.values());
	}
	
	@Override
	public void update(Observable sender, Object data) {
		if(data == null)
			return;
		
		if(!(data instanceof EquipmentManagerEventArgs)
				&& !(data instanceof RuleEventArgs))
			return;
		
		EquipmentManagerEventArgs emea = null;
		RuleEventArgs rea = null;
		
		if(data instanceof EquipmentManagerEventArgs)
			emea = (EquipmentManagerEventArgs)data;
		else
			rea = (RuleEventArgs)data;
		
		HashSet<String> visited = new HashSet<String>();
		
		for(String name : _statMap.keySet()) {
			if(!visited.contains(name)) {
				Stat stat = _statMap.get(name);
				for(Addition add : stat.getAdditions()) {
					if(emea != null)
						add.process(emea);
					else
						add.process(rea);
				}
				visited.addAll(stat.getAliases());
			}
		}
	}
		
}
