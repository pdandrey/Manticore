package com.ncgeek.android.manticore.widgets;


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
		ivIcon.setImageResource(Utility.getStatIcon(skill.getName()));
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
