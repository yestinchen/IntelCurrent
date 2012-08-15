package com.kernel.intelcurrent.activity;

import java.util.ArrayList;
import com.kernel.intelcurrent.adapter.GroupBlockPagerAdapter;
import com.kernel.intelcurrent.model.DBModel;
import com.kernel.intelcurrent.model.Group;
import com.kernel.intelcurrent.model.SimpleUser;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GroupBlockActivity extends Activity implements OnClickListener,Updateable{

	private static final String TAG = GroupBlockActivity.class.getSimpleName();
	private ViewPager viewPager;
	private ImageView leftImage,rightImage;
	
	@Override
	public void update(int type, Object param) {
		//do nothing
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_group_block);
		findViews();
		setListeners();
		setAdapter();
	}

	private void findViews(){
		viewPager = (ViewPager)findViewById(R.id.layout_group_block_viewpager);
		leftImage = (ImageView)findViewById(R.id.common_head_iv_left);
		rightImage = (ImageView)findViewById(R.id.common_head_iv_right);
		
		leftImage.setImageResource(R.drawable.ic_title_new);
		rightImage.setImageResource(R.drawable.ic_title_new_group);
//		Log.d(TAG, viewPager.toString());
	}
	
	private void setListeners(){
		leftImage.setOnClickListener(this);
		rightImage.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v == leftImage){
			Intent intent = new Intent(this,WeiboNewActivity.class);
			startActivity(intent);
		}else if(v == rightImage){
			newGroup();
		}
	}
	/**
	 * 新建分组对话框
	 */
	private void newGroup(){
		
		final EditText input=new EditText(this);
		input.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		input.setHint("输入组名");
		input.setSingleLine();
		input.setGravity(Gravity.CENTER_VERTICAL);
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("新建组").setCancelable(false).setView(input).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				if(input.getText()==null||input.getText().toString().equals("")){
					Toast.makeText(GroupBlockActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
				}else{
						String gname=input.getText().toString();
					if(DBModel.getInstance().GoupIsExists(GroupBlockActivity.this, gname.hashCode())){
						Toast.makeText(GroupBlockActivity.this, "对不起,组名已存在！", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(GroupBlockActivity.this, "添加组"+gname, Toast.LENGTH_SHORT).show();
						Group group=new Group(gname,null,null);
						DBModel.getInstance().addGroup(GroupBlockActivity.this, group);
						setAdapter();
					}
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				dialog.dismiss();
			}
		}).show();
	
	}
	
	/**
	 * 删除组的对话框
	 * @param group
	 */
	private void delGroup(final Group group){
		AlertDialog.Builder builder=new AlertDialog.Builder(GroupBlockActivity.this);
		builder.setMessage("确定删除该分组吗?").setCancelable(false).setPositiveButton("确定",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
					DBModel.getInstance().delGroup(GroupBlockActivity.this, group);
					setAdapter();
					Toast.makeText(GroupBlockActivity.this, "删除分组"+group.name, Toast.LENGTH_SHORT).show();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				dialog.dismiss();
			}
		}).show();
	}
	private void setAdapter(){
		ArrayList<Group> groupList = DBModel.getInstance().getAllGroups(this);
		Log.d(TAG, "get groups:"+groupList);
		GroupBlockPagerAdapter adapter = new GroupBlockPagerAdapter(groupList, this,
				new OnClickListener() {
				@Override
				public void onClick(View v) {
					Group group = (Group)v.getTag();
					ArrayList<SimpleUser> users=DBModel.getInstance().getUsersByGname(GroupBlockActivity.this,group.name);
					Log.d(TAG, group.toString());
					group.users.addAll(users);
					startTimelineActivity(group);
				}
			},new OnLongClickListener() {
				
						
						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							delGroup((Group)v.getTag());
							return false;			
				}
						
			});
		viewPager.setAdapter(adapter);
	}
	

	private void startTimelineActivity(Group group){
		LinearLayout container=(LinearLayout)((ActivityGroup)getParent()).getWindow().findViewById(R.id.layout_main_layout_container);	
		container.removeAllViews();
        Intent intent=new Intent(this,TimelineActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable("group", group);
        intent.putExtra("ext", bundle);
        Window subActivity=((ActivityGroup)this.getParent()).getLocalActivityManager().startActivity(TimelineActivity.class.getSimpleName(),intent);
        container.addView(subActivity.getDecorView());
	}

}
