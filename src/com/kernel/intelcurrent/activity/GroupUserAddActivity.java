package com.kernel.intelcurrent.activity;

import java.util.ArrayList;
import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kernel.intelcurrent.adapter.GroupFollowListAdapter;
import com.kernel.intelcurrent.model.DBModel;
import com.kernel.intelcurrent.model.Group;
import com.kernel.intelcurrent.model.SimpleUser;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;

public class GroupUserAddActivity extends BaseActivity implements Updateable{
	private ImageView list_return,list_ok;
	private TextView loadMore;
	private View footview;
	private ListView follow_list;
	private Group group;
	private LinkedList<User>users;
	private ArrayList<String>userids;
	private GroupFollowListAdapter gflAdapter;
	public static final int REQUEST_TYPE_RESET=-1;
	public static final int REQUEST_TYPE_INIT =1;
	public static final int REQUEST_TYPE_MORE =2;
	private int state=REQUEST_TYPE_RESET;
	private int startindex=1;
	private int onstate=1;
	private static final String TAG=GroupUserAddActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_useradd);
		Intent intent=getIntent();
		Bundle bundle=intent.getBundleExtra("gbundle");
		group=(Group) bundle.get("group");
		findViews();
	}
	public void findViews(){
		footview=LayoutInflater.from(this).inflate(R.layout.common_foot_load_more, null);
		loadMore = (TextView)footview.findViewById(R.id.common_foot_load_more_tv);
		loadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startindex++;
				if(state==REQUEST_TYPE_RESET){
					state=REQUEST_TYPE_MORE;
					mService.getUserFriendsList(startindex);
					loadMore.setText(R.string.common_loading);
				}
			}
		});
		list_ok=(ImageView)findViewById(R.id.group_myfollowlist_down);
		follow_list=(ListView)findViewById(R.id.group_myfollowlist);
		follow_list.addFooterView(footview);
		list_return=(ImageView)findViewById(R.id.group_myfollowlist_confirm);
		list_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GroupUserAddActivity.this.finish();
			}
		});
		list_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final LinkedList<SimpleUser>susers=new LinkedList<SimpleUser>();
				if(userids.size()!=0){
					for(String id:userids){
						SimpleUser suser=new SimpleUser();
						suser.id=id;
						suser.platform=User.PLATFORM_TENCENT_CODE;
						susers.add(suser);
					}
					AlertDialog.Builder builder=new AlertDialog.Builder(GroupUserAddActivity.this);
					builder.setMessage("确认添加到组:"+group.name+"吗？").setCancelable(false)
					.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							DBModel.getInstance().addUsers(GroupUserAddActivity.this, group.name.hashCode(), susers);
							userids.clear();
							gflAdapter.notifyDataSetChanged();
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
					Toast.makeText(GroupUserAddActivity.this, "请选择要添加的组员！",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v(TAG, "onstart");

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(TAG, "onresume");
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v(TAG, "onstop()");
		onstate=2;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v(TAG, "onpause()");
	}
	@Override
	public void onConnectionFinished() {
		// TODO Auto-generated method stub
		if(onstate==1){
			state=REQUEST_TYPE_INIT;
			users=new LinkedList<User>();
			userids=new ArrayList<String>();
			mService.getUserFriendsList(startindex);
		}
	}

	@Override
	public void onConnectionDisConnected() {
		// TODO Auto-generated method stub

	}
	@SuppressWarnings("unchecked")
	@Override
	public void update(int type, Object param) {
		// TODO Auto-generated method stub
		if(type!=Task.USER_FRIENDS_LIST)return;
		if(((Task)param).result.size()==0){return;}
		ArrayList<User> result;
		LinkedList<User> tmpList;
		switch(state){
		case REQUEST_TYPE_INIT:
			result=(ArrayList<User>) ((Task)param).result.get(0);
			for(User user: result){
				users.add(user);
			}
			if(users.size()!=0){
				gflAdapter=new GroupFollowListAdapter(this, users, userids,group.name.hashCode());
				follow_list.setAdapter(gflAdapter);
			}
			break;
		case REQUEST_TYPE_MORE:
			tmpList=new LinkedList<User>();
			result=(ArrayList<User>) ((Task)param).result.get(0);
			for(User user: result){
				tmpList.add(user);
			}
			users.addAll(users.size(),tmpList);
			gflAdapter.notifyDataSetChanged();
			loadMore.setText(R.string.common_load_more);
			break;
			
		}
		state  = REQUEST_TYPE_RESET;
	}

}
