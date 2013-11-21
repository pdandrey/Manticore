package com.ncgeek.android.manticore.fragments;

import com.ncgeek.manticore.util.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment {
	
	private static final String LOG_TAG = "AlertDialogFragment";
	
	public interface AlertDialogListener {
        public void onDialogPositiveClick(DialogInterface dialog);
        public void onDialogNegativeClick(DialogInterface dialog);
    }
	
	public static AlertDialogFragment newInstance(int title, int message) {
		AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putInt("message", message);
        frag.setArguments(args);
        return frag;
    }
	
	private AlertDialogListener listener;
	
	public void setListener(AlertDialogListener listener) { this.listener = listener; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        int message = getArguments().getInt("message");

        return new AlertDialog.Builder(getActivity())
                //.setIcon(R.drawable.alert_dialog_icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	if(listener != null)
                        		listener.onDialogPositiveClick(dialog);
                        }
                    }
                )
                .setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(listener != null)
                            	listener.onDialogNegativeClick(dialog);
                        }
                    }
                )
                .create();
    }
}
