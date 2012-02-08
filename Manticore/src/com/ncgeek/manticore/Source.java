package com.ncgeek.manticore;

import java.io.Serializable;
import java.util.HashMap;

public final class Source implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static HashMap<String, Source> _allSources;
	
	static {
		_allSources = new HashMap<String, Source>();
	}
	
	public static Source forName(String name) {
		if(!_allSources.containsKey(name.toLowerCase())) {
			Source s = new Source(name);
			_allSources.put(name.toLowerCase(), s);
			return s;
		} else {
			return _allSources.get(name.toLowerCase());
		}
	}
	
	private String _name;
	
	private Source(String name) {
		_name = name;
	}
	
	public String getName() { return _name; }
}
