package com.ncgeek.android.manticore.widgets;

import com.ncgeek.android.manticore.ManticorePreferences;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.StatBreakdown;
import com.ncgeek.android.manticore.widgets.GalleryMenu.SavedState;
import com.ncgeek.manticore.character.stats.Stat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatView extends LinearLayout implements OnLongClickListener {

	private ImageView ivIcon;
	private TextView tvName;
	private TextView tvValue;
	private Context context;
	
	public StatView(Context context) {
		super(context);
		 init(context, null);
	}
	
	public StatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		this.context = context;
		setOrientation(VERTICAL);
		LayoutParams lp = (LinearLayout.LayoutParams)getLayoutParams();

		ivIcon = new ImageView(context, attrs);
		tvName = new TextView(context, attrs);
		tvValue = new TextView(context, attrs);
		
		ivIcon.setImageResource(R.drawable.icon);
		tvName.setText("Stat");
		tvValue.setText("99");
		
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
		tvName.setLayoutParams(lp);
		tvName.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		Typeface font = tvName.getTypeface();
		font = Typeface.create(font, Typeface.BOLD);
		tvName.setTypeface(font);
		
		LinearLayout ll = new LinearLayout(context);
		lp =  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ll.setLayoutParams(lp);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		
		
		lp =  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		ivIcon.setLayoutParams(lp);
				
		lp =  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
		tvValue.setLayoutParams(lp);
		tvValue.setTypeface(tvValue.getTypeface(), Typeface.BOLD);
		tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
		
		ll.addView(ivIcon);
		ll.addView(tvValue);
		
		addView(tvName);
		addView(ll);
		
		setOnLongClickListener(this);
	}
	
	public void setStat(Stat stat) {
		String name = stat.getAliases().get(0);
		ivIcon.setImageResource(SkillTableRow.getStatIcon(name));
		tvName.setText(name + ":");
		setTag(name);
		
		ManticorePreferences pref = new ManticorePreferences(context);
		if(pref.useCalculatedStats()) {
			tvValue.setText(stat.getCalculatedValue() + "");
		} else {
			tvValue.setText(stat.getAbsoluteValue() + "");
		}
	}

	@Override
	public boolean onLongClick(View v) {
		Intent i = new Intent(context, StatBreakdown.class);
        i.setAction(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.putExtra("defense", (String)getTag());
        i.putExtra("icon", ((BitmapDrawable)ivIcon.getDrawable()).getBitmap());
        context.startActivity(i);
		return true;
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
