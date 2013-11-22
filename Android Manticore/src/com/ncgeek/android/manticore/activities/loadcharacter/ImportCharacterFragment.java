package com.ncgeek.android.manticore.activities.loadcharacter;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.Comparator;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.data.CharacterRepository;
import com.ncgeek.android.manticore.fragments.AlertDialogFragment;
import com.ncgeek.android.manticore.fragments.ProgressDialogFragment;
import com.ncgeek.android.manticore.loaders.Dnd4eLoader;
import com.ncgeek.android.manticore.loaders.DownloadLoader;
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
	implements AlertDialogFragment.AlertDialogListener {

	private static final String LOG_TAG = "ImportCharacterFragment";
	private static final int LOADER_CHARACTER = 0;
	private static final int LOADER_DOWNLOAD = 1;
	
	private static FilenameFilter filenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String filename) {
			return filename.endsWith(".dnd4e");
		}
	};
	
	private Handler characterHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if(msg.what == 0)
				_dlgLoadProgress.setMessage("Loading " + (String)msg.obj);
			else if(msg.what == 1) {
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
			} else if(msg.what == 2) {
				CharacterExistsDialog dlg = new CharacterExistsDialog(getActivity(), (ManticoreCharacter)msg.obj);
				dlg.show();
				_dlgLoadProgress.dismiss();
			} else if(msg.what == 3) {
				ManticoreCharacter data = (ManticoreCharacter)msg.obj;
				repos.insert(data);
				callback.onCharacterLoaded(data);
			}
				
			return true;
		}
	});
	
	private Handler downloadHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if(msg.what == 0)
				_dlgLoadProgress.setMessage((String)msg.obj);
			else if(msg.what == 1)
				_dlgLoadProgress.setMessage("Downloading: " + msg.arg1 + "%");
			else if(msg.what == 2) {
				_dlgLoadProgress.dismiss();
				_dlgLoadProgress = null;
				fillData();
			}
			return true;
		}
	});
	
	private LoaderManager.LoaderCallbacks<ManticoreCharacter> lmCallback_Character = new LoaderManager.LoaderCallbacks<ManticoreCharacter>() {
		
		@Override
		public Loader<ManticoreCharacter> onCreateLoader(int loaderID, Bundle args) {
			if(loaderID != LOADER_CHARACTER) {
				Logger.error(LOG_TAG, "Unknown loaderID %d", loaderID);
				return null;
			}
			
			File file = (File)args.getSerializable("file");
			Logger.verbose(LOG_TAG, "Created a DND4eLoader for file " + file.toString());
			return new Dnd4eLoader(getActivity(), file, characterHandler);
		}

		@Override
		public void onLoadFinished(Loader<ManticoreCharacter> loader, ManticoreCharacter data) {
				_dlgLoadProgress.setMessage("Finalizing...");
				if(data == null) {
					characterHandler.sendMessage(characterHandler.obtainMessage(1));
				} else {
					if(repos.contains(data.getName())) {
						characterHandler.sendMessage(characterHandler.obtainMessage(2, data));
					} else {
						characterHandler.sendMessage(characterHandler.obtainMessage(3, data));
					}
				}
			}

		@Override
		public void onLoaderReset(Loader<ManticoreCharacter> loader) {
			// do nothing
			Logger.verbose(LOG_TAG, "Loader reset");
		}
	};
	
	private LoaderManager.LoaderCallbacks<File> lmCallback_Downloader = new LoaderManager.LoaderCallbacks<File>() {
		
		@Override
		public Loader<File> onCreateLoader(int loaderID, Bundle args) {
			if(loaderID != LOADER_DOWNLOAD) {
				Logger.error(LOG_TAG, "Unknown loaderID %d", loaderID);
				return null;
			}
			
			File dest = new File(Utility.getExternalStorageDirectory(), "Example-GreysonVier.dnd4e");
			try {
				DownloadLoader dl = new DownloadLoader(getActivity(), "https://raw.github.com/pdandrey/Manticore/android_upgrade/Sample%20Characters/GreysonVier.dnd4e", dest, downloadHandler);
				return dl;
			} catch(MalformedURLException urlEx) {
				Logger.error(LOG_TAG, "Malformed URL: %s", urlEx.getMessage());
				return null;
			}
		}

		@Override
		public void onLoadFinished(Loader<File> loader, File data) {
//			_dlgLoadProgress.dismiss();
			//fillData();
			Message msg = downloadHandler.obtainMessage(2);
			downloadHandler.sendMessage(msg);
		}

		@Override
		public void onLoaderReset(Loader<File> loader) {
			// do nothing
			Logger.verbose(LOG_TAG, "Loader reset");
		}
	};
	
	private ICharacterListCallback callback;
	private ArrayAdapter<File> adapter;
	private CharacterRepository repos;
	private ProgressDialogFragment _dlgLoadProgress;
	private AlertDialogFragment _dlgAlert;
	
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
	
	public void fillData() {
		
		File dir = Utility.getExternalStorageDirectory();
		File[] files = dir.listFiles(filenameFilter);
		
		if(files.length == 0) {
			promptToDownloadExamples();
		}
		
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

	private boolean dnd4eLoaderCreated = false;
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		_dlgLoadProgress = new ProgressDialogFragment();
		_dlgLoadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		_dlgLoadProgress.setCancelable(false);
		
		File file = adapter.getItem(position);
		
		_dlgLoadProgress.setMessage("Loading " + file.getName());
		
		_dlgLoadProgress.show(getFragmentManager(), "dlgLoading");
		
		LoaderManager lm = getActivity().getSupportLoaderManager();
		Bundle b = new Bundle();
		b.putSerializable("file", file);
		
		if(!dnd4eLoaderCreated) {
			lm.initLoader(LOADER_CHARACTER, b, lmCallback_Character);
			dnd4eLoaderCreated = true;
		} else {
			lm.restartLoader(LOADER_CHARACTER, b, lmCallback_Character);
		}
	}

	private void promptToDownloadExamples() {
		_dlgAlert = AlertDialogFragment.newInstance(R.string.dialog_download_examples_title, R.string.dialog_download_examples_message);
		_dlgAlert.setListener(this);
		_dlgAlert.show(getActivity().getSupportFragmentManager(), "download_examples");
	}

	private boolean downloaderLoaderCreated = false;
	@Override
	public void onDialogPositiveClick(DialogInterface dialog) {
		Logger.debug(LOG_TAG, "Download Examples OK");
		_dlgAlert.dismiss();
		_dlgAlert = null;
		
		_dlgLoadProgress = new ProgressDialogFragment();
		_dlgLoadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		_dlgLoadProgress.setCancelable(false);
		
		_dlgLoadProgress.setMessage("Connecting...");
		
		_dlgLoadProgress.show(getFragmentManager(), "dlgDownloading");
		
		LoaderManager lm = getActivity().getSupportLoaderManager();
		if(!downloaderLoaderCreated) {
			lm.initLoader(LOADER_DOWNLOAD, new Bundle(), lmCallback_Downloader);
			downloaderLoaderCreated = true;
		} else {
			lm.restartLoader(LOADER_DOWNLOAD, new Bundle(), lmCallback_Downloader);
		}
	}

	@Override
	public void onDialogNegativeClick(DialogInterface dialog) {
		Logger.debug(LOG_TAG, "Download Examples Cancel");
		_dlgAlert.dismiss();
		_dlgAlert = null;
	}
	
}
