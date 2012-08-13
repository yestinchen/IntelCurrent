package com.kernel.intelcurrent.activity;

import java.util.LinkedList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.kernel.intelcurrent.model.Status;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.service.MainService;
import com.kernel.intelcurrent.service.MainService.ICBinder;
import com.kernel.intelcurrent.widget.PullToRefreshListView;
import com.kernel.intelcurrent.widget.PullToRefreshListView.OnLoadMoreListener;
import com.kernel.intelcurrent.widget.PullToRefreshListView.OnRefreshListener;

public class ShowAllListActivity extends Activity implements Updateable{
	public static final int SHOW_ME_FANS_LIST=0;
	public static final int SHOW_ME_SHOUCANG_LIST=1;
	public static final int SHOW_ME_FOLLOW_LIST=2;
	public static final int SHOW_ME_WEIBO_LIST=3;
	public static final int SHOW_OTHER_FANS_LIST=10;
	public static final int SHOW_OTHER_FOLLOW_LIST=11;
	public static final int SHOW_OTHER_WEIBO_LIST=12;
	private int show_list_type;
	private ImageView head_left;
	private TextView head_title;
	private PullToRefreshListView show_list;
	private LinkedList<Status> statuses=new LinkedList<Status>();
	private static final String TAG=ShowAllListActivity.class.getSimpleName();
	protected MainService mService;
	boolean isBound=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showlist);
		findViews();
		setListener();
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
	   	   super.onStart();
	   	   Intent intent=new Intent(this,MainService.class);
	   	   bindService(intent,connection, Context.BIND_AUTO_CREATE);

	}
    protected void onStop()
	{
	   super.onStop();
	   unbindService(connection);
	   isBound=false;
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
		show_list.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
			}
		});
		show_list.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	public void update(int type, Object param) {
		// TODO Auto-generated method stub
		if(type!=Task.USER_FANS_LIST&&type!=Task.USER_FAV_LIST&&type!=Task.USER_FRIENDS_LIST
				&&type!=Task.USER_WEIBO_LIST&&type!=Task.USER_OTHER_FANS_LIST
				&&type!=Task.USER_OTHER_FRIENDS_LIST&&type!=Task.USER_OTHER_WEIBO_LIST)return;
		if(((Task)param).result.size() == 0) return;	
		Log.v(TAG,((Task)param).result.toString());
	}
	   /**
     * inner class:ServiceConnection
     * */
    private ServiceConnection connection= new ServiceConnection()
    {

		public void onServiceConnected(ComponentName className, IBinder service)
		{
			ICBinder binder=(ICBinder)service;
			mService=binder.getService();
			isBound=true;
			mService.changeCurrentActivity(ShowAllListActivity.this);	
			init();
			
		}

		public void onServiceDisconnected(ComponentName name) 
		{
			isBound = false;
		}
    	
    };
	private void init(){
	   	   if(mService!=null){
	   		   Log.v(TAG,"mService is not null");
	   	   }else{
	   		   return;
	   	   }
		Intent intent=getIntent();
		show_list_type=intent.getIntExtra("show_list_type", -1);
		Log.v(TAG, show_list_type+"");
		String other_name=intent.getStringExtra("other_name");
		switch(show_list_type){
			case SHOW_ME_FANS_LIST:
				head_title.setText(R.string.showlist_me_fans);
				mService.getUserFansList(1);
				break;
			case SHOW_ME_FOLLOW_LIST:
				head_title.setText(R.string.showlist_me_follow);
				mService.getUserFriendsList(1);
				break;
			case SHOW_ME_SHOUCANG_LIST:
				head_title.setText(R.string.showlist_me_shoucang);
				mService.getUserShoucangList(0, 0, "0");
				break;
			case SHOW_ME_WEIBO_LIST:
				head_title.setText(R.string.showlist_me_weibo);
				mService.getUserWeiboList(0, 0, "0");
				break;
			case SHOW_OTHER_FANS_LIST:
				head_title.setText(other_name+R.string.showlist_other_fans);
				break;
			case SHOW_OTHER_FOLLOW_LIST:
				head_title.setText(other_name+R.string.showlist_other_follow);
				break;
			case SHOW_OTHER_WEIBO_LIST:
				head_title.setText(other_name+R.string.showlist_other_weibo);
				break;
			default :
				break;
		}
	}
}
