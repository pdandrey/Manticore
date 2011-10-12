package com.ncgeek.android.manticore.partial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.manticore.party.Party;
import com.ncgeek.manticore.party.PartyEventArgs;
import com.ncgeek.manticore.party.PartyMember;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.PartyChat;
import com.ncgeek.android.manticore.http.ManticoreHttpClient;
import com.ncgeek.android.manticore.threads.GetUpdatesThread;
import com.ncgeek.android.manticore.threads.ResponseThread;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.android.manticore.widgets.PartyMemberView;
import com.ncgeek.manticore.character.HitPoints;
import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.util.Logger;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PartyPartial extends Partial implements Observer {

	
	
	private static final String LOG_TAG = "PartyPartial";
	
	public static final int NOTIFICATION_MESSAGES = 1;
	public static final int NOTIFICATION_CHAT = 2;
	
	public static final int JOIN_PARTY = 1;
	public static final int CHARACTER_UPDATE = 2;
	public static final int SEND_MESSAGE = 3;
	public static final int GET_UPDATES = 4;
	public static final int PARTY_CHAT = 5;
	
	public static final int RESPONSE_ERROR = 1;
	public static final int RESPONSE_PARTY_JOINED = 2;
	public static final int RESPONSE_CHARACTER_UPDATED = 3;
	public static final int RESPONSE_MESSAGE_SENT = 4;
	public static final int RESPONSE_CHARACTER_JOINED = 5;
	public static final int RESPONSE_CHARACTER_LEFT = 6;
	public static final int RESPONSE_MESSAGE_RECIEVED = 7;
	public static final int RESPONSE_PARTY_MEMBER_UPDATED = 8;
	
	private Activity activity;
	private NotificationManager mgrNotifications;
	private Notification noteMessages;
	private final ManticorePreferences prefs;
	
	private GetUpdatesThread thGetUpdates;
	
	private Handler partyHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == ResponseThread.RESPONSE_ERROR) {
				Bundle data = msg.getData();
				int what = data.getInt("what");
				String message = data.getString("message");
				Toast.makeText(activity, String.format("Error from party server: Method %d, %s", what, message), Toast.LENGTH_LONG).show();
				return;
			}
			
			switch(msg.what) {
				case JOIN_PARTY:
					Toast.makeText(activity, "Party joined", Toast.LENGTH_SHORT).show();
					Party party = new Party((String)msg.obj, ManticoreStatus.getPC().getPortraitBitmap());
					ManticoreStatus.setParty(party);
					
					for(PartyMember pm : party.getMembers()) {
						addPartyMember(pm);
					}
					
					party.addObserver(PartyPartial.this);
					thGetUpdates = new GetUpdatesThread(party.getToken(), prefs, this);
					thGetUpdates.start();
					
					View v = getView();
					v.findViewById(R.id.partial_party_btnJoin).setVisibility(View.GONE);
					v.findViewById(R.id.partial_party_btnLeave).setVisibility(View.VISIBLE);
					v.findViewById(R.id.partial_party_btnChat).setVisibility(View.VISIBLE);
					
					break;
					
				default:
					ManticoreStatus.getParty().parseJullianEvent((String)msg.obj);
					break;
			}
			
		}
	};
	
	public PartyPartial(final Activity activity) {
		super(activity, R.layout.partial_party);
		
		prefs = new ManticorePreferences(activity);
		this.activity = activity;
		View v = getView();
		
		v.findViewById(R.id.partial_party_btnJoin).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				joinParty();
			}
		});
		
		v.findViewById(R.id.partial_party_btnChat).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(activity, PartyChat.class);
				i.putExtra("mode", PartyChat.Mode.Chat);
				activity.startActivity(i);
			}
		});
		
		mgrNotifications = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	private void joinParty() {
		/*
		HttpGet get = ManticoreHttpClient.get(prefs.getJullianServerPartyUrl("Status"));
		ManticoreHttpClient status = new ManticoreHttpClient("Android Manticore/PartyService");
		status.equals(get);
		try {
			String statusResult = status.get().get(0).getResponse();
			JSONObject json = new JSONObject(statusResult);
			boolean live = json.getBoolean("Status");
			String version = json.getString("Version");
			if(!live) {
				Toast.makeText(activity, "JullianServer returned a false status", Toast.LENGTH_LONG).show();
				Logger.error(LOG_TAG, "JullianServer returned a false status.  Version: " + version);
				return;
			}
		} catch(Exception ex) {
			Toast.makeText(activity, String.format("Error while getting server status: %s", ex.getMessage()), Toast.LENGTH_LONG).show();
			Logger.error(LOG_TAG, "Error while getting server status", ex);
			return;
		}
		*/
		PlayerCharacter pc = ManticoreStatus.getPC();
		pc.getHP().addObserver(PartyPartial.this);
		
		HitPoints hp = pc.getHP();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Name", pc.getName()));
		params.add(new BasicNameValuePair("MaxHP", hp.getMax() + ""));
		params.add(new BasicNameValuePair("MaxSurges", hp.getTotalSurges() + ""));
		params.add(new BasicNameValuePair("CurrentHP", hp.getCurrent() + ""));
		params.add(new BasicNameValuePair("CurrentSurges", hp.getRemainingSurges() + ""));
		params.add(new BasicNameValuePair("DeathSaves", hp.getDeathSaves() + ""));
		
		String portraitUrl = pc.getPortrait();
		
		if(portraitUrl != null) {
			params.add(new BasicNameValuePair("Portrait", String.format(portraitUrl, prefs.CharacterBuilderVersion())));
		}
		
		HttpPut put = ManticoreHttpClient.put(prefs.getJullianServerPartyUrl("Join"), params);
		
		final ManticoreHttpClient http = new ManticoreHttpClient("Android Manticore/PartyService");
		http.execute(put);
		
		new ResponseThread(JOIN_PARTY, "Join", http, partyHandler).start();
	}
	
	private void removePartyMember(PartyMember pm) {
		PartyMemberView pmv = getPartyMemberView(pm);
		pmv.setPartyMember(null);
		((LinearLayout)getView()).removeView(pmv);
	}

	private void addPartyMember(PartyMember pc) {
		if(pc.getPortrait() == null) {
			pc.setPortrait(Utility.getPortrait(pc.getPortraitUrl(), prefs));
		}
		LinearLayout view = (LinearLayout)getView();
		
		PartyMemberView pmv = new PartyMemberView(activity);
		pmv.setPartyMember(pc);
		int height = Utility.dpToPx(100);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
		pmv.setLayoutParams(params);
		view.addView(pmv);
	}
	
	private PartyMemberView getPartyMemberView(PartyMember pm) {
		LinearLayout view = (LinearLayout)getView();
		int size = view.getChildCount();
		for(int i=0; i<size; ++i) {
			View v = view.getChildAt(i);
			if(v instanceof PartyMemberView) {
				PartyMemberView pmv = (PartyMemberView)v;
				if(pmv.getPartyMember() == pm) {
					return pmv;
				}
			}
		}
		return null;
	}

	@Override
	public void update() {
	}

	@Override
	public int getContextMenuID() {
		return 0;
	}

	@Override
	public void setupContextMenu(Menu menu) {
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return false;
	}

	@Override
	public void onPause() {
		//activity.unbindService(connParty);
	}

	@Override
	public void update(Observable sender, Object oArgs) {
		Party party = ManticoreStatus.getParty();
		
		if(sender instanceof HitPoints) {
			HitPoints hp = (HitPoints)sender;
			
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("token", party.getToken()));
			params.add(new BasicNameValuePair("hp", hp.getCurrent() + ""));
			params.add(new BasicNameValuePair("tempHP", hp.getTemp() + ""));
			params.add(new BasicNameValuePair("surges", hp.getRemainingSurges() + ""));
			params.add(new BasicNameValuePair("deathSaves", hp.getDeathSaves() + ""));
			
			HttpPut put = ManticoreHttpClient.put(prefs.getJullianServerPartyUrl("Update"), params);
			final ManticoreHttpClient http = new ManticoreHttpClient("Android Manticore/PartyService");
			http.execute(put);
			
			new ResponseThread(CHARACTER_UPDATE, "Update", http, partyHandler).start();
		} else if(sender == party) {
			PartyEventArgs args = (PartyEventArgs)oArgs;
			PartyMember pm;
			com.ncgeek.manticore.party.Message msg;
			switch(args.getType()) {
				case CharacterJoined:
					pm = (PartyMember)args.getData();
					addPartyMember(pm);
					break;
					
				case CharacterLeft:
					pm = (PartyMember)args.getData();
					removePartyMember(pm);
					break;
					
				case Message:
					msg = (com.ncgeek.manticore.party.Message)args.getData();
					pm = msg.getFrom();
					
					String contentTitle = "Manticore Party";
					String contentText = String.format("Message from %s: %s", pm.getName(), msg.getMessage());
					String text = String.format("New message from %s.", pm.getName());
					
					if(noteMessages == null) {
						Logger.debug(LOG_TAG, "Creating new notification");
						noteMessages = new Notification(R.drawable.i_icn_new_messages, text, System.currentTimeMillis());
						noteMessages.flags |= Notification.FLAG_AUTO_CANCEL;
					} else {
						if(noteMessages.number <= 1)
							noteMessages.number = 2;
						else
							++noteMessages.number;
						contentText = String.format("You have %d new messages.", noteMessages.number);
					}
					
					Intent noteIntent = new Intent(activity, PartyChat.class);
					noteIntent.putExtra("member", pm.getID());
					noteIntent.putExtra("mode", PartyChat.Mode.Message);
					
					PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, noteIntent, 0);
					
					noteMessages.setLatestEventInfo(activity, contentTitle, contentText, contentIntent);
					mgrNotifications.notify(NOTIFICATION_MESSAGES, noteMessages);
					break;
					
				case PartyChat:
					
					
				default:
					Logger.warn(LOG_TAG, String.format("Unknown PartyEventArgs.Type: %s", args.getType()));
					break;
			}
		}
	}
	
	private void logAndToastError(String msg, Exception ex) {
		Logger.error(LOG_TAG, msg, ex);
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

}
