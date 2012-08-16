package com.kernel.intelcurrent.adapter;

import java.util.List;

import com.kernel.intelcurrent.activity.R;
import com.kernel.intelcurrent.activity.OrderCenterListActivity.GroupEntry;
import com.kernel.intelcurrent.model.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderListAdapter extends BaseAdapter {

	private List<GroupEntry> list;
	private LayoutInflater mInflater;
	public OrderListAdapter(Context context,List<GroupEntry> list){
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.cell_order_list_group, null);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if(holder == null){
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.cell_order_list_group_tv);
			holder.iv = (ImageView) convertView.findViewById(R.id.cell_order_list_group_iv);
		}
		GroupEntry entry = list.get(position);
		holder.tv.setText(entry.group.name);
		holder.iv.setBackgroundResource(entry.exists ? R.drawable.ic_order_group_added : R.drawable.ic_order_group_add);
		return convertView;
	}
	
	class ViewHolder{
		TextView tv;
		ImageView iv;
	}

}
