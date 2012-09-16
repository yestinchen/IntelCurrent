package com.kernel.intelcurrent.activity;

import com.kernel.intelcurrent.adapter.DraftListAdapter;
import com.kernel.intelcurrent.adapter.ViewListAdapter;
import com.kernel.intelcurrent.model.DBModel;
import com.kernel.intelcurrent.widget.MoreListCell;
import com.kernel.intelcurrent.widget.NoScrollListView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends Activity implements Updateable{

	private NoScrollListView lv1,lv2,lv3;
	private MoreListCell[] views1,views2,views3;
	private TextView titleTv;
	
	@Override
	public void update(int type, Object param) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		findViews();
	}
	
	@Override
	protected void onResume() {
		setAdapter();
		super.onResume();
	}

	private void findViews(){
		titleTv = (TextView)findViewById(R.id.common_head_tv_title);
		
		lv1 = (NoScrollListView)findViewById(R.id.activity_more_lv_1);
		lv2 = (NoScrollListView)findViewById(R.id.activity_more_lv_2);
		lv3 = (NoScrollListView)findViewById(R.id.activity_more_lv_3);
		
		titleTv.setText(R.string.more_title);
	}
	
	private void setAdapter(){
		views1 = new MoreListCell[3];
		views1[0] = new MoreListCell(this, R.string.more_group_draft
				,DBModel.getInstance().getAllDrafts(this).size(),
				new Intent(this,WeiboDraftsListActivity.class));
		views1[1] = new MoreListCell(this, R.string.more_group_order_center,0,
				new Intent(this,OrderCenterListActivity.class));
		views1[2] = new MoreListCell(this, R.string.more_group_push_center,0,
				new Intent(this,PushCenterActivity.class));
//		views1[3] = new MoreListCell(this, R.string.more_group_shake,0,null);

		views2 = new MoreListCell[1];
//		views2[0] = new MoreListCell(this, R.string.more_group_settings,0,null);
//		views2[1] = new MoreListCell(this, R.string.more_group_skin,0,null);
		views2[0] = new MoreListCell(this, R.string.more_group_about,0,
				new Intent(this,AboutActivity.class));
		
		
		views3 = new MoreListCell[1];
//		views3[0] = new MoreListCell(this,R.string.more_group_account,0 ,null);
		views3[0] = new MoreListCell(this, R.string.more_group_clear_data,0,null){
			@Override
			public void onClick(View v) {
				super.onClick(v);
				AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
				builder.setTitle("提示")
					.setMessage("缓存课有效节省加载重复图片的流量\n确定清空缓存?")
					.setNegativeButton("取消", null)
					.setPositiveButton("清空", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(MoreActivity.this, "清空完毕", Toast.LENGTH_SHORT).show();
						}
					}).create().show();
			}
			
		};
		lv1.setAdapter(new ViewListAdapter(views1));
		lv2.setAdapter(new ViewListAdapter(views2));
		lv3.setAdapter(new ViewListAdapter(views3));
		
	}

	
}
