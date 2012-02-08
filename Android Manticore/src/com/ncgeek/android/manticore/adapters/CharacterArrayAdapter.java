package com.ncgeek.android.manticore.adapters;

import java.util.List;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.data.model.CharacterModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CharacterArrayAdapter extends ArrayAdapter<CharacterModel> {
	
	private static class ViewHolder {
		TextView txtName;
	}
    public CharacterArrayAdapter(Context context, List<CharacterModel> items) {
        super(context, -1, items);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            
    	ViewHolder holder = null;
    	CharacterModel ch = getItem(position);
    	
        if (convertView == null) {
        	LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.loadcharacter_listitem, parent, false);
            holder = new ViewHolder();
            holder.txtName = (TextView)convertView;
            convertView.setTag(holder);
        } else {
        	holder = (ViewHolder)convertView.getTag();
        }
        
        if(ch == null)
        	holder.txtName.setText("Import Character");
    	else
    		holder.txtName.setText(ch.getName());

    	return convertView;
    }
}
