package com.kernel.intelcurrent.adapter;

import java.util.ArrayList;

import com.kernel.intelcurrent.activity.R;
import com.kernel.intelcurrent.model.Group;
import com.kernel.intelcurrent.widget.GroupBlock;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * groupblock的滑页adapter类
 * @author sheling*/
public class GroupBlockPagerAdapter extends PagerAdapter {

	private ArrayList<Group> groupList;
	private LayoutInflater mInflater;
	private int groupNum,pageSize,pageNum;
	private ArrayList<View> views = new ArrayList<View>();;
	private GroupBlock blocks[];
	private OnClickListener listener;
	
	public GroupBlockPagerAdapter(ArrayList<Group> groupList,Context context,OnClickListener listener){
		this.groupList = groupList;
		this.listener = listener;
		mInflater = LayoutInflater.from(context);
		groupNum = groupList.size();
		pageSize = 9;
		pageNum = groupNum % pageSize == 0 ? groupNum / pageSize : groupNum /pageSize +1;
		for(int i=0;i<pageNum;i++){
			views.add(mInflater.inflate(R.layout.page_group_block, null));
		}
		blocks = new GroupBlock[pageSize];
	}
	
	@Override
	public int getCount() {
		return pageNum;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeViewAt(position);
	}

	@Override
	public Object instantiateItem(View container, int position) {
		blocks[0] = (GroupBlock) views.get(position).findViewById(R.id.page_group_block_1);
		blocks[1] = (GroupBlock) views.get(position).findViewById(R.id.page_group_block_2);
		blocks[2] = (GroupBlock) views.get(position).findViewById(R.id.page_group_block_3);
		blocks[3] = (GroupBlock) views.get(position).findViewById(R.id.page_group_block_4);
		blocks[4] = (GroupBlock) views.get(position).findViewById(R.id.page_group_block_5);
		blocks[5] = (GroupBlock) views.get(position).findViewById(R.id.page_group_block_6);
		blocks[6] = (GroupBlock) views.get(position).findViewById(R.id.page_group_block_7);
		blocks[7] = (GroupBlock) views.get(position).findViewById(R.id.page_group_block_8);
		blocks[8] = (GroupBlock) views.get(position).findViewById(R.id.page_group_block_9);
		int i=position*pageSize;
		for(; i< Math.min((position+1)*pageSize, groupNum);i++){
			GroupBlock tmp = blocks[ i % pageSize];
			tmp.setText(groupList.get(i).getName());
			tmp.setTag(groupList.get(i));
			tmp.setOnClickListener(listener);
		}
		for(;i % pageSize != 0;i++){
			blocks[i % pageSize].setVisibility(View.INVISIBLE);
		}
		LayoutParams params = new LayoutParams();
		params.height = LayoutParams.FILL_PARENT;
		params.width = LayoutParams.FILL_PARENT;
		((ViewPager)container).addView(views.get(position), 0,params);
		return views.get(position);
	}

}
