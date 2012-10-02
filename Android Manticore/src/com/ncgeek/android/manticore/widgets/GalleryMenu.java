package com.ncgeek.android.manticore.widgets;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.adapters.GalleryMenuAdapter;
import com.ncgeek.android.manticore.menus.GalleryMenuItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@Deprecated
public class GalleryMenu extends LinearLayout {

	private GalleryMenuAdapter adapter;
	private Gallery gallery;
	
	public GalleryMenu(Context context) {
		super(context);
		init(context, null);
	}
	
	public GalleryMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		gallery.setOnItemClickListener(listener);
	}
	
	public GalleryMenuItem getMenuItem(int position) {
		return (GalleryMenuItem)adapter.getItem(position);
	}
	
	private void init(Context context, AttributeSet attrs) {
		LinearLayout llSubMenu = new LinearLayout(context, attrs);
		llSubMenu.setOrientation(getOrientation());
		llSubMenu.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
		addView(llSubMenu);
		
		if(!this.isInEditMode()) {
			gallery = new Gallery(context, attrs);
			adapter = new GalleryMenuAdapter(context, R.menu.mainmenu);
			gallery.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
			gallery.setAdapter(adapter);
			//gallery.setOnItemClickListener(this);
			
			if(context instanceof Activity) {
				Intent i = ((Activity)context).getIntent();
				int position = i.getIntExtra("gallerymenu_index", 0);
				gallery.setSelection(position);
			}
			
			addView(gallery);
		} else {
			TextView edit = new TextView(context, attrs);
			edit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
			edit.setText("Gallery Menu");
			addView(edit);
		}
	}
	
	public void setMenuItemIcon(int position, Drawable icon) {
		GalleryMenuItem mi = (GalleryMenuItem)adapter.getItem(position);
		mi.setIcon(icon);
		LinearLayout ll = (LinearLayout)gallery.getChildAt(position);
		((ImageView)ll.getChildAt(position)).setImageDrawable(icon);
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
