package com.ncgeek.android.manticore.adapters;


import com.ncgeek.android.manticore.ManticoreStatus;
import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.menus.GalleryMenu;
import com.ncgeek.android.manticore.menus.GalleryMenuItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryMenuAdapter extends BaseAdapter {
    
	private LayoutInflater inflater;
    private GalleryMenu menu;

    public GalleryMenuAdapter(Context c, int menuID) {
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MenuInflater menuInflater = new MenuInflater(c);
        menu = new GalleryMenu(c.getResources());
        menuInflater.inflate(menuID, menu);
    }
    
    public GalleryMenu getMenu() { return menu; }

    public int getCount() {
        return menu.getVisibleCount();
    }

    public Object getItem(int position) {
        return menu.getVisibleItem(position);
    }

    public long getItemId(int position) {
        return menu.getVisibleItem(position).getItemId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder = null;
    	GalleryMenuItem item = (GalleryMenuItem)menu.getVisibleItem(position);
    	
    	if(convertView == null) {
    		convertView = inflater.inflate(R.layout.gallerymenuitem, parent, false);
            holder = new ViewHolder();
            holder.txtText = (TextView)convertView.findViewById(R.id.gallerymenuitem_text);
            convertView.setTag(holder);
    	} else {
    		holder = (ViewHolder)convertView.getTag();
    	}
    	
//    	if(item.getItemId() == R.id.mainmenu_mnuCharacter && ManticoreStatus.getPC() != null) {
//    		item.setIcon(new BitmapDrawable((Bitmap)ManticoreStatus.getPC().getPortraitBitmap()));
//    	}
    	
    	holder.txtText.setText(item.getTitle());
    	holder.txtText.setCompoundDrawables(null, item.getIcon(), null, null);
    	
    	return convertView;
    }
    
    public static class ViewHolder {
		TextView txtText;
	}
    
     
}
