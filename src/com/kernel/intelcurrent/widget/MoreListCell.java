package com.kernel.intelcurrent.widget;

import com.kernel.intelcurrent.activity.R;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

public class MoreListCell extends LinearLayout implements View.OnClickListener {

	private Context context;
	private String name;
	private int num;
	private TextView nameTv,numTv;
	private Intent intent;
	public MoreListCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	public MoreListCell(Context context,String name,int num,Intent intent) {
		super(context);
		this.context = context;
		this.name = name;
		this.num = num;
		this.intent = intent;
		init();
	}
	
	public MoreListCell(Context context,int name,int num,Intent intent) {
		super(context);
		this.context = context;
		this.name = getResources().getString(name);
		this.num = num;
		this.intent = intent;
		init();
	}
	
	private void init(){
		LayoutInflater.from(context).inflate(R.layout.cell_more_list, this,true);

		nameTv = (TextView) findViewById(R.id.cell_more_list_tv_name);
		numTv = (TextView) findViewById(R.id.cell_more_list_tv_num);
		

		nameTv.setText(name);
		numTv.setText(num == 0 ? "":num+"");
		setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(intent != null)
			context.startActivity(intent);
	}

	
}
