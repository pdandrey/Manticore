package com.ncgeek.android.manticore.widgets;

import java.util.Observable;
import java.util.Observer;

import com.ncgeek.manticore.party.PartyMember;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.PartyChat;
import com.ncgeek.manticore.character.HitPoints;
import com.ncgeek.manticore.util.Logger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PartyMemberView extends RelativeLayout implements Observer {

	private static final String LOG_TAG = "PartyMemberView";
	
	private PartyMember member;
	
	private LabelBar barHP;
	private LabelBar barSurge;
	private ImageView imgNewMessages;
	private final Context context;
	
	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(context, PartyChat.class);
			i.putExtra("mode", PartyChat.Mode.Message);
			i.putExtra("member", member.getID());
			context.startActivity(i);
		}
	};
	
	public PartyMemberView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
		this.context = context;
		
	}

	public PartyMemberView(Context context) {
		super(context);
		init(context, null);
		this.context = context;
	}

	private void init(Context context, AttributeSet attrs) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.partymember, this);
        
        barHP = (LabelBar)findViewById(R.id.partymember_hpbar);
        barSurge = (LabelBar)findViewById(R.id.partymember_surgebar);
        
        barHP.addChange(50, "Bloodied", Color.RED, getResources().getDrawable(R.drawable.hp_bar_bloodied));
        
        imgNewMessages = (ImageView)findViewById(R.id.partyMember_imgNewMessages);
        
        setClickable(true);
        setOnClickListener(clickListener);
        
	}
	
	public void setPartyMember(PartyMember pm) {
		if(member != null) {
			member.getHP().deleteObserver(this);
		}
		member = pm;
		
		if(pm != null) {
			ImageView iv = (ImageView)findViewById(R.id.partymember_img);
			iv.setImageBitmap((Bitmap)pm.getPortrait());
			
			TextView tv = (TextView)findViewById(R.id.partymember_tvName);
			tv.setText(pm.getName());
			
			HitPoints hp = pm.getHP();
			update(hp, null);
			
			hp.addObserver(this);
			pm.addObserver(this);
		}
	}
	
	public PartyMember getPartyMember() { return member; }

	@Override
	public void update(Observable sender, Object args) {
		Logger.debug(LOG_TAG, String.format("Update from %s", sender.getClass().getSimpleName()));
		if(sender instanceof HitPoints) {
			HitPoints hp = member.getHP();
			
			Logger.debug(LOG_TAG, String.format("%s Hitpoint Update: %s", member.getName(), hp.toString()));
			
			barHP.setMax(hp.getMax());
			barSurge.setMax(hp.getTotalSurges());
			
			barHP.setCurrent(hp.getCurrent());
			barHP.setTemporary(hp.getTemp());
			barSurge.setCurrent(hp.getRemainingSurges());
		} else if(sender == member) {
			imgNewMessages.setVisibility(member.hasUnreadMessages() ? View.VISIBLE : View.GONE);
		}
	}
}
