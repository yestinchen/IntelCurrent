package com.kernel.intelcurrent.activity;

import java.util.ArrayList;
import com.kernel.intelcurrent.adapter.AtListAdapter;
import com.kernel.intelcurrent.model.ICArrayList;
import com.kernel.intelcurrent.model.SimpleUser;
import com.kernel.intelcurrent.model.Task;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AtUserActivity extends BaseActivity implements Updateable,View.OnClickListener{

	private static final int REQUEST_FIRST = 1;
	private static final int REQUEST_MORE = 2;
	
	private TextView titleTv,loadTv;
	private ImageView leftImage,rightImage;
	private ListView lv;
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
			lv.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			request = -2;
		}else if(request == REQUEST_MORE){
			users.addAll(list);
			adapter.notifyDataSetChanged();
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
		titleTv = (TextView)findViewById(R.id.common_head_tv_title);
		leftImage = (ImageView)findViewById(R.id.common_head_iv_left);
		rightImage = (ImageView)findViewById(R.id.common_head_iv_right);
		loadTv = (TextView)findViewById(R.id.activity_at_tv);
		editText = (EditText)findViewById(R.id.activity_at_et);
		lv = (ListView)findViewById(R.id.activity_at_lv);
		
		lv.setSelector(R.drawable.list_cell_bg);
	}
	
	private void setAdapter(){
		adapter = new AtListAdapter(this, users);
	}
	
	private void setListeners(){
		leftImage.setOnClickListener(this);
		loadTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == leftImage){
			setResult(RESULT_CANCELED);
			finish();
		}else if(v == loadTv){
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
		}
	}
	
	@Override
	public void onConnectionFinished() {
		
	}

	@Override
	public void onConnectionDisConnected() {
		
	}

}
