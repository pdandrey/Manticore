package com.ncgeek.android.manticore.menus;

import java.util.ArrayList;

import com.ncgeek.manticore.NotImplementedException;
import com.ncgeek.manticore.util.Logger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

@Deprecated
public class GalleryMenu implements Menu {

	private Resources res;
	private ArrayList<GalleryMenuItem> items;
	
	public GalleryMenu(Resources r) {
		res = r;
		items = new ArrayList<GalleryMenuItem>();
	}
	
	@Override
	public MenuItem add(CharSequence title) {
		GalleryMenuItem mi = new GalleryMenuItem(res);
		items.add(mi);
		
		mi.setTitle(title);
		
		return mi;
	}

	@Override
	public MenuItem add(int titleRes) {
		CharSequence title = res.getString(titleRes);
		return add(title);
	}

	@Override
	public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
		GalleryMenuItem mi = (GalleryMenuItem)add(title);
		mi.setGroupId(groupId);
		mi.setItemId(itemId);
		mi.setOrder(order);
		return mi;
	}

	@Override
	public MenuItem add(int groupId, int itemId, int order, int titleRes) {
		return add(groupId, itemId, order, res.getString(titleRes));
	}

	@Override
	public int addIntentOptions(int groupId, int itemId, int order,
			ComponentName caller, Intent[] specifics, Intent intent, int flags,
			MenuItem[] outSpecificItems) {
		throw new NotImplementedException();
	}

	@Override
	public SubMenu addSubMenu(CharSequence title) {
		GallerySubMenu mnu = new GallerySubMenu();
		
		return mnu;
	}

	@Override
	public SubMenu addSubMenu(int titleRes) {
		return addSubMenu(res.getString(titleRes));
	}

	@Override
	public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
		GallerySubMenu mnu = new GallerySubMenu();
		return mnu;
	}

	@Override
	public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
		return addSubMenu(groupId, itemId, order, res.getString(titleRes));
	}

	@Override
	public void clear() {
		items.clear();
	}

	@Override
	public void close() {
	}

	@Override
	public MenuItem findItem(int id) {
		for(GalleryMenuItem mi : items)
			if(mi.getItemId() == id)
				return mi;
		return null;
	}

	@Override
	public MenuItem getItem(int index) {
		return items.get(index);
	}
	
	public GalleryMenuItem getVisibleItem(int index) {
		Logger.debug("Gallery Menu", "Finding visible item at index " + index);
		for(int i=0; i<=index; ++i) {
			GalleryMenuItem mi = items.get(i);
			if(mi.isVisible()) {
				if(i == index)
					return mi;
			} else {
				++index;
			}
		}
		return null;
	}

	@Override
	public boolean hasVisibleItems() {
		for(GalleryMenuItem mi : items)
			if(mi.isVisible())
				return true;
		return false;
	}
	
	public int getVisibleCount() {
		int count = 0;
		for(GalleryMenuItem mi : items) {
			if(mi.isVisible())
				++count;
		}
		return count;
	}

	@Override
	public boolean isShortcutKey(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean performIdentifierAction(int id, int flags) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeGroup(int groupId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeItem(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGroupCheckable(int group, boolean checkable,
			boolean exclusive) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGroupEnabled(int group, boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGroupVisible(int group, boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setQwertyMode(boolean isQwerty) {
		// TODO Auto-generated method stub

	}

	@Override
	public int size() {
		return items.size();
	}

}
