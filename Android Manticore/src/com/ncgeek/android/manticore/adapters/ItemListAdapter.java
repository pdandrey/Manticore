package com.ncgeek.android.manticore.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.util.Utility;
import com.ncgeek.manticore.character.inventory.EquipmentManager;
import com.ncgeek.manticore.character.inventory.EquipmentSlot;
import com.ncgeek.manticore.character.inventory.ItemStack;
import com.ncgeek.manticore.items.Armor;
import com.ncgeek.manticore.items.EnchantedItem;
import com.ncgeek.manticore.items.EquippableItem;
import com.ncgeek.manticore.items.Item;
import com.ncgeek.manticore.items.ItemType;
import com.ncgeek.manticore.items.Weapon;
import com.ncgeek.manticore.util.Logger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter implements Observer {

	private EquipmentManager mgr;
	private List<ListItem> lst;
	private LayoutInflater inflater;
	private boolean isItems;
	
	public ItemListAdapter(Context context, boolean isItems) {
		lst = new ArrayList<ListItem>();
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isItems = isItems;
	}
	
	public void setInventory(EquipmentManager manager) {
		if(mgr != null)
			mgr.deleteObserver(this);
		
		mgr = manager;
		
		mgr.addObserver(this);
		
		lst.clear();
		
		if(isItems) {
			HashMap<ItemType,ArrayList<ItemStack>> map = new HashMap<ItemType, ArrayList<ItemStack>>();
			
			for(ItemStack i : mgr.getItems()) {
				if(i.getCount() > 0) {
					ItemType type = i.getItem().getType();
					if(!map.containsKey(type)) {
						map.put(type, new ArrayList<ItemStack>());
					}
					map.get(type).add(i);
				}
			}
			
			ArrayList<ItemType> lstTypes = new ArrayList<ItemType>(map.keySet());
			Collections.sort(lstTypes, new Comparator<ItemType>() {
				@Override
				public int compare(ItemType type1, ItemType type2) {
					return type1.getName().compareToIgnoreCase(type2.getName());
				}
			});
			
			for(ItemType type : lstTypes) {
				lst.add(new ListItem(type));
				for(ItemStack stack : map.get(type)) {
					if(stack.getCount() > 0) {
						lst.add(new ListItem(stack));
					}
				}
			}
		} else {
			Map<EquipmentSlot,EquippableItem> map = mgr.getEquippedItems();
			
			ArrayList<EquipmentSlot> lstTypes = new ArrayList<EquipmentSlot>(map.keySet());
			Collections.sort(lstTypes, new Comparator<EquipmentSlot>() {
				@Override
				public int compare(EquipmentSlot type1, EquipmentSlot type2) {
					return type1.ordinal() - type2.ordinal();
				}
			});
			
			for(EquipmentSlot type : lstTypes) {
				lst.add(new ListItem(type));
				lst.add(new ListItem(map.get(type)));
			}
		}
		
		super.notifyDataSetChanged();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		if(mgr != null) {
			mgr.deleteObserver(this);
			mgr = null;
		}
	}
	
	@Override
	public int getCount() {
		return lst.size();
	}

	@Override
	public Object getItem(int position) {
		return lst.get(position);
	}

	@Override
	public long getItemId(int position) {
		return lst.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ListItem li = lst.get(position);
		ViewHolder holder = null;
		
		if(convertView != null) {
			holder = (ViewHolder)convertView.getTag();
		}
		
		if(convertView == null || (li.isHeader() && holder.tvHeader == null) || (!li.isHeader() && holder.tvName == null)) {
			holder = new ViewHolder();
			if(li.isHeader()) {
				convertView = inflater.inflate(R.layout.item_headeritem, null);
				holder.tvHeader = (TextView)convertView.findViewById(R.id.item_headeritem_tvHeader);
			} else {
				convertView = inflater.inflate(R.layout.item_listitem, null);
				holder.tvName = (TextView)convertView.findViewById(R.id.item_listitem_tvName);
				holder.tvCount = (TextView)convertView.findViewById(R.id.item_listitem_tvCount);
				holder.img = (ImageView)convertView.findViewById(R.id.item_listitem_img);
			}
			convertView.setTag(holder);
		}
		
		if(li.isHeader()) {
			String txt = null;
			if(li.isInventory())
				txt = li.getItemType().getName();
			else
				txt = li.getEquipmentSlot().name();
			
			holder.tvHeader.setText(txt);
		} else {
			int imgID = 0;
			Item item = null;
			
			if(li.isInventory())
				item = li.getItemStack().getItem();
			else
				item = li.getEquippableItem();
			
			if(item instanceof EnchantedItem) {
				item = ((EnchantedItem)item).getItem();
			}
			if(item instanceof Armor) {
				imgID = Utility.getIcon(((Armor)item).getArmorCategory());
			} else if(item instanceof Weapon) {
				imgID = Utility.getIcon(((Weapon)item).getGroups());
			} else {
				imgID = Utility.getIcon(item.getType());
			}
			if(imgID > 0) {
				holder.img.setImageResource(imgID);
			} else {
				holder.img.setImageBitmap(null);
			}
			holder.tvName.setText(item.getName());
			
			if(li.isInventory())
				holder.tvCount.setText(li.getItemStack().getCount() + "");
		}
		
		return convertView;
	}
	
	private static class ViewHolder {
		public TextView tvHeader;
		public ImageView img;
		public TextView tvName;
		public TextView tvCount;
	}

	private static class ListItem {
		private static int totalCount = 0;
		
		private ItemStack item;
		private ItemType header;
		private EquipmentSlot equipmentSlot;
		private EquippableItem equipItem;
		private int id;
		
		private ListItem() {
			id = totalCount++;
			item = null;
			header = null;
			equipmentSlot = null;
			equipItem = null;
		}
		
		public ListItem(ItemStack i) {
			this();
			item = i;
		}
		
		public ListItem(ItemType header) {
			this();
			this.header = header;
		}
		
		public ListItem(EquipmentSlot slot) {
			this();
			equipmentSlot = slot;
		}
		
		public ListItem(EquippableItem item) {
			equipItem = item;
		}
		
		public ItemStack getItemStack() { return item; }
		public ItemType getItemType() { return header; }
		public EquipmentSlot getEquipmentSlot() { return equipmentSlot; }
		public EquippableItem getEquippableItem() { return equipItem; }
		public int getId() { return id; }
		
		public boolean isHeader() { return header != null || equipmentSlot != null; }
		public boolean isInventory() { return header != null || item != null; }
	}

	@Override
	public void update(Observable sender, Object args) {
		Logger.error("LOG_TAG", "ItemListAdapter.update() has not been implemented");
	}
}
