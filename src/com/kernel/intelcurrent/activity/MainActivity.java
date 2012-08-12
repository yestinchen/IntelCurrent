package com.kernel.intelcurrent.activity;

import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.service.MainService;
import com.kernel.intelcurrent.service.MainService.ICBinder;
import android.app.ActivityGroup;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 主界面显示的ActivityGroup
 * @author sheling*/
public class MainActivity extends ActivityGroup implements Updateable{

	private static final String TAG = MainActivity.class.getSimpleName();
	
	private ImageView iv1,iv2,iv3,iv4,iv5,selectedIv,foucusIv;
	private LinearLayout container;
	boolean isBound = false;
	private MainService mService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		setListeners();
		//init the first activity
		switchActivityInContent(GroupBlockActivity.class);
	}
	
	protected void onStart(){
	   super.onStart();
	   Intent intent=new Intent(this,MainService.class);
	   bindService(intent,connection, Context.BIND_AUTO_CREATE);

	}
	 
	protected void onStop(){
	   super.onStop();
	   unbindService(connection);
	   isBound = false;
	}

	@Override
	public void update(int type, Object param){
		//TODO 这里应该增加一些判断，确保正确的结果传递给正确的activity
		((Updateable)getCurrentActivity()).update(type, param);
		
	}
	public MainService getService(){
		return mService;
	}
	
	private void findViews(){
		iv1 = (ImageView) findViewById(R.id.common_bottom_iv_1);
		iv2 = (ImageView) findViewById(R.id.common_bottom_iv_2);
		iv3 = (ImageView) findViewById(R.id.common_bottom_iv_3);
		iv4 = (ImageView) findViewById(R.id.common_bottom_iv_4);
		iv5 = (ImageView) findViewById(R.id.common_bottom_iv_5);
		foucusIv = (ImageView)findViewById(R.id.common_bottom_iv_focus);
		container = (LinearLayout)findViewById(R.id.layout_main_layout_container);
		//底部默认选中的
		selectedIv = iv1;
	}
	private void setListeners(){
		BottomListener l = new BottomListener();
		iv1.setOnClickListener(l);
		iv2.setOnClickListener(l);
		iv3.setOnClickListener(l);
		iv4.setOnClickListener(l);
		iv5.setOnClickListener(l);
	}

	/**切换中间显示的activity
	 * */
	private void switchActivityInContent(@SuppressWarnings("rawtypes") Class className){
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(className.getSimpleName(),
				new Intent(MainActivity.this,className)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
					).getDecorView());
	}
	
	/**
	 * 更改下方tab的图标背景*/
	private void switchImageViewBackground(View v){
		selectedIv.setBackgroundColor(Color.parseColor("#00000000"));
//		v.setBackgroundResource(R.drawable.ic_tab_foucus_bg);
		TranslateAnimation anim = new TranslateAnimation(selectedIv.getLeft(), v.getLeft(), selectedIv.getTop(), v.getTop());
		anim.setDuration(250);
		anim.setFillAfter(true);
		foucusIv.startAnimation(anim);
		selectedIv = (ImageView) v;
	}
	/**
	 * 监听下方按钮的监听器
	 * @author sheling*/
	private class BottomListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(v == iv1){
				if(selectedIv != v){
					switchActivityInContent(GroupBlockActivity.class);
					switchImageViewBackground(v);
				}
			}else if (v == iv2){
				if(selectedIv != v){
					switchActivityInContent(InfoCenterActivity.class);
					switchImageViewBackground(v);
				}
			}else if (v == iv3){
				if(selectedIv != v){
					switchActivityInContent(UserCenterActivity.class);
					switchImageViewBackground(v);
				}
			}else if(v == iv4){
				if(selectedIv != v){
					switchActivityInContent(SearchActivity.class);
					switchImageViewBackground(v);
				}
			}else if(v == iv5){
				if(selectedIv != v){
					switchActivityInContent(MoreActivity.class);
					switchImageViewBackground(v);
				}
			}
		}
	}
	
	/**
    * */
   private ServiceConnection connection= new ServiceConnection(){
		public void onServiceConnected(ComponentName className, IBinder service){
			ICBinder binder=(ICBinder)service;
			mService = binder.getService();
			binder.getService();
			isBound = true;
			mService.changeCurrentActivity(MainActivity.this);
		}

		public void onServiceDisconnected(ComponentName name){
			isBound = false;
		}
   };
	
}
