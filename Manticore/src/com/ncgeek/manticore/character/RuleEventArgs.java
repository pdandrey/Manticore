package com.ncgeek.manticore.character;

import com.ncgeek.manticore.rules.Rule;

public class RuleEventArgs {
	
	private Rule _rule;
	private RuleEventType _type;
	
	public RuleEventArgs(RuleEventType type, Rule rule) {
		_rule = rule;
		_type = type;
	}
	
	public Rule getRule() { return _rule; }
	
	public RuleEventType getType() { return _type; }
}
