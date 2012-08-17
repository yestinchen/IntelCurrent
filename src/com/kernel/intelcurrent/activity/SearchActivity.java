package com.kernel.intelcurrent.activity;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kernel.intelcurrent.adapter.UserFriendListAdapter;
import com.kernel.intelcurrent.model.ICArrayList;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.service.MainService;

public class SearchActivity extends Activity implements Updateable{
	private EditText input;
	private TextView title,search_btn;
	private ImageView del_btn;
	private ListView listview;
	private ImageView load_img;
	private RelativeLayout search_layout;
	private RotateAnimation load_amin;
	private LinkedList<User> users;
	private MainActivity activityGroup;
	private MainService mService;
	private Long CurTime;
	private static final String TAG=SearchActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_search);
		activityGroup = (MainActivity)getParent();
		mService = activityGroup.getService();
		findViews();
		setListener();
	}
	public void findViews(){
		input=(EditText)findViewById(R.id.search_input);
		title=(TextView)findViewById(R.id.common_head_tv_title);
		title.setText(R.string.search_title);
		search_btn=(TextView)findViewById(R.id.search_btn);
		del_btn=(ImageView)findViewById(R.id.search_del);
		del_btn.setVisibility(View.INVISIBLE);
		search_layout=(RelativeLayout)findViewById(R.id.search_layout);
		listview=(ListView)findViewById(R.id.search_list);
		load_img=(ImageView)findViewById(R.id.search_load);
		
		//旋转动画
		load_amin=new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		load_amin.setDuration(600);	
		load_amin.setRepeatCount(Animation.INFINITE);
		AccelerateDecelerateInterpolator ainter=new AccelerateDecelerateInterpolator();
		load_amin.setInterpolator(ainter);
		load_img.startAnimation(load_amin);
	}
	public void setListener(){
		search_btn.setOnClickListener(new OnBtnClickListener());
		del_btn.setOnClickListener(new OnBtnClickListener());
		input.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()==0){
					del_btn.setVisibility(View.INVISIBLE);
				}else{
					del_btn.setVisibility(View.VISIBLE);
				}
			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
	}
	private class OnBtnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.search_btn:
				if(input.getText()==null||input.getText().toString().equals("")){
					Toast.makeText(SearchActivity.this,"输入不能为空!",Toast.LENGTH_SHORT).show();
				}else{		
					//隐藏软键盘 
					((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					search_layout.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
					Toast.makeText(SearchActivity.this,input.getText(),Toast.LENGTH_SHORT).show();
					users=new LinkedList<User>();
					CurTime=System.currentTimeMillis();
					mService.searchUser(CurTime,input.getText().toString(),1);//启动任务
				}
				break;
			case R.id.search_del:
				input.setText("");
				break;
			}
		}
		
	}
	@Override
	public void update(int type, Object param) {
		if(type!=Task.USER_SEARCH)return;
		Task t=(Task)param;
		if(t.time!=CurTime)return;
		if(t.result.size()==0){
			Log.v(TAG, "没有找到相关用户!");
			return;
		}
		ICArrayList result;
		int hasNext;
		result=(ICArrayList)t.result.get(0);
		hasNext=result.hasNext;
		for(Object user: result.list){
			users.add((User)user);
			Log.v(TAG, user.toString());
		}
		if(users.size()!=0){
			search_layout.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			listview.setAdapter(new UserFriendListAdapter(mService, this, users));
		}
	}

}
