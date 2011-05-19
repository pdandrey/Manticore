package com.ncgeek.android.manticore.adapters;

import java.io.File;
import java.util.List;

import com.ncgeek.android.manticore.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CharacterArrayAdapter extends ArrayAdapter<CharacterFileInfo> {
	public static class ViewHolder {
		TextView txtName;
		TextView txtFile;
		File file;
		
		public File getFile() {
			return file;
		}
	}
	
	private List<CharacterFileInfo> items;
	private LayoutInflater inflater;
	private int textViewId;
	
    public CharacterArrayAdapter(Context context, int textViewResourceId, List<CharacterFileInfo> items) {
        super(context, textViewResourceId, items);
        this.items = items;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        textViewId = textViewResourceId;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            
    	ViewHolder holder = null;
    	CharacterFileInfo fi = items.get(position);
    	
        if (convertView == null) {
        	//LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(textViewId, parent, false);
            holder = new ViewHolder();
            holder.txtName = (TextView)convertView.findViewById(R.id.character_list_item_txtName);
            holder.txtFile = (TextView)convertView.findViewById(R.id.character_list_item_txtFile);
            holder.file = fi.getFile();
            convertView.setTag(holder);
        } else {
        	holder = (ViewHolder)convertView.getTag();
        }
        
    	if(fi.isValid()) {
    		StringBuilder buf = new StringBuilder(fi.getName());
    		buf.append(" (");
    		buf.append(fi.getRace());
    		buf.append(" ");
    		buf.append(fi.getCharacterClass());
    		buf.append(" ");
    		buf.append(fi.getLevel());
    		buf.append(")");
    		holder.txtName.setText(buf.toString());
    	} else {
    		holder.txtName.setText("Invalid dnd4e file");
    	}
    	
    	holder.txtFile.setText(holder.file.getName());

    	return convertView;
    }
}
