package com.ncgeek.android.manticore.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ncgeek.android.manticore.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LabelBar extends RelativeLayout {

	private static final String LOG_TAG = "LabelBar";
	private int max;
	private int temp;
	private int current;
	private String status;
	private String separator;
	private Integer statusColor;
	
	private TextView txtLabel;
	private TextView txtValue;
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
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.labelbar, this);
		
		lstChanges = new ArrayList<BarChange>();
		
		txtLabel = (TextView)findViewById(R.id.labelbar_tvLabel);
		txtValue = (TextView)findViewById(R.id.labelbar_tvValue);
		bar = (ProgressBar)findViewById(R.id.labelbar_bar);
		
		TypedArray styles = context.obtainStyledAttributes(attrs, R.styleable.LabelBar);
		
		if(styles.hasValue(R.styleable.LabelBar_fontAsset)) {
			String fontName = styles.getString(R.styleable.LabelBar_fontAsset);
			Log.d(LOG_TAG, String.format("Using font from asset %s", fontName));
			Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
			txtLabel.setTypeface(font, Typeface.BOLD);
			txtValue.setTypeface(font, Typeface.BOLD);
		}
		
		if(styles.hasValue(R.styleable.LabelBar_textSize)) {
			float size = styles.getDimension(R.styleable.LabelBar_textSize, 0);
			Log.d(LOG_TAG, String.format("Using text size %f", size));
			txtLabel.setTextSize(size);
			txtValue.setTextSize(size);
		}
		
		if(styles.hasValue(R.styleable.LabelBar_barHeight)) {
			int height = styles.getDimensionPixelSize(R.styleable.LabelBar_barHeight, 0);
			Log.d(LOG_TAG, String.format("Using bar height %d", height));
			bar.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, height));
		}
		
		if(styles.hasValue(R.styleable.LabelBar_barDrawable)) {
			int barStyle = styles.getResourceId(R.styleable.LabelBar_barDrawable, -1);
			Log.d(LOG_TAG, "Using barstyle from xml");
			Drawable draw = context.getResources().getDrawable(barStyle);
			setBarDrawable(draw);
		}
		
		setLabel(styles.getString(R.styleable.LabelBar_label));
		max = styles.getInt(R.styleable.LabelBar_max, 100);
		current = styles.getInt(R.styleable.LabelBar_current, 50);
		temp = styles.getInt(R.styleable.LabelBar_temporary, 0);
		separator = styles.getString(R.styleable.LabelBar_separator);
		if(separator == null)
			separator = "/";
		status = styles.getString(R.styleable.LabelBar_status);
		
		setMax(max);
		setCurrent(current);
		
		updateValue();
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
		this.status = status;
		updateValue();
	}
	
	public String getStatus() {
		return status;
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
		this.separator = separator;
		updateValue();
	}
	
	public String getSeparator() { return separator; }

	public void setMax(int max) { 
		this.max = max;
		this.current = Math.min(max, current);
		calculateChange();
		
		updateValue();
		bar.setMax(max);
	}
	public int getMax() { return max; }
	
	public void setTemporary(int temp) { 
		this.temp = temp; 
		if(temp > 0) {
			bar.setSecondaryProgress(current + temp);
		} else {
			bar.setSecondaryProgress(current);
		}
		updateValue();
	}
	public int getTemporary() { return temp; }
	
	public void setCurrent(int current) { 
		this.current = current;
		calculateChange();
		updateValue();
		bar.setProgress(current);
		if(temp > 0) {
			bar.setSecondaryProgress(current + temp);
		} else {
			bar.setSecondaryProgress(current);
		}
	}
	
	private void updateValue() {
		SpannableStringBuilder buf = new SpannableStringBuilder();
		buf.append(Integer.toString(current));
		if(temp > 0) {
			buf.append("+");
			buf.append(Integer.toString(temp));
		}
		buf.append(separator);
		buf.append(Integer.toString(max));
		
		if(status != null) {
			buf.append("  ");
			int index = buf.length();
			buf.append(status);
			if(statusColor != null) {
				buf.setSpan(new ForegroundColorSpan(statusColor), index, buf.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}
		
		txtValue.setText(buf);
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
			statusColor = bc.getStatusColor();
		else
			statusColor = null;
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

}
