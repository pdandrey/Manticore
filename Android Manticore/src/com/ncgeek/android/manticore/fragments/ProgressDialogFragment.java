package com.ncgeek.android.manticore.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {
	
	private ProgressDialog _dlg;
	private Integer style;
	private CharSequence message;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		_dlg= new ProgressDialog(getActivity());
		
		if(style != null)
			_dlg.setProgressStyle(style);
		
		if(message != null)
			_dlg.setMessage(message);
		
		return _dlg;
	}
	
	public void setProgressStyle(int style) {
		if(_dlg != null)
			_dlg.setProgressStyle(style);
		this.style = style;
	}
	
	public void setMessage(CharSequence message) {
		if(_dlg != null)
			_dlg.setMessage(message);
		this.message = message;
	}
}
