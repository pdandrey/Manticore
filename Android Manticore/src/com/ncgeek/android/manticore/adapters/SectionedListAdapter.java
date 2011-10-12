package com.ncgeek.android.manticore.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.ncgeek.android.manticore.R;
import com.ncgeek.android.manticore.activities.PowerViewer;
import com.ncgeek.manticore.util.Logger;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class SectionedListAdapter<H,V> extends BaseAdapter {

	private LayoutInflater inflater;
	
	private Comparator<ListSection<H,V>> headerCompare;
	private int size;
	private List<ListSection<H,V>> lstSections;
	private Class<? extends H> clsHeader;
	private Class<? extends V> clsValue;
	
	private int headerLayoutID;
	private int itemLayoutID;
	
	private Context context;
	
	public SectionedListAdapter(Context context, int headerLayout, int itemLayout, Class<? extends H> headerClass, Class<? extends V> valueClass) {
		this(context, null, headerLayout, itemLayout, headerClass, valueClass);
	}
	public SectionedListAdapter(Context context, final Comparator<H> headerComparator, int headerLayout, int itemLayout, Class<? extends H> headerClass, Class<? extends V> valueClass) {
		this.context = context;
		lstSections = new ArrayList<SectionedListAdapter.ListSection<H,V>>();
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		size = 0;
		headerLayoutID = headerLayout;
		itemLayoutID = itemLayout;
		
		clsHeader = headerClass;
		clsValue = valueClass;
		
		if(headerComparator != null) {
			headerCompare = new Comparator<SectionedListAdapter.ListSection<H,V>>() {
				@Override
				public int compare(ListSection<H, V> object1, ListSection<H, V> object2) {
					return headerComparator.compare(object1.getHeader().getValue(), object2.getHeader().getValue());
				}
			};
		}
	}
	
	protected void add(H header, V value) {
		
		ListSection<H,V> section = new ListSection<H,V>(header);
		int hdrIndex = headerCompare == null ?
				Collections.binarySearch(lstSections, section) :
					Collections.binarySearch(lstSections, section, headerCompare);
		
		if(hdrIndex >= 0) {
			section = lstSections.get(hdrIndex);
		} else {
			hdrIndex = -hdrIndex - 1;
			lstSections.add(hdrIndex, section);
			++size;
		}
		
		section.add(value);
		++size;
		
		super.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return size;
	}

	private ListItem<?> getListItem(int position) {
		for(ListSection<H,V> ls : lstSections) {
			if(position == 0)
				return ls.getHeader();
			else if(position <= ls.size())
				return ls.get(position - 1);
			else
				position -= (ls.size() + 1);
		}
		return null;
	}
	
	@Override
	public Object getItem(int position) {
		ListItem<?> li = getListItem(position);
		return li.getValue();
	}

	@Override
	public long getItemId(int position) {
		return getListItem(position).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ListItem<?> li = getListItem(position);
		ISectionedViewHolder holder = null;
		
		if(convertView != null) {
			holder = (ISectionedViewHolder)convertView.getTag();
		}
		
		boolean isHeader = clsHeader.isInstance(li.getValue());
		
		if(convertView == null || (isHeader && !holder.isHeader()) || (!isHeader && !holder.isValue())) {
			if(isHeader) {
				convertView = inflater.inflate(headerLayoutID, null);
			} else {
				convertView = inflater.inflate(itemLayoutID, null);
			}
			holder = createViewHolder(convertView, isHeader);
			convertView.setTag(holder);
		}
		
		fillView(holder, li.getValue());
		
		return convertView;
	}
	
	protected abstract void fillView(ISectionedViewHolder holder, Object value);
	
	protected abstract ISectionedViewHolder createViewHolder(View view, boolean isHeader);
	
	private static class ListSection<H,V> implements Comparable<ListSection<H,V>>{
		
		private ListItem<H> header;
		private List<ListItem<V>> lstItems;
		private Comparator<ListItem<V>> comparator;
		
		private ListSection() {
			lstItems = new ArrayList<ListItem<V>>();
		}
		
		public ListSection(H header) {
			this();
			this.header = new ListItem<H>(header);
			comparator = null;
		}
		
		public ListSection(H header, final Comparator<V> compare) {
			this();
			this.header = new ListItem<H>(header);
			comparator = new Comparator<SectionedListAdapter.ListItem<V>>() {
				@Override
				public int compare(ListItem<V> object1, ListItem<V> object2) {
					return compare.compare(object1.getValue(), object2.getValue());
				}
			};
		}
		
		public ListItem<H> getHeader() { return header; }
		
		public int size() { return lstItems.size(); }
		
		public void add(V value) {
			ListItem<V> li = new ListItem<V>(value);
			int idx = comparator == null ?
					Collections.binarySearch(lstItems, li) :
						Collections.binarySearch(lstItems, li, comparator);
			if(idx < 0)
				idx = -idx - 1;
			lstItems.add(idx, li);
		}
		
		public ListItem<V> get(int position) {
			return lstItems.get(position);
		}

		@Override
		public int compareTo(ListSection<H, V> another) {
			return header.toString().compareToIgnoreCase(another.getHeader().toString());
		}
	}
	
	private static final class ListItem<V> implements Comparable<ListItem<V>> {
		private static int id_counter = 0;
		
		private int ID;
		private V value;
		public ListItem(V val) {
			ID = id_counter++;
			value = val;
		}
		public int getID() { return ID; }
		public V getValue() { return value; }
		@Override 
		public String toString() { return value.toString(); }
		@Override
		public int compareTo(ListItem<V> another) {
			return value.toString().compareToIgnoreCase(another.getValue().toString());
		}
	}
}
