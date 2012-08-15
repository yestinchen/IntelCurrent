package com.kernel.intelcurrent.activity;

import com.kernel.intelcurrent.adapter.ViewListAdapter;
import com.kernel.intelcurrent.widget.CheckboxEntry;
import com.kernel.intelcurrent.widget.NoScrollListView;

import android.app.Activity;
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
	
	private NoScrollListView lv1,lv2,lv3;
	private TextView tv1,tv2,titleTv;
	private ImageView leftImage;
	private CheckboxEntry[] views1,views2,views3;
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
		editor.putBoolean("push_type_pinglun", views2[0].isChecked());
		editor.putBoolean("push_type_tiji", views2[1].isChecked());
		editor.putBoolean("push_type_sixin", views2[2].isChecked());
		editor.putBoolean("push_type_tingzhong", views2[3].isChecked());
		editor.putBoolean("push_noti_type_shake", views3[0].isChecked());
		editor.putBoolean("push_noti_type_music", views3[1].isChecked());
		editor.commit();
		super.onStop();
	}

	private void findViews(){
		titleTv = (TextView)findViewById(R.id.common_head_tv_title);
		leftImage = (ImageView)findViewById(R.id.common_head_iv_left);
		lv1 = (NoScrollListView) findViewById(R.id.activity_push_center_lv_1);
		lv2 = (NoScrollListView)findViewById(R.id.activity_push_center_lv_2);
		lv3 = (NoScrollListView)findViewById(R.id.activity_push_center_lv_3);
		tv1 = (TextView)findViewById(R.id.activity_push_center_tv_tip1);
		tv2 = (TextView)findViewById(R.id.activity_push_center_tv_tip2);
		
		leftImage.setImageResource(R.drawable.ic_title_back);
		titleTv.setText(R.string.more_group_push_center);
		tv1.setText(R.string.push_center_group_push_type);
		tv2.setText(R.string.push_center_group_push_noti);
	}
	
	private void init(){
		views1 = new CheckboxEntry[2];
		views1[0] = new CheckboxEntry(this, R.string.push_center_enable);
		views1[1] = new CheckboxEntry(this, R.string.push_center_time_field);
		lv1.setAdapter(new ViewListAdapter(views1));
		
		views2 = new CheckboxEntry[4];
		views2[0] = new CheckboxEntry(this, R.string.push_center_push_type_pinglun);
		views2[1] = new CheckboxEntry(this, R.string.push_center_push_type_tiji);
		views2[2] = new CheckboxEntry(this, R.string.push_center_push_type_sixin);
		views2[3] = new CheckboxEntry(this, R.string.push_center_push_type_tingzhong);
		lv2.setAdapter(new ViewListAdapter(views2));
		
		views3 = new CheckboxEntry[2];
		views3[0] = new CheckboxEntry(this, R.string.push_center_push_shake);
		views3[1] = new CheckboxEntry(this, R.string.push_center_push_music);
		lv3.setAdapter(new ViewListAdapter(views3));
		

		prefs = getSharedPreferences(PREFERENCE_FILE_PUSH_CENTER,MODE_PRIVATE);
		views1[0].setChecked(prefs.getBoolean("push_enable", true));
		views1[1].setChecked(prefs.getBoolean("time_enable", true));
		views2[0].setChecked(prefs.getBoolean("push_type_pinglun", true));
		views2[1].setChecked(prefs.getBoolean("push_type_tiji", true));
		views2[2].setChecked(prefs.getBoolean("push_type_sixin", true));
		views2[3].setChecked(prefs.getBoolean("push_type_tingzhong", true));
		views3[0].setChecked(prefs.getBoolean("push_noti_type_shake", true));
		views3[1].setChecked(prefs.getBoolean("push_noti_type_music", true));
		checkSelectable();
	}
	
	private void setListeners(){
		leftImage.setOnClickListener(this);
		views1[0].setOnClickListener(this);
		views1[1].setOnClickListener(this);
		views2[0].setOnClickListener(this);
		views2[1].setOnClickListener(this);
		views2[2].setOnClickListener(this);
		views2[3].setOnClickListener(this);
		views3[0].setOnClickListener(this);
		views3[1].setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v instanceof CheckboxEntry)
			((CheckboxEntry)v).onClick(v);
		if(v == views1[0]){
			checkSelectable();
		}else if(v == leftImage){
			finish();
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
	}
	
}
