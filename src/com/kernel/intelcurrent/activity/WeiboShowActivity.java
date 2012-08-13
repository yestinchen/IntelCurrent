package com.kernel.intelcurrent.activity;

import java.io.IOException;
import java.net.MalformedURLException;

import com.kernel.intelcurrent.model.SimpleUser;
import com.kernel.intelcurrent.model.Status;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.widget.UrlImageView;
import com.kernel.intelcurrent.widget.WeiboTextView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeiboShowActivity extends Activity {

	
	private ImageView leftImage;
	private TextView titleTv,platformTv,nameTv,retweetNameTv,commentBtn,forwordBtn,moreBtn,retweetMargin;
	private UrlImageView headIv,picIv,retweetPicIv;
	private WeiboTextView contentIv,retweetContentIv;
	private RelativeLayout retweetLayout;
	
	private Status status;
	private SimpleUser user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_show);
		Intent intent = getIntent();
		status = (Status) intent.getSerializableExtra("status");
		user = status.user;
		findViews();
		init();
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

}
