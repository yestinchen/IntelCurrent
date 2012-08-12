package com.kernel.intelcurrent.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoCenterActivity extends Activity implements Updateable{
	private TextView info_title,info_at_me,info_comments,info_msg; 
	private static final int INFO_AT_POSITION=0;
	private static final int INFO_COMMENTS_POSITION=1;
	private static final int INFO_MSG_POSITION=2;
	private int cur_tab_positon;
	private Context context;
	private LinearLayout show_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		findViews();
		getAtInfo();
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
		
		show_layout=(LinearLayout)findViewById(R.id.info_show_layout);
		cur_tab_positon=INFO_AT_POSITION;
	}
	public class MyTabClickedListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			show_layout.removeAllViews();
			switch(v.getId()){
			case R.id.info_btn_at_me:		
				if(cur_tab_positon!=INFO_AT_POSITION){
				switchBG(cur_tab_positon);
				info_at_me.setBackgroundResource(R.drawable.ic_info_tab_selected);
				info_at_me.setTextColor(context.getResources().getColor(R.color.info_tab_selected_color));
				cur_tab_positon=INFO_AT_POSITION;
				}
				getAtInfo();
				break;
			case R.id.info_btn_comments:
				if(cur_tab_positon!=INFO_COMMENTS_POSITION){
					switchBG(cur_tab_positon);
					info_comments.setBackgroundResource(R.drawable.ic_info_tab_selected);
					info_comments.setTextColor(context.getResources().getColor(R.color.info_tab_selected_color));
					cur_tab_positon=INFO_COMMENTS_POSITION;
				}
				getCommentsInfo();
				break;
			case R.id.info_btn_msg:
				if(cur_tab_positon!=INFO_MSG_POSITION){
					switchBG(cur_tab_positon);
					info_msg.setBackgroundResource(R.drawable.ic_info_tab_selected);
					info_msg.setTextColor(context.getResources().getColor(R.color.info_tab_selected_color));
					cur_tab_positon=INFO_MSG_POSITION;
				}
				getMsgInfo();
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

		
	}
	
	/**
	 * 获取评论的信息
	 */
	private void getCommentsInfo(){

	}
	
	/**
	 * 获取消息的信息
	 */
	private void getMsgInfo(){

	}

	@Override
	public void update(int type, Object param) {

	}

}
