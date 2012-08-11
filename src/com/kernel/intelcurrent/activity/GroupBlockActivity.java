package com.kernel.intelcurrent.activity;

import java.util.ArrayList;
import com.kernel.intelcurrent.adapter.GroupBlockPagerAdapter;
import com.kernel.intelcurrent.db.DataBaseHelper;
import com.kernel.intelcurrent.model.DBModel;
import com.kernel.intelcurrent.model.Group;
import com.kernel.intelcurrent.model.User;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

public class GroupBlockActivity extends Activity{

	private static final String TAG = GroupBlockActivity.class.getSimpleName();
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_group_block);
		findViews();
		setListeners();
		setAdapter();
	}

	private void findViews(){
		viewPager = (ViewPager)findViewById(R.id.layout_group_block_viewpager);
//		Log.d(TAG, viewPager.toString());
	}
	
	private void setListeners(){
		
	}
	
	private void setAdapter(){
		ArrayList<Group> groupList = DBModel.getInstance().getGroupList(this);
		Log.d(TAG, "get groups:"+groupList);
		GroupBlockPagerAdapter adapter = new GroupBlockPagerAdapter(groupList, this,
				new OnClickListener() {
				@Override
				public void onClick(View v) {
					Group group = (Group)v.getTag();
					Log.d(TAG, group.toString());
					startTimelineActivity(group);
				}
			});
		viewPager.setAdapter(adapter);
	}
	

	private void startTimelineActivity(Group group){
		LinearLayout container=(LinearLayout)((ActivityGroup)getParent()).getWindow().findViewById(R.id.layout_main_layout_container);	
		container.removeAllViews();
        Intent intent=new Intent(this,TimelineActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable("group", group);
        intent.putExtra("ext", bundle);
        Window subActivity=((ActivityGroup)this.getParent()).getLocalActivityManager().startActivity(TimelineActivity.class.getSimpleName(),intent);
        container.addView(subActivity.getDecorView());
	}

}
