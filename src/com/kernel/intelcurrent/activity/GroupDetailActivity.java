package com.kernel.intelcurrent.activity;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kernel.intelcurrent.adapter.GroupUserListAdapter;
import com.kernel.intelcurrent.model.DBModel;
import com.kernel.intelcurrent.model.Group;
import com.kernel.intelcurrent.model.SimpleUser;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;

public class GroupDetailActivity extends BaseActivity implements Updateable{
	private TextView group_title,group_gname,group_user_nums,group_edit,group_search;
	private ImageView group_left,group_right,group_bg;
	private ListView listview;
	private RelativeLayout loading_layout;
	private GroupUserListAdapter gadapter;
	private  int userlist_next=0;
	private LinkedList<User> users;
	private static final int REQUEST_FIRST = 1;
	private static final int REQUEST_LOAD_MORE = 2;
	private int request = -1;
	private Group group;
	private long curtime;
	private Bundle bundle;
	private static final String TAG=GroupDetailActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_detail);
		Intent intent=getIntent();
		bundle=intent.getBundleExtra("bundle");
		group=(Group) bundle.get("group");
		findViews();
		setListener();
	}
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		users=new LinkedList<User>();
		ArrayList<SimpleUser> susers=DBModel.getInstance().getUsersByGname(GroupDetailActivity.this,group.name);
		group.users.clear();
		group.users.addAll(susers);
		group_user_nums.setText("共"+group.users.size()+"个组员");
		if(group.users.size()==0){
			loading_layout.setVisibility(View.GONE);
		}
	}


	private void findViews(){
		group_title=(TextView)findViewById(R.id.common_head_tv_title);
		group_title.setText(group.name);
		group_left=(ImageView)findViewById(R.id.common_head_iv_left);
		group_bg=(ImageView)findViewById(R.id.group_detail_bg);
		group_gname=(TextView)findViewById(R.id.group_gname);
		group_gname.setText(getResources().getString(R.string.group_detail_gname)+group.name);
		group_edit=(TextView)findViewById(R.id.group_edit_btn);
		group_search=(TextView)findViewById(R.id.group_search_btn);
		group_user_nums=(TextView)findViewById(R.id.group_user_nums);	
		listview=(ListView)findViewById(R.id.group_user_list);
		group_left.setBackgroundResource(R.drawable.ic_title_back);
		group_right=(ImageView)findViewById(R.id.common_head_iv_right);
		group_right.setBackgroundResource(R.drawable.ic_group_user_add);
		loading_layout=(RelativeLayout)findViewById(R.id.group_user_loading);
	}
	private void setListener(){
		group_edit.setOnClickListener(new OnBtnClickListener());
		group_search.setOnClickListener(new OnBtnClickListener());
		group_left.setOnClickListener(new OnBtnClickListener());
		group_right.setOnClickListener(new OnBtnClickListener());
	}
	private class OnBtnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.group_edit_btn:			
				break;
			case R.id.group_search_btn:
				break;
			case R.id.common_head_iv_left:
				GroupDetailActivity.this.finish();
				break;
			case R.id.common_head_iv_right:
				Intent intent =new Intent(GroupDetailActivity.this,GroupUserAddActivity.class);
				intent.putExtra("gbundle", bundle);
				startActivity(intent);
				break;
			}
		}
		
	}
	@Override
	public void onConnectionFinished() {
		// TODO Auto-generated method stub
		request=REQUEST_FIRST;
		StringBuilder openids = new StringBuilder();
		if(group.users.size()!=0){
			if(group.users.size()>30){ //当组中人数超过30个另行处理
				userlist_next=30;
				for(int i=0;i<30;i++){
				 openids.append(group.users.get(i).id).append("_");
				}
			}else{
				for(SimpleUser user:group.users){
					openids.append(user.id).append("_");
				}
			}
			Log.v(TAG, "get users");
			if(mService==null){
				Log.v(TAG, "mservice is null");
			}else{
				curtime=System.currentTimeMillis();
				mService.getSimpleUserList(openids.toString(),curtime);
			}
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
		if(type!=Task.USER_SIMPLE_INFO_LIST)return;
		if(curtime!=((Task)param).time)return;
		if(((Task)param).result.size() == 0) return;	
		ArrayList<User> result;
		LinkedList<User> tmpList;
		loading_layout.setVisibility(View.GONE);
		if(request == REQUEST_FIRST){
			result=(ArrayList<User>) ((Task)param).result.get(0);
			for(User user: result){
				users.add(user);
			}
			setAdapter();
		}else if(request == REQUEST_LOAD_MORE){
			tmpList=new LinkedList<User>();
			result=(ArrayList<User>) ((Task)param).result.get(0);
			for(User user: result){
				tmpList.add(user);
			}
			users.addAll(users.size(),tmpList);
			gadapter.notifyDataSetChanged();
		}
		request = -1;
		
	}
	private void setAdapter() {
		gadapter=new GroupUserListAdapter(this, users,group,group_user_nums);
		if(users.size()!=0){
			listview.setAdapter(gadapter);
		}
		
	}

}
