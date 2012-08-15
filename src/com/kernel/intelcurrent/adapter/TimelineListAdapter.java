package com.kernel.intelcurrent.adapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.kernel.intelcurrent.activity.ImageViewerActivity;
import com.kernel.intelcurrent.activity.OtherUserInfoActivity;
import com.kernel.intelcurrent.activity.R;
import com.kernel.intelcurrent.activity.WeiboShowActivity;
import com.kernel.intelcurrent.model.Status;
import com.kernel.intelcurrent.model.User;
import com.kernel.intelcurrent.widget.UrlImageView;
import com.kernel.intelcurrent.widget.WeiboTextView;
import com.tencent.weibo.utils.QStrOperate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TimelineListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private List<Status> statuses;
	private static final String TAG = TimelineListAdapter.class.getSimpleName();
	
	public TimelineListAdapter(Context context,List<Status> statuses){
		mInflater = LayoutInflater.from(context);
		this.statuses = statuses;
		this.context = context;
	}
	@Override
	public int getCount() {
		return statuses.size();
	}

	@Override
	public Object getItem(int position) {
		return statuses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.timeline_list_cell, null);
			holder.platformTv = (TextView) convertView.findViewById(R.id.timeline_list_cell_tv_platform);
			holder.headNameTv = (TextView) convertView.findViewById(R.id.timeline_list_cell_tv_head);
			holder.timeTv = (TextView)convertView.findViewById(R.id.timeline_list_cell_tv_time);
			holder.retweetNameTv = (TextView)convertView.findViewById(R.id.timeline_list_cell_tv_retweet_head);
			holder.contentTv = (WeiboTextView)convertView.findViewById(R.id.timeline_list_cell_tv_tweet_txt);
			holder.retweetContentTv = (WeiboTextView)convertView.findViewById(R.id.timeline_list_cell_tv_retweet_txt);
			holder.headIv= (UrlImageView)convertView.findViewById(R.id.timeline_list_cell_urlimage_head);
			holder.tweetIv = (UrlImageView)convertView.findViewById(R.id.timeline_list_cell_urlimage_tweet_image);
			holder.retweetIv = (UrlImageView)convertView.findViewById(R.id.timeline_list_cell_urlimage_retweet_image);
			holder.retweetLayout = (RelativeLayout)convertView.findViewById(R.id.timeline_list_cell_layout_right_retweet_content);
			holder.cCountTv = (TextView)convertView.findViewById(R.id.timeline_list_cell_tv_ccount);
			holder.rCountTv = (TextView)convertView.findViewById(R.id.timeline_list_cell_tv_rcount);
			holder.retweetLineView = (TextView)convertView.findViewById(R.id.timeline_list_cell_tv_retweet_margin);
			holder.sourceTv = (TextView)convertView.findViewById(R.id.timeline_list_cell_tv_source);
			holder.containerLayout = (RelativeLayout)convertView.findViewById(R.id.timeline_list_cell_layout_container);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			//把之前的清理掉
			holder.retweetLayout.setVisibility(View.GONE);
			holder.tweetIv.setVisibility(View.GONE);
			holder.retweetLineView.setVisibility(View.GONE);
			holder.retweetIv.setVisibility(View.GONE);
			holder.tweetIv.reset();
			holder.retweetIv.reset();
			holder.headIv.reset();
		}
		final Status status = statuses.get(position);
		Log.v(TAG, status.toString());
		holder.platformTv.setText(status.platform == User.PLATFORM_TENCENT_CODE ? User.PLATFORM_TENCENT: User.PLATFORM_SINA);
		holder.headNameTv.setText(status.user.nick);
		holder.contentTv.setText(status.text);
		holder.timeTv.setText(QStrOperate.getTimeState(status.timestamp+""));
		holder.cCountTv.setText(String.valueOf(status.cCount));
		holder.rCountTv.setText(String.valueOf(status.rCount));
		holder.sourceTv.setText("来自:"+status.source);
		
		CellListener l = new CellListener(status);
		try {
			holder.headIv.bindUrl(status.user.head,UrlImageView.PLAT_FORM_TENCENT,UrlImageView.LARGE_HEAD);
			if(status.image.size() != 0){
				holder.tweetIv.setVisibility(View.VISIBLE);
				String url = status.image.get(0);
				holder.tweetIv.bindUrl(url,UrlImageView.PLAT_FORM_TENCENT,UrlImageView.SMALL_IMAGE);	
				holder.tweetIv.setTag( url);
				holder.tweetIv.setOnClickListener(l);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Status reStatus = status.reStatus;
		if(reStatus != null){
			holder.retweetLineView.setVisibility(View.VISIBLE);
			holder.retweetLayout.setVisibility(View.VISIBLE);
			holder.retweetNameTv.setText(reStatus.user.nick);
			holder.retweetContentTv.setText(reStatus.text);
			if(reStatus.image.size() != 0){
				holder.retweetIv.setVisibility(View.VISIBLE);
				try {
					String url = reStatus.image.get(0);
					holder.retweetIv.bindUrl(url,UrlImageView.PLAT_FORM_TENCENT,UrlImageView.SMALL_IMAGE);
					holder.retweetIv.setTag(url);
					holder.retweetIv.setOnClickListener(l);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//所有元素加监听
		holder.headIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,OtherUserInfoActivity.class);
				intent.putExtra("user_openid", status.user.id);
				intent.putExtra("user_nick", status.user.nick);
				context.startActivity(intent);
			}
		});
		holder.containerLayout.setOnClickListener(l);
		return convertView;
	}

	class ViewHolder{
		TextView platformTv,headNameTv,retweetNameTv,timeTv,cCountTv,rCountTv,retweetLineView,sourceTv;
		WeiboTextView contentTv,retweetContentTv;
		UrlImageView headIv,tweetIv,retweetIv;
		RelativeLayout retweetLayout,containerLayout;
	}
	
	class CellListener implements OnClickListener{
		Status status;
		public CellListener(Status status){
			this.status = status;
		}
		@Override
		public void onClick(View v) {
			if(v instanceof UrlImageView){
				//若是图片，添加监听后可以点击看大图
				Intent intent = new Intent(context,ImageViewerActivity.class);
				intent.putExtra("url",v.getTag().toString());
				context.startActivity(intent);
			}else {
				Intent intent = new Intent(context,WeiboShowActivity.class);
				intent.putExtra("status", status);
				context.startActivity(intent);
			}
		}
		
	}
}
