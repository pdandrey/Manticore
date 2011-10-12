package com.ncgeek.manticore.parsers;

import org.xml.sax.Attributes;

import com.ncgeek.manticore.Dice;
import com.ncgeek.manticore.character.CharacterPower;
import com.ncgeek.manticore.character.CharacterPower.PowerWeapon;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.powers.Power;
import com.ncgeek.manticore.util.Logger;

public class PowerHandler implements IElementHandler {

	private final String LOG_TAG = "Power Handler";
	
	private enum WeaponElements { AttackBonus, Damage, AttackStat, Defense, HitComponents, DamageComponents }
	
	private static final IElementHandler instance = new PowerHandler();
	public static final IElementHandler getInstance() { return instance; }
	
	private Power power;
	private CharacterPower cPower;
	private PowerWeapon weapon;
	private String attributeName;
	private boolean isSpecific;
	
	private PowerHandler() {
		weapon = null;
		power = null;
		cPower = null;
		isSpecific = false;
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
		
		isSpecific = true;
		
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
		
		if(isSpecific) {
			isSpecific = false;
			power.addSpecific(attributeName, body);
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
					if(body.length() > 0)
						weapon.setAttackStat(pc.getStats().get(body));
					break;
					
				case Damage:
					if(body.length() > 0)
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
}
