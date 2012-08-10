package com.kernel.intelcurrent.widget;

import com.kernel.intelcurrent.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupBlock extends LinearLayout {

	private LayoutInflater mInflater;
	private ImageView iv;
	private TextView tv;
	
	public GroupBlock(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public GroupBlock(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		mInflater =  LayoutInflater.from(context);
		mInflater.inflate(R.layout.widget_group_block,this,true);
		iv = (ImageView)findViewById(R.id.widget_group_block_iv);
		tv = (TextView)findViewById(R.id.widget_group_block_tv);
	}
	
	public void setImageBitmap(Bitmap bitmap){
		iv.setImageBitmap(bitmap);
	}
	
	public void setImageDrawable(Drawable dw){
		iv.setImageDrawable(dw);
	}
	
	public void setImageResources(int resid){
		iv.setImageResource(resid);
	}
	
	public void setText(CharSequence cs){
		tv.setText(cs);
	}
	
	public void setText(int resid){
		tv.setText(resid);
	}
	
}
