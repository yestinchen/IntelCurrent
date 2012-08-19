package com.kernel.intelcurrent.adapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kernel.intelcurrent.activity.OtherUserInfoActivity;
import com.kernel.intelcurrent.activity.R;
import com.kernel.intelcurrent.model.DBModel;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.widget.UrlImageView;

public class GroupFollowListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private LinkedList<User>users;
	private ArrayList<String>userids;
	private int gid;
	public GroupFollowListAdapter(Context context,LinkedList<User>users,ArrayList<String>userids,int gid) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.users=users;
		this.mInflater=LayoutInflater.from(context);
		this.userids=userids;
		this.gid=gid;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		boolean flag;
		User user=users.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.group_userlist_cell, null);
			holder.layout=(LinearLayout)convertView.findViewById(R.id.group_userlist_cell_layout);
			holder.name=(TextView)convertView.findViewById(R.id.group_user_list_cell_name);
			holder.user_info=(Button)convertView.findViewById(R.id.group_user_list_cell_info);
			holder.check=(TextView)convertView.findViewById(R.id.group_user_list_cell_btn);
			holder.head=(UrlImageView)convertView.findViewById(R.id.group_user_list_cell_head);
			holder.check.setBackgroundResource(R.drawable.ic_group_user_add_check_nor);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();			
			holder.head.reset();
		}	
		if(DBModel.getInstance().UserIsExists(context, gid, user.id)){
			holder.check.setBackgroundResource(R.drawable.ic_group_user_add_confirm);
		}else{
			if(userids.contains(user.id)){
				holder.check.setBackgroundResource(R.drawable.ic_group_user_add_check_ok);
				flag=true;
			}else{
				holder.check.setBackgroundResource(R.drawable.ic_group_user_add_check_nor);
				flag=false;
			}
			holder.check.setOnClickListener(new OnBtnClickListener(user,flag));
		}
		holder.name.setText(user.nick+"("+user.name+")");
		Drawable platform_img=null;
		if(user.platform==User.PLATFORM_TENCENT_CODE){
			platform_img=context.getResources().getDrawable(R.drawable.ic_platform_small_tencent);
			platform_img.setBounds(0, 0, 23,23);
			
		}else if(user.platform==User.PLATFORM_SINA_CODE){
			platform_img=context.getResources().getDrawable(R.drawable.ic_platform_small_sina);
			platform_img.setBounds(0, 0, 23,23);
		}
			holder.user_info.setCompoundDrawables(platform_img, null, null, null);
			holder.user_info.setText("共"+user.fansnum+"位听众");
		try {
				holder.head.bindUrl(user.head,UrlImageView.PLAT_FORM_TENCENT,UrlImageView.LARGE_HEAD);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		//holder.layout.setOnClickListener(new OnBtnClickListener(user));
		return convertView;
	}
	 class ViewHolder{
		TextView name;
		Button user_info;
		TextView check;
		UrlImageView head;
		LinearLayout layout;
	}
	 private  class OnBtnClickListener implements OnClickListener {
			User user;
			boolean flag;
		public OnBtnClickListener(User user,boolean flag){
			this.user=user;
			this.flag=flag;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(v.getId()==R.id.group_user_list_cell_btn){		
					TextView tv=(TextView)v;
					if(!flag){
						tv.setBackgroundResource(R.drawable.ic_group_user_add_check_ok);
						userids.add(user.id);
						flag=true;
					}else{
						tv.setBackgroundResource(R.drawable.ic_group_user_add_check_nor);
						for(int i=0;i<userids.size();i++){
							if(userids.get(i).equals(user.id)){
								userids.remove(i);
							}
						}
						if(userids.contains(user.id))userids.remove(user.id);
						Log.v("dd","click");
						if(userids.contains(user.id)){Log.v("dd", "contain");}else{
							Log.v("ff", "no contain");
						}
						flag=false;
					}
			}else{
				Toast.makeText(context, user.nick, Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(context,OtherUserInfoActivity.class);
				intent.putExtra("user_openid",user.id);
				intent.putExtra("user_nick", user.nick);
				context.startActivity(intent);		
			}
		}
	}
}
