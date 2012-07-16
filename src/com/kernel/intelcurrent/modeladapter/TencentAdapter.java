package com.kernel.intelcurrent.modeladapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.raw;
import android.util.Log;

import com.kernel.intelcurrent.model.Comment;
import com.kernel.intelcurrent.model.ICArrayList;
import com.kernel.intelcurrent.model.Status;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.tencent.weibo.api.FriendsAPI;
import com.tencent.weibo.api.StatusesAPI;
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
	/**根据微博ID获取评论列表
	 * 处理结果为一个ICArrayList对象，hasnext为是否还有可拉取的评论，true为有，list存获取的评论列表。
	 * @author allenjin
	 */
	public void getreList(){
		TAPI tapi=new TAPI();
		Map<String, Object> map=task.param;
		String response=null;
		try{
			response=tapi.reList((OAuth)map.get("oauth"),"josn",map.get("flag").toString(),map.get("rootid").toString(),
					map.get("pageflag").toString(), map.get("pagetime").toString(),
					map.get("reqnum").toString(), map.get("twitterid").toString());
			ICArrayList ica=new ICArrayList();
			JSONObject jsonobject=new JSONObject(response.substring(5, response.length()-1));
			JSONObject data=jsonobject.getJSONObject("data");
			ica.hasNext = data.getInt("hasnext");
			JSONArray info=data.getJSONArray("info");
			for(int i=0;i<info.length();i++){
				Comment comment=new Comment();
				JSONObject infoobject=info.getJSONObject(i);
				comment.id=infoobject.getString("id");
				comment.nick=infoobject.getString("nick");
				comment.text=infoobject.getString("origtext");
				comment.timestamp=Integer.parseInt(infoobject.getString("timestamp"));
				ica.list.add(comment);
			}	
			task.result.add(ica);
		}catch (Exception e) {
			e.printStackTrace();
		}
		tapi.shutdownConnection();
	}
	
	/**得到用户发表的微博列表
	 * 处理结果为ArrayList
	 * @author sheling*/
	public void getUserWeiboList(){
		StatusesAPI sapi = new StatusesAPI();
		Map<String, Object> map = task.param;
		String response = null;
		JSONObject jsonObj = null;
		JSONArray jsonArr = null,arrTmp = null;
		ICArrayList arraylist = new ICArrayList();
		try {
			response = sapi.broadcastTimeline((OAuth)map.get("oauth"), "json", map.get("pageflag").toString()
					, map.get("pagetime").toString(), map.get("reqnum").toString(), map.get("lastid").toString()
					, "0", "1");
			jsonObj = new JSONObject(response).getJSONObject("data");
			arraylist.hasNext = jsonObj.getInt("hasnext");
			jsonArr = jsonObj.getJSONArray("info");
			int length = jsonArr.length();
			for(int i=0;i<length;i++){
				jsonObj = jsonArr.getJSONObject(i);
				Status status = new Status();
				//TODO 封装为微博对象
				status.id = jsonObj.getString("id");
				status.text = jsonObj.getString("text");
				status.source = jsonObj.getString("from");
				if(jsonObj.get("image") instanceof JSONArray){
					arrTmp = jsonObj.getJSONArray("image");
					for(int j=0;j<arrTmp.length();j++){
						status.image.add(arrTmp.getString(j));	
					}
				}
				status.timestamp = jsonObj.getLong("timestamp");
				status.rCount = jsonObj.getInt("count");
				status.cCount = jsonObj.getInt("mcount");
				status.user.id = jsonObj.getString("openid");
				status.user.nick = jsonObj.getString("nick");
				String province = jsonObj.getString("province_code");
				if(province!=null && !province.equals("")) status.user.province = Integer.valueOf(province);
				String city = jsonObj.getString("city_code");
				if(city !=null && !city.equals("")) status.user.city = Integer.valueOf(city);
				status.user.location = jsonObj.getString("location");
				status.user.head = jsonObj.getString("head");
				status.platform = TENCENT_PLAT_FORM;
				arraylist.list.add(status);
			}
			task.result.add(arraylist);
			sapi.shutdownConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.v(TAG, "getUserWeibos "+response);		
		Log.v(TAG, "getUserWeibos Obj"+arraylist);		
	}
}
