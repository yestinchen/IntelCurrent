package com.kernel.intelcurrent.activity;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.widget.UrlImageView;

public class OtherUserInfoActivity extends BaseActivity implements Updateable{
	public static final int OTHER_USER_INFO_ONE=1;
	public static final int OTHER_USER_INFO_TWO=2;
	private Intent intent;
	private User userinfo=null;
	private static final String TAG=OtherUserInfoActivity.class.getSimpleName();
	
	private TextView other_title,other_words,other_location,other_follow_btn,other_at_btn;
	private TextView other_weibo,other_fans,other_follow;
	private TextView other_weibo_nums,other_fans_nums,other_follow_nums;
	private LinearLayout other_weibo_layout,other_fans_layout,other_follow_layout;
	private ImageView other_head;
	private Button other_name,other_platform;
	private ImageView other_left,other_right;
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 2:
				String localimgpath=(String) msg.obj;
				other_head.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(localimgpath)));
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_user_info);
		intent=getIntent();
		findViews();		
	}
	private void findViews(){
		other_title=(TextView)findViewById(R.id.common_head_tv_title);
		other_left=(ImageView)findViewById(R.id.common_head_iv_left);
		other_left.setBackgroundResource(R.drawable.ic_title_back);
		other_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OtherUserInfoActivity.this.finish();
			}
		});
		other_right=(ImageView)findViewById(R.id.common_head_iv_right);
		
		//获取控件
		other_head=(ImageView)findViewById(R.id.other_user_head);
		other_name=(Button)findViewById(R.id.other_user_name);
		other_location=(TextView)findViewById(R.id.other_user_location);
		other_platform=(Button)findViewById(R.id.other_user_platform);
		other_words=(TextView)findViewById(R.id.other_user_words);
		other_follow_btn=(TextView)findViewById(R.id.other_user_follow_btn);
		other_at_btn=(TextView)findViewById(R.id.other_user_at_btn);
		other_fans_layout=(LinearLayout)findViewById(R.id.other_user_fans_layout);
		other_follow_layout=(LinearLayout)findViewById(R.id.other_user_follow_layout);
		other_weibo_layout=(LinearLayout)findViewById(R.id.other_user_weibo_layout);
		other_fans=(TextView) other_fans_layout.findViewById(R.id.other_user_cell_title);
		other_fans.setText(R.string.user_table_fans);
		other_fans_nums=(TextView) other_fans_layout.findViewById(R.id.other_user_cell_nums);
		other_follow=(TextView) other_follow_layout.findViewById(R.id.other_user_cell_title);
		other_follow.setText(R.string.user_table_follows);
		other_follow_nums=(TextView) other_follow_layout.findViewById(R.id.other_user_cell_nums);
		other_weibo=(TextView) other_weibo_layout.findViewById(R.id.other_user_cell_title);
		other_weibo.setText(R.string.user_table_weibo);
		other_weibo_nums=(TextView) other_weibo_layout.findViewById(R.id.other_user_cell_nums);
	}
	private void setListener(){
		other_follow_btn.setOnClickListener(new OnBtnListener(userinfo));
		other_at_btn.setOnClickListener(new OnBtnListener(userinfo));
		other_weibo_layout.setOnClickListener(new OnBtnListener(userinfo));
		other_fans_layout.setOnClickListener(new OnBtnListener(userinfo));
		other_follow_layout.setOnClickListener(new OnBtnListener(userinfo));
	}
	private class OnBtnListener implements OnClickListener{
		User user;
		public OnBtnListener(User user) {
			this.user=user;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.other_user_follow_btn:
				Log.v(TAG, user.toString());
				follow(v,user.name);
				break;
			case R.id.other_user_at_btn:
				break;
			case R.id.other_user_fans_layout:
				Log.v(TAG, user.toString());
				Intent intent1 =new Intent(OtherUserInfoActivity.this,ShowAllListActivity.class);
				intent1.putExtra("show_list_type", Task.USER_OTHER_FANS_LIST);
				intent1.putExtra("other_name", user.name);
				intent1.putExtra("other_nick", user.nick);
				Log.v(TAG, user.name);
				startActivity(intent1);
				break;
			case R.id.other_user_follow_layout:
				Intent intent2 =new Intent(OtherUserInfoActivity.this,ShowAllListActivity.class);
				intent2.putExtra("show_list_type",Task.USER_OTHER_FRIENDS_LIST);
				intent2.putExtra("other_name", user.name);
				intent2.putExtra("other_nick", user.nick);
				Log.v(TAG, user.name);
				startActivity(intent2);
				break;
			case R.id.other_user_weibo_layout:
				Intent intent3 =new Intent(OtherUserInfoActivity.this,ShowAllListActivity.class);
				intent3.putExtra("show_list_type", Task.USER_OTHER_WEIBO_LIST);
				intent3.putExtra("other_openid", user.id);
				intent3.putExtra("other_name", user.name);
				intent3.putExtra("other_nick", user.nick);
				Log.v(TAG, user.name+user.id);
				startActivity(intent3);
				break;
			}
		}
		
	}
	@Override
	public void update(int type, Object param) {
		// TODO Auto-generated method stub
		if(type!=Task.USER_OTHER_INFO)return;
		if(((Task)param).result.size() == 0) return;	
		Task task=(Task)param;
		userinfo=(User)task.result.get(0);	
		if(userinfo!=null){
			Log.v(TAG,"username:"+userinfo.nick+"userhead:"+userinfo.head+"userlocation:"+userinfo.location);
			initOne();
			setListener();
		}
	}

	@Override
	public void onConnectionFinished() {
		// TODO Auto-generated method stub
		if(intent!=null){
			String openid=intent.getStringExtra("user_openid");
			String name=intent.getStringExtra("user_nick");
			other_title.setText(name);
			Log.v(TAG, openid);
			mService.getOtherUserInfo(openid);
		}

	}

	@Override
	public void onConnectionDisConnected() {
		// TODO Auto-generated method stub
		
	}
	public void initOne(){		
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
		//other_title.setText(userinfo.nick);
		other_name.setText(userinfo.nick);
		Drawable sex_img;
		if(userinfo.gender==1){
			other_at_btn.setText("@他");
			sex_img=this.getResources().getDrawable(R.drawable.ic_user_sex_male);
			sex_img.setBounds(0,0,40,40);
			
		}else{
			other_at_btn.setText("@她");
			sex_img=this.getResources().getDrawable(R.drawable.ic_user_sex_female);
			sex_img.setBounds(0,0,40,40);
		}
		other_name.setCompoundDrawables(null, null,sex_img , null);
		other_location.setText(userinfo.location!=null?userinfo.location:"无");
		if(userinfo.ismyidol){
			other_follow_btn.setText(R.string.user_follow_cancle);
		}else{
			other_follow_btn.setText(R.string.user_follow_add);
		}
		Drawable platform_img=null;
		if(userinfo.platform==User.PLATFORM_TENCENT_CODE){
			other_platform.setText(R.string.tencent_weibo);
			platform_img=getResources().getDrawable(R.drawable.ic_platform_small_tencent);
			platform_img.setBounds(0, 0, 32, 32);
			
		}else if(userinfo.platform==User.PLATFORM_SINA_CODE){
			other_platform.setText(R.string.sina_weibo);
			platform_img=getResources().getDrawable(R.drawable.ic_platform_small_sina);
			platform_img.setBounds(0, 0, 32, 32);
		}
		other_platform.setCompoundDrawables(platform_img, null, null, null);
		other_words.setText(userinfo.description!=null?userinfo.description:"无");
		
		other_weibo_nums.setText(userinfo.statusnum+"");
		other_fans_nums.setText(userinfo.fansnum+"");
		other_follow_nums.setText(userinfo.idolnum+"");
	}
	private void follow(View v,final String name){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		String message=null;
		final TextView tv=(TextView)v;
		if(tv.getText().equals(getResources().getString(R.string.user_follow_add))){
			message=getResources().getString(R.string.user_follow_add);
		}else{
			message=getResources().getString(R.string.user_follow_cancle);
		}
		builder.setCancelable(false).setMessage("确定"+message+"吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(tv.getText().equals(getResources().getString(R.string.user_follow_add))){
					if(mService!=null){
						Log.v(TAG,"Get SERVICE SUCCESS");
						mService.addFriend(name);//确定添加收听
					}
					tv.setText(R.string.user_follow_cancle);
				}else{
					if(mService!=null){
						Log.v(TAG,"Get SERVICE SUCCESS");
						mService.delFriend(name);//确定取消收听
					}
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
	}
}
