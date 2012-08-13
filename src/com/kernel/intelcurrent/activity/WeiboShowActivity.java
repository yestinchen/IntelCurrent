package com.kernel.intelcurrent.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.kernel.intelcurrent.adapter.CommentListAdapter;
import com.kernel.intelcurrent.model.Comment;
import com.kernel.intelcurrent.model.ICArrayList;
import com.kernel.intelcurrent.model.SimpleUser;
import com.kernel.intelcurrent.model.Status;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.widget.UrlImageView;
import com.kernel.intelcurrent.widget.WeiboTextView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeiboShowActivity extends BaseActivity implements View.OnClickListener,Updateable{

	private static final String TAG = WeiboShowActivity.class.getSimpleName();
	
	private ImageView leftImage;
	private TextView titleTv,platformTv,nameTv,retweetNameTv,commentBtn,forwordBtn,moreBtn,retweetMargin;
	private UrlImageView headIv,picIv,retweetPicIv;
	private WeiboTextView contentIv,retweetContentIv;
	private RelativeLayout retweetLayout;
	private ListView commentsLv;
	
	private Status status;
	private SimpleUser user;
	
	private int hasNext;
	private ArrayList<Comment> comments = new ArrayList<Comment>();
	private CommentListAdapter listAdapter;
	
	@Override
	public void update(int type, Object param) {
		Log.d(TAG, "task"+param);
		if(type != Task.WEIBO_COMMENTS_BY_ID)return;
		if(((Task)param).result.size() == 0) return;
		ICArrayList result =(ICArrayList) ((Task)param).result.get(0);
		comments = new ArrayList<Comment>();
		for(Object comment: result.list){
			comments.add((Comment)comment);
		}
		setAdapter();
		Log.d(TAG, "comments"+comments);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_show);
		Intent intent = getIntent();
		status = (Status) intent.getSerializableExtra("status");
		user = status.user;
		findViews();
		init();
		setListeners();
		setAdapter();
	}
	
	private void findViews(){
		leftImage = (ImageView)findViewById(R.id.common_head_iv_left);
		headIv = (UrlImageView)findViewById(R.id.activity_weibo_show_iv_head);
		nameTv = (TextView)findViewById(R.id.activity_weibo_show_tv_name);
		platformTv = (TextView)findViewById(R.id.activity_weibo_show_tv_platform);
		commentBtn = (TextView)findViewById(R.id.activity_weibo_show_tv_btn_1);
		forwordBtn = (TextView)findViewById(R.id.activity_weibo_show_tv_btn_2);
		moreBtn = (TextView)findViewById(R.id.activity_weibo_show_tv_btn_3);
		contentIv = (WeiboTextView)findViewById(R.id.activity_weibo_show_tv_tweet_txt);
		picIv = (UrlImageView)findViewById(R.id.activity_weibo_show_urlimage_tweet_image);
		retweetLayout = (RelativeLayout)findViewById(R.id.activity_weibo_show_layout_right_retweet_content);
		retweetContentIv = (WeiboTextView)findViewById(R.id.activity_weibo_show_tv_retweet_txt);
		retweetPicIv = (UrlImageView)findViewById(R.id.activity_weibo_show_urlimage_retweet_image);
		retweetNameTv = (TextView)findViewById(R.id.activity_weibo_show_tv_retweet_head);
		
		retweetMargin = (TextView)findViewById(R.id.activity_weibo_show_tv_retweet_margin);
		commentsLv = (ListView)findViewById(R.id.activity_weibo_show_lv_comments);
	}
	
	private void init(){
		try {
			headIv.bindUrl(user.head,UrlImageView.PLAT_FORM_TENCENT, UrlImageView.LARGE_HEAD);
			if(status.image != null && status.image.size() > 0){
				picIv.bindUrl(status.image.get(0), UrlImageView.PLAT_FORM_TENCENT, UrlImageView.SMALL_IMAGE);
				picIv.setVisibility(View.VISIBLE);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		nameTv.setText(user.nick);
		platformTv.setText(user.platform == User.PLATFORM_SINA_CODE ? User.PLATFORM_SINA:User.PLATFORM_TENCENT);
		
		contentIv.setText(status.text,true);
		if(status.reStatus!= null){
			retweetLayout.setVisibility(View.VISIBLE);
			retweetContentIv.setText(status.reStatus.text,true);
			retweetNameTv.setText(status.reStatus.user.nick);
			retweetMargin.setVisibility(View.VISIBLE);
			if(status.reStatus.image != null&& status.reStatus.image.size() > 0){
				try {
					retweetPicIv.bindUrl(status.reStatus.image.get(0), UrlImageView.PLAT_FORM_TENCENT, UrlImageView.SMALL_IMAGE);
					retweetPicIv.setVisibility(View.VISIBLE);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void setListeners(){
		commentBtn.setOnClickListener(this);
		forwordBtn.setOnClickListener(this);
		moreBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == commentBtn){
			Intent intent = new Intent(this,WeiboNewActivity.class);
			intent.putExtra("model", WeiboNewActivity.MODEL_NEW_COMMENT);
			intent.putExtra("platform", Task.PLATFORM_TENCENT);
			intent.putExtra("ext", status.id);
			startActivity(intent);
		}else if(v == forwordBtn){
			Intent intent = new Intent(this,WeiboNewActivity.class);
			intent.putExtra("model", WeiboNewActivity.MODEL_FORWORD);
			intent.putExtra("platform", Task.PLATFORM_TENCENT);
			intent.putExtra("ext", status.id);
			startActivity(intent);
		}else if(v == moreBtn){
			
		}
	}

	private void setAdapter(){
		listAdapter = new CommentListAdapter(this, comments);
		commentsLv.setAdapter(listAdapter);
	}
	
	@Override
	public void onConnectionFinished() {
//		if(user.platform == User.PLATFORM_TENCENT_CODE){
			mService.getCommentList(status.id,0,0,"0",Task.PLATFORM_TENCENT);	
//		}
	}

	@Override
	public void onConnectionDisConnected() {
		
	}



}
