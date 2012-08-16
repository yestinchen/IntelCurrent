package com.kernel.intelcurrent.adapter;

import java.util.List;

import com.kernel.intelcurrent.activity.R;
import com.kernel.intelcurrent.activity.WeiboNewActivity;
import com.kernel.intelcurrent.model.DBModel;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.WeiboDraftEntryDAO;
import com.tencent.weibo.utils.QStrOperate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**用于显示草稿的列表适配
 * @author sheling*/
public class DraftListAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private List<WeiboDraftEntryDAO> list;
	private Context context;
	
	public DraftListAdapter(Context context,List<WeiboDraftEntryDAO> list){
		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(view == null){
			view = mInflater.inflate(R.layout.cell_weibo_drafts, null);
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		if(holder == null){
			holder = new ViewHolder();
			holder.typeTv = (TextView) view.findViewById(R.id.cell_weibo_drafts_type);
			holder.timeTv = (TextView) view.findViewById(R.id.cell_weibo_drafts_time);
			holder.contentTv = (TextView) view.findViewById(R.id.cell_weibo_drafts_content);
			holder.layout = (LinearLayout)view.findViewById(R.id.cell_weibo_drafts_layout);
			holder.sinaImage = (ImageView)view.findViewById(R.id.cell_weibo_drafts_platform_sina);
			holder.tencentImage = (ImageView)view.findViewById(R.id.cell_weibo_drafts_platform_tencent);
		}else{
			holder.sinaImage.setVisibility(View.GONE);
			holder.tencentImage.setVisibility(View.GONE);
		}
		final WeiboDraftEntryDAO draft = list.get(position);
		if(draft.type == WeiboNewActivity.MODEL_NEW_WEIBO){
			holder.typeTv.setText(R.string.weibo_type_new);
		}else if(draft.type == WeiboNewActivity.MODEL_NEW_COMMENT){
			holder.typeTv.setText(R.string.weibo_type_comment);
		}else if(draft.type == WeiboNewActivity.MODEL_FORWORD){
			holder.typeTv.setText(R.string.weibo_type_repost);
		}
		holder.timeTv.setText(QStrOperate.getTimeState(draft.created+""));
		holder.contentTv.setText(draft.content);
		if(draft.platform == Task.PLATFORM_SINA || draft.platform == Task.PLATFORM_ALL){
			holder.sinaImage.setVisibility(View.VISIBLE);
		}
		if(draft.platform == Task.PLATFORM_TENCENT || draft.platform == Task.PLATFORM_ALL){
			holder.tencentImage.setVisibility(View.VISIBLE);
		}
		holder.layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,WeiboNewActivity.class);
				intent.putExtra("model", draft.type);
				intent.putExtra("text", draft.content);
				intent.putExtra("img", draft.img);
				intent.putExtra("ext", draft.statusid);
				intent.putExtra("platform", draft.platform);
				context.startActivity(intent);
				DBModel.getInstance().removeDraft(context, draft.created);
			}
		});
		holder.layout.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(R.string.weibo_show_comment_menu_title)
					.setItems(R.array.weibo_draft_list_menu, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which){
						case 0:
							list.remove(draft);
							DraftListAdapter.this.notifyDataSetChanged();
							DBModel.getInstance().removeDraft(context, draft.created);
							break;
						}
					}
				}).show();
				return false;
			}
		});
		return view;
	}

	class ViewHolder{
		public TextView typeTv,timeTv,contentTv;
		public ImageView sinaImage,tencentImage;
		public LinearLayout layout;
	}
}
