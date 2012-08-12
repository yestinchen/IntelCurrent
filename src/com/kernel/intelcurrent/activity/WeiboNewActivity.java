package com.kernel.intelcurrent.activity;

import com.kernel.intelcurrent.adapter.ExpressionGridAdapter;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.widget.ImageCheckBox;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 新建微博，包括评论，转发
 * <br/>使用时intent放入"model"指定模式，int类型，默认为新建
 * <br/>"platform"指定平台,int,平台为User.PLATFORM_TENCENT_CODE 或 User.PLATFORM_SINA_CODE，或3(两个平台都有),-1都没
 * <br/>"ext"放入其他如评论微博id或转播微博id,String
 * @author sheling*/
public class WeiboNewActivity extends Activity implements View.OnClickListener{

	public static final int MODEL_NEW_WEIBO = 0;
	public static final int MODEL_NEW_COMMENT = 1;
	public static final int MODEL_FORWORD = 2;
	
	private ImageView picImage,atImage,topicImage,expressionImage,leftImage,rightImage;
	private ImageCheckBox sinaCheckBox,tencentCheckBox;
	private TextView numTips,titleTv;
	private EditText inputEditText;
	private GridView expressionsGv;
	private LinearLayout expressionsLayout;
	private float textLength;
	private int model,platform;
	private boolean hasTencent,hasSina;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_new);
		findViews();
		setListeners();
		initModel();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if(newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_NO){
			removeExpressions();
		}
		super.onConfigurationChanged(newConfig);
	}

	private void findViews(){
		titleTv = (TextView)findViewById(R.id.common_head_tv_title);
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
		
		expressionsLayout = (LinearLayout)findViewById(R.id.activity_weibo_new_layout_expressions);
		expressionsGv = (GridView)findViewById(R.id.activity_weibo_new_gv_expressions);
		
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
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				textLength = caculateLength(s.toString());
				numTips.setText((140-Math.round(textLength))+"字");
			}
		});
		inputEditText.setOnClickListener(this);
		leftImage.setOnClickListener(this);
		rightImage.setOnClickListener(this);
		picImage.setOnClickListener(this);
		atImage.setOnClickListener(this);
		topicImage.setOnClickListener(this);
		expressionImage.setOnClickListener(this);
		
		sinaCheckBox.setOnClickListener(this);
		tencentCheckBox.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		if(v == leftImage){
			finish();
		}else if(v == rightImage){
			if(checkContentLength())
				wrapAndSend();
		}else if (v == picImage){
			
		}else if (v == topicImage){
			//添加话题#号并置光标在两个#之间
			int start = inputEditText.getSelectionStart();
			inputEditText.getText().insert(start,"##");
			inputEditText.setSelection(start+1);
		}else if(v == expressionImage){
			if(expressionsLayout.getVisibility() == View.VISIBLE){
				removeExpressions();
			}else {
				showExpressions();
			}
		}else if(v == inputEditText){
			//如果软键盘出现，表情还在显示，去掉表情
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if(imm.isActive() && expressionsLayout.getVisibility() == View.VISIBLE)	
				removeExpressions();
		}else if (v == sinaCheckBox){
			//要先执行默认的方法
			sinaCheckBox.onClick(v);	
			hasSina = !hasSina;
			platformChanaged();
		}else if( v== tencentCheckBox){
			tencentCheckBox.onClick(v);
			hasTencent = !hasTencent;
			platformChanaged();
		}
	}
	
	/**根据不同Model调整布局*/
	private void initModel(){
		Intent intent = getIntent();
		model = intent.getIntExtra("model", 0);
		platform = intent.getIntExtra("platform", 3);
		switch(model){
		case MODEL_NEW_WEIBO:
			titleTv.setText(R.string.weibo_new_new);
			if(platform == 3){
				atImage.setVisibility(View.GONE);
				expressionImage.setVisibility(View.GONE);
				hasTencent = hasSina = true;
				tencentCheckBox.setChecked(true);
				sinaCheckBox.setChecked(true);
			}else if(platform == User.PLATFORM_SINA_CODE){
				hasSina = true;
				sinaCheckBox.setChecked(true);
			}else {
				hasTencent = true;
				tencentCheckBox.setChecked(true);
			}
			break;
		case MODEL_NEW_COMMENT:
			picImage.setVisibility(View.GONE);
			sinaCheckBox.setVisibility(View.GONE);
			tencentCheckBox.setVisibility(View.GONE);
			titleTv.setText(R.string.weibo_new_comment);
			break;
		case MODEL_FORWORD:
			picImage.setVisibility(View.GONE);
			sinaCheckBox.setVisibility(View.GONE);
			tencentCheckBox.setVisibility(View.GONE);
			titleTv.setText(R.string.weibo_new_forword);
			break;
		}
	}
	
	/**检查输入文字是否超出满足字数显示
	 * @return true 满足 fasle 不满足*/
	private boolean checkContentLength(){
		if(caculateLength(inputEditText.getText().toString()) < 140)
			return true;
		return false;
	}
	
	/**处理并发送微博*/
	private void wrapAndSend(){
		//TODO wrap and send weibo
	}
	
	/**平台更改变化调整布局
	 * <br>目前只在新建微博时的平台监听上用到
	 * */
	private void platformChanaged(){
		if(hasSina && hasTencent){
			platform = 3;
		}else if(hasSina){
			platform = User.PLATFORM_SINA_CODE;
		}else if(hasTencent){
			platform = User.PLATFORM_TENCENT_CODE;
		}else {
			platform = -1;
		}
		switch(platform){
		case 3:
			atImage.setVisibility(View.GONE);
			expressionImage.setVisibility(View.GONE);
			break;
		default:
			atImage.setVisibility(View.VISIBLE);
			expressionImage.setVisibility(View.VISIBLE);
			break;
		}
		if(expressionsLayout.getVisibility() == View.VISIBLE){
			removeExpressions();
		}
	}
	
	/**显示下方发表情列表*/
	private void showExpressions(){
		switch(platform){
		//根据平台添加需要显示的表情
		case User.PLATFORM_TENCENT_CODE:
			expressionsGv.setAdapter(new ExpressionGridAdapter(
					this, ExpressionGridAdapter.MODEL_TENCENT_EXPRESSION,inputEditText));
			break;
		}
		
		expressionsLayout.setVisibility(View.VISIBLE);
		//隐藏键盘显示
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**移走下方的表情列表*/
	private void removeExpressions(){
		expressionsGv.setAdapter(null);
		expressionsLayout.setVisibility(View.GONE);
	}
	
	/**计算已写微博长度*/
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
