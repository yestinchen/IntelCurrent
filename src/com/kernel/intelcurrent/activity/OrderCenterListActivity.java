package com.kernel.intelcurrent.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import com.kernel.intelcurrent.adapter.OrderListAdapter;
import com.kernel.intelcurrent.model.DBModel;
import com.kernel.intelcurrent.model.Group;
import com.kernel.intelcurrent.model.SimpleUser;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class OrderCenterListActivity extends Activity implements OnItemClickListener,View.OnClickListener{

	private static final String TAG = OrderCenterListActivity.class.getSimpleName();
	
	private ArrayList<GroupEntry> groups;
	private TextView titleTv;
	private ImageView leftImage;
	private ListView lv;
	private OrderListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_center);
		findViews();
		init();
		
	}
	
	private void init(){
		groups = parseXml();
		ArrayList<Group> nowGroups = DBModel.getInstance().getAllGroups(this);
		ArrayList<String> groupNames = new ArrayList<String>();
		for(Group g:nowGroups){
			groupNames.add(g.name);
		}
		for(GroupEntry group:groups){
			if(groupNames.contains(group.group.name)){
				group.exists = true;
			}
		}
		Log.d(TAG, "groups:"+groups);
		
		adapter = new OrderListAdapter(this, groups);
		lv.setAdapter(adapter);
	}
	
	private void findViews(){
		titleTv = (TextView)findViewById(R.id.common_head_tv_title);
		leftImage = (ImageView)findViewById(R.id.common_head_iv_left);
		lv = (ListView)findViewById(R.id.activity_order_center_lv);
		
		titleTv.setText(R.string.more_group_order_center);
		leftImage.setImageResource(R.drawable.ic_title_back);
		lv.setSelector(R.drawable.list_cell_bg);
		lv.setOnItemClickListener(this);
		leftImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == leftImage){
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		GroupEntry entry = groups.get(position); 
		if(! entry.exists){
			Toast.makeText(this,"["+entry.group.name+"]"+getResources().getString(R.string.activity_order_list_add_tip), Toast.LENGTH_LONG).show();
			entry.exists = true;
			DBModel.getInstance().addGroup(OrderCenterListActivity.this,entry.group);
			adapter.notifyDataSetChanged();
		}
	}

	private ArrayList<GroupEntry> parseXml(){
		ArrayList<GroupEntry> list = null;
		GroupEntry entry = null;
		SimpleUser user = null;
		try {
			InputStream ips = getResources().getAssets().open("sample.xml");
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(ips,"utf-8");
			int eventType = parser.getEventType();  
			while(eventType != XmlPullParser.END_DOCUMENT){
				String nodeName = parser.getName();
				switch(eventType){
				case XmlPullParser.START_TAG:
					if(nodeName.equals("iclist")){
						list = new ArrayList<OrderCenterListActivity.GroupEntry>();
					}else if(nodeName.equals("group")){
						entry = new GroupEntry();
						entry.group.name = parser.getAttributeValue(0);
						list.add(entry);
					}else if(nodeName.equals("user")){
						user = new SimpleUser();
						user.name = parser.getAttributeValue(0);
						user.id = parser.getAttributeValue(1);
						user.platform =  Integer.valueOf(parser.getAttributeValue(2));
						entry.group.users.add(user);
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				eventType = parser.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "list:"+list);
		return list;
	}
	
	public static class GroupEntry{
		public Group group = new Group();
		public boolean exists;
		@Override
		public String toString() {
			return "GroupEntry [group=" + group + ", exists=" + exists + "]";
		}
		
	}


}
