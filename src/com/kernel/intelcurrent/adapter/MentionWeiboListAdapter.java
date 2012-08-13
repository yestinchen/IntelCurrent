package com.kernel.intelcurrent.adapter;

import java.util.List;

import com.kernel.intelcurrent.model.Status;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MentionWeiboListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Status> statuses;
	
	public MentionWeiboListAdapter(Context context,List<Status> statuses){
		mInflater=LayoutInflater.from(context);
		this.statuses=statuses;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return statuses.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
