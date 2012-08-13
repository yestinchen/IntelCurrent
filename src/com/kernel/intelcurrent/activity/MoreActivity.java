package com.kernel.intelcurrent.activity;

import java.util.ArrayList;

import com.kernel.intelcurrent.adapter.MoreListAdapter;
import com.kernel.intelcurrent.adapter.MoreListAdapter.MoreListEntry;
import com.kernel.intelcurrent.widget.NoScrollListView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MoreActivity extends Activity implements Updateable{

	private NoScrollListView lv1,lv2,lv3;
	private MoreListAdapter adapter1,adapter2,adapter3;
	private TextView titleTv;
	
	@Override
	public void update(int type, Object param) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		findViews();
		setAdapter();
	}
	
	private void findViews(){
		titleTv = (TextView)findViewById(R.id.common_head_tv_title);
		
		lv1 = (NoScrollListView)findViewById(R.id.activity_more_lv_1);
		lv2 = (NoScrollListView)findViewById(R.id.activity_more_lv_2);
		lv3 = (NoScrollListView)findViewById(R.id.activity_more_lv_3);
		
		titleTv.setText(R.string.more_title);
	}
	
	private void setAdapter(){
		ArrayList<MoreListAdapter.MoreListEntry> list = new ArrayList<MoreListAdapter.MoreListEntry>();
		list.add(new MoreListEntry(getResources().getString(R.string.more_group_draft),1));
		list.add(new MoreListEntry(getResources().getString(R.string.more_group_order_center),0));
		list.add(new MoreListEntry(getResources().getString(R.string.more_group_push_center),0));
		list.add(new MoreListEntry(getResources().getString(R.string.more_group_shake),0));
		adapter1 = new MoreListAdapter(this, list);
		
		list = new ArrayList<MoreListAdapter.MoreListEntry>();
		list.add(new MoreListEntry(getResources().getString(R.string.more_group_settings),0));
		list.add(new MoreListEntry(getResources().getString(R.string.more_group_skin),0));
		list.add(new MoreListEntry(getResources().getString(R.string.more_group_about),0));
		adapter2 = new MoreListAdapter(this, list);
		
		list = new ArrayList<MoreListAdapter.MoreListEntry>();
		list.add(new MoreListEntry(getResources().getString(R.string.more_group_account),0));
		adapter3 = new MoreListAdapter(this, list);
		
		lv1.setAdapter(adapter1);
		lv2.setAdapter(adapter2);
		lv3.setAdapter(adapter3);
		
	}

	
}
