package com.kernel.intelcurrent.activity;

import com.kernel.intelcurrent.widget.ImageCheckBox;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WeiboNewActivity extends Activity {

	private ImageView picImage,atImage,topicImage,expressionImage,leftImage,rightImage;
	private ImageCheckBox sinaCheckBox,tencentCheckBox;
	private TextView numTips;
	private EditText inputEditText;
	private float textLength;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_new);
		findViews();
		setListeners();
	}

	private void findViews(){
		leftImage =(ImageView)findViewById(R.id.common_head_iv_left);
		rightImage =(ImageView)findViewById(R.id.common_head_iv_right);
		inputEditText = (EditText)findViewById(R.id.activity_weibo_new_edittext);
		numTips = (TextView)findViewById(R.id.activity_weibo_new_tv_num_tip);
		picImage = (ImageView) findViewById(R.id.activity_weibo_new_iv_pic);
		atImage = (ImageView) findViewById(R.id.activity_weibo_new_iv_at);
		topicImage = (ImageView) findViewById(R.id.activity_weibo_new_iv_topic);
		expressionImage = (ImageView) findViewById(R.id.activity_weibo_new_iv_expression);
		sinaCheckBox = (ImageCheckBox)findViewById(R.id.activity_weibo_new_cb_sina);
		tencentCheckBox = (ImageCheckBox)findViewById(R.id.activity_weibo_new_cb_tencent);
		
		rightImage.setImageResource(R.drawable.ic_title_finish);
		leftImage.setImageResource(R.drawable.ic_title_back);
		sinaCheckBox.init(R.drawable.ic_new_weibo_platform_sina_selected, R.drawable.ic_new_weibo_platform_sina_unselected);
		tencentCheckBox.init(R.drawable.ic_new_weibo_platform_tencent_selected, R.drawable.ic_new_weibo_platform_tencent_unselected);
	}
	
	private void setListeners(){
		inputEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				textLength = caculateLength(s.toString());
				numTips.setText((140-Math.round(textLength))+"字");
			}
		});
	}
	
	private float caculateLength(String s){
		float length = 0f;
		//\u0391-\uFFE5
		//\u4e00-\u9fa5
		for(int i=0;i<s.length();i++){
			if(s.charAt(i) >0 && s.charAt(i) < 127){
				length += 0.5;
			}else length += 1;
		}
		return length;
	}
}
