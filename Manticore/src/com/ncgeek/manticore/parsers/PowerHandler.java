package com.ncgeek.manticore.parsers;

import org.xml.sax.Attributes;

import com.ncgeek.manticore.Dice;
import com.ncgeek.manticore.character.CharacterPower;
import com.ncgeek.manticore.character.CharacterPower.PowerWeapon;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.powers.Power;
import com.ncgeek.manticore.powers.PowerActions;
import com.ncgeek.manticore.powers.PowerKeywords;
import com.ncgeek.manticore.powers.PowerSpecific;
import com.ncgeek.manticore.powers.PowerSpecificAttribute;
import com.ncgeek.manticore.powers.PowerTypes;
import com.ncgeek.manticore.powers.PowerUsages;
import com.ncgeek.manticore.util.Logger;

public class PowerHandler implements IElementHandler {

	private final String LOG_TAG = "Power Handler";
	
	private enum Specifics { Flavor, PowerUsage, Display, ActionType, PowerType, Level }
	private enum WeaponElements { AttackBonus, Damage, AttackStat, Defense, HitComponents, DamageComponents }
	
	private static final IElementHandler instance = new PowerHandler();
	public static final IElementHandler getInstance() { return instance; }
	
	private Power power;
	private CharacterPower cPower;
	private PowerWeapon weapon;
	private PowerSpecific specific;
	private String attributeName;
	private Specifics spec; 
	
	private PowerHandler() {
		weapon = null;
		power = null;
		cPower = null;
	}
	
	@Override
	public void startElement(PlayerCharacter pc, String name, Attributes attributes) {
		if(weapon != null && name.equals("RulesElement")) {
			Item o = pc.getEquipment().getById(attributes.getValue("internal-id"));
			if(o == null)
				throw new IllegalArgumentException(String.format("Could not find weapon %s for power %s", attributes.getValue("name"), power.getName()));
			if(!(o instanceof EquippableItem))
				throw new IllegalArgumentException(String.format("Weapon %s is not an equippable item for power %s", o.getName(), power.getName()));
			weapon.setWeapon((EquippableItem)o);
		} else if(name.equals("Power")) {
			power = new Power();
			power.setName(attributes.getValue("name"));
			cPower = new CharacterPower(power);
			pc.add(cPower);
		} else if(name.equals("specific")) {
			parseSpecific(attributes);
		} else if(name.equals("Weapon")) {
			weapon = new PowerWeapon();
			cPower.add(weapon);
		}
	}

	private void parseSpecific(Attributes attributes) {
		String name = attributes.getValue("name");
		attributeName = null;
		
		if(name.trim().startsWith("_"))
			return;
		
		name = name.replace('\t', ' ');
		if(!name.startsWith(" ")) {
			try {
				spec = Specifics.valueOf(name.replace(" ", ""));
				specific = new PowerSpecific();
				power.add(specific);
			} catch(IllegalArgumentException iaex) {
				spec = null;
			}
		}
		
		attributeName = name.trim();
	}

	@Override
	public void endElement(PlayerCharacter pc, String name, String body) {
		
		if(name.equals("Power")) {
			power = null;
			cPower = null;
		} else if(name.equals("Weapon")) {
			weapon = null;
		}
		
		if(body == null)
			body = "";
		body = body.trim();
		
		if(spec != null) {
			Logger.debug(LOG_TAG, String.format("Parsing specific %s, value=%s", spec, body));
			switch(spec) {
				case ActionType:
					power.setAction(PowerActions.forName(body.trim()));
					break;
					
				case Display:
					power.setDisplay(body);
					break;
					
				case Flavor:
					power.setFlavor(body);
					break;
					
				case Level:
					try {
						power.setLevel(Integer.parseInt(body));
					} catch(NumberFormatException nfex) {
						Logger.info(LOG_TAG, String.format("No level for power %s", power.getName()));
					}
					break;
					
				case PowerType:
					power.setType(PowerTypes.forName(body));
					break;
					
				case PowerUsage:
					power.setUsage(PowerUsages.forName(body));
					break;	
			}
			spec = null;
		} else if(attributeName != null) {
			if(attributeName.equals("Keywords")) {
				parseKeywords(body);
			} else {
				specific.add(new PowerSpecificAttribute(attributeName, body));
			}
			attributeName = null;
		} else if(weapon != null) {
			parseWeapon(name, body, pc);
		}
	}

	private void parseWeapon(String name, String body, PlayerCharacter pc) {
		body = body.trim();
		WeaponElements we = null;
		try {
			we = WeaponElements.valueOf(name);
		} catch(IllegalArgumentException iaex) {}
		if(we != null) {
			switch(we) {
				case AttackBonus:
					weapon.setAttackBonus(Integer.parseInt(body));
					break;
					
				case AttackStat:
					weapon.setAttackStat(pc.getStats().get(body));
					break;
					
				case Damage:
					weapon.setDamage(new Dice(body));
					break;
					
				case DamageComponents:
					for(String line : body.split("\\n")) {
						line = line.trim();
						if(line.length() > 0) 
							weapon.addDamageComponent(line.trim());
					}
					break;
					
				case Defense:
					weapon.setDefense(body);
					break;
					
				case HitComponents:
					for(String line : body.split("\\n")) {
						line = line.trim();
						if(line.length() > 0) 
							weapon.addHitComponent(line.trim());
					}
					break;
			}
		}
	}

	private void parseKeywords(String keywords) {
		String [] parts = keywords.split(";");
		for(String p : parts) {
			String [] keys = p.split(",| or ");
			boolean isAlt = p.contains(" or ");
			for(String k : keys) {
				PowerKeywords keyword = PowerKeywords.forName(k.trim());
				if(keyword == null) {
					throw new IllegalArgumentException(String.format("Cannot find keyword %s. (%s)", k, keywords));
				} 
				specific.add(keyword, isAlt);
			}
		}
	}
}
