package com.ncgeek.android.manticore.activities.loadcharacter;

import java.text.MessageFormat;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.data.CharacterRepository;
import com.ncgeek.android.manticore.data.model.CharacterModel;
import com.ncgeek.manticore.util.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CharacterInfoFragment extends Fragment {
	
	private static final String LOG_TAG = "CharInfoFgmt";
	
	private ICharacterListCallback callback;
	private CharacterModel character;
	
	public CharacterInfoFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null) {
			character = (CharacterModel)savedInstanceState.getParcelable("character");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("character", character);
	}

	@Override
	public void onAttach(Activity activity) {
		Logger.verbose(LOG_TAG, "attaching view");
		super.onAttach(activity);
		try {
			callback = (ICharacterListCallback)activity;
		} catch(ClassCastException ccex) {
			Logger.error(LOG_TAG, String.format("Activity %s does not implement ICharacterSelectedCallback.", activity.getClass().getName()), ccex);
			activity.finish();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		Logger.verbose(LOG_TAG, "creating view");
		
		View v = inflater.inflate(R.layout.loadcharacter_characterinfo, null);
		fillInfo(v);
		((Button)v.findViewById(R.id.btnLoadCharacter)).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) { btnLoadCharacter_Click(v); } 
		});
		return v;
	}
	
	public void displayCharacter(CharacterModel character) {
		this.character = character;
		fillInfo(getView());
	}
	
	private void fillInfo(View v) {
		if(v == null) {
			Logger.verbose(LOG_TAG, "Failed to fill view due to null view");
			return;
		}
		
		if(character == null) {
			Logger.verbose(LOG_TAG, "Failed to fill view due to null character");
			return;
		}
		
		ImageView iv = (ImageView)v.findViewById(R.id.ivPortrait);
		iv.setImageBitmap(character.getPortrait());
		
		setText(v, R.id.tvName, character.getName());
		setTextFromFormat(v, R.id.tvLevel, character.getLevel());
		setText(v, R.id.tvImportedOn, character.getImportedOn().format("%b %d, %Y %T"));
		setText(v, R.id.tvUpdatedOn, character.getImportedOn().format("%b %d, %Y %T"));
		setTextFromFormat(v, R.id.tvRaceAndClass,
			character.getRace(),
			character.getHeroicClass(),
			character.getParagonClass() == null ? "" : character.getParagonClass(),
			character.getEpicClass() == null ? "" : character.getEpicClass()
			);
	}
	
	private void setText(View v, int id, String text) {
		TextView tv = (TextView)v.findViewById(id);
		tv.setVisibility(text == null || text.length() == 0 ? View.GONE : View.VISIBLE);
		tv.setText(text);
	}
	
	private void setTextFromFormat(View v, int id, Object...args) {
		TextView tv = (TextView)v.findViewById(id);
		String format = (String)tv.getTag();
		if(format == null) {
			format = tv.getText().toString();
			tv.setTag(format);
		}
		tv.setText(MessageFormat.format(format, args).trim());
	}
	
	public void btnLoadCharacter_Click(View v) {
		CharacterRepository repos = new CharacterRepository(getActivity());
		ManticoreCharacter mc = repos.load(character);
		if(mc == null) {
			// there was an error
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder
				.setMessage("There was an error loading the saved character. Check the log for more information.")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
			AlertDialog dlgError = builder.create();
			dlgError.show();
		} else {
			callback.onCharacterLoaded(mc);
		}
	}
}
