package com.ncgeek.android.manticore.activities.loadcharacter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Comparator;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.data.CharacterRepository;
import com.ncgeek.android.manticore.loaders.Dnd4eLoader;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.util.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ImportCharacterFragment 
	extends ListFragment 
	implements LoaderManager.LoaderCallbacks<ManticoreCharacter> {

	private static final String LOG_TAG = "ImportCharacterFragment";
	private static FilenameFilter filenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String filename) {
			return filename.endsWith(".dnd4e");
		}
	};
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			_dlgLoadProgress.setMessage("Loading " + (String)msg.obj);
		}
	};
	
	private ICharacterListCallback callback;
	private ArrayAdapter<File> adapter;
	private CharacterRepository repos;
	private ProgressDialog _dlgLoadProgress;
	
	public ImportCharacterFragment() {}
	
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
		fillData();
		repos = new CharacterRepository(getActivity());
	}
	
	private void fillData() {
		File dir = Utility.getExternalStorageDirectory();
		File[] files = dir.listFiles(filenameFilter);
		
		adapter = new ArrayAdapter<File>(getActivity(), R.layout.loadcharacter_listitem, files) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null) {
					convertView = getActivity().getLayoutInflater().inflate(R.layout.loadcharacter_listitem, parent, false);
				}
				File f = getItem(position);
				((TextView)convertView).setText(f.getName());
				return convertView;
			}
		};
		adapter.sort(new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				return file1.getName().compareToIgnoreCase(file2.getName());
			}
		});
		setListAdapter(adapter);
	}

	private boolean loaderCreated = false;
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		_dlgLoadProgress = new ProgressDialog(getActivity());
		_dlgLoadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		_dlgLoadProgress.setCancelable(false);
		
		File file = adapter.getItem(position);
		
		_dlgLoadProgress.setMessage("Loading " + file.getName());
		_dlgLoadProgress.show();
		
		LoaderManager lm = getActivity().getSupportLoaderManager();
		Bundle b = new Bundle();
		b.putSerializable("file", file);
		
		if(!loaderCreated) {
			lm.initLoader(0, b, this);
			loaderCreated = true;
		} else {
			lm.restartLoader(0, b, this);
		}
	}

	@Override
	public Loader<ManticoreCharacter> onCreateLoader(int loaderID, Bundle args) {
		File file = (File)args.getSerializable("file");
		Logger.verbose(LOG_TAG, "Created a DND4eLoader for file " + file.toString());
		return new Dnd4eLoader(getActivity(), file, handler);
	}

	@Override
	public void onLoadFinished(Loader<ManticoreCharacter> loader, ManticoreCharacter data) {
		_dlgLoadProgress.setMessage("Finalizing...");
		if(data == null) {
			_dlgLoadProgress.dismiss();
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder
				.setCancelable(false)
				.setMessage("There was an error importing the character. Please check the logs for more information")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.create()
				.show();
		} else {
			if(repos.contains(data.getName())) {
				CharacterExistsDialog dlg = new CharacterExistsDialog(getActivity(), data);
				dlg.show();
				_dlgLoadProgress.dismiss();
			} else {
				repos.insert(data);
				callback.onCharacterLoaded(data);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<ManticoreCharacter> loader) {
		// do nothing
		Logger.verbose(LOG_TAG, "Loader reset");
	}
}
