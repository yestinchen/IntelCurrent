package com.kernel.intelcurrent.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 主界面显示的ActivityGroup
 * @author sheling*/
public class MainActivity extends ActivityGroup implements Updateable{

	private ImageView iv1,iv2,iv3,iv4,iv5,selectedIv;
	private LinearLayout container;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		setListeners();
		//init the first activity
//		switchActivityInContent(GroupBlockActivity.class);
	}
	
	private void findViews(){
		iv1 = (ImageView) findViewById(R.id.common_bottom_iv_1);
		iv2 = (ImageView) findViewById(R.id.common_bottom_iv_2);
		iv3 = (ImageView) findViewById(R.id.common_bottom_iv_3);
		iv4 = (ImageView) findViewById(R.id.common_bottom_iv_4);
		iv5 = (ImageView) findViewById(R.id.common_bottom_iv_5);
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
	
	/**
	 * 监听下方按钮的监听器
	 * @author sheling*/
	private class BottomListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(v == iv1){
				if(selectedIv != v){
//					switchActivityInContent(GroupBlockActivity.class);
					switchImageViewBackground(v);
				}
			}else if (v == iv2){
				if(selectedIv != v){
//					switchActivityInContent(InfoCenterActivity.class);
					switchImageViewBackground(v);
				}
			}else if (v == iv3){
				if(selectedIv != v){
//					switchActivityInContent(UserCenterActivity.class);
					switchImageViewBackground(v);
				}
			}else if(v == iv4){
				if(selectedIv != v){
//					switchActivityInContent(SearchActivity.class);
					switchImageViewBackground(v);
				}
			}else if(v == iv5){
				if(selectedIv != v){
//					switchActivityInContent(MoreActivity.class);
					switchImageViewBackground(v);
				}
			}
		}
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
		v.setBackgroundResource(R.drawable.ic_tab_foucus_bg);
		selectedIv = (ImageView) v;
	}

	@Override
	public void update(int type, Object param){
		((Updateable)getCurrentActivity()).update(type, param);
	}
}
