package com.ncgeek.android.manticore.loaders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;

import com.ncgeek.android.manticore.ManticoreCharacter;
import com.ncgeek.manticore.parsers.CharacterParserEventArgs;
import com.ncgeek.manticore.util.Logger;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.AsyncTaskLoader;

public class DownloadLoader extends AsyncTaskLoader<File> {

	private static final String LOG_TAG = "DownloadLoader";
	
	private URL _url;
	private Handler _handler;
	private File _destination;
	private String _status;
	
	public DownloadLoader(Context context, String url, File dest, Handler handler) throws MalformedURLException {
		super(context);
		_url = new URL(url);
		_handler = handler;
		_destination = dest;
	}
	
	public final String getStatus() { return _status; }

	@Override
	public File loadInBackground() {
		Logger.verbose(LOG_TAG, "Starting download");
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
        	Message msg = _handler.obtainMessage();
        	msg.what = 0;
			msg.obj = "Connecting...";
			_handler.sendMessage(msg);
			
            connection = (HttpURLConnection) _url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report 
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                 _status = "Server returned HTTP " + connection.getResponseCode() 
                     + " " + connection.getResponseMessage();
                 return null;
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(_destination.getAbsolutePath());

            msg = _handler.obtainMessage();
        	msg.what = 0;
			msg.obj = "Downloading...";
			_handler.sendMessage(msg);
            
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                if (fileLength > 0) {
                	msg = _handler.obtainMessage();
                	msg.what = 1;
					msg.arg1 = (int) (total * 100 / fileLength);
					_handler.sendMessage(msg);
                }
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            _status = e.toString();
            return null;
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } 
            catch (IOException ignored) { }

            if (connection != null)
                connection.disconnect();
        }
        
		
        return _destination;
	}

	@Override
	protected void onReset() {
		super.onReset();
		_status = null;
	}

	@Override
	protected void onStartLoading() {
		if(_destination.exists())
			deliverResult(_destination);
		else
			forceLoad();
	}
}
