package com.ncgeek.android.manticore.widgets;

import java.util.HashMap;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.android.manticore.widgets.GalleryMenu.SavedState;
import com.ncgeek.manticore.character.Skill;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View.BaseSavedState;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class SkillTableRow extends TableRow {

	private static final HashMap<String,Integer> STAT_ICONS = new HashMap<String, Integer>();
	
	static {
		STAT_ICONS.put("ac", R.drawable.i_stat_ac);
		STAT_ICONS.put("acrobatics", R.drawable.i_stat_acrobatics);
		STAT_ICONS.put("arcana", R.drawable.i_stat_arcana);
		STAT_ICONS.put("athletics", R.drawable.i_stat_athletics);
		STAT_ICONS.put("bluff", R.drawable.i_stat_bluff);
		STAT_ICONS.put("cha", R.drawable.i_stat_cha);
		STAT_ICONS.put("con", R.drawable.i_stat_con);
		STAT_ICONS.put("dex", R.drawable.i_stat_dex);
		STAT_ICONS.put("diplomacy", R.drawable.i_stat_diplomacy);
		STAT_ICONS.put("dungeoneering", R.drawable.i_stat_dungeoneering);
		STAT_ICONS.put("endurance", R.drawable.i_stat_endurance);
		STAT_ICONS.put("fortitude", R.drawable.i_stat_fortitude);
		STAT_ICONS.put("heal", R.drawable.i_stat_heal);
		STAT_ICONS.put("history", R.drawable.i_stat_history);
		STAT_ICONS.put("initiative", R.drawable.i_stat_initiative);
		STAT_ICONS.put("insight", R.drawable.i_stat_insight);
		STAT_ICONS.put("int", R.drawable.i_stat_int);
		STAT_ICONS.put("intimidate", R.drawable.i_stat_intimidate);
		STAT_ICONS.put("nature", R.drawable.i_stat_nature);
		STAT_ICONS.put("perception", R.drawable.i_stat_perception);
		STAT_ICONS.put("reflex", R.drawable.i_stat_reflex);
		STAT_ICONS.put("religion", R.drawable.i_stat_religion);
		STAT_ICONS.put("speed", R.drawable.i_stat_speed);
		STAT_ICONS.put("stealth", R.drawable.i_stat_stealth);
		STAT_ICONS.put("str", R.drawable.i_stat_str);
		STAT_ICONS.put("streetwise", R.drawable.i_stat_streetwise);
		STAT_ICONS.put("thievery", R.drawable.i_stat_thievery);
		STAT_ICONS.put("will", R.drawable.i_stat_will);
		STAT_ICONS.put("wis", R.drawable.i_stat_wis);
	}
	
	public static final int getStatIcon(String stat) {
		stat = stat.toLowerCase().trim();
		if(STAT_ICONS.containsKey(stat))
			return STAT_ICONS.get(stat);
		else
			return 0;
	}
	
	private TextView tvName;
	private TextView tvTotal;
	private TextView tvHalfLevel;
	private TextView tvTrained;
	private TextView tvAbility;
	private TextView tvAbilityMod;
	private TextView tvMisc;
	private TextView tvArmorPenalty;
	private ImageView ivIcon;
	
	public SkillTableRow(Context context) {
		super(context);
		init(context, null);
	}
	
	public SkillTableRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs) {
		tvName = new TextView(context, attrs, R.style.charactersheet_stat_name);
		tvTotal = new TextView(context, attrs, R.style.charactersheet_stat_value);
		tvHalfLevel = new TextView(context, attrs, R.style.charactersheet_stat_value);
		tvTrained = new TextView(context, attrs, R.style.charactersheet_stat_value);
		tvAbility = new TextView(context, attrs, R.style.charactersheet_stat_value);
		tvAbilityMod = new TextView(context, attrs, R.style.charactersheet_stat_value);
		tvMisc = new TextView(context, attrs, R.style.charactersheet_stat_value);
		tvArmorPenalty = new TextView(context, attrs, R.style.charactersheet_stat_value);
		ivIcon = new ImageView(context, attrs, R.style.charactersheet_stat_icon);
		
		int px = Utility.dpToPx(30);
		
		TableRow.LayoutParams lp = new TableRow.LayoutParams(px, px);
		ivIcon.setLayoutParams(lp);
		
		addView(ivIcon);
		addView(tvName);
		addView(tvTotal);
		tvTotal.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		addView(createStaticTextView("=", context, attrs));
		addView(tvHalfLevel);
		addView(createStaticTextView("+", context, attrs));
		addView(tvTrained);
		addView(createStaticTextView("+", context, attrs));
		addView(tvAbilityMod);
		addView(createStaticTextView("+", context, attrs));
		addView(tvMisc);
		addView(createStaticTextView("+", context, attrs));
		addView(tvArmorPenalty);
	}
	
	public void setSkill(Skill skill) {
		tvName.setText(skill.getName());
		tvTotal.setText(skill.getTotal() + "");
		tvHalfLevel.setText(skill.getHalfLevel() + "");
		tvTrained.setText(getValue(skill.getTrained()));
		tvAbilityMod.setText(skill.getAbilityMod() + "");
		tvAbility.setText(skill.getAbilityModName());
		tvMisc.setText(getValue(skill.getMisc()));
		tvArmorPenalty.setText(getValue(skill.getArmorPenalty()));
		ivIcon.setImageResource(SkillTableRow.getStatIcon(skill.getName()));
	}
	
	private String getValue(int value) {
		if(value == 0)
			return "0";
		else
			return value + "";
	}
	
	private TextView createStaticTextView(String text, Context context, AttributeSet attrs) {
		TextView tv = new TextView(context, attrs);
		tv.setText(text);
		return tv;
	}
	
	@Override
	  public Parcelable onSaveInstanceState() {
	    //begin boilerplate code that allows parent classes to save state
	    Parcelable superState = super.onSaveInstanceState();

	    SavedState ss = new SavedState(superState);
	    //end

	    return ss;
	  }

	  @Override
	  public void onRestoreInstanceState(Parcelable state) {
	    //begin boilerplate code so parent classes can restore state
	    if(!(state instanceof SavedState)) {
	      super.onRestoreInstanceState(state);
	      return;
	    }

	    SavedState ss = (SavedState)state;
	    super.onRestoreInstanceState(ss.getSuperState());
	    //end
	  }

	  static class SavedState extends BaseSavedState {
	   
	    SavedState(Parcelable superState) {
	      super(superState);
	    }

	    private SavedState(Parcel in) {
	      super(in);
	      //this.stateToSave = in.readInt();
	    }

	    @Override
	    public void writeToParcel(Parcel out, int flags) {
	      super.writeToParcel(out, flags);
	      //out.writeInt(this.stateToSave);
	    }

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
	          public SavedState createFromParcel(Parcel in) {
	            return new SavedState(in);
	          }
	          public SavedState[] newArray(int size) {
	            return new SavedState[size];
	          }
	    };
	  }
}
