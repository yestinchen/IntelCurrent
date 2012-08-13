package com.kernel.intelcurrent.widget;

import com.kernel.intelcurrent.activity.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * 用于显示自定义的listview,永远无ScrollBar，需要放在ScrollView中显示
 * @author sheling*/
public class NoScrollListView extends ListView {

	private static final String TAG = NoScrollListView.class.getSimpleName();
	
	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public NoScrollListView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		setVerticalScrollBarEnabled(false);
		setHorizontalScrollBarEnabled(false);
//		setBackgroundResource(R.drawable.more_group_bg);
	}

	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

		super.onMeasure(widthMeasureSpec, 
		MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, 0)); 
	
		ListAdapter listAdapter = getAdapter(); 
	
		if (listAdapter == null) { 
			return; 
		} 
		int totalHeight = 0; 
		int height; 
	
		int footCount = getFooterViewsCount(); 
		int headCount = getHeaderViewsCount(); 
		int adapterCount = listAdapter.getCount(); 
		int childCount = getChildCount(); 
		Log.v(TAG, "getChildCount():" + childCount + ",getAdapterCount():" 
		+ adapterCount); 
		Log.v(TAG, "footCount:" + footCount + ",headCount:" + headCount); 
		for (int j = 0; j < childCount; j++) { 
			Log.v(TAG, "child :" + j + ",height:"+ getChildAt(j).getHeight()); 
		} 
		for (int i = 0; i < adapterCount; i++) { // listAdapter.getCount()返回数据项的数目 
			View listItem = null; 
		if (childCount > 0) // 只有childCount 存在时候才使用已有的子件直接计算 
		{ 
	
			if (footCount > 0 && i >= adapterCount - footCount) {// 获取listItem 
				// footer部分 
				listItem = getChildAt((i - adapterCount) + childCount); 
			} else if (i >= childCount - footCount) 
				listItem = getChildAt(i + headCount + footCount); // 在listView内foothead的子项index都是前面的 
			else 
				listItem = getChildAt(i); 
			} 
			height = 0; 
			if (listItem == null) { 
				listItem = listAdapter.getView(i, null, this); 
				Log.v(TAG, listItem.toString());
				try { 
					listItem.measure(MeasureSpec.UNSPECIFIED, 
					MeasureSpec.UNSPECIFIED); // 计算子项View 的宽高 
					height = listItem.getMeasuredHeight(); 
				} catch (Throwable t) { 
					t.printStackTrace();
				}
			} else 
				height = listItem.getHeight(); 
		
			Log.v(TAG, "listAdapter row " + i + "height:" + height); 
		
			totalHeight += height; // 统计所有子项的总高度 
	
		} 
		// 总高度 每个item总高度 + 子项间隔总和 +头部缩进 + 底部缩进 
		totalHeight = totalHeight 
		+ (getDividerHeight() * (listAdapter.getCount() - 1)) 
		+ getPaddingBottom() + getPaddingTop(); 
		// Log.v(TAG, "params.height" + params.height); 
		// // listView.getDividerHeight()获取子项间分隔符占用的高度 
		// 
		// // params.height最后得到整个ListView完整显示需要的高度 
	
		setMeasuredDimension(getMeasuredWidth(), totalHeight); 

	} 
}
