package com.kernel.intelcurrent.adapter;

import java.util.List;
import com.kernel.intelcurrent.util.ICEmotions;
import com.kernel.intelcurrent.util.ICEmotions.EmotionPair;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

/**表情显示的Adapter，用于显示所有待选择的表情*/
public class ExpressionGridAdapter extends BaseAdapter {

	public static final int MODEL_SINA_EXPRESSION = 1;
	public static final int MODEL_TENCENT_EXPRESSION = 2;
	public static final int MODEL_TEXT_EXPRESSION = 3;
	
	private Context context;
	private int model;
	private EditText inputET;
	private List<ICEmotions.EmotionPair> expressionsMap;
	
	public ExpressionGridAdapter(Context context,int model,EditText inputET){
		this.context = context;
		this.model = model;
		this.inputET = inputET;
		switch(model){
		case MODEL_TENCENT_EXPRESSION:
			expressionsMap = ICEmotions.getInstance().getTencentNewEmotions();
			break;
		}
	}
	
	@Override
	public int getCount() {
		switch(model){
		case MODEL_TENCENT_EXPRESSION:
			return expressionsMap.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return expressionsMap.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		switch(model){
		case MODEL_TENCENT_EXPRESSION:
			EmotionPair emotion = expressionsMap.get(position);
			if(convertView == null){
				convertView = new ImageView(context);
				final String emotionName = emotion.name;
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int start = inputET.getSelectionStart();
						inputET.getText().insert(start, "/"+emotionName);
					}
				});
			}
			((ImageView)convertView).setImageResource(emotion.resid);
		}
		return convertView;
	}

}
