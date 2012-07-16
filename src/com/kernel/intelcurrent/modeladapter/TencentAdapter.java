package com.kernel.intelcurrent.modeladapter;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.raw;
import android.util.Log;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.tencent.weibo.api.FriendsAPI;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.api.UserAPI;
import com.tencent.weibo.beans.OAuth;

/**腾讯的adapter
 * @author sheling*/
public class TencentAdapter extends ModelAdapter {

	private static final String TAG = TencentAdapter.class.getSimpleName();
	public static final String TENCENT_PLAT_FORM = "腾讯微博";
	
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
	
	/**得到用户的信息
	 * 自行判断是本用户还是其他用户，task参数列表中需要有openid。0为自己，指定为他人
	 * 处理结果为一个User对象
	 *@author sheling*/
	public void getUserInfo(){
		UserAPI uapi = new UserAPI();
		Map<String,Object> map = task.param;
		String response = null,openid =null;
		JSONObject rawObj,infoObj = null;
		User me = null;
		try {
			openid = map.get("openid").toString();
			if(openid.equals("0")){
				response = uapi.info((OAuth)map.get("oauth"), "json");	
			}else{
				response = uapi.otherInfo((OAuth)map.get("oauth"), "json",null,openid);
			}
			rawObj = new JSONObject(response);
			infoObj = rawObj.getJSONObject("data");
			me = new User();
			me.id = infoObj.getString("openid");
			me.nick = infoObj.getString("nick");
			String province = infoObj.getString("province_code");
			if(province!=null && !province.equals("")) me.province = Integer.valueOf(province);
			String city = infoObj.getString("city_code");
			if(city !=null && !city.equals("")) me.city = Integer.valueOf(city);
			me.location = infoObj.getString("location");
//			me.description = infoObj.getString("");
			me.homepage = infoObj.getString("homepage");
			me.head = infoObj.getString("head");
			me.gender = infoObj.getInt("sex");
			me.fansnum = infoObj.getInt("fansnum");
			me.idolnum = infoObj.getInt("idolnum");
			me.favnum = infoObj.getInt("favnum");
			me.statusnum = infoObj.getInt("tweetnum");
			me.regTime = infoObj.getString("regtime");
			if(!openid.equals("0")){
				if(infoObj.getInt("ismyfans") ==1) me.ismyfan = true;
				if(infoObj.getInt("ismyidol") == 1); me.ismyidol = true;
			}
			me.platform = TENCENT_PLAT_FORM;
			uapi.shutdownConnection();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		task.result.add(me);
		Log.v(TAG, "get User Info:"+response);
		Log.v(TAG, "get User object:"+me.toString());
	}
	
	/**
	 * 得到用户列表
	 * 处理结果为一个由User对象构成的ArrayList
	 * @param type 1为粉丝 2为关注
	 * @author sheling*/
	public void getUserList(int type){
		FriendsAPI fapi = new FriendsAPI();
		Map<String, Object> map = task.param;
		String response = null;
		JSONObject rawObj = null ,obj = null;
		JSONArray rawArrays = null;
		ArrayList<User> users = new ArrayList<User>();
		try {
			switch(type){
			case 1:
				response = fapi.fanslist((OAuth)map.get("oauth"),"json",map.get("reqnum").toString(), 
						map.get("startindex").toString(),"0", "0");
				break;
			case 2:
				response = fapi.idollist((OAuth)map.get("oauth"), "json", map.get("reqnum").toString(), 
						map.get("startindex").toString(), "0");
				break;
			}
			rawObj = new JSONObject(response);
			rawArrays = rawObj.getJSONObject("data").getJSONArray("info");
			int length = rawArrays.length();
			for(int i=0;i<length;i++){
				obj = rawArrays.getJSONObject(i);
				User user = new User();
				user.id = obj.getString("openid");
				user.nick = obj.getString("nick");
				String province = obj.getString("province_code");
				if(province!=null && !province.equals("")) user.province = Integer.valueOf(province);
				String city = obj.getString("city_code");
				if(city !=null && !city.equals("")) user.city = Integer.valueOf(city);
				user.location = obj.getString("location");
//				user.description = obj.getString("");
//				user.homepage = obj.getString("homepage");
				user.head = obj.getString("head");
				user.gender = obj.getInt("sex");
				user.fansnum = obj.getInt("fansnum");
				user.idolnum = obj.getInt("idolnum");
//				user.favnum = obj.getInt("favnum");
//				user.statusnum = obj.getInt("tweetnum");
//				user.regTime = obj.getString("regtiuser");
				user.ismyfan = obj.getBoolean("isfans");
				user.ismyidol = obj.getBoolean("isidol");
				user.platform = TENCENT_PLAT_FORM;
				users.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		task.result.add(users);
		fapi.shutdownConnection();
		Log.v(TAG, "getUserList:"+type+" "+response);
		Log.v(TAG, "get object:"+users.toString());
	}
	
	
}
