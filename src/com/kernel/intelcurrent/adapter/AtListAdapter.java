package com.kernel.intelcurrent.adapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.kernel.intelcurrent.activity.R;
import com.kernel.intelcurrent.model.SimpleUser;
import com.kernel.intelcurrent.widget.UrlImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AtListAdapter extends BaseAdapter {

	private Context context;
	private List<SimpleUser> list;
	private LayoutInflater mInflater;
	
	public AtListAdapter(Context context, List<SimpleUser> list){
		this.context = context;
		this.list = list;
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
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(view == null){
			view = mInflater.inflate(R.layout.cell_user_simple_list, null);
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		if(holder == null){
			holder = new ViewHolder();
			holder.nameTv = (TextView)view.findViewById(R.id.cell_user_simple_list_name);
			holder.nickTv = (TextView)view.findViewById(R.id.cell_user_simple_list_nick);
			holder.headIv = (UrlImageView)view.findViewById(R.id.cell_user_simple_list_head);
		}
		SimpleUser user = list.get(position);
		holder.nickTv.setText(user.nick);
		holder.nameTv.setText("@"+user.name);
		try {
			holder.headIv.bindUrl(user.head, UrlImageView.PLAT_FORM_TENCENT, UrlImageView.LARGE_HEAD);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return view;
	}

	class ViewHolder{
		public TextView nameTv,nickTv;
		public UrlImageView headIv;
	}
}
