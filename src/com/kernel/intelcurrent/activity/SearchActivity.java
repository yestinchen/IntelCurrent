package com.kernel.intelcurrent.activity;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
	private TextView title,search_btn,loadMore;
	private View footview;
	private ImageView del_btn;
	private ListView listview;
	private RelativeLayout search_layout;
	private LinkedList<User> users;
	private MainActivity activityGroup;
	private MainService mService;
	private Long CurTime;
	private UserFriendListAdapter uflAdapter;
	private static final String TAG=SearchActivity.class.getSimpleName();
	private static final int REQUEST_RESET=-1;
	private static final int REQUEST_INIT=1;
	private static final int REQUEST_LOADMORE=2;
	private int request_type=REQUEST_INIT;
	private int u_page=1;
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
		footview=LayoutInflater.from(this).inflate(R.layout.common_foot_load_more, null);
		loadMore = (TextView)footview.findViewById(R.id.common_foot_load_more_tv);
		input=(EditText)findViewById(R.id.search_input);
		title=(TextView)findViewById(R.id.common_head_tv_title);
		title.setText(R.string.search_title);
		search_btn=(TextView)findViewById(R.id.search_btn);
		del_btn=(ImageView)findViewById(R.id.search_del);
		del_btn.setVisibility(View.INVISIBLE);
		search_layout=(RelativeLayout)findViewById(R.id.search_layout);
		listview=(ListView)findViewById(R.id.search_list);
		listview.addFooterView(footview);
		
	
	}
	public void setListener(){
		loadMore.setOnClickListener(new OnBtnClickListener());
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
				}else if(input.getText().toString().getBytes().length>20){
					Toast.makeText(SearchActivity.this,"输入的长度过长!",Toast.LENGTH_SHORT).show();
				}
				else{		
					//隐藏软键盘 
					request_type=REQUEST_INIT;
					((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					search_layout.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
					Toast.makeText(SearchActivity.this,input.getText(),Toast.LENGTH_SHORT).show();
					users=new LinkedList<User>();
					CurTime=System.currentTimeMillis();
					mService.searchUser(CurTime,input.getText().toString(),u_page);//启动任务
				}
				break;
			case R.id.search_del:
				input.setText("");
				break;
			case R.id.common_foot_load_more_tv:
					u_page++;
					if(request_type==REQUEST_RESET){
						request_type=REQUEST_LOADMORE;
						CurTime=System.currentTimeMillis();
						mService.searchUser(CurTime, input.getText().toString(), u_page);
						loadMore.setText(R.string.common_loading);
					}
				break;
			}
		}
		
	}
	private void searchUser(Task t){
		ICArrayList result;
		result=(ICArrayList)t.result.get(0);
		int hasNext;		
		hasNext=result.hasNext;
		LinkedList<User> tmpList;
		switch(request_type){
		case REQUEST_INIT:
			for(Object user: result.list){
				users.add((User)user);
				Log.v(TAG, user.toString());
			}
			if(users.size()!=0){
				search_layout.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				uflAdapter=new UserFriendListAdapter(mService, this, users);
				listview.setAdapter(uflAdapter);
				}
			break;
		case REQUEST_LOADMORE:
			tmpList=new LinkedList<User>();
			for(Object user: result.list){
				tmpList.add((User)user);
			}
			users.addAll(users.size(),tmpList);
			uflAdapter.notifyDataSetChanged();
			loadMore.setText(R.string.common_load_more);
			break;
		}
		request_type=REQUEST_RESET;
	}
	@Override
	public void update(int type, Object param) {
		if(type!=Task.USER_SEARCH)return;
		Task t=(Task)param;
		if(t.time!=CurTime)return;
		if(t.result.size()==0){
			search_layout.setVisibility(View.GONE);
			
			Toast.makeText(this, "没有找到匹配的记录。", Toast.LENGTH_SHORT).show();
			return;
		}
		switch(type){
		case Task.USER_SEARCH:
			searchUser(t);
			break;
		
		}
		
	}

}
