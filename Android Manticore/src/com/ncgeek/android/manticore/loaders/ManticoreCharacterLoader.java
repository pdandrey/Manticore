package com.ncgeek.android.manticore.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.data.CharacterRepository;
import com.ncgeek.android.manticore.data.model.CharacterModel;

public class ManticoreCharacterLoader extends
		AsyncTaskLoader<ManticoreCharacter> {

	private CharacterModel character;
	public ManticoreCharacterLoader(Context context, CharacterModel character) {
		super(context);
		this.character = character;
	}
	
	@Override
	public ManticoreCharacter loadInBackground() {
		CharacterRepository repos = new CharacterRepository(getContext());
		ManticoreCharacter mc = repos.load(character);
		return mc;
	}

}
