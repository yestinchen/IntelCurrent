package com.kernel.intelcurrent.adapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kernel.intelcurrent.activity.OtherUserInfoActivity;
import com.kernel.intelcurrent.activity.R;
import com.kernel.intelcurrent.model.DBModel;
import com.kernel.intelcurrent.model.Group;
import com.kernel.intelcurrent.model.SimpleUser;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.widget.UrlImageView;

public class GroupUserListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context context;
	private LinkedList<User> users;
	private Group group;
	public GroupUserListAdapter(Context context,LinkedList<User> users,Group group){
		this.context=context;
		mInflater=LayoutInflater.from(context);
		this.users=users;
		this.group=group;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return users.get(position);
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
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.group_userlist_cell, null);
			holder.user_layout=(LinearLayout)convertView.findViewById(R.id.group_userlist_cell_layout);
			holder.user_name=(TextView)convertView.findViewById(R.id.group_user_list_cell_name);
			holder.user_info=(Button)convertView.findViewById(R.id.group_user_list_cell_info);
			holder.user_del=(TextView)convertView.findViewById(R.id.group_user_list_cell_btn);
			holder.user_head=(UrlImageView)convertView.findViewById(R.id.group_user_list_cell_head);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
			holder.user_head.reset();
		}
		User user=users.get(position);
		holder.user_name.setText(user.nick+"("+user.name+")");
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
				holder.user_head.bindUrl(user.head,UrlImageView.PLAT_FORM_TENCENT,UrlImageView.LARGE_HEAD);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		holder.user_del.setOnClickListener(new OnBtnClickListener(user));
		holder.user_layout.setOnClickListener(new OnBtnClickListener(user));
		return convertView;
	}
	class ViewHolder{
		TextView user_name,user_del;
		Button user_info;
		UrlImageView user_head;
		LinearLayout user_layout;
	}
	private  class OnBtnClickListener implements OnClickListener {
			User user;
		public OnBtnClickListener(User user){
			this.user=user;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.group_user_list_cell_btn){
			AlertDialog.Builder builder=new AlertDialog.Builder(context);
			builder.setCancelable(false).setMessage("确定删除该组员吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SimpleUser simpleuser=new SimpleUser();
					simpleuser.id=user.id;
					simpleuser.platform=user.platform;
					DBModel.getInstance().delUser(context, group.name.hashCode(), simpleuser);
					Toast.makeText(context, "已经成功删除了"+user.nick, Toast.LENGTH_SHORT).show();
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
					dialog.dismiss();
				}
			}).show();
			
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
