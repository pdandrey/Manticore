package com.ncgeek.android.manticore.activities;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;

import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.R.id;
import com.ncgeek.android.manticore.http.HttpCompendiumRepository;
import com.ncgeek.manticore.rules.Rule;
import com.ncgeek.manticore.rules.RuleTypes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_sheet);
        
        ManticoreStatus.initialize(this);
        
        Intent i = new Intent(this, LoadCharacter.class);
        this.startActivity(i);
        
        //Intent i = new Intent(Intent.ACTION_VIEW);
        //i.setClassName("com.ncgeek.android.manticore", "Test");
        //this.startActivity(i);
        
//        File dir = Environment.getExternalStorageDirectory();
//        File dirManticore = new File(dir, "Manticore/cache/");
//        File testFile = new File(dirManticore, "test.html");
//        try {
//        	boolean success = dirManticore.mkdirs();
//        	success = testFile.createNewFile();
//        	int i = success ? 0 : 1;
//        	i++;
//        } catch(Exception ioex) {
//        	Log.e("manticore main", "create error", ioex);
//        }
//        HttpCompendiumRepository repos = new HttpCompendiumRepository("", "");
//
//        Rule r = new Rule("Acrobatics", RuleTypes.forName("Skill"), "ID_FMP_SKILL_1", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=1", "rules-legal");
//        repos.getItem(r);
//        repos.getCharacterList();
    }
    
    private void old() {
    	HttpCompendiumRepository repos = new HttpCompendiumRepository("", "");
    	
        repos.cacheRule(new Rule("Acrobatics", RuleTypes.forName("Skill"), "ID_FMP_SKILL_1", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=1", "rules-legal"));
        repos.cacheRule(new Rule("Arcana", RuleTypes.forName("Skill"), "ID_FMP_SKILL_2", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=2", "rules-legal"));
        repos.cacheRule(new Rule("Bluff", RuleTypes.forName("Skill"), "ID_FMP_SKILL_3", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=3", "rules-legal"));
        repos.cacheRule(new Rule("Diplomacy", RuleTypes.forName("Skill"), "ID_FMP_SKILL_6", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=6", "rules-legal"));
        repos.cacheRule(new Rule("Dungeoneering", RuleTypes.forName("Skill"), "ID_FMP_SKILL_7", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=7", "rules-legal"));
        repos.cacheRule(new Rule("Endurance", RuleTypes.forName("Skill"), "ID_FMP_SKILL_8", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=8", "rules-legal"));
        repos.cacheRule(new Rule("Heal", RuleTypes.forName("Skill"), "ID_FMP_SKILL_9", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=9", "rules-legal"));
        repos.cacheRule(new Rule("History", RuleTypes.forName("Skill"), "ID_FMP_SKILL_11", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=11", "rules-legal"));
        repos.cacheRule(new Rule("Insight", RuleTypes.forName("Skill"), "ID_FMP_SKILL_13", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=13", "rules-legal"));
        repos.cacheRule(new Rule("Intimidate", RuleTypes.forName("Skill"), "ID_FMP_SKILL_14", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=14", "rules-legal"));
        repos.cacheRule(new Rule("Nature", RuleTypes.forName("Skill"), "ID_FMP_SKILL_16", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=16", "rules-legal"));
        repos.cacheRule(new Rule("Perception", RuleTypes.forName("Skill"), "ID_FMP_SKILL_17", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=17", "rules-legal"));
        repos.cacheRule(new Rule("Religion", RuleTypes.forName("Skill"), "ID_FMP_SKILL_18", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=18", "rules-legal"));
        repos.cacheRule(new Rule("Stealth", RuleTypes.forName("Skill"), "ID_FMP_SKILL_20", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=20", "rules-legal"));
        repos.cacheRule(new Rule("Streetwise", RuleTypes.forName("Skill"), "ID_FMP_SKILL_21", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=21", "rules-legal"));
        repos.cacheRule(new Rule("Thievery", RuleTypes.forName("Skill"), "ID_FMP_SKILL_23", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=23", "rules-legal"));
        repos.cacheRule(new Rule("Athletics", RuleTypes.forName("Skill"), "ID_FMP_SKILL_27", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=27", "rules-legal"));
        repos.cacheRule(new Rule("Human", RuleTypes.forName("Race"), "ID_FMP_RACE_7", "http://www.wizards.com/dndinsider/compendium/race.aspx?id=7", "rules-legal"));
        repos.cacheRule(new Rule("Arcane Reserves", RuleTypes.forName("Feat"), "ID_FMP_FEAT_1116", "http://www.wizards.com/dndinsider/compendium/feat.aspx?id=1116", "rules-legal"));
        repos.cacheRule(new Rule("Booming Blade", RuleTypes.forName("Power"), "ID_FMP_POWER_1725", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=1725", "rules-legal"));
        repos.cacheRule(new Rule("Swordmage", RuleTypes.forName("Class"), "ID_FMP_CLASS_53", "http://www.wizards.com/dndinsider/compendium/class.aspx?id=53", "rules-legal"));
        repos.cacheRule(new Rule("Aegis of Ensnarement", RuleTypes.forName("Power"), "ID_FMP_POWER_5736", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=5736", "rules-legal"));
        repos.cacheRule(new Rule("Sword Burst", RuleTypes.forName("Power"), "ID_FMP_POWER_1710", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=1710", "rules-legal"));
        repos.cacheRule(new Rule("Lightning Lure", RuleTypes.forName("Power"), "ID_FMP_POWER_2078", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=2078", "rules-legal"));
        repos.cacheRule(new Rule("Aegis Vitality", RuleTypes.forName("Feat"), "ID_FMP_FEAT_2262", "http://www.wizards.com/dndinsider/compendium/feat.aspx?id=2262", "rules-legal"));
        repos.cacheRule(new Rule("Fox's Feint", RuleTypes.forName("Power"), "ID_FMP_POWER_3892", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=3892", "rules-legal"));
        repos.cacheRule(new Rule("Whirling Blade", RuleTypes.forName("Power"), "ID_FMP_POWER_3329", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=3329", "rules-legal"));
        repos.cacheRule(new Rule("Intelligent Blademaster", RuleTypes.forName("Feat"), "ID_FMP_FEAT_613", "http://www.wizards.com/dndinsider/compendium/feat.aspx?id=613", "rules-legal"));
        repos.cacheRule(new Rule("Fear No Elements", RuleTypes.forName("Power"), "ID_FMP_POWER_3332", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=3332", "rules-legal"));
        repos.cacheRule(new Rule("Incendiary Sword", RuleTypes.forName("Power"), "ID_FMP_POWER_4798", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=4798", "rules-legal"));
        repos.cacheRule(new Rule("Longsword", RuleTypes.forName("Weapon"), "ID_FMP_WEAPON_19", "http://www.wizards.com/dndinsider/compendium/item.aspx?fid=19&ftype=3", "rules-legal"));
        repos.cacheRule(new Rule("Vigilant Blade +1", RuleTypes.forName("Magic Item"), "ID_FMP_MAGIC_ITEM_8286", "http://www.wizards.com/dndinsider/compendium/item.aspx?fid=8286&ftype=1", "rules-legal"));
        repos.cacheRule(new Rule("Leather Armor", RuleTypes.forName("Armor"), "ID_FMP_ARMOR_2", "http://www.wizards.com/dndinsider/compendium/item.aspx?fid=2&ftype=2", "rules-legal"));
        repos.cacheRule(new Rule("Shaper's Armor +1", RuleTypes.forName("Magic Item"), "ID_FMP_MAGIC_ITEM_9597", "http://www.wizards.com/dndinsider/compendium/item.aspx?fid=9597&ftype=1", "rules-legal"));
        repos.cacheRule(new Rule("Amulet of Double Fortune +1", RuleTypes.forName("Magic Item"), "ID_FMP_MAGIC_ITEM_6856", "http://www.wizards.com/dndinsider/compendium/item.aspx?fid=6856&ftype=1", "rules-legal"));
        repos.cacheRule(new Rule("Leather Armor", RuleTypes.forName("Armor"), "ID_FMP_ARMOR_2", "http://www.wizards.com/dndinsider/compendium/item.aspx?fid=2&ftype=2", "rules-legal"));
        repos.cacheRule(new Rule("Adventurer's Kit", RuleTypes.forName("Gear"), "ID_FMP_GEAR_1", "http://www.wizards.com/dndinsider/compendium/item.aspx?fid=1&ftype=4", "rules-legal"));
        repos.cacheRule(new Rule("Longsword", RuleTypes.forName("Weapon"), "ID_FMP_WEAPON_19", "http://www.wizards.com/dndinsider/compendium/item.aspx?fid=19&ftype=3", "rules-legal"));
        repos.cacheRule(new Rule("Acrobatics", RuleTypes.forName("Skill"), "ID_FMP_SKILL_1", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=1", "rules-legal"));
        repos.cacheRule(new Rule("Arcana", RuleTypes.forName("Skill"), "ID_FMP_SKILL_2", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=2", "rules-legal"));
        repos.cacheRule(new Rule("Bluff", RuleTypes.forName("Skill"), "ID_FMP_SKILL_3", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=3", "rules-legal"));
        repos.cacheRule(new Rule("Diplomacy", RuleTypes.forName("Skill"), "ID_FMP_SKILL_6", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=6", "rules-legal"));
        repos.cacheRule(new Rule("Dungeoneering", RuleTypes.forName("Skill"), "ID_FMP_SKILL_7", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=7", "rules-legal"));
        repos.cacheRule(new Rule("Endurance", RuleTypes.forName("Skill"), "ID_FMP_SKILL_8", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=8", "rules-legal"));
        repos.cacheRule(new Rule("Heal", RuleTypes.forName("Skill"), "ID_FMP_SKILL_9", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=9", "rules-legal"));
        repos.cacheRule(new Rule("History", RuleTypes.forName("Skill"), "ID_FMP_SKILL_11", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=11", "rules-legal"));
        repos.cacheRule(new Rule("Insight", RuleTypes.forName("Skill"), "ID_FMP_SKILL_13", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=13", "rules-legal"));
        repos.cacheRule(new Rule("Intimidate", RuleTypes.forName("Skill"), "ID_FMP_SKILL_14", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=14", "rules-legal"));
        repos.cacheRule(new Rule("Nature", RuleTypes.forName("Skill"), "ID_FMP_SKILL_16", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=16", "rules-legal"));
        repos.cacheRule(new Rule("Perception", RuleTypes.forName("Skill"), "ID_FMP_SKILL_17", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=17", "rules-legal"));
        repos.cacheRule(new Rule("Religion", RuleTypes.forName("Skill"), "ID_FMP_SKILL_18", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=18", "rules-legal"));
        repos.cacheRule(new Rule("Stealth", RuleTypes.forName("Skill"), "ID_FMP_SKILL_20", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=20", "rules-legal"));
        repos.cacheRule(new Rule("Streetwise", RuleTypes.forName("Skill"), "ID_FMP_SKILL_21", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=21", "rules-legal"));
        repos.cacheRule(new Rule("Thievery", RuleTypes.forName("Skill"), "ID_FMP_SKILL_23", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=23", "rules-legal"));
        repos.cacheRule(new Rule("Athletics", RuleTypes.forName("Skill"), "ID_FMP_SKILL_27", "http://www.wizards.com/dndinsider/compendium/skill.aspx?id=27", "rules-legal"));
        repos.cacheRule(new Rule("Human", RuleTypes.forName("Race"), "ID_FMP_RACE_7", "http://www.wizards.com/dndinsider/compendium/race.aspx?id=7", "rules-legal"));
        repos.cacheRule(new Rule("Sacrifice to Caiphon", RuleTypes.forName("Feat"), "ID_FMP_FEAT_744", "http://www.wizards.com/dndinsider/compendium/feat.aspx?id=744", "rules-legal"));
        repos.cacheRule(new Rule("Hand of Blight", RuleTypes.forName("Power"), "ID_FMP_POWER_12887", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=12887", "rules-legal"));
        repos.cacheRule(new Rule("Warlock", RuleTypes.forName("Class"), "ID_FMP_CLASS_7", "http://www.wizards.com/dndinsider/compendium/class.aspx?id=7", "rules-legal"));
        repos.cacheRule(new Rule("Eldritch Blast", RuleTypes.forName("Power"), "ID_FMP_POWER_1333", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=1333", "rules-legal"));
        repos.cacheRule(new Rule("Hellish Rebuke", RuleTypes.forName("Power"), "ID_FMP_POWER_1458", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=1458", "rules-legal"));
        repos.cacheRule(new Rule("Dark One's Blessing", RuleTypes.forName("Power"), "ID_FMP_POWER_2095", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=2095", "rules-legal"));
        repos.cacheRule(new Rule("Warlock's Curse", RuleTypes.forName("Power"), "ID_FMP_POWER_5597", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=5597", "rules-legal"));
        repos.cacheRule(new Rule("Bloodied Boon", RuleTypes.forName("Feat"), "ID_FMP_FEAT_2759", "http://www.wizards.com/dndinsider/compendium/feat.aspx?id=2759", "rules-legal"));
        repos.cacheRule(new Rule("Diabolic Grasp", RuleTypes.forName("Power"), "ID_FMP_POWER_1460", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=1460", "rules-legal"));
        repos.cacheRule(new Rule("Flames of Phlegethos", RuleTypes.forName("Power"), "ID_FMP_POWER_1323", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=1323", "rules-legal"));
        repos.cacheRule(new Rule("Telekinetic Grasp", RuleTypes.forName("Power"), "ID_FMP_POWER_12408", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=12408", "rules-legal"));
        repos.cacheRule(new Rule("Wasteland Fury", RuleTypes.forName("Power"), "ID_FMP_POWER_12360", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=12360", "rules-legal"));
        repos.cacheRule(new Rule("Arcane Defiling", RuleTypes.forName("Power"), "ID_FMP_POWER_12399", "http://www.wizards.com/dndinsider/compendium/power.aspx?id=12399", "rules-legal"));
        repos.cacheRule(new Rule("Leather Armor", RuleTypes.forName("Armor"), "ID_FMP_ARMOR_2", "http://www.wizards.com/dndinsider/compendium/item.aspx?fid=2&ftype=2", "rules-legal"));
        repos.cacheRule(new Rule("Adventurer's Kit", RuleTypes.forName("Gear"), "ID_FMP_GEAR_1", "http://www.wizards.com/dndinsider/compendium/item.aspx?fid=1&ftype=4", "rules-legal"));
        repos.cacheRule(new Rule("Rod Implement", RuleTypes.forName("Gear"), "ID_FMP_GEAR_12", "http://www.wizards.com/dndinsider/compendium/item.aspx?fid=12&ftype=4", "rules-legal"));

        
    }
}