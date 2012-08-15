package com.kernel.intelcurrent.activity;

import java.util.ArrayList;
import com.kernel.intelcurrent.adapter.DraftListAdapter;
import com.kernel.intelcurrent.model.DBModel;
import com.kernel.intelcurrent.model.WeiboDraftEntryDAO;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WeiboDraftsListActivity extends Activity implements OnClickListener{

	private TextView titleTv;
	private ImageView leftImage,rightImage;
	private ListView lv;
	private DBModel dbModel;
	private ArrayList<WeiboDraftEntryDAO> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_draft_list);
		findViews();
		setListeners();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		init();
		setAdapter();
	}

	private void findViews(){
		titleTv = (TextView)findViewById(R.id.common_head_tv_title);
		leftImage = (ImageView)findViewById(R.id.common_head_iv_left);
		rightImage = (ImageView)findViewById(R.id.common_head_iv_right);
		lv = (ListView)findViewById(R.id.activity_weibo_draft_list_lv);
		
		leftImage.setImageResource(R.drawable.ic_title_back);
		titleTv.setText(R.string.more_group_draft);
	}
	
	private void init(){
		dbModel = DBModel.getInstance();
		list = dbModel.getAllDrafts(this);
	}
	
	private void setListeners(){
		leftImage.setOnClickListener(this);
	}
	
	private void setAdapter(){
		lv.setAdapter(new DraftListAdapter(this, list));
	}

	@Override
	public void onClick(View v) {
		if(v == leftImage){
			finish();
		}
	}

}
