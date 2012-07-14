package com.kernel.intelcurrent.modeladapter;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.api.UserAPI;
import com.tencent.weibo.beans.OAuth;

/**腾讯的adapter
 * @author sheling*/
public class TencentAdapter extends ModelAdapter {

	private static final String TAG = TencentAdapter.class.getSimpleName();
	
	public TencentAdapter(Task t) {
		super(t);
	}

	@Override
	public void run() {
		switch(task.type){
		}
		model.callBack(task);
	}

	/**添加一条微博
	 * @author sheling*/
	public String addWeibo(){
		TAPI tapi = new TAPI();
		Map<String, Object> map = task.param;
		String response = null;
		try {
			response = tapi.add((OAuth)map.get("oauth"), "json",
					map.get("content").toString(),map.get("clientip").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		tapi.shutdownConnection();
		task.result.add(response);
		Log.d(TAG, response);
		return response;
	}
	
	/**添加一条评论
	 * @author sheling*/
	public String addComment(){
		TAPI tapi = new TAPI();
		Map<String,Object> map = task.param;
		String response = null;
		try {
			response = tapi.comment((OAuth)map.get("oauth"), "json",
					map.get("content").toString(), map.get("clientip").toString(), 
					map.get("reid").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		tapi.shutdownConnection();
		task.result.add(response);
		return response;
	}
	
	/**转发一条微博
	 * @author sheling*/
	public String repostWeibo(){
		TAPI tapi = new TAPI();
		Map<String,Object> map = task.param;
		String response = null;
		try {
			response = tapi.reAdd((OAuth)map.get("oauth"), "json",
					map.get("content").toString(), map.get("clientip").toString(),
					map.get("reid").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		tapi.shutdownConnection();
		task.result.add(response);
		return response;
	}
	
	
	
}
