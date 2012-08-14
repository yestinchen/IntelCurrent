package com.kernel.intelcurrent.activity;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.kernel.intelcurrent.adapter.TimelineListAdapter;
import com.kernel.intelcurrent.adapter.UserFriendListAdapter;
import com.kernel.intelcurrent.model.ICArrayList;
import com.kernel.intelcurrent.model.Status;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.widget.PullToRefreshListView;
import com.kernel.intelcurrent.widget.PullToRefreshListView.OnLoadMoreListener;

public class ShowAllListActivity extends BaseActivity implements Updateable{
	
	public static final int REQUEST_TYPE_RESET=7;
	public static final int REQUEST_TYPE_INIT =8;
	public static final int REQUEST_TYPE_MORE =9;
	private int state=REQUEST_TYPE_RESET;
	private int init_type=1;
	private int startindex=1;
	private int show_list_type;
	private ImageView head_left;
	private TextView head_title;
	private PullToRefreshListView show_list;
	private LinkedList<Status> statuses=new LinkedList<Status>();
	private LinkedList<User> users=new LinkedList<User>();
	private static final String TAG=ShowAllListActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showlist);
		findViews();
		setListener();
		
	}


	private void findViews(){
		head_left=(ImageView)findViewById(R.id.common_head_iv_left);
		head_left.setBackgroundResource(R.drawable.ic_title_back);
		head_title=(TextView)findViewById(R.id.common_head_tv_title);
		show_list=(PullToRefreshListView)findViewById(R.id.activity_showlist_main);
	}
	private void setListener(){
		head_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowAllListActivity.this.finish();
			}
		});
