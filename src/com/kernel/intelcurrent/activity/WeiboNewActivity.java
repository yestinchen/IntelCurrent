package com.kernel.intelcurrent.activity;

import java.io.File;
import java.io.IOException;
import com.kernel.intelcurrent.adapter.ExpressionGridAdapter;
import com.kernel.intelcurrent.model.DBModel;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.WeiboDraftEntryDAO;
import com.kernel.intelcurrent.widget.ImageCheckBox;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 新建微博，包括评论，转发
 * <br/>使用时intent放入"model"指定模式，int类型，默认为新建
 * <br/>"platform"指定平台,int,平台为Task.PLATFORM_ALL,Task.PLATFORM_SINA 或 Task.PLATFORM_TENCENT，或-1都没
 * <br/>"ext"放入其他如评论微博id或转播微博id,String
 * <br/>"text"加入预置文字
 * <br/>"cursor"指定光标显示位置,CURSOR_BEGEIN 或 CURSOR_END
 * @author sheling*/
public class WeiboNewActivity extends BaseActivity implements View.OnClickListener,Updateable{

	/**用于model的模式*/
	public static final int MODEL_NEW_WEIBO = 0;
	/**用于model的模式*/
	public static final int MODEL_NEW_COMMENT = 1;
	/**用于model的模式*/
	public static final int MODEL_FORWORD = 2;
	/**用于cursor*/
	public static final int CURSOR_BEGEIN = 1;
	/**用于cursor*/
	public static final int CURSOR_END = 0;
	
	private static final int ON_RESULT_TAKE_PHOTO = 1;
	private static final int ON_RESULT_CHOOSE_PICTURE = 2;
	private static final int ON_RESULT_CHOOSE_AT = 3;
	private static final String TAG = WeiboNewActivity.class.getSimpleName();
	
	private ImageView picImage,atImage,topicImage,expressionImage,leftImage,rightImage,picTipImage;
	private ImageCheckBox sinaCheckBox,tencentCheckBox;
	private TextView numTips,titleTv;
	private EditText inputEditText;
	private GridView expressionsGv;
	private LinearLayout expressionsLayout;
	private float textLength;
	private int model,platform;
	private boolean hasTencent,hasSina;
	private String lastPhotoPath;//存储图片路径
	private int cursorPosition;
	private String text;
	String statusId;//非新建时对应的statusid

	@Override
	public void update(int type, Object param) {
		//这个忽略各种返回的update。。。实际上它不需要刷新
		Toast.makeText(this, "操作成功", Toast.LENGTH_SHORT).show();
		finish();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			switch(requestCode){
			case ON_RESULT_TAKE_PHOTO:
			case ON_RESULT_CHOOSE_PICTURE:
				if(lastPhotoPath != null){
					setPic(lastPhotoPath);
				}
				break;
			case ON_RESULT_CHOOSE_AT:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**设置显示图片缩略图*/
	private void setPic(String path){
		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bmOptions);
		int targetH = 60;

		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		int targetW = photoW / (photoH/targetH);
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
		picTipImage.setImageBitmap(bitmap);
		picTipImage.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_new);
		findViews();
		setListeners();
		initModel();
	}

	@Override
	public void onBackPressed() {
		if(inputEditText.getText().toString().length() != 0){
			backWarn();
		}else{
			super.onBackPressed();
		}
	}
	private void findViews(){
		titleTv = (TextView)findViewById(R.id.common_head_tv_title);
		leftImage =(ImageView)findViewById(R.id.common_head_iv_left);
		rightImage =(ImageView)findViewById(R.id.common_head_iv_right);
		inputEditText = (EditText)findViewById(R.id.activity_weibo_new_edittext);
		picTipImage = (ImageView)findViewById(R.id.activity_weibo_new_iv_pic_tip);
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
		
		picTipImage.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		if(v == leftImage){
			if(inputEditText.getText().toString().length() != 0){
				backWarn();
			}else{
				finish();
			}
		}else if(v == rightImage){
			if(checkContentLength())
				wrapAndSend();
		}else if (v == picImage){
			chooseImageType();
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
		}else if( v == picTipImage){
			//提示删除图片
			removePic();
		}
	}
	
