package com.kernel.intelcurrent.adapter;

import java.util.List;
import com.kernel.intelcurrent.activity.R;
import com.kernel.intelcurrent.model.Comment;
import com.kernel.intelcurrent.widget.WeiboTextView;
import com.tencent.weibo.utils.QStrOperate;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentListAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private List<Comment> comments;
	
	public CommentListAdapter(Context context,List<Comment> list){
		mInflater = LayoutInflater.from(context);
		comments = list;
	}
	
	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.cell_comment_list, null);
		}
		holder = (ViewHolder) convertView.getTag();
		if(holder == null){
			holder = new ViewHolder();
			holder.nameTv = (TextView)convertView.findViewById(R.id.cell_comment_list_tv_name);
			holder.timeTv = (TextView)convertView.findViewById(R.id.cell_comment_list_tv_time);
			holder.content = (WeiboTextView)convertView.findViewById(R.id.cell_comment_list_tv_content);
			convertView.setTag(holder);
		}
		Comment comment = comments.get(position);
		holder.nameTv.setText(comment.nick);
		holder.timeTv.setText(QStrOperate.getTimeState(comment.timestamp+""));
		holder.content.setText(comment.text,true);
		return convertView;
	}

	class ViewHolder{
		TextView nameTv,timeTv;
		WeiboTextView content;
	}
}
