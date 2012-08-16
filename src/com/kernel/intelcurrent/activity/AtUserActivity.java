package com.kernel.intelcurrent.activity;

import java.util.ArrayList;
import com.kernel.intelcurrent.adapter.AtListAdapter;
import com.kernel.intelcurrent.model.ICArrayList;
import com.kernel.intelcurrent.model.SimpleUser;
import com.kernel.intelcurrent.model.Task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AtUserActivity extends BaseActivity implements Updateable,View.OnClickListener,OnItemClickListener{

	private static final int REQUEST_FIRST = 1;
	private static final int REQUEST_MORE = 2;
	
	private TextView titleTv,loadTv,tipTv;
	private ImageView leftImage,rightImage;
	private ListView lv;
	private View footView,headView;
	private EditText editText;
	private ArrayList<SimpleUser> users = new ArrayList<SimpleUser>();
	private AtListAdapter adapter;
	private int request = -1;
	private int page = 1;
	
	@Override
	public void update(int type, Object param) {
		if(type != Task.WEIBO_ADD_AT) return;
		ICArrayList result = (ICArrayList) ((Task)param).result.get(0);
		ArrayList<SimpleUser> list = new ArrayList<SimpleUser>();
		for(Object o:result.list){
			list.add((SimpleUser)o);
		}
		if(request == REQUEST_FIRST){
			users.clear();
			users.addAll(list);
			adapter.notifyDataSetChanged();
			loadTv.setText(R.string.common_load_more);
			request = -2;
		}else if(request == REQUEST_MORE){
			users.addAll(list);
			adapter.notifyDataSetChanged();
			loadTv.setText(R.string.common_load_more);
			request = -2;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_at);
		findViews();
		setListeners();
		setAdapter();
	}
	
	private void findViews(){
		footView = LayoutInflater.from(this).inflate(R.layout.common_foot_load_more, null);
		headView = LayoutInflater.from(this).inflate(R.layout.text_view_small, null);
		tipTv = (TextView)headView.findViewById(R.id.text_view_small_tv);
		titleTv = (TextView)findViewById(R.id.common_head_tv_title);
		leftImage = (ImageView)findViewById(R.id.common_head_iv_left);
		rightImage = (ImageView)findViewById(R.id.common_head_iv_right);
		loadTv = (TextView)footView.findViewById(R.id.common_foot_load_more_tv);
		editText = (EditText)findViewById(R.id.activity_at_et);
		lv = (ListView)findViewById(R.id.activity_at_lv);
		
		leftImage.setImageResource(R.drawable.ic_title_back);
		loadTv.setText(R.string.activity_at_net_tip);
		
		lv.setSelector(R.drawable.list_cell_bg);
		lv.addFooterView(footView);
	}
	
	private void setAdapter(){
		adapter = new AtListAdapter(this, users);
		lv.setAdapter(adapter);
	}
	
	private void setListeners(){
		leftImage.setOnClickListener(this);
		lv.setOnItemClickListener(this);
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length() >0 ){
					if(lv.getHeaderViewsCount() == 0){
						lv.addHeaderView(headView);
					}
					users.clear();
					adapter.notifyDataSetChanged();
					headView.setVisibility(View.VISIBLE);
					tipTv.setText("@"+s);
					loadTv.setText(R.string.activity_at_net_tip);
				}else {
					lv.removeHeaderView(headView);
					users.clear();
					headView.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
					loadTv.setText(R.string.activity_at_net_tip);
					request = -1;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(v == leftImage){
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(view == headView){
			Intent intent = new Intent(this,WeiboNewActivity.class);
			intent.putExtra("name", editText.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
		}else if(view == footView){
			if(editText.getText().toString().length() == 0)return;
			//判断是否有请求正在相应中
			if(request == -1){
				request = REQUEST_FIRST;
				mService.searchAtUser(editText.getText().toString(),page,Task.PLATFORM_TENCENT);
				loadTv.setText(R.string.common_loading);
			}else if(request == -2){
				request = REQUEST_MORE;
				page ++;
				mService.searchAtUser(editText.getText().toString(),page,Task.PLATFORM_TENCENT);
				loadTv.setText(R.string.common_loading);
			}

			//隐藏键盘显示
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}else{
			editText.setText(users.get(position-1).name);
			Intent intent = new Intent(this,WeiboNewActivity.class);
			intent.putExtra("name",editText.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
		}
	}
	
	@Override
	public void onConnectionFinished() {
		
	}

	@Override
	public void onConnectionDisConnected() {
		
	}


}
