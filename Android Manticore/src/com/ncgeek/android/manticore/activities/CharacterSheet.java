package com.ncgeek.android.manticore.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.MessageTypes;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.adapters.FeatListAdapter;
import com.ncgeek.android.manticore.adapters.ItemListAdapter;
import com.ncgeek.android.manticore.adapters.PowerListAdapter;
import com.ncgeek.android.manticore.adapters.RitualListAdapter;
import com.ncgeek.android.manticore.widgets.GalleryMenu;
import com.ncgeek.android.manticore.partial.ListPartial;
import com.ncgeek.android.manticore.partial.Partial;
import com.ncgeek.android.manticore.partial.PartyPartial;
import com.ncgeek.android.manticore.partial.SkillPartial;
import com.ncgeek.android.manticore.partial.StatPartial;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.android.manticore.widgets.LabelBar;
import com.ncgeek.manticore.Ritual;
import com.ncgeek.manticore.character.Feat;
import com.ncgeek.manticore.character.HitPoints;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.util.Logger;
import com.ncgeek.manticore.util.Tuple;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class CharacterSheet extends Activity {
	
	private final static String LOG_TAG = "Character Sheet";
	private final static int DIALOG_LOADING = 1;
	private final static int DIALOG_DAMAGE = 2;
	private final static int DIALOG_HEALING = 3;
	
	private ProgressDialog dlgLoading;
	private PlayerCharacter _pc;
	//private LoadCharacterThread thdLoad;
	private AlertDialog dlgDamage;
	private AlertDialog dlgHealing;
	private ManticorePreferences prefs;
	
	private Partial currentPartial;
	private HashMap<Integer,Tuple<Partial,Integer>> mapPartials;
	
	private FeatListAdapter adpFeats;
	private RitualListAdapter adpRituals;
	private ItemListAdapter adpItems;
	private ItemListAdapter adpEquipment;
	private PowerListAdapter adpPowerListAdapter;
	
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
	
	private final android.view.View.OnClickListener ContextMenuClick = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View v) {
			v.performLongClick();
		}
	};
	
	public android.view.View.OnClickListener getContextMenuClickListener() {
		return ContextMenuClick;
	}
	
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
        
        LabelBar hp = (LabelBar)findViewById(R.id.charactersheet_hpbar);
        hp.addChange(50, "Bloodied", Color.RED, getResources().getDrawable(R.drawable.hp_bar_bloodied));
        
        final GalleryMenu gallery = (GalleryMenu)findViewById(R.id.charactersheet_mainmenu);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				Logger.debug(LOG_TAG, String.format("Menu click: %x (%s)", id, gallery.getMenuItem(position).getTitle()));
				ViewAnimator va = (ViewAnimator)findViewById(R.id.charactersheet_children);
				Tuple<Partial,Integer> t = mapPartials.get((int)id);
				if(t != null) {
					currentPartial = t.getItem1();
					currentPartial.update();
					va.setDisplayedChild(t.getItem2());
				}
			}
		});
        
        mapPartials = new HashMap<Integer, Tuple<Partial,Integer>>(); 
        
        currentPartial = new StatPartial(this);
        addPartial(currentPartial, R.id.mainmenu_mnuCharacter);
        addPartial(new SkillPartial(this), R.id.mainmenu_mnuSkills);
        addPartial(new ListPartial(this, adpFeats = new FeatListAdapter(this, R.layout.feat_list_item)), R.id.mainmenu_mnuFeats);
        addPartial(new ListPartial(this, adpRituals = new RitualListAdapter(this, R.layout.ritual_listitem)), R.id.mainmenu_mnuRituals);
        addPartial(new ListPartial(this, adpItems = new ItemListAdapter(this, true)), R.id.mainmenu_mnuBackpack);
        addPartial(new ListPartial(this, adpEquipment = new ItemListAdapter(this, false)), R.id.mainmenu_mnuEquipment);
        addPartial(new ListPartial(this, adpPowerListAdapter = new PowerListAdapter(this)), R.id.mainmenu_mnuPowers);
        addPartial(new PartyPartial(this), R.id.mainmenu_mnuParty);
        
        
	 }
	
	private void addPartial(Partial partial, int menuID) {
		ViewAnimator va = (ViewAnimator)findViewById(R.id.charactersheet_children);
		Logger.debug(LOG_TAG, String.format("Adding partial: id=%x, class=%s, vaPosition=%s", menuID, partial.getClass().getSimpleName(), va.getChildCount()));
		mapPartials.put(menuID, new Tuple<Partial, Integer>(partial, va.getChildCount()));
        va.addView(partial.getView());
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
	    
//	    thdLoad = LoadCharacterThread.getThread(f, dialogHandler);
////	    thdLoad.setRepository(new DatabaseRepository(this));
//	    
//	    if(!thdLoad.hasStarted()) {
//	    	showDialog(DIALOG_LOADING);
//	    } else if(thdLoad.isFinished()) {
//	    	_pc = thdLoad.getCharacter();
//	    	update();
//	    }
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		for(Tuple<Partial,Integer> t : mapPartials.values()) {
			t.getItem1().onPause();
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
//		if(thdLoad == null)
//			thdLoad = LoadCharacterThread.getThread();
//        switch(id) {
//	        case DIALOG_LOADING:
//	        	if(!thdLoad.hasStarted())
//	        		thdLoad.start();
//	        	else
//	        		thdLoad.setHandler(dialogHandler);
//	            break;
//        }
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
				
			default:
				inflater.inflate(currentPartial.getContextMenuID(), menu);
				currentPartial.setupContextMenu(menu);
				break;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		if(item.getGroupId() == R.id.charactersheet_mnugrpHitpoints
				|| item.getGroupId() == R.id.charactersheet_mnugrpDeathSave) {
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
		} else {
			return currentPartial.onContextItemSelected(item);
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
		/*
		if(_pc.getPortrait() != null) {
			Bitmap bitmap = (Bitmap)_pc.getPortraitBitmap();
			
			if(bitmap == null) {
				//String.format(_pc.getPortrait(), prefs.CharacterBuilderVersion())
				bitmap = Utility.getPortrait(_pc.getPortrait(), prefs);
				_pc.setPortraitBitmap(bitmap);
			}
			
			ImageView iv = (ImageView)findViewById(R.id.charactersheet_img);
			iv.setImageBitmap(bitmap);
						
			//((GalleryMenu)findViewById(R.id.mainmenu)).setMenuItemIcon(0, new BitmapDrawable(bitmap));
		}
		*/
		TextView txtName = (TextView)findViewById(R.id.charactersheet_txtName);
		txtName.setText(_pc.getName());
		
		
		TextView txtLevel = (TextView)findViewById(R.id.charactersheet_txtLevel);
		txtLevel.setText(_pc.getLevel() + "");
		
		TextView txtRace = (TextView)findViewById(R.id.charactersheet_txtRace);
		txtRace.setText(_pc.getRace());
		
		TextView txtClass = (TextView)findViewById(R.id.charactersheet_txtClass);
		txtClass.setText(_pc.getHeroicClass());
		
		updateHP();
		currentPartial.update();
		
		if(adpFeats.getCount() == 0)
			adpFeats.add(_pc.getFeats());
		if(adpRituals.getCount() == 0)
			adpRituals.add(_pc.getRituals());
		if(adpItems.getCount() == 0)
			adpItems.setInventory(_pc.getEquipment());
		if(adpEquipment.getCount() == 0)
			adpEquipment.setInventory(_pc.getEquipment());
		if(adpPowerListAdapter.getCount() == 0)
			adpPowerListAdapter.setPower(_pc.getPowers());
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
}