	/**根据不同Model调整布局*/
	private void initModel(){
		Intent intent = getIntent();
		model = intent.getIntExtra("model", 0);
		platform = intent.getIntExtra("platform", 3);
		statusId = intent.getStringExtra("ext");
		text = intent.getStringExtra("text");
		cursorPosition = intent.getIntExtra("cursor", 0);
		switch(model){
		case MODEL_NEW_WEIBO:
			titleTv.setText(R.string.weibo_new_new);
			if(platform == Task.PLATFORM_ALL){
				atImage.setVisibility(View.GONE);
				expressionImage.setVisibility(View.GONE);
				hasTencent = hasSina = true;
				tencentCheckBox.setChecked(true);
				sinaCheckBox.setChecked(true);
			}else if(platform == Task.PLATFORM_SINA){
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
		if(text != null){
			inputEditText.setText(text);
		}
		if(cursorPosition == CURSOR_BEGEIN)
			inputEditText.setSelection(0);
	}
	
	/**提醒是否要存入草稿*/
	private void backWarn(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.message_store_draft)
			.setPositiveButton(R.string.btn_store_draft_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					WeiboDraftEntryDAO draft = new WeiboDraftEntryDAO();
					draft.userid = "1";
					draft.type = model;
					draft.platform = platform;
					draft.statusid = statusId;
					draft.content = inputEditText.getText().toString();
					draft.created = System.currentTimeMillis();
					DBModel.getInstance().addDraft(WeiboNewActivity.this, draft);
					WeiboNewActivity.this.finish();
				}
			}).setNegativeButton(R.string.btn_store_draft_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					WeiboNewActivity.this.finish();
				}
			}).show();
	}
	
	/**检查输入文字是否超出满足字数显示
	 * @return true 满足 fasle 不满足*/
	private boolean checkContentLength(){
		float length = caculateLength(inputEditText.getText().toString());
		if(platform == -1){
			Toast.makeText(this,R.string.weibo_new_platform_none_tip, Toast.LENGTH_SHORT).show();
			return false;
		}
		if(length < 140 && length >0)
			return true;
		else if(length == 0){
			Toast.makeText(this,R.string.weibo_new_content_empty_tip, Toast.LENGTH_SHORT).show();
		}else if(length > 140){
			Toast.makeText(this,R.string.weibo_new_content_empty_tip, Toast.LENGTH_SHORT).show();
		} 
		return false;
	}
	
	/**处理并发送微博*/
	private void wrapAndSend(){
		//TODO wrap and send weibo
		switch(model){
		case MODEL_NEW_WEIBO:
			mService.addWeibo(inputEditText.getText().toString(), lastPhotoPath,platform);
			break;
		case MODEL_NEW_COMMENT:
			mService.addComment(inputEditText.getText().toString(), statusId,platform);
			break;
		case MODEL_FORWORD:
			mService.rePost(inputEditText.getText().toString(), statusId,platform);
			break;
		}
	}
	
	/**弹出对话框选择图片或拍照*/
	private void chooseImageType(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(R.array.weibo_new_pic_types, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == 0){
					takePhoto();
				}else if(which == 1){
					choosePic();
				}
			}
		}).show();
	}
	
	/**拍照*/
	private void takePhoto(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File f;
		try {
			f = createImageFile();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startActivityForResult(intent, ON_RESULT_TAKE_PHOTO);
	}
	
	/**选择图片*/
	private void choosePic(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
        intent.setType("image/*");  
        intent.putExtra("crop", "true");  
        File f;
		try {
			f = createImageFile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));  
		} catch (IOException e) {
			e.printStackTrace();
		}
        intent.putExtra("outputFormat", "JPEG");  

        startActivityForResult(intent,ON_RESULT_CHOOSE_PICTURE); 
	}
	
	/**创建文件*/
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String imageFileName = System.currentTimeMillis()+"_";
	    File dir = new File(Environment.getExternalStorageDirectory().toString() + "/icfiles/img/tmp");
	    File image = File.createTempFile(imageFileName,".jpg",dir);
	    Log.v(TAG, "image path:"+image.getAbsolutePath());
	    lastPhotoPath = image.getAbsolutePath();
	    return image;
	}
	
	/**移除已经添加的图片*/
	private void removePic(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.weibo_new_remove_pic_confirm)
			.setPositiveButton(R.string.btn_positive_txt, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(lastPhotoPath != null){
						lastPhotoPath = null;
						picTipImage.setImageBitmap(null);
						picTipImage.setVisibility(View.GONE);
					}
					dialog.dismiss();
				}
			}).setNegativeButton(R.string.btn_negative_txt, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
	}
	
	/**平台更改变化调整布局
	 * <br>目前只在新建微博时的平台监听上用到
	 * */
	private void platformChanaged(){
		if(hasSina && hasTencent){
			platform = Task.PLATFORM_ALL;
		}else if(hasSina){
			platform = Task.PLATFORM_SINA;
		}else if(hasTencent){
			platform = Task.PLATFORM_TENCENT;
		}else {
			platform = -1;
		}
		Log.d(TAG, "platform "+platform);
		switch(platform){
		case Task.PLATFORM_ALL:
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
		case Task.PLATFORM_TENCENT:
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
	@Override
	public void onConnectionFinished() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onConnectionDisConnected() {
		// TODO Auto-generated method stub
		
	}

	
}
