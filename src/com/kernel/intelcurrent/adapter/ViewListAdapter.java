package com.kernel.intelcurrent.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ViewListAdapter extends BaseAdapter {

	View views[];
	
	public ViewListAdapter(View views[]){
		this.views = views;
	}
	
	@Override
	public int getCount() {
		return views.length;
	}

	@Override
	public Object getItem(int position) {
		return views[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return views[position];
	}

}
