package com.kernel.intelcurrent.widget;

import com.kernel.intelcurrent.activity.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckboxEntry extends LinearLayout implements View.OnClickListener{

	private Context context;
	private TextView text;
	private ImageView checkBox;
	private boolean checked,enable = true;
	String name;
	public CheckboxEntry(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	
	public CheckboxEntry(Context context,String name) {
		super(context);
		this.context = context;
		this.name = name;
		init();
	}
	
	public CheckboxEntry(Context context,int name) {
		super(context);
		this.context = context;
		this.name = getResources().getString(name);
		init();
	}
	
	private void init(){
		LayoutInflater.from(context).inflate(R.layout.cell_checkbox_entry,this,true);
		text = (TextView) findViewById(R.id.cell_checkbox_name);
		checkBox = (ImageView) findViewById(R.id.cell_checkbox_iv);
		if(name!=null)text.setText(name);
		setOnClickListener(this);
	}

	public void setEnable(boolean enable){
		this.enable = enable;
		enableChanged();
	}
	
	public boolean isEnabled(){
		return enable;
	}
	
	public void setChecked(boolean checked){
		this.checked = checked;
		stateChanged();
	}
	
	public boolean isChecked(){
		return checked;
	}
	
	private void enableChanged(){
		if(!enable){
			if(checked){
				checkBox.setImageResource(R.drawable.ic_checkbox_checked_unable);
			}else{
				checkBox.setImageResource(R.drawable.ic_checkbox_unchecked);
			}
		}else{
			stateChanged();
		}
	}
	
	private void stateChanged(){
		if(checked){
			checkBox.setImageResource(R.drawable.ic_checkbox_checked);
		}else{
			checkBox.setImageResource(R.drawable.ic_checkbox_unchecked);
		}
	}

	@Override
	public void onClick(View v) {
		if(enable)
			setChecked(!checked);
	}
}
