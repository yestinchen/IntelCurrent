package com.kernel.intelcurrent.activity;

import java.util.ArrayList;
import com.kernel.intelcurrent.adapter.GroupBlockPagerAdapter;
import com.kernel.intelcurrent.model.Group;
import com.kernel.intelcurrent.model.User;
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

public class GroupBlockActivity extends BaseActivity{

	private static final String TAG = GroupBlockActivity.class.getSimpleName();
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_group_block);
		findViews();
		setListeners();
	}

	private void findViews(){
		viewPager = (ViewPager)findViewById(R.id.layout_group_block_viewpager);
//		Log.d(TAG, viewPager.toString());
	}
	
	private void setListeners(){
		ArrayList<Group> groupList  = new ArrayList<Group>();
		Group group = new Group("生活",null, null);
		User user = new User();
		user.id = "5E8681F98139420CB194F0D1E4718370";
		group.addUser(user);
		user = new User();
		user.id = "086FA8A117BCB9C8EBBC3FD4F2FD8376";
		group.addUser(user);
		user = new User();
		user.id = "43F2B3CB5DA2213AFCDB76F1AC70C569";
		group.addUser(user);
		user = new User();
		user.id = "428A900FCA135ACBF5811135E0DF1EE4";
		group.addUser(user);
		user = new User();
		user.id = "89C271210B9B7861332B51E5264E946F";
		group.addUser(user);
		user.id = "8491A789BBA1A5B9435C643C8424FDF5";
		group.addUser(user);
		groupList.add(group);
		group = new Group("财经",null, null);
		user.id = "82AFF1BC839CA811C0C6B6093262A41E";
		group.addUser(user);
		user = new User();
		user.id = "D4D61FE9B75C6D2909CE9D61DB03345E";
		group.addUser(user);
		user = new User();
		user.id = "9D69D933C8F86DC167DC70DFBAA407CA";
		group.addUser(user);
		user = new User();
		user.id = "6C37CAC0BCBAA5ABC1CFAA2F38A39E56";
		group.addUser(user);
		user = new User();
		user.id = "89C271210B9B7861332B51E5264E946F";
		group.addUser(user);
		groupList.add(group);
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
//		LinearLayout container=(LinearLayout)((ActivityGroup)getParent()).getWindow().findViewById(R.id.layout_main_layout_container);	
//		container.removeAllViews();
//        Intent intent=new Intent(this,TimelineActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("group", group);
//        intent.putExtra("ext", bundle);
//        Window subActivity=((ActivityGroup)this.getParent()).getLocalActivityManager().startActivity(TimelineActivity.class.getSimpleName(),intent);
//        container.addView(subActivity.getDecorView());
	}

}
