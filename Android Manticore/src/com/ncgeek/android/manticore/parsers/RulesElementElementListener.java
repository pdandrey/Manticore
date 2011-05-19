package com.ncgeek.android.manticore.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xml.sax.Attributes;

import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;
import com.ncgeek.manticore.rules.Specific;

import android.sax.Element;
import android.sax.ElementListener;
import android.sax.TextElementListener;

public class RulesElementElementListener implements ElementListener {

	private Rule current;
	private List<Rule> lstRules;
	
	public RulesElementElementListener(Element rule) {
		lstRules = new ArrayList<Rule>(10);
		Element spec = rule.getChild("specific");
		spec.setTextElementListener(new TextElementListener() {

			private String name;
			
			@Override
			public void start(Attributes attributes) {
				name = attributes.getValue("name");
			}

			@Override
			public void end(String body) {
				body = body.trim();
				current.addSpecific(new Specific(name, body));
			}
			
		});
		
		// android sax can't handle elements that have both children and text
		rule.setElementListener(this);
	}
	
	@Override
	public void start(Attributes attrs) {
		current = parseRule(attrs);
	}

	@Override
	public void end() {
		//current.setBody(body);
		lstRules.add(current);
		current = null;
	}
	
	public List<Rule> getRules() {
		return Collections.unmodifiableList(lstRules);
	}
	
	public void reset() {
		lstRules.clear();
		current = null;
	}

	private Rule parseRule(Attributes attrs) {
		String name = attrs.getValue("name");
		RuleTypes type = RuleTypes.forName(attrs.getValue("type"));
		String internalID = attrs.getValue("internal-id");
		String url = attrs.getValue("url");
		String legal = attrs.getValue("legality");
		return new Rule(name, type, internalID, url, legal);
	}
}
