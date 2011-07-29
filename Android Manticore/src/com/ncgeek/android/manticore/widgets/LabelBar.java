package com.ncgeek.android.manticore.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.widgets.GalleryMenu.SavedState;
import com.ncgeek.manticore.util.Logger;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.BaseSavedState;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LabelBar extends LinearLayout {

	private int max;
	private int temp;
	private int current;
	
	private TextView txtLabel;
	private TextView txtCurrent;
	private TextView txtTemp;
	private TextView txtMax;
	private TextView txtSeparator;
	private TextView txtStatus;
	private ProgressBar bar;
	
	private List<BarChange> lstChanges;
	
	public LabelBar(Context context) {
		super(context);
		init(context, null);
	}
	
	public LabelBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	
	private void init(Context context, AttributeSet attrs) {
		
		lstChanges = new ArrayList<BarChange>();
		
		txtLabel = new TextView(context, attrs);
		txtCurrent = new TextView(context, attrs);
		txtTemp = new TextView(context, attrs);
		txtMax = new TextView(context, attrs);
		txtSeparator = new TextView(context, attrs);
		txtStatus = new TextView(context, attrs);
		bar = new ProgressBar(context, attrs, android.R.attr.progressBarStyleHorizontal);
		
		TypedArray styles = context.obtainStyledAttributes(attrs, R.styleable.LabelBar);
		
		Typeface font = Typeface.createFromAsset(context.getAssets(), "centaur.ttf");
		txtLabel.setTypeface(font, Typeface.BOLD);
		txtCurrent.setTypeface(font, Typeface.BOLD);
		txtTemp.setTypeface(font, Typeface.BOLD);
		txtMax.setTypeface(font, Typeface.BOLD);
		txtSeparator.setTypeface(font, Typeface.BOLD);
		txtStatus.setTypeface(font, Typeface.BOLD);
		
		float size = styles.getDimension(R.styleable.LabelBar_textSize, 0);
		Logger.verbose("LabelBar", "Using text size " + size);
		if(size > 0) {
			txtLabel.setTextSize(size);
			txtCurrent.setTextSize(size);
			txtTemp.setTextSize(size);
			txtMax.setTextSize(size);
			txtSeparator.setTextSize(size);
			txtStatus.setTextSize(size);
		} else {
			txtLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			txtCurrent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			txtTemp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			txtMax.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			txtSeparator.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			txtStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		}
		bar.setIndeterminate(false);
		
		int height = styles.getDimensionPixelSize(R.styleable.LabelBar_barHeight, 0);
		
		if(height == 0) {
			height = 15;
			if(!this.isInEditMode()) {
				DisplayMetrics metrics = new DisplayMetrics();
				WindowManager mgr = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE));
				mgr.getDefaultDisplay().getMetrics(metrics);
				height = 15 * (metrics.densityDpi / 160);
			}
		}
		bar.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, height));
		
		super.setOrientation(LinearLayout.VERTICAL);
		LinearLayout llText = new LinearLayout(context);
		llText.setOrientation(LinearLayout.HORIZONTAL);
		llText.addView(txtLabel);
		llText.addView(txtCurrent);
		llText.addView(txtTemp);
		llText.addView(txtSeparator);
		llText.addView(txtMax);
		llText.addView(txtStatus);
		addView(llText);
		addView(bar);
		
		if(styles.hasValue(R.styleable.LabelBar_barDrawable)) {
			int barStyle = styles.getResourceId(R.styleable.LabelBar_barDrawable, -1);
			Drawable draw = context.getResources().getDrawable(barStyle);
			setBarDrawable(draw);
		}
		
		setLabel(styles.getString(R.styleable.LabelBar_label));
		setMax(styles.getInt(R.styleable.LabelBar_max, 100));
		setCurrent(styles.getInt(R.styleable.LabelBar_current, 50));
		setTemporary(styles.getInt(R.styleable.LabelBar_temporary, 0));
		setSeparator(styles.getString(R.styleable.LabelBar_separator));
		setStatus(styles.getString(R.styleable.LabelBar_status));
	}
	
	public void addChange(int percentage, Drawable drawable) {
		this.addChange(percentage, null, null, drawable);
	}
	
	public void addChange(int percentage, String status) {
		addChange(percentage, status, null, null);
	}
	
	public void addChange(int percentage, String status, int statusColor) {
		addChange(percentage, status, statusColor, null);
	}
	
	public void addChange(int percentage, String status, Integer statusColor, Drawable drawable) {
		BarChange bc = new BarChange(status, statusColor, percentage, drawable);
		int index = Collections.binarySearch(lstChanges, bc);
		
		if(index < 0) {
			index = (-(index) - 1);
			if(index >= lstChanges.size())
				lstChanges.add(bc);
			else
				lstChanges.add(index, bc);
		} else {
			lstChanges.set(index, bc);
		}
	}
	
	public void setStatus(String status) {
		txtStatus.setText(status);
	}
	
	public String getStatus() {
		return txtStatus.getText().toString();
	}
	
	public void setLabel(String label) {
		txtLabel.setText(label);
	}
	public String getLabel() { return txtLabel.getText().toString(); }
	
	/**
	 * Sets the separator 
	 * @param separator The string used to separate values
	 */
	public void setSeparator(String separator) {
		if(separator == null)
			separator = "/";
		txtSeparator.setText(separator);
	}
	
	public String getSeparator() { return txtSeparator.getText().toString(); }

	public void setMax(int max) { 
		this.max = max;
		this.current = Math.min(max, current);
		calculateChange();
		
		txtMax.setText(max + "");
		bar.setMax(max);
	}
	public int getMax() { return max; }
	
	public void setTemporary(int temp) { 
		this.temp = temp; 
		if(temp < 0) {
			txtTemp.setText(temp + "");
		} else if(temp == 0) {
			txtTemp.setText("");
		} else {
			txtTemp.setText("+" + temp);
		}
	}
	public int getTemporary() { return temp; }
	
	public void setCurrent(int current) { 
		this.current = current;
		calculateChange();
		txtCurrent.setText(current + "");
		bar.setProgress(current);
	}
	
	public int getCurrent() { return current; }
	
	public void setBarDrawable(Drawable drawable) { 
		bar.setProgressDrawable(drawable); 
		addChange(100, drawable);
	}
	
	public int getPercentage() {
		double dCurrent = (double)current;
		double dMax = (double)max;
		return (int)(dCurrent / dMax * 100);
	}
	
	private void calculateChange() {
		BarChange bc = new BarChange(null, null, getPercentage(), null);
		int index = Collections.binarySearch(lstChanges, bc);
		
		if(index < 0) {
			index = -index - 1;
			index = Math.min(lstChanges.size()-1, index);
		}
		
		bc = lstChanges.get(index);
		
		Drawable d = bc.getDrawable();
		if(d != null) {
			Rect r = d.getBounds();
			if(r.isEmpty()) {
				Rect old = bar.getProgressDrawable().getBounds();
				d.setBounds(old);
			}
			bar.setProgressDrawable(d);
		}
		setStatus(bc.getStatus());
		if(bc.getStatusColor() != null)
			txtStatus.setTextColor(bc.getStatusColor());
	}
	
	private static class BarChange implements Comparable<BarChange> {
		private String status;
		private Integer statusColor;
		private int atPercentage;
		private Drawable drawable;
		
		public BarChange(String status, Integer statusColor, int percentage, Drawable draw) {
			this.status = status;
			this.statusColor = statusColor;
			this.atPercentage = percentage;
			this.drawable = draw;
		}
		
		public String getStatus() { return status; }
		public Integer getStatusColor() { return statusColor; }
		@SuppressWarnings("unused")
		public int getPercentage() { return atPercentage; }
		public Drawable getDrawable() { return drawable; }

		@Override
		public int compareTo(BarChange another) {
			int other = 0;
			if(another != null)
				other = another.atPercentage;
			return atPercentage - other;
		}
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
		  
		  if(state.getClass().getName().contains("ProgressBar")) {
			  bar.onRestoreInstanceState(state);
			  super.onRestoreInstanceState(state);
			  return;
		  } 
		  
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
