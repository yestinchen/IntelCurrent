package com.kernel.intelcurrent.activity;

import com.kernel.intelcurrent.adapter.ViewListAdapter;
import com.kernel.intelcurrent.widget.CheckboxEntry;
import com.kernel.intelcurrent.widget.NoScrollListView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PushCenterActivity extends Activity implements View.OnClickListener{

	public static final String PREFERENCE_FILE_PUSH_CENTER = "push_config";
	
	private NoScrollListView lv1,lv2,lv3,lv4;
	private TextView tv1,tv2,tv3,titleTv;
	private ImageView leftImage;
	private CheckboxEntry[] views1,views2,views3,views4;
	private SharedPreferences prefs;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_center);
		findViews();
		init();
		setListeners();
	}
	
	@Override
	protected void onStop() {
		Editor editor = prefs.edit();
		editor.putBoolean("push_enable", views1[0].isChecked());
		editor.putBoolean("time_enable", views1[1].isChecked());
		editor.putBoolean("push_type_tiji", views2[0].isChecked());
		editor.putBoolean("push_type_sixin", views2[1].isChecked());
		editor.putBoolean("push_type_tingzhong", views2[2].isChecked());
		editor.putBoolean("push_noti_type_shake", views3[0].isChecked());
		editor.putBoolean("push_noti_type_music", views3[1].isChecked());
		int checked = 0;
		for(int i=0;i<views4.length;i++){
			if(views4[i].isChecked()){
				checked = i;
			}
		}
		switch(checked){
		case 0:
			checked = 5;
			break;
		case 1:
			checked = 10;
			break;
		case 2:
			checked = 20;
			break;
		case 3:
			checked = 30;
			break;
		}
		editor.putInt("push_time_rate", checked);
		editor.commit();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Intent intent = new Intent(this,com.kernel.intelcurrent.service.PushService.class);
		startService(intent);
		super.onDestroy();
	}

	private void findViews(){
		titleTv = (TextView)findViewById(R.id.common_head_tv_title);
		leftImage = (ImageView)findViewById(R.id.common_head_iv_left);
		lv1 = (NoScrollListView) findViewById(R.id.activity_push_center_lv_1);
		lv2 = (NoScrollListView)findViewById(R.id.activity_push_center_lv_2);
		lv3 = (NoScrollListView)findViewById(R.id.activity_push_center_lv_3);
		lv4 = (NoScrollListView)findViewById(R.id.activity_push_center_lv_4);
		tv1 = (TextView)findViewById(R.id.activity_push_center_tv_tip1);
		tv2 = (TextView)findViewById(R.id.activity_push_center_tv_tip2);
		tv3 = (TextView)findViewById(R.id.activity_push_center_tv_tip3);
		
		leftImage.setImageResource(R.drawable.ic_title_back);
		titleTv.setText(R.string.more_group_push_center);
		tv1.setText(R.string.push_center_group_push_type);
		tv2.setText(R.string.push_center_group_push_noti);
		tv3.setText(R.string.push_center_group_time_rate);
	}
	
	private void init(){
		views1 = new CheckboxEntry[2];
		views1[0] = new CheckboxEntry(this, R.string.push_center_enable);
		views1[1] = new CheckboxEntry(this, R.string.push_center_time_field);
		lv1.setAdapter(new ViewListAdapter(views1));
		
		views2 = new CheckboxEntry[3];
		views2[0] = new CheckboxEntry(this, R.string.push_center_push_type_tiji);
		views2[1] = new CheckboxEntry(this, R.string.push_center_push_type_sixin);
		views2[2] = new CheckboxEntry(this, R.string.push_center_push_type_tingzhong);
		lv2.setAdapter(new ViewListAdapter(views2));
		
		views3 = new CheckboxEntry[2];
		views3[0] = new CheckboxEntry(this, R.string.push_center_push_shake);
		views3[1] = new CheckboxEntry(this, R.string.push_center_push_music);
		lv3.setAdapter(new ViewListAdapter(views3));
		
		views4 = new CheckboxEntry[4];
		views4[0] = new CheckboxEntry(this, R.string.push_center_time_rate_5);
		views4[1] = new CheckboxEntry(this, R.string.push_center_time_rate_10);
		views4[2] = new CheckboxEntry(this, R.string.push_center_time_rate_20);
		views4[3] = new CheckboxEntry(this, R.string.push_center_time_rate_30);
		lv4.setAdapter(new ViewListAdapter(views4));

		prefs = getSharedPreferences(PREFERENCE_FILE_PUSH_CENTER,MODE_PRIVATE);
		views1[0].setChecked(prefs.getBoolean("push_enable", true));
		views1[1].setChecked(prefs.getBoolean("time_enable", true));
		views2[0].setChecked(prefs.getBoolean("push_type_tiji", true));
		views2[1].setChecked(prefs.getBoolean("push_type_sixin", true));
		views2[2].setChecked(prefs.getBoolean("push_type_tingzhong", true));
		views3[0].setChecked(prefs.getBoolean("push_noti_type_shake", true));
		views3[1].setChecked(prefs.getBoolean("push_noti_type_music", true));
		int rate = prefs.getInt("push_time_rate",20);
		switch(rate){
		case 5:
			views4[0].setChecked(true);
			break;
		case 10:
			views4[1].setChecked(true);
			break;
		case 20:
			views4[2].setChecked(true);
			break;
		case 30:
			views4[3].setChecked(true);
			break;
		}
		checkSelectable();
	}
	
	private void setListeners(){
		leftImage.setOnClickListener(this);
		views1[0].setOnClickListener(this);
		views1[1].setOnClickListener(this);
		views2[0].setOnClickListener(this);
		views2[1].setOnClickListener(this);
		views2[2].setOnClickListener(this);
		views3[0].setOnClickListener(this);
		views3[1].setOnClickListener(this);
		views4[0].setOnClickListener(this);
		views4[1].setOnClickListener(this);
		views4[2].setOnClickListener(this);
		views4[3].setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v instanceof CheckboxEntry)
			((CheckboxEntry)v).onClick(v);
		if(v == views1[0]){
			checkSelectable();
		}else if(v == leftImage){
			finish();
		}else if(v == views4[0] || v == views4[1] || v== views4[2]||v == views4[3]){
			if(((CheckboxEntry)v).isChecked()){
				for(int i=0;i<views4.length;i++){
					if(views4[i] != v){
						views4[i].setChecked(false);
					}
				}
			}
		}
	}
	
	/**较检其他选项是否可选*/
	private void checkSelectable(){
		boolean value = views1[0].isChecked();
		views1[1].setEnable(value);
		for(int i=0;i<views2.length;i++){
			views2[i].setEnable(value);
		}
		for(int i=0;i<views3.length;i++){
			views3[i].setEnable(value);
		}
		for(int i=0;i<views4.length;i++){
			views4[i].setEnable(value);
		}
	}
	
}
