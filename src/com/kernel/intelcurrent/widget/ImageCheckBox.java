package com.kernel.intelcurrent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;;

/**自定义的checkbox,只有两个图片来回切换
 * 使用之前需要调用init
 * @author sheling*/
public class ImageCheckBox extends ImageView implements OnClickListener{
	
	private boolean checked = false;
	private int selected;
	private int unselected;
	public ImageCheckBox(Context context) {
		super(context);
		setOnClickListener(this);
	}
	
	public ImageCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	
	public ImageCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);
	}
	
	public void init(int residChecked,int residUnChecked){
		init(residChecked,residUnChecked,false);
	}
	
	public void init(int residChecked,int residUnChecked,boolean defaultValue){
		this.selected = residChecked;
		this.unselected = residUnChecked;
		setChecked(defaultValue);
	}
	
	public void setChecked(boolean checked){
		this.checked = checked;
		this.setImageResource(checked ? selected : unselected);
	}
	
	public boolean isChecked(){
		return checked;
	}

	@Override
	public void onClick(View v) {
		setChecked(!checked);
	}
	
}
