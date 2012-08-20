package com.kernel.intelcurrent.adapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kernel.intelcurrent.activity.OtherUserInfoActivity;
import com.kernel.intelcurrent.activity.R;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.service.MainService;
import com.kernel.intelcurrent.widget.UrlImageView;

public class UserFriendListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context context;
	private MainService mService;
	private List<User> users;
	private static final String TAG=UserFriendListAdapter.class.getSimpleName();
	public UserFriendListAdapter(MainService mService,Context context,List<User> users){
		mInflater=LayoutInflater.from(context);
		this.context=context;
		this.users=users;
		this.mService=mService;
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
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.user_friendslist_cell, null);
			holder.user_layout=(LinearLayout)convertView.findViewById(R.id.user_list_cell_layout);
			holder.user_name=(TextView)convertView.findViewById(R.id.user_list_cell_name);
			holder.user_info=(TextView)convertView.findViewById(R.id.user_list_cell_info);
			holder.user_is_friend=(TextView)convertView.findViewById(R.id.user_list_cell_btn);
			holder.user_head=(UrlImageView)convertView.findViewById(R.id.user_list_cell_head);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
			holder.user_head.reset();
		}
		User user=users.get(position);
		holder.user_name.setText(user.nick+"("+user.name+")");
		holder.user_info.setText(user.location);
		holder.user_is_friend.setText(user.ismyidol?R.string.user_follow_cancle:R.string.user_follow_add);
		holder.user_layout.setOnClickListener(new OnBtnClickListener(user));
		holder.user_is_friend.setOnClickListener(new OnBtnClickListener(user));
		try {
			holder.user_head.bindUrl(user.head,UrlImageView.PLAT_FORM_TENCENT,UrlImageView.LARGE_HEAD);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	private  class OnBtnClickListener implements OnClickListener {
		User user;
		public OnBtnClickListener(User user){
			this.user=user;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.user_list_cell_btn){
			AlertDialog.Builder builder=new AlertDialog.Builder(context);
			String message=null;
			final TextView tv=(TextView)v;
			if(tv.getText().equals(context.getResources().getString(R.string.user_follow_add))){
				message=context.getResources().getString(R.string.user_follow_add);
			}else{
				message=context.getResources().getString(R.string.user_follow_cancle);
			}
			builder.setCancelable(false).setMessage("确定"+message+"吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(context, user.name+"operation", Toast.LENGTH_SHORT).show();
					if(tv.getText().equals(context.getResources().getString(R.string.user_follow_add))){
						userFollowAdd(user.name);//确定添加收听
						tv.setText(R.string.user_follow_cancle);
					}else{
						userFollowCancle(user.name);//确定取消收听
						tv.setText(R.string.user_follow_add);
					}
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
				intent.putExtra("user_name", user.name);
				context.startActivity(intent);
				
			}
		}
		
	}
	class ViewHolder{
		TextView user_name,user_info,user_is_friend;
		UrlImageView user_head;
		LinearLayout user_layout;
	}
	private void userFollowAdd(String name){
		if(mService!=null){
			Log.v(TAG,"Get SERVICE SUCCESS");
			mService.addFriend(name);
		}
	}
	private void userFollowCancle(String name){
		if(mService!=null){
			Log.v(TAG,"Get SERVICE SUCCESS");
			mService.delFriend(name);
		}
	}
}
