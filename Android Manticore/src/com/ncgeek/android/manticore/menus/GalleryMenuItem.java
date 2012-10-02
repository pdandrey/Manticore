package com.ncgeek.android.manticore.menus;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;

@Deprecated
public class GalleryMenuItem implements MenuItem {

	private char alphabeticShortcut;
	private int groupID;
	private Drawable icon;
	private Integer iconID;
	private Intent intent;
	private int itemID;
	private char numericShortcut;
	private int order;
	private GallerySubMenu submenu;
	private CharSequence title;
	private CharSequence shortTitle;
	private boolean enabled;
	private boolean visible;
	
	private Resources res;
	
	public GalleryMenuItem(Resources r) {
		res = r;
		enabled = true;
	}
	
	@Override
	public char getAlphabeticShortcut() {
		return alphabeticShortcut;
	}

	@Override
	public int getGroupId() {
		return groupID;
	}

	@Override
	public Drawable getIcon() {
		if(icon == null && iconID != null && iconID != 0) {
			icon = res.getDrawable(iconID);
		}
		return icon;
	}

	@Override
	public Intent getIntent() {
		return intent;
	}

	@Override
	public int getItemId() {
		return itemID;
	}

	@Override
	public ContextMenuInfo getMenuInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getNumericShortcut() {
		return numericShortcut;
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public SubMenu getSubMenu() {
		return submenu;
	}

	@Override
	public CharSequence getTitle() {
		return title;
	}

	@Override
	public CharSequence getTitleCondensed() {
		return shortTitle;
	}

	@Override
	public boolean hasSubMenu() {
		return submenu != null;
	}

	@Override
	public boolean isCheckable() {
		return false;
	}

	@Override
	public boolean isChecked() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public MenuItem setAlphabeticShortcut(char alphaChar) {
		alphabeticShortcut = alphaChar;
		return this;
	}

	@Override
	public MenuItem setCheckable(boolean checkable) {
		return this;
	}

	@Override
	public MenuItem setChecked(boolean checked) {
		return this;
	}

	@Override
	public MenuItem setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	@Override
	public MenuItem setIcon(Drawable icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public MenuItem setIcon(int iconRes) {
		iconID = iconRes;
		return this;
	}

	@Override
	public MenuItem setIntent(Intent intent) {
		this.intent = intent;
		return this;
	}

	@Override
	public MenuItem setNumericShortcut(char numericChar) {
		numericShortcut = numericChar;
		return this;
	}

	@Override
	public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
		return this;
	}

	@Override
	public MenuItem setShortcut(char numericChar, char alphaChar) {
		alphabeticShortcut = alphaChar;
		numericShortcut = numericChar;
		return this;
	}

	@Override
	public MenuItem setTitle(CharSequence title) {
		this.title = title;
		return this;
	}

	@Override
	public MenuItem setTitle(int title) {
		this.title = res.getString(title);
		return this;
	}

	@Override
	public MenuItem setTitleCondensed(CharSequence title) {
		shortTitle = title;
		return this;
	}

	@Override
	public MenuItem setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public MenuItem setOrder(int order) {
		this.order = order;
		return this;
	}
	
	public MenuItem setGroupId(int id) {
		groupID = id;
		return this;
	}
	
	public MenuItem setItemId(int id) {
		itemID = id;
		return this;
	}

	@Override
	public View getActionView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setActionView(View view) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setActionView(int resId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setShowAsAction(int actionEnum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean collapseActionView() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean expandActionView() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ActionProvider getActionProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isActionViewExpanded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MenuItem setActionProvider(ActionProvider actionProvider) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem setShowAsActionFlags(int actionEnum) {
		// TODO Auto-generated method stub
		return null;
	}
}
