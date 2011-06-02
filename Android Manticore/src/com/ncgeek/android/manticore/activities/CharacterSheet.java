package com.ncgeek.android.manticore.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.MessageTypes;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.adapters.GalleryMenuAdapter;
import com.ncgeek.android.manticore.database.DatabaseRepository;
import com.ncgeek.android.manticore.menus.GalleryMenuItem;
import com.ncgeek.android.manticore.threads.LoadCharacterThread;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.android.manticore.widgets.GalleryMenu;
import com.ncgeek.android.manticore.widgets.LabelBar;
import com.ncgeek.android.manticore.widgets.StatView;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageView;
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
	
	private android.view.View.OnClickListener ContextMenuClick = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View v) {
			v.performLongClick();
		}
	};
	
	private android.view.View.OnLongClickListener ExplainDefenseLongClick = new android.view.View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			Intent i = new Intent(CharacterSheet.this, DetailsView.class);
	        i.setAction(Intent.ACTION_VIEW);
	        i.addCategory(Intent.CATEGORY_DEFAULT);
	        i.putExtra("item", _pc.getStats().get((String)v.getTag()));
	        startActivity(i);
			return true;
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		ManticoreStatus.initialize(this);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_sheet);
        
        View v = findViewById(R.id.charactersheet_hpbar);
        v.setOnClickListener(ContextMenuClick);
        registerForContextMenu(v);
        
        v = findViewById(R.id.charactersheet_surgebar);
        v.setOnClickListener(ContextMenuClick);
        registerForContextMenu(v);
        
        v = findViewById(R.id.charactersheet_llActionPoints);
        v.setOnClickListener(ContextMenuClick);
        registerForContextMenu(v);
        
        findViewById(R.id.charactersheet_llDefenseAC).setOnLongClickListener(ExplainDefenseLongClick);
        findViewById(R.id.charactersheet_llDefenseFort).setOnLongClickListener(ExplainDefenseLongClick);
        findViewById(R.id.charactersheet_llDefenseReflex).setOnLongClickListener(ExplainDefenseLongClick);
        findViewById(R.id.charactersheet_llDefenseWill).setOnLongClickListener(ExplainDefenseLongClick);
        findViewById(R.id.charactersheet_llStatSpeed).setOnLongClickListener(ExplainDefenseLongClick);
        findViewById(R.id.charactersheet_llStatInitiative).setOnLongClickListener(ExplainDefenseLongClick);
        
        LabelBar hp = (LabelBar)findViewById(R.id.charactersheet_hpbar);
        hp.addChange(50, "Bloodied", Color.RED, getResources().getDrawable(R.drawable.hp_bar_bloodied));
	 }
	 
	@Override
	public void onStart() {
		super.onStart();
		
		Logger.debug(LOG_TAG, "CharacterSheet.onStart");
		
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
					LinearLayout ll2 = new LinearLayout(this);
					ll2.setOrientation(LinearLayout.HORIZONTAL);
					ll2.addView(chk);
					TextView tvSurgeValue = new TextView(this);
					tvSurgeValue.setText(String.format("(+%d hp)", _pc.getHP().getSurgeValue()));
					tvSurgeValue.setTextColor(Color.BLACK);
					ll2.addView(tvSurgeValue);
					ll.addView(ll2);
					txt.setSelectAllOnFocus(true);
					txt.setInputType(InputType.TYPE_CLASS_NUMBER);
					builder.setView(ll);
					
					builder.setPositiveButton("Heal", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								String s = txt.getText().toString();
								if(s == null)
									s = "0";
								s = s.trim();
								if(s.length() == 0)
									s = "0";
								
								int healing = Integer.parseInt(s);
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
							CharacterSheet.this.dismissDialog(DIALOG_HEALING);
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
			case R.id.charactersheet_hpbar:
			case R.id.charactersheet_surgebar:
				inflater.inflate(R.menu.charactersheet_hitpoints, menu);
				menu.setGroupVisible(R.id.charactersheet_mnugrpDeathSave, _pc.getHP().isBleedingOut());
				break;
				
			case R.id.charactersheet_llActionPoints:
				inflater.inflate(R.menu.charactersheet_actionpoints, menu);
				menu.setGroupEnabled(R.id.charactersheet_mnugrpActionPoints, _pc.getActionPoints() > 0);
				break;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		switch(item.getGroupId()) {
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
		Logger.error(LOG_TAG, menu.getClass().getName());
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.charactersheet, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId()) {
		    case R.id.charactersheet_mnuPreferences:
		        Intent i = new Intent(this, Preferences.class);
		        startActivity(i);
		        return true;
		    case R.id.charactersheet_mnuFullRest:
		    	_pc.fullRest();
		    	update();
		    	return true;
		    case R.id.charactersheet_mnuShortRest:
		    	_pc.shortRest();
		    	update();
		    	return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void update() {
		if(_pc == null)
			return;
		
		if(_pc.getPortrait() != null) {
			Bitmap bitmap = (Bitmap)_pc.getPortraitBitmap();
			
			if(bitmap == null) {
				try {
					
					URL url = new URL(String.format(_pc.getPortrait(), prefs.CharacterBuilderVersion()));
					
					String cachePortraitFilename = null;
					File cacheDir = null; 
					
					if(prefs.cacheImages() && Utility.isExternalAvailable()) {
						cacheDir = new File(ManticoreStatus.getExternalStorageDirectory(), "cache/portraits/");
						if(!cacheDir.exists() && !cacheDir.mkdirs()) {
							Logger.error(LOG_TAG, "Failed to create portrait cache directory");
						} else {
							Pattern regexFilename = Pattern.compile("\\d+\\.png$");
							Matcher m = regexFilename.matcher(_pc.getPortrait());
							if(m.find()) {
								File portrait = new File(cacheDir, m.group());
								if(portrait.exists()) {
									bitmap = BitmapFactory.decodeFile(portrait.toString());
								} else {
									cachePortraitFilename = portrait.toString();
								}
							}
						}
					}
					
					if(bitmap == null) {
						bitmap = BitmapFactory.decodeStream(url.openStream());
					}
					
					if(cachePortraitFilename != null && cacheDir != null) {
						FileOutputStream fos = new FileOutputStream(cachePortraitFilename);
						bitmap.compress(CompressFormat.PNG, 100, fos);
						fos.close();
					}
					
					_pc.setPortraitBitmap(bitmap);
				} catch(Exception ex) {
					Logger.error(LOG_TAG, "Error loading portrait", ex);
				}
			}
			
			ImageView iv = (ImageView)findViewById(R.id.charactersheet_img);
			iv.setImageBitmap(bitmap);
						
			//((GalleryMenu)findViewById(R.id.mainmenu)).setMenuItemIcon(0, new BitmapDrawable(bitmap));
		}
		
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
	
		((StatView)findViewById(R.id.charactersheet_llDefenseAC)).setStat(_pc.getStats().get("AC"));
//		updateDefenses("AC", R.id.charactersheet_txtAC);
		updateDefenses("Fortitude", R.id.charactersheet_txtFort);
		updateDefenses("Reflex", R.id.charactersheet_txtReflex);
		updateDefenses("Will", R.id.charactersheet_txtWill);
		
		updateDefenses("Speed", R.id.charactersheet_txtSpeed);
		updateDefenses("Initiative", R.id.charactersheet_txtInitiative);
		
		updateActionPoints();
		updateHP();
		
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
		int mod = s.getModifier();
		String sMod = mod + "";
		if(mod >= 0)
			sMod = "+" + mod;
		txt.setText(sMod);
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
		
		LabelBar hpbar = (LabelBar)findViewById(R.id.charactersheet_hpbar);
		LabelBar surgebar = (LabelBar)findViewById(R.id.charactersheet_surgebar);
		
		hpbar.setMax(hp.getMax());
		hpbar.setCurrent(hp.getCurrent());
		hpbar.setTemporary(hp.getTemp());
		
		surgebar.setMax(hp.getTotalSurges());
		surgebar.setCurrent(hp.getRemainingSurges());
		surgebar.setTemporary(0);
		
		/*
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
		*/
	}
	
	private void updateActionPoints() {
		TextView txt = (TextView)findViewById(R.id.charactersheet_txtActionPoints);
		txt.setText(_pc.getActionPoints() + "");
	}
}
