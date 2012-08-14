package com.kernel.intelcurrent.activity;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.service.MainService;
import com.kernel.intelcurrent.widget.UrlImageView;

/**
 * 个人资料显示中心
 * @author allenjin
 *
 */
public class UserCenterActivity extends Activity implements Updateable{
	private TextView head_title,user_name,location_detail,words_detail;
	private TextView follow_btn,fans_btn,shoucang_btn,weibo_btn;
	private TextView follow_counts,fans_counts,shoucang_counts,weibo_counts;
	private ImageView user_sex,switcher_left,switcher_right,user_platform;
	private ImageView user_touxiang;
	
	private User userinfo=null;
	private MainActivity activityGroup;
	private MainService mService;
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 2:
			String localimgpath=(String) msg.obj;
			user_touxiang.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(localimgpath)));
			}
		}
		
	};
	private static final String TAG=UserCenterActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		activityGroup=(MainActivity)getParent();
		mService=activityGroup.getService();
		init();

	}
	
	private void findViews(){
		
		//横栏标题
		head_title=(TextView)findViewById(R.id.common_head_tv_title);
		head_title.setText(R.string.user_title);
		
		//用户信息
		user_name=(TextView)findViewById(R.id.user_name);
		user_name.setText(userinfo.nick);
		user_sex=(ImageView)findViewById(R.id.user_sex);
		if(userinfo.gender==1)user_sex.setBackgroundResource(R.drawable.ic_user_sex_male);
		else if(userinfo.gender==2)user_sex.setBackgroundResource(R.drawable.ic_user_sex_female);
		user_touxiang=(ImageView)findViewById(R.id.user_touxiang);
		user_platform=(ImageView)findViewById(R.id.user_platform_logo);
		
		if(userinfo.platform==User.PLATFORM_TENCENT_CODE){
			user_platform.setBackgroundResource(R.drawable.ic_user_tencent_logo);
		try {
			UrlImageView urlimg=new UrlImageView(this);
			urlimg.bindUrl(userinfo.head,UrlImageView.PLAT_FORM_TENCENT ,UrlImageView.LARGE_HEAD,handler);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else if(userinfo.platform==User.PLATFORM_SINA_CODE){
			user_platform.setBackgroundResource(R.drawable.ic_user_sina_logo);
			
			//新浪用户头像添加
		}
		location_detail=(TextView)findViewById(R.id.user_location);
		location_detail.setText(userinfo.location!=null?userinfo.location:"无");
		words_detail=(TextView)findViewById(R.id.user_words);
		words_detail.setText(userinfo.description!=null?userinfo.description:"无");
		switcher_left=(ImageView)findViewById(R.id.user_switcher_left);
		switcher_right=(ImageView)findViewById(R.id.user_switcher_right);
		//粉丝，关注，微博，收藏信息
		fans_btn=(TextView)findViewById(R.id.user_tab_fans);
		follow_btn=(TextView)findViewById(R.id.user_tab_follows);
		shoucang_btn=(TextView)findViewById(R.id.user_tab_shoucang);
		weibo_btn=(TextView)findViewById(R.id.user_tab_weibo);
		
		fans_counts=(TextView)findViewById(R.id.user_tab_fans_counts);
		fans_counts.setText(userinfo.fansnum+"");
		weibo_counts=(TextView)findViewById(R.id.user_tab_weibo_counts);
		weibo_counts.setText(userinfo.statusnum+"");
		shoucang_counts=(TextView)findViewById(R.id.user_tab_shoucang_counts);
		shoucang_counts.setText(userinfo.favnum+"");
		follow_counts=(TextView)findViewById(R.id.user_tab_follows_counts);
		follow_counts.setText(userinfo.idolnum+"");
		 
	}
	private void setListener(){
		fans_btn.setOnClickListener(new UserOnclickListener());
		follow_btn.setOnClickListener(new UserOnclickListener());
		shoucang_btn.setOnClickListener(new UserOnclickListener());
		weibo_btn.setOnClickListener(new UserOnclickListener());
		switcher_left.setOnClickListener(new UserOnclickListener());
		switcher_right.setOnClickListener(new UserOnclickListener());
		
	}
	private class UserOnclickListener implements OnClickListener{
			
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				int tab_type=-1;
			switch(v.getId()){
			case R.id.user_tab_fans:
				tab_type=Task.USER_FANS_LIST;
				//显示粉丝列表
				break;
			case R.id.user_tab_follows:
				tab_type=Task.USER_FRIENDS_LIST;
				//显示关注列表
				break;
			case R.id.user_tab_shoucang:
				tab_type=Task.USER_FAV_LIST;
				//显示收藏微博列表
				break;
			case R.id.user_tab_weibo:
				//显示发布微博列表
				tab_type=Task.USER_WEIBO_LIST;
				break;
			case R.id.user_switcher_left:
				//切换平台
				break;
			case R.id.user_switcher_right:
				//切换平台
				break;
			}
			if(tab_type!=-1){
				Intent intent=new Intent(UserCenterActivity.this,ShowAllListActivity.class);
				intent.putExtra("show_list_type", tab_type);
				startActivity(intent);
			}
		}
		
	}
	@Override
	public void update(int type, Object param) {
		//类型较检，不符合自己类型的数据忽略
		if(type != Task.USER_INFO)return;
		if(((Task)param).result.size() == 0) return;
		Task task=(Task)param;
		userinfo=(User)task.result.get(0);	
		if(userinfo!=null){
			Log.v(TAG,"username:"+userinfo.nick+"userhead:"+userinfo.head+"userlocation:"+userinfo.location);
			findViews();
			setListener();
		}
		
	}
	public void init(){
		Log.v(TAG, "init");
		mService.getUserInfo("0");
	}
}
