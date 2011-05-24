package com.ncgeek.android.manticore.widgets;

import com.ncgeek.android.manticore.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.WindowManager;
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
	private ProgressBar bar;
	
	public LabelBar(Context context) {
		super(context);
		
		init(context, null);
	}
	
	public LabelBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init(context, attrs);
	}
	
	
	private void init(Context context, AttributeSet attrs) {
		txtLabel = new TextView(context, attrs);
		txtCurrent = new TextView(context, attrs);
		txtTemp = new TextView(context, attrs);
		txtMax = new TextView(context, attrs);
		txtSeparator = new TextView(context, attrs);
		bar = new ProgressBar(context, attrs, android.R.attr.progressBarStyleHorizontal);
		
		Typeface font = Typeface.createFromAsset(context.getAssets(), "centaur.ttf");
		txtLabel.setTypeface(font, Typeface.BOLD);
		txtLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		txtCurrent.setTypeface(font, Typeface.BOLD);
		txtCurrent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		txtTemp.setTypeface(font, Typeface.BOLD);
		txtTemp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		txtMax.setTypeface(font, Typeface.BOLD);
		txtMax.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		txtSeparator.setTypeface(font, Typeface.BOLD);
		txtSeparator.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		
		bar.setIndeterminate(false);
		
		int height = 15;
		
		if(!this.isInEditMode()) {
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager mgr = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE));
			mgr.getDefaultDisplay().getMetrics(metrics);
			height = 15 * (metrics.densityDpi / 160);
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
		addView(llText);
		addView(bar);
		
		TypedArray styles = context.obtainStyledAttributes(attrs, R.styleable.LabelBar);
		
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
		txtCurrent.setText(current + "");
		bar.setProgress(current);
	}
	
	public int getCurrent() { return current; }
	
	public void setBarDrawable(Drawable drawable) { 
		bar.setProgressDrawable(drawable); 
	}
}