//		show_list.setOnRefreshListener(new OnRefreshListener() {
//			
//			@Override
//			public void onRefresh() {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		show_list.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				state=REQUEST_TYPE_MORE;
				switch(show_list_type){
				case Task.USER_FAV_LIST:
					mService.getUserShoucangList(1, statuses.getLast().timestamp, statuses.getLast().id);
					break;
				case Task.USER_WEIBO_LIST:
					mService.getUserWeiboList(1, statuses.getLast().timestamp, statuses.getLast().id);
					break;
				case Task.USER_FANS_LIST:
					startindex++;
					mService.getUserFansList(startindex);
					break;
				case Task.USER_FRIENDS_LIST:
					startindex++;
					mService.getUserFriendsList(startindex);
					break;
				}
				
			}
		});
	}
	@Override
	public void update(int type, Object param) {
		// TODO Auto-generated method stub
		if(type!=show_list_type)return;
		if(((Task)param).result.size() == 0) return;	
		Log.v(TAG,((Task)param).result.toString());
		switch(type){
			case Task.USER_FAV_LIST:			
				showShoucangList(param);
				break;
			case Task.USER_WEIBO_LIST:
			case Task.USER_OTHER_WEIBO_LIST:
				showUserWeiboList(param);
				break;
			case Task.USER_FANS_LIST:
				showUserFansList(param);
				break;
			case Task.USER_FRIENDS_LIST:
				showUserFriendsList(param);
				break;
			case Task.USER_OTHER_FANS_LIST:
			case Task.USER_OTHER_FRIENDS_LIST:
				showOtherFList(param);
		}

	}
	//处理获取的其他听众和收听列表
	private void showOtherFList(Object param){
		ICArrayList result;
		LinkedList<User> tmpList;
		int hasNext;
		switch(state){
		case REQUEST_TYPE_INIT:
			result=(ICArrayList) ((Task)param).result.get(0);
			hasNext=result.hasNext;
			for(Object user: result.list){
				users.add((User)user);
			}
			if(users.size()!=0)
			show_list.setAdapter(new UserFriendListAdapter(mService,this, users));
			break;
		case REQUEST_TYPE_MORE:
			tmpList=new LinkedList<User>();
			result=(ICArrayList) ((Task)param).result.get(0);
			for(Object user: result.list){
				tmpList.add((User)user);
			}
			users.addAll(users.size(),tmpList);
			show_list.onLoadMoreComplete();
			break;
			
		}
		state  = REQUEST_TYPE_RESET;
	}
	//处理获取的听众列表
	@SuppressWarnings("unchecked")
	private void showUserFansList(Object param){
		ArrayList<User> result;
		LinkedList<User> tmpList;
		switch(state){
		case REQUEST_TYPE_INIT:
			result=(ArrayList<User>) ((Task)param).result.get(0);
			for(User user: result){
				Log.v(TAG, user.toString());
				users.add(user);
			}
			if(users.size()!=0)
			show_list.setAdapter(new UserFriendListAdapter(mService,this, users));
			break;
		case REQUEST_TYPE_MORE:
			tmpList=new LinkedList<User>();
			result=(ArrayList<User>) ((Task)param).result.get(0);
			for(User user: result){
				Log.v(TAG, user.toString());
				tmpList.add(user);
			}
			users.addAll(users.size(),tmpList);
			show_list.onLoadMoreComplete();
			break;
			
		}
		state  = REQUEST_TYPE_RESET;
	}
	//处理获取的收听列表
	@SuppressWarnings("unchecked")
	private void showUserFriendsList(Object param){
		ArrayList<User> result;
		LinkedList<User> tmpList;
		switch(state){
		case REQUEST_TYPE_INIT:
			result=(ArrayList<User>) ((Task)param).result.get(0);
			for(User user: result){
				Log.v(TAG, user.toString());
				users.add(user);
			}
			if(users.size()!=0)
			show_list.setAdapter(new UserFriendListAdapter(mService,this, users));
			break;
		case REQUEST_TYPE_MORE:
			tmpList=new LinkedList<User>();
			result=(ArrayList<User>) ((Task)param).result.get(0);
			for(User user: result){
				Log.v(TAG, user.toString());
				tmpList.add(user);
			}
			users.addAll(users.size(),tmpList);
			show_list.onLoadMoreComplete();
			break;
			
		}
		state  = REQUEST_TYPE_RESET;
	}
	//处理获取的我的用户的微博列表信息
	private void showUserWeiboList(Object param){
		int hasNext;
		ICArrayList result;
		LinkedList<Status> tmpList;
		switch(state){
		case REQUEST_TYPE_INIT:
			result=(ICArrayList) ((Task)param).result.get(0);
			hasNext=result.hasNext;
			for(Object status: result.list){
				statuses.add((Status)status);
			}
			if(statuses.size()!=0){
				show_list.setAdapter(new TimelineListAdapter(this, statuses));
			}
			break;
		case REQUEST_TYPE_MORE:
			tmpList = new LinkedList<Status>();
			result = (ICArrayList)((Task)param).result.get(0);
			hasNext = result.hasNext;
			for(Object status: result.list){
				tmpList.add((Status)status);
			}
			statuses.addAll(statuses.size(), tmpList);
			show_list.onLoadMoreComplete();
			break;
		}
		state  = REQUEST_TYPE_RESET;
	}
	//处理获取的收藏微博列表信息
	private void showShoucangList(Object param){
		int hasNext;
		ICArrayList result;
		LinkedList<Status> tmpList;
		switch(state){
		case REQUEST_TYPE_INIT:
			result=(ICArrayList) ((Task)param).result.get(0);
			hasNext=result.hasNext;
			for(Object status: result.list){
				statuses.add((Status)status);
			}
			if(statuses.size()!=0){
				show_list.setAdapter(new TimelineListAdapter(this, statuses));
			}
			break;
		case REQUEST_TYPE_MORE:
			tmpList = new LinkedList<Status>();
			result = (ICArrayList)((Task)param).result.get(0);
			hasNext = result.hasNext;
			for(Object status: result.list){
				tmpList.add((Status)status);
			}
			statuses.addAll(statuses.size(), tmpList);
			show_list.onLoadMoreComplete();
			break;
		}
		state  = REQUEST_TYPE_RESET;
	}
	


	private void init(){
		state=REQUEST_TYPE_INIT;
	   	   if(mService!=null){
	   		   Log.v(TAG,"mService is not null");
	   	   }else{
	   		   return;
	   	   }
		Intent intent=getIntent();
		if(intent==null)return;
		show_list_type=intent.getIntExtra("show_list_type", -1);
		Log.v(TAG, show_list_type+"");
		String other_name=intent.getStringExtra("other_name");
		String other_nick=intent.getStringExtra("other_nick");
		String other_openid=intent.getStringExtra("other_openid");
		switch(show_list_type){
			case Task.USER_FANS_LIST:
				head_title.setText(R.string.showlist_me_fans);
				mService.getUserFansList(startindex);
				break;
			case Task.USER_FRIENDS_LIST:
				head_title.setText(R.string.showlist_me_follow);
				mService.getUserFriendsList(startindex);
				break;
			case Task.USER_FAV_LIST:
				head_title.setText(R.string.showlist_me_shoucang);
				mService.getUserShoucangList(0, 0, "0");
				break;
			case Task.USER_WEIBO_LIST:
				head_title.setText(R.string.showlist_me_weibo);
				mService.getUserWeiboList(0, 0, "0");
				break;
			case Task.USER_OTHER_FANS_LIST:
				head_title.setText(other_nick+getResources().getString(R.string.showlist_other_fans));
				mService.getOtherFansList(other_name, startindex);
				break;
			case Task.USER_OTHER_FRIENDS_LIST:
				head_title.setText(other_nick+getResources().getString(R.string.showlist_other_follow));
				mService.getOtherFollowList(other_name, startindex);
				break;
			case Task.USER_OTHER_WEIBO_LIST:
				head_title.setText(other_nick+getResources().getString(R.string.showlist_other_weibo));
				mService.getOtherWeiboList(other_openid, 0, 0, "0");
				break;
			default :
				break;
		}
	}

	@Override
	public void onConnectionFinished() {
		// TODO Auto-generated method stub
		if(init_type==1){
			init();
			init_type=-1;
		}
	}

	@Override
	public void onConnectionDisConnected() {
		// TODO Auto-generated method stub
		
	}
}
