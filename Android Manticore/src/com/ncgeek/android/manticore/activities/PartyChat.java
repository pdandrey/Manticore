package com.ncgeek.android.manticore.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;

import com.ncgeek.manticore.party.Message;
import com.ncgeek.manticore.party.Party;
import com.ncgeek.manticore.party.PartyEventArgs;
import com.ncgeek.manticore.party.PartyMember;
import com.ncgeek.manticore.util.Logger;
import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.adapters.PartyMessageAdapter;
import com.ncgeek.android.manticore.http.ManticoreHttpClient;
import com.ncgeek.android.manticore.partial.PartyPartial;
import com.ncgeek.android.manticore.threads.ResponseThread;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PartyChat extends Activity implements Observer {

	public static final String LOG_TAG = "PartyChatActivity";
	
	public enum Mode { Message, Chat }
	
	private PartyMember member;
	private PartyMessageAdapter adapter;
	private Mode mode;
	private ManticorePreferences prefs;
	private EditText txtMessage;
	private NotificationManager mgrNotifications;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
				case PartyPartial.RESPONSE_MESSAGE_RECIEVED:
					Logger.debug(LOG_TAG, "Message recieved");
					ManticoreStatus.getParty().parseJullianEvent((String)msg.obj);
					break;
					
				default:
					Logger.warn(LOG_TAG, String.format("Unknown android.os.Message received: %d", msg.what));
					super.handleMessage(msg);
					break;
			}
		}
	};
	
	private OnClickListener btnSend_Click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Party party = ManticoreStatus.getParty();
			
			if(mode == Mode.Message) {
				party.sendMessageTo(member.getID(), txtMessage.getText().toString());
				sendMail(member.getID(), txtMessage.getText().toString());
				mgrNotifications.cancel(PartyPartial.NOTIFICATION_MESSAGES);
			} else {
				party.chat(txtMessage.getText().toString());
				sendChat(txtMessage.getText().toString());
				mgrNotifications.cancel(PartyPartial.NOTIFICATION_CHAT);
			}
			txtMessage.setText("");
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.party_messages);
        adapter = new PartyMessageAdapter(this);
        ListView lv = (ListView)findViewById(R.id.partymessages_lvChat);
        lv.setAdapter(adapter);
        
        prefs = new ManticorePreferences(this);
        
        Button send = (Button)findViewById(R.id.partymessages_btnSend);
        send.setOnClickListener(btnSend_Click);
        
        txtMessage = (EditText)findViewById(R.id.partymessages_txtMessage);
        mgrNotifications = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Intent i = getIntent();
		Bundle data = i.getExtras();
		Mode mode = (Mode)data.get("mode");
		
		if(this.mode != mode) {
			adapter.setShowPortraits(mode == Mode.Chat);
			int vis = mode == Mode.Chat ? View.GONE : View.VISIBLE;
			findViewById(R.id.partymessages_img).setVisibility(vis);
			findViewById(R.id.partymessages_tvName).setVisibility(vis);
		}
		
		if(mode == Mode.Message) {
			String id = data.getString("member");
			member = ManticoreStatus.getParty().getPartyMember(id);
			
			if(member.getPortrait() instanceof Bitmap)
				((ImageView)findViewById(R.id.partymessages_img)).setImageBitmap((Bitmap)member.getPortrait());
			
			adapter.clear();
			for(Message m : member.getMessages()) {
				adapter.add(m);
			}
			
			((TextView)findViewById(R.id.partymessages_tvName)).setText(member.getName());
			
			member.addObserver(this);
			
			member.setMessagesRead();
		} else {
			ManticoreStatus.getParty().addObserver(this);
			adapter.clear();
			for(Message m : ManticoreStatus.getParty().getChat()) {
				adapter.add(m);
			}
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(mode == Mode.Message)
			member.deleteObserver(this);
		else
			ManticoreStatus.getParty().deleteObserver(this);
	}
	
	private void sendMail(String to, String message, Serializable...attachments) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("from", ManticoreStatus.getParty().getToken()));
		params.add(new BasicNameValuePair("to", to));
		params.add(new BasicNameValuePair("message", message));
		
		HttpPut put = ManticoreHttpClient.put(prefs.getJullianServer() + "Message", params);
		
		final ManticoreHttpClient http = new ManticoreHttpClient("Android Manticore/PartyService");
		http.execute(put);
		
		new ResponseThread(PartyPartial.SEND_MESSAGE, "Message", http, handler).start();
	}
	
	private void sendChat(String message) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", ManticoreStatus.getParty().getToken()));
		params.add(new BasicNameValuePair("message", message));
		
		HttpPut put = ManticoreHttpClient.put(prefs.getJullianServer() + "Chat", params);
		
		final ManticoreHttpClient http = new ManticoreHttpClient("Android Manticore/PartyService");
		http.execute(put);
		
		new ResponseThread(PartyPartial.PARTY_CHAT, "Chat", http, handler).start();
	}

	@Override
	public void update(Observable sender, Object oArgs) {
		Logger.debug(LOG_TAG, String.format("PartyMember updated: %s", oArgs == null ? "null" : oArgs.getClass().getSimpleName()));
		if(oArgs instanceof Message) {
			Message msg = (Message)oArgs;
			adapter.add(msg);
			member.setMessagesRead();
			mgrNotifications.cancel(PartyPartial.NOTIFICATION_MESSAGES);
		} else if(oArgs instanceof PartyEventArgs) {
			PartyEventArgs args = (PartyEventArgs)oArgs;
			Message msg = (Message)args.getData();
			adapter.add(msg);
			mgrNotifications.cancel(PartyPartial.NOTIFICATION_CHAT);
		}
	}
}
