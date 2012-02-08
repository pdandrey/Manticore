package com.ncgeek.android.manticore.activities.loadcharacter;

import java.util.ArrayList;
import java.util.List;

import com.ncgeek.android.manticore.adapters.CharacterArrayAdapter;
import com.ncgeek.android.manticore.data.CharacterRepository;
import com.ncgeek.android.manticore.data.model.CharacterModel;
import com.ncgeek.manticore.util.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class CharacterListFragment extends ListFragment {

	private static final String LOG_TAG = "CharacterListFragment";
	
	private ICharacterListCallback callback;
	private CharacterArrayAdapter adapter;
	private ArrayList<CharacterModel> lst;
	
	public CharacterListFragment() {}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callback = (ICharacterListCallback)activity;
		} catch(ClassCastException ccex) {
			Logger.error(LOG_TAG, String.format("Activity %s does not implement ICharacterSelectedCallback.", activity.getClass().getName()), ccex);
			activity.finish();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if(savedInstanceState != null) {
			lst = savedInstanceState.getParcelableArrayList("list");
		}
		
		fillData();
	}
	
	private void fillData() {
		if(lst == null) {
			CharacterRepository repos = new CharacterRepository(getActivity());
			lst = repos.get();
			lst.add(null);
		}
		adapter = new CharacterArrayAdapter(getActivity(), lst);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		CharacterModel selectedCharacter = adapter.getItem(position);
		
		if(selectedCharacter == null)
			callback.onImportCharacter();
		else
		callback.onCharacterSelected(selectedCharacter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList("list", lst);
	}
}
