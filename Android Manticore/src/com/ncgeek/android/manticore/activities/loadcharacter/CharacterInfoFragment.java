package com.ncgeek.android.manticore.activities.loadcharacter;

import java.text.MessageFormat;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.data.model.CharacterModel;
import com.ncgeek.android.manticore.fragments.ProgressDialogFragment;
import com.ncgeek.android.manticore.loaders.ManticoreCharacterLoader;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.util.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CharacterInfoFragment 
	extends Fragment 
	implements LoaderManager.LoaderCallbacks<ManticoreCharacter> {
	
	private static final String LOG_TAG = "CharInfoFgmt";
	
	private ICharacterListCallback callback;
	private CharacterModel character;
	private ProgressDialogFragment dlgLoading;
	
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
		
		Utility.setText(v, R.id.tvName, character.getName());
		Utility.setTextFromFormat(v, R.id.tvLevel, character.getLevel());
		Utility.setText(v, R.id.tvImportedOn, character.getImportedOn().format("%b %d, %Y %T"));
		Utility.setText(v, R.id.tvUpdatedOn, character.getImportedOn().format("%b %d, %Y %T"));
		Utility.setTextFromFormat(v, R.id.tvRaceAndClass,
			character.getRace(),
			character.getHeroicClass(),
			character.getParagonClass() == null ? "" : character.getParagonClass(),
			character.getEpicClass() == null ? "" : character.getEpicClass()
			);
	}
	
	public void btnLoadCharacter_Click(View v) {
		if(dlgLoading == null) {
			dlgLoading = new ProgressDialogFragment();
			dlgLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dlgLoading.setCancelable(true);
		}
		
		dlgLoading.setMessage(String.format("Loading %s", character.getName()));
		dlgLoading.show(getFragmentManager(), "dlgLoading");
		
		getActivity().getSupportLoaderManager().initLoader(0, null, this).forceLoad();
	}

	@Override
	public Loader<ManticoreCharacter> onCreateLoader(int id, Bundle args) {
		Logger.verbose(LOG_TAG, "Creating loader for %s", character.getName());
		return new ManticoreCharacterLoader(getActivity(), character);
	}

	@Override
	public void onLoadFinished(Loader<ManticoreCharacter> loader, ManticoreCharacter mc) {
		dlgLoading.setMessage("Finalizing");
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
			dlgLoading.dismiss();
			dlgError.show();
		} else {
			callback.onCharacterLoaded(mc);
		}
	}

	@Override
	public void onLoaderReset(Loader<ManticoreCharacter> loader) {
		
	}
}
