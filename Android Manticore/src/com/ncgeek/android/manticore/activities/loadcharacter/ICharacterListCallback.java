package com.ncgeek.android.manticore.activities.loadcharacter;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.data.model.CharacterModel;

public interface ICharacterListCallback {
	void onCharacterSelected(CharacterModel character);
	//void onCharacterListLoaded(int count);
	void onImportCharacter();
	void onCharacterLoaded(ManticoreCharacter character);
}
