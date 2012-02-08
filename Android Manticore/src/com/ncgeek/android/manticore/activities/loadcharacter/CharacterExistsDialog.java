package com.ncgeek.android.manticore.activities.loadcharacter;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.data.CharacterRepository;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CharacterExistsDialog extends Dialog {

	private ManticoreCharacter character;
	private ICharacterListCallback callback;
	
	private TextView tvLabel;
	private EditText txtName;
	
	public CharacterExistsDialog(Context context, ManticoreCharacter character) {
		super(context);
		callback = (ICharacterListCallback)context;
		setContentView(R.layout.dialog_rename_character);
		this.character = character;
		setTitle("Character Exists");
		
		tvLabel = (TextView)findViewById(R.id.dialog_lblRenameCharacter);
		txtName = (EditText)findViewById(R.id.dialog_etRenameCharacter);
		
		txtName.setText(character.getName());
		tvLabel.setText(String.format(tvLabel.getText().toString(), character.getName(), character.getName()));
		
		((Button)findViewById(R.id.dialog_btnRename)).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) { renameCharacter(); } 
		});
		
		((Button)findViewById(R.id.dialog_btnOverwrite)).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) { overwriteCharacter(); } 
		});
		
		((Button)findViewById(R.id.dialog_btnCancel)).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) { cancel(); } 
		});
	}

	private void renameCharacter() {
		CharacterRepository repos = new CharacterRepository(getContext());
		String name = txtName.getText().toString().trim();
		
		if(name.length() == 0)
			return;
		
		character.setName(name);
		
		if(repos.contains(name)) {
			tvLabel.setText(String.format("%s already exists. If you want to save with this name, click Overwrite.", name));
			return;
		}
		
		repos.insert(character);
		callback.onCharacterLoaded(character);
		dismiss();
	}
	
	private void overwriteCharacter() {
		CharacterRepository repos = new CharacterRepository(getContext());
		character.setID(repos.getID(character.getName()));
		repos.update(character);
		callback.onCharacterLoaded(character);
		dismiss();
	}
}
