package com.ncgeek.android.manticore.activities;

import java.io.File;
import java.util.List;

import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.MessageTypes;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.adapters.CharacterArrayAdapter;
import com.ncgeek.android.manticore.adapters.CharacterFileInfo;
import com.ncgeek.android.manticore.threads.LoadFileListThread;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class LoadCharacter extends Activity {
	
	private static final String LOG_TAG = "Load Character";
	
	private enum CharacterSource { 
		SDCard(0),
		Internal(1),
		Wizards(2),
		URL(3);
		
		public static CharacterSource forID(int id) {
			for(CharacterSource cs : CharacterSource.values())
				if(cs._sourceID == id)
					return cs;
			return null;
		}
		private int _sourceID;
		CharacterSource(int id) { _sourceID = id; }
	}
	
	private static final int DIALOG_LOAD_CHARACTER_LIST = 0;
	
	private ProgressDialog dlgProgress;
	private LoadFileListThread thread;
	
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case MessageTypes.MESSAGE_TOTAL:
					dlgProgress.setMax(msg.arg1);
					dlgProgress.setProgress(0);
					break;
					
				case MessageTypes.MESSAGE_CURRENT:
					dlgProgress.setProgress(msg.arg1);
					break;
					
				case MessageTypes.MESSAGE_FINISHED:
					dlgProgress.dismiss();
					setFiles();
					break;
			}
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		ManticoreStatus.initialize(this);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_character);
        
        View v = findViewById(R.id.loadcharacter_cmbSource);
        Spinner spinner = (Spinner)v;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.loadcharacter_cmbSource_sources, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				loadCharacterList(CharacterSource.forID(position));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing
			}
        });
        
        findViewById(R.id.loadcharacter_txtURL).setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					EditText txt = (EditText)v;
					String s = txt.getText().toString();
					
					if(s.trim().startsWith("http://"))
						txt.setSelection(s.indexOf("http://") + 7, s.length());
					else if(s.trim().startsWith("https://"))
						txt.setSelection(s.indexOf("https://") + 8, s.length());
					else
						txt.selectAll();
				}
			}
        	
        });
        
        ((ListView)findViewById(R.id.loadcharacter_lstCharacters)).setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		View selected = parent.getChildAt(position);
				CharacterArrayAdapter.ViewHolder holder = (CharacterArrayAdapter.ViewHolder)selected.getTag();
				
				File f = holder.getFile();
				
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.addCategory(Intent.CATEGORY_DEFAULT);
				i.setDataAndType(Uri.fromFile(f), "text/plain");
				i.setClass(LoadCharacter.this, CharacterSheet.class);
				startActivity(i);
        	}
        });
	 
        /*
        ImageView iv = (ImageView)findViewById(R.id.loadcharacter_iv);
        android.graphics.Path p = new android.graphics.Path();
		p.moveTo(50, 0);
		p.arcTo(new android.graphics.RectF(0, 0, 100,100), 270, 90);
		p.lineTo(75, 50);
		p.arcTo(new android.graphics.RectF(25, 25, 75, 75), 0, -90);
		p.close();
		
		android.graphics.drawable.shapes.PathShape pathShape = new android.graphics.drawable.shapes.PathShape(p, 100, 100);
		android.graphics.drawable.ShapeDrawable outline = new android.graphics.drawable.ShapeDrawable(pathShape);
		outline.getPaint().setARGB(255, 0, 0, 0);
		outline.getPaint().setStyle(android.graphics.Paint.Style.STROKE);
		
		android.graphics.drawable.ShapeDrawable fill = new android.graphics.drawable.ShapeDrawable(pathShape);
		fill.getPaint().setARGB(255, 200, 200, 200);
		
		android.graphics.drawable.LayerDrawable layers = new android.graphics.drawable.LayerDrawable(new android.graphics.drawable.Drawable[] { fill, outline });
		
		iv.setBackgroundDrawable(layers);
		*/
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
			case DIALOG_LOAD_CHARACTER_LIST:
				dlgProgress = new ProgressDialog(this);
				dlgProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				dlgProgress.setMessage("Loading...");
				return dlgProgress;
				
			default:
				return null;
		}
	}
	
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
		
		if(thread != null && !thread.hasStarted()) {
	        switch(id) {
		        case DIALOG_LOAD_CHARACTER_LIST:
		            dlgProgress.setProgress(0);
		            thread.start();
		            break;
	        }
		}
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.loadcharacter, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = null;
	    switch(item.getItemId()) {
		    case R.id.loadcharacter_mnuPreferences:
		        i = new Intent(this, Preferences.class);
		        startActivity(i);
		        return true;
		    case R.id.loadcharacter_mnuViewPrefs:
		    	i = new Intent(this, PreferenceViewer.class);
		    	startActivity(i);
		    	return true;
		    	
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}

	public void btnLoad_onClick(View view) {
		EditText txt = (EditText)findViewById(R.id.loadcharacter_txtURL);
		Uri uri = null;
		uri = Uri.parse(txt.getText().toString());
		
		String scheme = uri.getScheme();
		
		if(scheme != null && (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
			Intent i = new Intent(Intent.ACTION_VIEW, uri);
			i.addCategory(Intent.CATEGORY_DEFAULT);
			i.setClass(this, CharacterSheet.class);
			i.setDataAndType(uri, "text/plain");
			startActivity(i);
		} else {
			Toast.makeText(this, "Invalid URL", Toast.LENGTH_LONG).show();
		}
	}
	
	private void loadCharacterList(CharacterSource source) {
		if(source == null)
			throw new IllegalArgumentException("source cannot be null");
		
		switch(source) {
			case SDCard:
				getExternalFiles();
				break;
				
			case Internal:
				getInternalFiles();
				break;
				
			case Wizards:
				getOnlineFiles();
				break;
				
			case URL:
				loadRemoteFile();
				return;
		}
	}
	
	private void getExternalFiles() {
		//File dir = this.getExternalFilesDir(null);
		File dir = ManticoreStatus.getExternalStorageDirectory();
		
		if(dir == null) {
			Toast.makeText(this, "External memory is not available.", Toast.LENGTH_LONG).show();
			return;
		}
		
		thread = LoadFileListThread.getExternal(handler, dir);
		
		View lv = findViewById(R.id.loadcharacter_lstCharacters);
		View tbl = findViewById(R.id.loadcharacter_tblURL);
		
		if(lv.getVisibility() == View.GONE) {
			lv.setVisibility(View.VISIBLE);
			tbl.setVisibility(View.GONE);
		}
		
		if(thread.isFinished())
			setFiles();
		else 
			showDialog(DIALOG_LOAD_CHARACTER_LIST);
	}
	
	private void getInternalFiles() {
		File dir = this.getFilesDir();
		
		thread = LoadFileListThread.getInternal(handler, dir);
		
		View lv = findViewById(R.id.loadcharacter_lstCharacters);
		View tbl = findViewById(R.id.loadcharacter_tblURL);
		
		if(lv.getVisibility() == View.GONE) {
			lv.setVisibility(View.VISIBLE);
			tbl.setVisibility(View.GONE);
		}
		
		if(thread.isFinished())
			setFiles();
		else
			showDialog(DIALOG_LOAD_CHARACTER_LIST);
	}
	
	private void getOnlineFiles() {
		Toast.makeText(this, "Wizards integration is not yet available.", Toast.LENGTH_LONG).show();
		
		View lv = findViewById(R.id.loadcharacter_lstCharacters);
		View tbl = findViewById(R.id.loadcharacter_tblURL);
		
		if(lv.getVisibility() == View.GONE) {
			lv.setVisibility(View.VISIBLE);
			tbl.setVisibility(View.GONE);
		}
	}
	
	private void loadRemoteFile() {
		View lv = findViewById(R.id.loadcharacter_lstCharacters);
		View tbl = findViewById(R.id.loadcharacter_tblURL);
		
		if(lv.getVisibility() == View.VISIBLE) {
			lv.setVisibility(View.GONE);
			tbl.setVisibility(View.VISIBLE);
		}
	}
	
	private void setFiles() {
		List<CharacterFileInfo> lstItems = null;
		lstItems = thread.getCharacterInfo();
		CharacterArrayAdapter adapter = new CharacterArrayAdapter(LoadCharacter.this, R.layout.character_list_item, lstItems);
		ListView lv = (ListView)findViewById(R.id.loadcharacter_lstCharacters);
		lv.setAdapter(adapter);
	}
}
