package com.kernel.intelcurrent.adapter;

import java.util.ArrayList;

import com.kernel.intelcurrent.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MoreListAdapter extends android.widget.BaseAdapter {

	private ArrayList<MoreListEntry> list;
	private Context context;
	private LayoutInflater mInflater;
	
	public MoreListAdapter(Context context,ArrayList<MoreListEntry> list){
		this.list = list;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.cell_more_list, null);
		}
		holder = (ViewHolder) convertView.getTag();
		if(holder == null){
			holder = new ViewHolder();
			holder.nameTv = (TextView) convertView.findViewById(R.id.cell_more_list_tv_name);
			holder.numTv = (TextView) convertView.findViewById(R.id.cell_more_list_tv_num);
			convertView.setTag(holder);
		}
		
		holder.nameTv.setText(list.get(position).name);
		holder.numTv.setText(list.get(position).tip == 0 ? "":list.get(position).tip+"");
		return convertView;
	}


	class ViewHolder{
		public TextView nameTv,numTv;
	}
	
	public static class MoreListEntry{
		public MoreListEntry(String name,int tip){
			this.name = name;
			this.tip = tip;
		}
		public String name;
		public int tip;
	}
}
