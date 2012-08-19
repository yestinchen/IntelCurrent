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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
	private TextView head_title,location_detail,words_detail;
	private Button user_name;
	private TextView follow_btn,fans_btn,shoucang_btn,weibo_btn;
	private TextView follow_counts,fans_counts,shoucang_counts,weibo_counts;
	private ImageView user_sex,switcher_left,switcher_right;
	private LinearLayout user_weibo_layout,user_fans_layout,user_follow_layout,user_shoucang_layout;
	private RelativeLayout loading_layout;
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
			break;
			}
		}
		
	};
	private static final String TAG=UserCenterActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		findViews();
		activityGroup=(MainActivity)getParent();
		mService=activityGroup.getService();
		mService.getUserInfo("0");

	}
	
	private void findViews(){
		
		//横栏标题
		head_title=(TextView)findViewById(R.id.common_head_tv_title);
		head_title.setText(R.string.user_title);
		
		//用户信息
		user_name=(Button)findViewById(R.id.user_name);		
		user_sex=(ImageView)findViewById(R.id.user_sex);		
		user_touxiang=(ImageView)findViewById(R.id.user_touxiang);
		location_detail=(TextView)findViewById(R.id.user_location);
		words_detail=(TextView)findViewById(R.id.user_words);

	//	switcher_left=(ImageView)findViewById(R.id.user_switcher_left);
	//	switcher_right=(ImageView)findViewById(R.id.user_switcher_right);
		//粉丝，关注，微博，收藏信息
		user_fans_layout=(LinearLayout)findViewById(R.id.user_fans_layout);
		user_follow_layout=(LinearLayout)findViewById(R.id.user_follow_layout);
		user_shoucang_layout=(LinearLayout)findViewById(R.id.user_shoucang_layout);
		user_weibo_layout=(LinearLayout)findViewById(R.id.user_weibo_layout);
		
		fans_btn=(TextView)user_fans_layout.findViewById(R.id.other_user_cell_title);
		fans_btn.setText(R.string.user_table_fans);
		follow_btn=(TextView)user_follow_layout.findViewById(R.id.other_user_cell_title);
		follow_btn.setText(R.string.user_table_follows);
		shoucang_btn=(TextView)user_shoucang_layout.findViewById(R.id.other_user_cell_title);
		shoucang_btn.setText(R.string.user_table_shoucang);
		weibo_btn=(TextView)user_weibo_layout.findViewById(R.id.other_user_cell_title);
		weibo_btn.setText(R.string.user_table_weibo);
		
		fans_counts=(TextView)user_fans_layout.findViewById(R.id.other_user_cell_nums);		
		weibo_counts=(TextView)user_weibo_layout.findViewById(R.id.other_user_cell_nums);
		shoucang_counts=(TextView)user_shoucang_layout.findViewById(R.id.other_user_cell_nums);
		follow_counts=(TextView)user_follow_layout.findViewById(R.id.other_user_cell_nums);
		//loading
		loading_layout=(RelativeLayout)findViewById(R.id.user_loading);
	}
	private void setListener(){
		user_fans_layout.setOnClickListener(new UserOnclickListener());
		user_follow_layout.setOnClickListener(new UserOnclickListener());
		user_shoucang_layout.setOnClickListener(new UserOnclickListener());
		user_weibo_layout.setOnClickListener(new UserOnclickListener());
	//	switcher_left.setOnClickListener(new UserOnclickListener());
	//	switcher_right.setOnClickListener(new UserOnclickListener());
		
	}
	private class UserOnclickListener implements OnClickListener{
			
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				int tab_type=-1;
			switch(v.getId()){
			case R.id.user_fans_layout:
				tab_type=Task.USER_FANS_LIST;
				//显示粉丝列表
				break;
			case R.id.user_follow_layout:
				tab_type=Task.USER_FRIENDS_LIST;
				//显示关注列表
				break;
			case R.id.user_shoucang_layout:
				tab_type=Task.USER_FAV_LIST;
				//显示收藏微博列表
				break;
			case R.id.user_weibo_layout:
				//显示发布微博列表
				tab_type=Task.USER_WEIBO_LIST;
				break;
		//	case R.id.user_switcher_left:
				//切换平台
			//	break;
		//	case R.id.user_switcher_right:
				//切换平台
			//	break;
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
		loading_layout.setVisibility(View.GONE);
		if(userinfo!=null){
			init();
			setListener();
		}
		
	}
	public void init(){
		user_name.setText(userinfo.nick);
		location_detail.setText(userinfo.location!=null?userinfo.location:"无");
		words_detail.setText(userinfo.description!=null?userinfo.description:"无");
		if(userinfo.gender==1)user_sex.setBackgroundResource(R.drawable.ic_user_sex_male);
		else if(userinfo.gender==2)user_sex.setBackgroundResource(R.drawable.ic_user_sex_female);
		Drawable platform_img=null;
		if(userinfo.platform==User.PLATFORM_TENCENT_CODE){
			platform_img=getResources().getDrawable(R.drawable.ic_platform_small_tencent);
			platform_img.setBounds(0, 0, 32, 32);
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
			platform_img=getResources().getDrawable(R.drawable.ic_platform_small_sina);
			platform_img.setBounds(0, 0, 32, 32);	
			//新浪用户头像添加
		}
		user_name.setCompoundDrawables(platform_img, null, null, null);
		fans_counts.setText(userinfo.fansnum+"");
		weibo_counts.setText(userinfo.statusnum+"");
		shoucang_counts.setText(userinfo.favnum+"");
		follow_counts.setText(userinfo.idolnum+"");
	}
}
