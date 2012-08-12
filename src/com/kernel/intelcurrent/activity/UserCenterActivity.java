package com.kernel.intelcurrent.activity;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

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
	private UrlImageView user_touxiang;
	
	private User userinfo=null;
	private MainActivity activityGroup;
	private MainService mService;
	
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
		user_touxiang=(UrlImageView)findViewById(R.id.user_touxiang);
		user_platform=(ImageView)findViewById(R.id.user_platform_logo);
		
		if(userinfo.platform==User.PLATFORM_TENCENT_CODE){
			user_platform.setBackgroundResource(R.drawable.ic_user_tencent_logo);
		try {
			user_touxiang.bindUrl(userinfo.head,UrlImageView.PLAT_FORM_TENCENT ,UrlImageView.LARGE_HEAD);
			
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
		location_detail.setText(userinfo.location==""?userinfo.location:"无");
		words_detail=(TextView)findViewById(R.id.user_words);
		words_detail.setText(userinfo.description);
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
			switch(v.getId()){
			case R.id.user_tab_fans:
				//显示粉丝列表
				break;
			case R.id.user_tab_follows:
				//显示关注列表
				break;
			case R.id.user_tab_shoucang:
				//显示收藏微博列表
				break;
			case R.id.user_tab_weibo:
				//显示发布微博列表
				break;
			case R.id.user_switcher_left:
				//切换平台
				break;
			case R.id.user_switcher_right:
				//切换平台
				break;
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
