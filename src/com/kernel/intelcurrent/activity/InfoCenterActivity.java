package com.kernel.intelcurrent.activity;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kernel.intelcurrent.adapter.TimelineListAdapter;
import com.kernel.intelcurrent.model.ICArrayList;
import com.kernel.intelcurrent.model.Status;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.service.MainService;
import com.kernel.intelcurrent.widget.PullToRefreshListView;
import com.kernel.intelcurrent.widget.PullToRefreshListView.OnLoadMoreListener;
import com.kernel.intelcurrent.widget.PullToRefreshListView.OnRefreshListener;

public class InfoCenterActivity extends Activity implements Updateable{
	private TextView info_title,info_at_me,info_comments,info_msg; 
	private static final int INFO_AT_POSITION=0;
	private static final int INFO_COMMENTS_POSITION=1;
	private static final int INFO_MSG_POSITION=2;
	private int info_curPosition=INFO_AT_POSITION;
	private int cur_type;
	private Context context;
	private PullToRefreshListView show_at_list,show_comment_list,show_msg_list;
	private LinearLayout showlayout;
	private MainActivity activityGroup;
	private MainService mService;
	private RelativeLayout loading_layout;
	public static final int REQUEST_TYPE_INIT = 1;
	public static final int REQUEST_TYPE_REFRESH = 2;
	public static final int REQUEST_TYPE_PAGE_DOWN = 3;
	public static final int REQUEST_TYPE_RESET=-1;
	private LinkedList<Status> statuses=new LinkedList<Status>();
	private int hasNext = -1;
	private int state =REQUEST_TYPE_RESET;
	private int type; //保存当前获取的提及微博列表的类型   0:为获取所有提及信息
	private static final String TAG=InfoCenterActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activityGroup = (MainActivity)getParent();
		mService = activityGroup.getService();
		setContentView(R.layout.activity_info);
		findViews();
		init();
	}
	private void findViews(){
		context=InfoCenterActivity.this;
		info_title=(TextView)findViewById(R.id.common_head_tv_title);
		info_title.setText(R.string.info_title);
		info_at_me=(TextView)findViewById(R.id.info_btn_at_me);
		info_at_me.setOnClickListener(new MyTabClickedListener());
		info_comments=(TextView)findViewById(R.id.info_btn_comments);
		info_comments.setOnClickListener(new MyTabClickedListener());
		info_msg=(TextView)findViewById(R.id.info_btn_msg);
		info_msg.setOnClickListener(new MyTabClickedListener());
		showlayout=(LinearLayout)findViewById(R.id.info_showlayout);
		loading_layout=(RelativeLayout)findViewById(R.id.infocenter_loading);
	
	}

	public class MyTabClickedListener implements OnClickListener{
			
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch(v.getId()){
			case R.id.info_btn_at_me:		
				if(info_curPosition!=INFO_AT_POSITION){
					loading_layout.setVisibility(View.VISIBLE);
					showlayout.removeAllViews();
				switchBG(info_curPosition);
				info_at_me.setBackgroundResource(R.drawable.ic_info_tab_selected);
				info_at_me.setTextColor(context.getResources().getColor(R.color.info_tab_selected_color));
				info_curPosition=INFO_AT_POSITION;
				statuses.clear();
				getAtInfo();
					
				}
				
				break;
			case R.id.info_btn_comments:
				if(info_curPosition!=INFO_COMMENTS_POSITION){
					loading_layout.setVisibility(View.VISIBLE);
					showlayout.removeAllViews();
					switchBG(info_curPosition);
					info_comments.setBackgroundResource(R.drawable.ic_info_tab_selected);
					info_comments.setTextColor(context.getResources().getColor(R.color.info_tab_selected_color));
					info_curPosition=INFO_COMMENTS_POSITION;
					statuses.clear();
					getCommentsInfo();
				}		
				break;
			case R.id.info_btn_msg:
				if(info_curPosition!=INFO_MSG_POSITION){
					loading_layout.setVisibility(View.VISIBLE);
					showlayout.removeAllViews();
					switchBG(info_curPosition);
					info_msg.setBackgroundResource(R.drawable.ic_info_tab_selected);
					info_msg.setTextColor(context.getResources().getColor(R.color.info_tab_selected_color));
					info_curPosition=INFO_MSG_POSITION;
					statuses.clear();
					getMsgInfo();									
				}		
				break;
			}
		}
		//切换按钮的背景图片和字体颜色
		private void switchBG(int position){
			switch(position){
			case INFO_AT_POSITION:
				info_at_me.setBackgroundDrawable(null);
				info_at_me.setTextColor(context.getResources().getColor(R.color.info_tab_textcolor));
				break;
			case INFO_COMMENTS_POSITION:
				info_comments.setBackgroundDrawable(null);
				info_comments.setTextColor(context.getResources().getColor(R.color.info_tab_textcolor));
				break;
			case INFO_MSG_POSITION:
				info_msg.setBackgroundDrawable(null);
				info_msg.setTextColor(context.getResources().getColor(R.color.info_tab_textcolor));
				break;
			}
		}
	}
	
	/**
	 * 获取At的信息
	 */
	private void getAtInfo(){
		state=REQUEST_TYPE_INIT;
		cur_type=Task.MSG_COMMENTS_MENTIONS;
		show_at_list=new PullToRefreshListView(this);
		showlayout.addView(show_at_list);	
		type=0x1|0x2|0x10|0x20;
		mService.getMentionWeiboList(type, 0, 0, "0");
		
		show_at_list.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				state=REQUEST_TYPE_REFRESH;
				mService.getMentionWeiboList(type, 0, 0, "0");
	
			}
		});
		show_at_list.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				state=REQUEST_TYPE_PAGE_DOWN;
					Log.v(TAG, "get more");
				mService.getMentionWeiboList(type, 1, statuses.getLast().timestamp, statuses.getLast().id);
				
			}
		});

	}
	
	/**
	 * 获取评论的信息
	 */
	private void getCommentsInfo(){
		state=REQUEST_TYPE_INIT;
		cur_type=Task.MSG_COMMENTS_ME_LIST;
		show_comment_list=new PullToRefreshListView(this);
		showlayout.addView(show_comment_list);		
		type=0x8|0x40;//如果改动，service也要改动，
		mService.getMentionWeiboList(type, 0, 0, "0");
		
		show_comment_list.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				state=REQUEST_TYPE_REFRESH;
				mService.getMentionWeiboList(type, 0, 0, "0");
	
			}
		});
		show_comment_list.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				state=REQUEST_TYPE_PAGE_DOWN;
					Log.v(TAG, "get more");
				mService.getMentionWeiboList(type, 1, statuses.getLast().timestamp, statuses.getLast().id);
			}
		});

	}
	
	/**
	 * 获取消息的信息
	 */
	private void getMsgInfo(){
		state=REQUEST_TYPE_INIT;
		cur_type=Task.MSG_PRIVATE_LIST;
		show_msg_list=new PullToRefreshListView(this);
		showlayout.addView(show_msg_list);	
		Log.v(TAG, "pri msg");
		mService.getPriMsgList(0, 0, "0");
		show_msg_list.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				state=REQUEST_TYPE_REFRESH;
				mService.getPriMsgList( 0, 0, "0");
	
			}
		});
		show_msg_list.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				state=REQUEST_TYPE_PAGE_DOWN;
					Log.v(TAG, "get more");
					mService.getPriMsgList(1, statuses.getLast().timestamp, statuses.getLast().id);
			}
		});
		
	}

	@Override
	public void update(int type, Object param) {
		if(type != cur_type)return;
		if(((Task)param).result.size() == 0) return;
		loading_layout.setVisibility(View.GONE);
		ICArrayList result;
		LinkedList<Status> tmpList;
		switch(state){
		//初始化
		case REQUEST_TYPE_INIT:
			result = (ICArrayList)((Task)param).result.get(0);
			hasNext = result.hasNext;
			Log.v(TAG,result.toString());
			for(Object status: result.list){
				statuses.add((Status)status);
			}
			setAdapter();
			break;
		//刷新第一页
		case REQUEST_TYPE_REFRESH:
			tmpList = new LinkedList<Status>();
			result = (ICArrayList)((Task)param).result.get(0);
			hasNext = result.hasNext;
			for(Object status: result.list){
				//判断当前已有的最新一条记录与请求的刷新记录是否相同，相同则不读取之后的记录
				if(!statuses.get(0).id.equals(((Status)status).id))
						tmpList.add((Status)status);
				else break;
			}
			statuses.addAll(0, tmpList);
			switch(info_curPosition){
				case INFO_AT_POSITION:
					show_at_list.onRefreshComplete();
					break;
				case INFO_COMMENTS_POSITION:
					show_comment_list.onRefreshComplete();
					break;
				case INFO_MSG_POSITION:
					show_msg_list.onRefreshComplete();
					break;			
				}
			Toast.makeText(this, tmpList.size() == 0? "没有最新消息":"刷新了"+tmpList.size()+"条微博", Toast.LENGTH_SHORT).show();
			break;
		//下翻页
		case REQUEST_TYPE_PAGE_DOWN:
			tmpList = new LinkedList<Status>();
			result = (ICArrayList)((Task)param).result.get(0);
			hasNext = result.hasNext;
			for(Object status: result.list){
				tmpList.add((Status)status);
			}
			statuses.addAll(statuses.size(), tmpList);
			switch(info_curPosition){
				case INFO_AT_POSITION:
					show_at_list.onLoadMoreComplete();
					break;
				case INFO_COMMENTS_POSITION:
					show_comment_list.onLoadMoreComplete();
					break;
				case INFO_MSG_POSITION:
					show_msg_list.onLoadMoreComplete();
					break;			
			}
			Toast.makeText(this, "又拉取了一页",Toast.LENGTH_SHORT).show();
			break;
		}
		//清零标志位
		state  = REQUEST_TYPE_RESET;
	}
	/**
	 * 为3种消息添加不同适配器
	 */
	private void setAdapter() {
		// TODO Auto-generated method stub
		if(statuses.size() != 0){
			switch(info_curPosition){
				case INFO_AT_POSITION:
					show_at_list.setAdapter(new TimelineListAdapter(this,statuses));
					break;
				case INFO_COMMENTS_POSITION:
					show_comment_list.setAdapter(new TimelineListAdapter(this, statuses));
					break;
				case INFO_MSG_POSITION:
					show_msg_list.setAdapter(new TimelineListAdapter(this, statuses));
					break;
			}
		}
	}
	private void init(){
		getAtInfo();
	}
	
}
