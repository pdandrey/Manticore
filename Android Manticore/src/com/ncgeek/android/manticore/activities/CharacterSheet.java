package com.ncgeek.android.manticore.activities;

import java.io.File;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.MessageTypes;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.database.DatabaseRepository;
import com.ncgeek.android.manticore.threads.LoadCharacterThread;
import com.ncgeek.manticore.character.HitPoints;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.stats.Stat;
import com.ncgeek.manticore.util.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CharacterSheet extends Activity {
	
	private final static String LOG_TAG = "Character Sheet";
	private final static int DIALOG_LOADING = 1;
	private final static int DIALOG_DAMAGE = 2;
	private final static int DIALOG_HEALING = 3;
	
	private ProgressDialog dlgLoading;
	private PlayerCharacter _pc;
	private LoadCharacterThread thdLoad;
	private AlertDialog dlgDamage;
	private AlertDialog dlgHealing;
	private ManticorePreferences prefs;
	
	private Handler dialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case MessageTypes.MESSAGE_FINISHED: 
					_pc = (PlayerCharacter)msg.obj;
					ManticoreStatus.setPC(_pc);
					update();
					break;
					
				case MessageTypes.MESSAGE_TIME_TAKEN:
					break;
					
				case MessageTypes.MESSAGE_ERROR:
					break;
					
				case MessageTypes.MESSAGE_LOADING_SECTION_NAME:
					dlgLoading.setMessage("Loading " + (String)msg.obj);
					return;
			}
			dlgLoading.dismiss();
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		ManticoreStatus.initialize(this);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_sheet);
        
        registerForContextMenu(findViewById(R.id.charactersheet_frameDefenses));
        registerForContextMenu(findViewById(R.id.charactersheet_frameHP));
        registerForContextMenu(findViewById(R.id.charactersheet_frameActionPoints));
	 }
	 
	@Override
	public void onStart() {
		super.onStart();
		
		if(prefs == null)
			prefs = new ManticorePreferences(this);
		
		_pc = ManticoreStatus.getPC();
		
		Intent i = getIntent();
	    Uri data = i.getData();
	    
	    if(data == null && _pc == null) {
	    	Logger.debug(LOG_TAG, "No Uri found");
	    	this.finish();
	    	return;
	    }
	    
	    if(_pc != null && data == null) {
	    	update();
	    	return;
	    }
	    
	    File f = new File(data.getEncodedPath());
	    File old = ManticoreStatus.getPCFile();
	    
	    if(f.equals(old)) {
	    	update();
	    	return;
	    }
	    
	    ManticoreStatus.setPCFile(f);
	    
	    thdLoad = LoadCharacterThread.getThread(f, dialogHandler);
	    thdLoad.setRepository(new DatabaseRepository(this));
	    
	    if(!thdLoad.hasStarted()) {
	    	showDialog(DIALOG_LOADING);
	    } else if(thdLoad.isFinished()) {
	    	_pc = thdLoad.getCharacter();
	    	update();
	    }
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
			case DIALOG_LOADING:
				dlgLoading = new ProgressDialog(this);
				dlgLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dlgLoading.setMessage("Loading...");
				return dlgLoading;
				
			case DIALOG_DAMAGE:
				if(dlgDamage == null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Damage taken:");
					final EditText txt = new EditText(this);
					txt.setSelectAllOnFocus(true);
					txt.setInputType(InputType.TYPE_CLASS_NUMBER);
					builder.setView(txt);
					
					builder.setPositiveButton("OK", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								int damage = Integer.parseInt(txt.getText().toString());
								_pc.getHP().takeDamage(damage);
								updateHP();
							} catch(Exception ex) {
								Logger.error(LOG_TAG, "Error updating HP", ex);
							}
						}
					});
					
					builder.setNegativeButton("Cancel", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							CharacterSheet.this.dismissDialog(DIALOG_DAMAGE);
						}
					});
					
					dlgDamage = builder.create();
				}
				
				return dlgDamage;
				
			case DIALOG_HEALING:
				if(dlgHealing == null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Healing received:");
					final EditText txt = new EditText(this);
					final CheckBox chk = new CheckBox(this);
					chk.setText("Use a Healing Surge");
					LinearLayout ll = new LinearLayout(this);
					ll.setOrientation(LinearLayout.VERTICAL);
					ll.addView(txt);
					ll.addView(chk);
					txt.setSelectAllOnFocus(true);
					txt.setInputType(InputType.TYPE_CLASS_NUMBER);
					builder.setView(ll);
					
					builder.setPositiveButton("Heal", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								int healing = Integer.parseInt(txt.getText().toString());
								if(chk.isChecked()) {
									_pc.getHP().useSurge(healing);
								} else {
									_pc.getHP().heal(healing);
								}
								updateHP();
							} catch(Exception ex) {
								Logger.error(LOG_TAG, "Error updating HP", ex);
							}
						}
					});
					
					builder.setNeutralButton("Temp HP", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								int healing = Integer.parseInt(txt.getText().toString());
								_pc.getHP().setTemp(healing);
								updateHP();
							} catch(Exception ex) {
								Logger.error(LOG_TAG, "Error updating HP", ex);
							}
						}
					});
					
					builder.setNegativeButton("Cancel", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							CharacterSheet.this.dismissDialog(DIALOG_DAMAGE);
						}
					});
					
					dlgHealing = builder.create();
				}
				
				
				return dlgHealing;
				
			default:
				return null;
		}
	}
	
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
		if(thdLoad == null)
			thdLoad = LoadCharacterThread.getThread();
        switch(id) {
	        case DIALOG_LOADING:
	        	if(!thdLoad.hasStarted())
	        		thdLoad.start();
	        	else
	        		thdLoad.setHandler(dialogHandler);
	            break;
        }
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		
		switch(v.getId()) {
			case R.id.charactersheet_frameDefenses:
				inflater.inflate(R.menu.charactersheet_explain_defense, menu);
				break;
				
			case R.id.charactersheet_frameHP:
				inflater.inflate(R.menu.charactersheet_hitpoints, menu);
				menu.setGroupVisible(R.id.charactersheet_mnugrpDeathSave, _pc.getHP().isBleedingOut());
				break;
				
			case R.id.charactersheet_frameActionPoints:
				inflater.inflate(R.menu.charactersheet_actionpoints, menu);
				menu.setGroupEnabled(R.id.charactersheet_mnugrpActionPoints, _pc.getActionPoints() > 0);
				break;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		switch(item.getGroupId()) {
			case R.id.charactersheet_mnugrpExplainDefense:
				Intent i = new Intent(CharacterSheet.this, DefenseBreakdown.class);
		        i.setAction(Intent.ACTION_VIEW);
		        i.addCategory(Intent.CATEGORY_DEFAULT);
		        i.putExtra("defense", item.getTitle().toString());
		        startActivity(i);
		        return true;
		        
			case R.id.charactersheet_mnugrpHitpoints:
			case R.id.charactersheet_mnugrpDeathSave:
				switch(item.getItemId()){
					case R.id.charactersheet_mnuDamage:
						showDialog(DIALOG_DAMAGE);
						return true;
						
					case R.id.charactersheet_mnuHealing:
						showDialog(DIALOG_HEALING);
						return true;
						
					case R.id.charactersheet_mnuSurge:
						_pc.getHP().expendSurge();
						updateHP();
						return true;
						
					case R.id.charactersheet_mnuDeathSaveFail:
						_pc.getHP().failDeathSave();
						updateHP();
						return true;
				}
				break;
				
			case R.id.charactersheet_mnugrpActionPoints:
				_pc.useActionPoint();
				updateActionPoints();
				return true;
		}
		
		switch(item.getItemId()) {
			case R.id.charactersheet_mnuMilestone:
				_pc.milestone();
				updateActionPoints();
				return true;
		}
		
		return super.onContextItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.loadcharacter, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId()) {
		    case R.id.loadcharacter_mnuPreferences:
		        Intent i = new Intent(this, Preferences.class);
		        startActivity(i);
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	public void showContextMenu(View v) {
		v.performLongClick();
	}
	
	private void update() {
		if(_pc == null)
			return;
		
		TextView txtName = (TextView)findViewById(R.id.charactersheet_txtName);
		txtName.setText(_pc.getName());
		
		TextView txtLevel = (TextView)findViewById(R.id.charactersheet_txtLevel);
		txtLevel.setText(_pc.getLevel() + "");
		
		TextView txtRace = (TextView)findViewById(R.id.charactersheet_txtRace);
		txtRace.setText(_pc.getRace());
		
		TextView txtClass = (TextView)findViewById(R.id.charactersheet_txtClass);
		txtClass.setText(_pc.getHeroicClass());
		
		updateAbilityScores("STR", R.id.charactersheet_txtStr, R.id.charactersheet_txtStrMod);
		updateAbilityScores("Con", R.id.charactersheet_txtCon, R.id.charactersheet_txtConMod);
		updateAbilityScores("Dex", R.id.charactersheet_txtDex, R.id.charactersheet_txtDexMod);
		updateAbilityScores("Int", R.id.charactersheet_txtInt, R.id.charactersheet_txtIntMod);
		updateAbilityScores("Wis", R.id.charactersheet_txtWis, R.id.charactersheet_txtWisMod);
		updateAbilityScores("Cha", R.id.charactersheet_txtCha, R.id.charactersheet_txtChaMod);
		
		updateDefenses("AC", R.id.charactersheet_txtAC);
		updateDefenses("Fortitude", R.id.charactersheet_txtFort);
		updateDefenses("Reflex", R.id.charactersheet_txtReflex);
		updateDefenses("Will", R.id.charactersheet_txtWill);
		
		updateHP();
		
		updateActionPoints();
	}
	
	private void updateAbilityScores(String stat, int txtID, int txtModID) {
		Stat s = _pc.getStats().get(stat);
		TextView txt = (TextView)findViewById(txtID);
		
		int calc = s.getCalculatedValue();
		int abs = s.getAbsoluteValue();
		
		if(calc != abs)
			Logger.warn(LOG_TAG, String.format("%s %d != %d", stat, calc, abs));
		
		if(prefs.useCalculatedStats())
			txt.setText(calc + "");
		else
			txt.setText(abs + "");
		
		txt = (TextView)findViewById(txtModID);
		txt.setText(s.getModifier() + "");
	}
	
	private void updateDefenses(String stat, int txtID) {
		Stat s = _pc.getStats().get(stat);
		TextView txt = (TextView)findViewById(txtID);
		int calc = s.getCalculatedValue();
		int abs = s.getAbsoluteValue();
		
		if(calc != abs)
			Logger.warn(LOG_TAG, String.format("%s %d != %d", stat, calc, abs));
		
		if(prefs.useCalculatedStats())
			txt.setText(calc + "");
		else
			txt.setText(abs + "");
	}
	
	private void updateHP() {
		HitPoints hp = _pc.getHP();
		
		TextView txtHP = (TextView)findViewById(R.id.charactersheet_txtHP);
		TextView surges = (TextView)findViewById(R.id.charactersheet_txtSurges);
		
		surges.setText(hp.getRemainingSurges() + "");
		
		StringBuilder buf = new StringBuilder(hp.getCurrent() + "");
		if(hp.getTemp() > 0) {
			buf.append(" (+");
			buf.append(hp.getTemp());
			buf.append(")");
		}
		txtHP.setText(buf.toString());
		
		int background = R.drawable.hp_background;
		if(hp.isDead()) {
			background = R.drawable.hp_dead_background;
		} else if(hp.isBleedingOut()) {
			background = R.drawable.hp_dead_background;
		} else if(hp.isBloodied()) {
			background = R.drawable.hp_bloodied_background;
		}
		
		int failed = hp.getDeathSaves();
		findViewById(R.id.charactersheet_imgDeathSave1).setVisibility(failed > 0 ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.charactersheet_imgDeathSave2).setVisibility(failed > 1 ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.charactersheet_imgDeathSave3).setVisibility(failed > 2 ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.charactersheet_imgDead).setVisibility(failed > 2 ? View.VISIBLE : View.GONE);
		
		findViewById(R.id.charactersheet_frameHP).setBackgroundResource(background);
	}
	
	private void updateActionPoints() {
		TextView txt = (TextView)findViewById(R.id.charactersheet_txtActionPoints);
		txt.setText(_pc.getActionPoints() + "");
	}
}
