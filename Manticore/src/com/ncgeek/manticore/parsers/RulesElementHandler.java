package com.ncgeek.manticore.parsers;

import org.xml.sax.Attributes;

import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;
import com.ncgeek.manticore.rules.Specific;

public class RulesElementHandler implements IElementHandler {
	
	private static final IElementHandler instance = new RulesElementHandler();
	public static final IElementHandler getInstance() { return instance; }
	
	private Rule rule;
	private String specName;
	
	private RulesElementHandler() {}
	
	public Rule getRule() { return rule; }

	@Override
	public void startElement(PlayerCharacter pc, String name, Attributes attrs) {
		if(name.equals("RulesElement")) {
			String ruleName = attrs.getValue("name");
			RuleTypes type = RuleTypes.forName(attrs.getValue("type"));
			String internalID = attrs.getValue("internal-id");
			String url = attrs.getValue("url");
			String legal = attrs.getValue("legality");
			rule = new Rule(ruleName, type, internalID, url, legal);
		} else if(name.equals("specific")) {
			specName = attrs.getValue("name");
		}
	}

	@Override
	public void endElement(PlayerCharacter pc, String name, String body) {
		
		if(body != null) {
			body = body.trim();
			if(body.length() == 0)
				body = null;
		}
		
		if(name.equals("specific")) {
			rule.addSpecific(new Specific(specName, body));
			specName = null;
		} else if(name.equals("RulesElement") && body != null) {
			rule.setBody(body);
		}
	}
	
	
}
