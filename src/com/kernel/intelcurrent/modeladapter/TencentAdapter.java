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
import com.kernel.intelcurrent.model.SimpleUser;
import com.kernel.intelcurrent.model.Status;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.tencent.weibo.api.FavAPI;
import com.tencent.weibo.api.FriendsAPI;
import com.tencent.weibo.api.SearchAPI;
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
	
	/**
	 * 添加一条带图片的微博
	 * @author allenjin
	 */
	public void addpicWeibo(){
		TAPI tapi=new TAPI();
		Map<String,Object> map=task.param;
		String response=null;
		try{
			response=tapi.addPic((OAuth)map.get("oauth"), "json",map.get("content").toString(),
									map.get("clientip").toString(),map.get("imgurl").toString());
			Log.v(TAG,response);
		}catch (Exception e) {
			e.printStackTrace();
		}
		task.result.add(response);
		tapi.shutdownConnection();
	}
	
	/**
	 * 获取一条微博的详细信息,通过微博ID
	 * @author allenjin
	 */
	public void showWeibo(){
		TAPI tapi=new TAPI();
		Map<String,Object> map=task.param;
		String response= null;
		try{
			response=tapi.show((OAuth)map.get("oauth"),"json", (String) map.get("id"));
			JSONObject data=new JSONObject(response).getJSONObject("data");
			Status s=new Status();
			s.user.id=data.getString("openid");
			s.user.city=data.getInt("city_code");
			s.user.head=data.getString("head");
			s.user.name=data.getString("name");
			s.user.nick=data.getString("nick");
			s.user.location=data.getString("location");
			s.id=data.getString("id");
			s.cCount=data.getInt("count");
			s.rCount=data.getInt("mcount");
			s.source=data.getString("from");
			if(data.get("image")instanceof JSONArray){
				JSONArray image=data.getJSONArray("data");
				for(int i=0;i<image.length();i++){
					s.image.add(image.getString(i));
				}
			}
			if(data.get("source") instanceof JSONObject){
				JSONObject res=data.getJSONObject("source");
				Status reStatus=new Status();
				reStatus.cCount=res.getInt("count");
				reStatus.rCount=res.getInt("mcount");
				reStatus.geo=null;
				reStatus.id=res.getString("id");
				reStatus.source=res.getString("from");
				reStatus.platform=TENCENT_PLAT_FORM;
				reStatus.timestamp=res.getLong("timestamp");
				reStatus.text=res.getString("origtext");
				reStatus.user.city=res.getInt("city_code");
				reStatus.user.head=res.getString("head");
				reStatus.user.id=res.getString("openid");
				reStatus.user.name=res.getString("name");
				reStatus.user.nick=res.getString("nick");
				reStatus.user.location=res.getString("location");
				if(res.get("image")instanceof JSONArray){
					JSONArray image=res.getJSONArray("data");
					for(int i=0;i<image.length();i++){
						s.image.add(image.getString(i));
					}
				}
				s.reStatus=reStatus;
			}		
			s.platform=TENCENT_PLAT_FORM;
			s.text=data.getString("origtext");
			s.timestamp=data.getLong("timestamp");
			Log.v(TAG, s.toString());
			task.result.add(s);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		tapi.shutdownConnection();
		
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
	/**
	 * 回复一条微博
	 * @author allenjin
	 */
	public void replyWeibo(){
		TAPI tapi=new TAPI();
		Map<String,Object> map=task.param;
		String response=null;
		try{
			response=tapi.reply((OAuth)map.get("oauth"), "json", map.get("content").toString(),
					map.get("clientip").toString(), map.get("reid").toString());
					Log.v(TAG,response);
		}catch (Exception e) {
			e.printStackTrace();
		}
		tapi.shutdownConnection();
		task.result.add(response);
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
	 * 处理结果为一个ICArrayList对象，hasnext为是否还有可拉取的评论，0为有，1为无，list存获取的评论列表。
	 * @author allenjin
	 */
	public void getCommentList(){
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
	
	/**
	 * 搜索用户列表，根据关键字
	 * 处理结果为ICArrayList
	 * 其中hastnext表示为：0，第一页且只有一页,1.多页第一页,还可向下翻页，2-还可向上翻页，3-可向上或向下翻页
	 * 其中list存放SimpleUser类的对象列表.
	 * @author allenjin
	 */
	public void searchUser(){
		SearchAPI sapi=new SearchAPI();
		Map<String, Object> map = task.param;
		String response = null;
		try{
			response=sapi.user((OAuth)map.get("oauth"),map.get("keyword").toString(),
					map.get("pagesize").toString(), map.get("page").toString());
			JSONObject data=new JSONObject(response).getJSONObject("data");
			ICArrayList list=new ICArrayList();
			list.hasNext=data.getInt("hasnext");
			if(data.get("info")instanceof JSONArray){
				JSONArray info=data.getJSONArray("info");
				for(int j=0;j<info.length();j++){
					SimpleUser users=new SimpleUser();
					JSONObject iobj=info.getJSONObject(j);
					users.head=iobj.getString("head");
					users.id=iobj.getString("openid");
					users.name=iobj.getString("name");
					users.nick=iobj.getString("nick");
					Log.v("第"+j+"个：用户", users.toString());
					list.list.add(users);
				}
			}
			Log.v(TAG,response);
		}catch(Exception e){
			e.printStackTrace();
		}
		sapi.shutdownConnection();
		task.result.add(response);
	}
	
	/**
	 * 收藏一个微博
	 * @author allenjin
	 */
	public void addFavWeibo(){
		FavAPI fav=new FavAPI();
		Map<String,Object> map=task.param;
		String response=null;
		try{
			response=fav.addFav((OAuth)map.get("oauth"), map.get("id").toString());
			Log.v(TAG, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		task.result.add(response);
		fav.shutdownConnection();
	}
	
	/**
	 * 取消一个收藏微博
	 * @author allenjin
	 */
	public void delFavWeibo(){
		FavAPI fav=new FavAPI();
		Map<String,Object> map=task.param;
		String response=null;
		try{
			response=fav.delFav((OAuth)map.get("oauth"), map.get("id").toString());
			Log.v(TAG, response);
			task.result.add(response);
		}catch (Exception e) {
			e.printStackTrace();
		}
		fav.shutdownConnection();
	}
	
	/**
	 * 获取收藏微博的列表
	 * 处理结果为ICArrayList 
	 * 其中hasnext :0-表示还有微博可拉取，1-已拉取完毕
	 * 其中list存放收藏的微博的列表
	 * @author allenjin
	 */
	public void getFavWeiboList(){
		FavAPI fav=new FavAPI();
		Map<String,Object> map=task.param;
		String response=null;
		try{
			response=fav.listFav((OAuth)map.get("oauth"), map.get("pageflag").toString(), 
					map.get("pagetime").toString(),map.get("reqnum").toString(), map.get("lastid").toString());
			Log.v(TAG, response);
			ICArrayList iclist=new ICArrayList();
			JSONObject data=new JSONObject(response).getJSONObject("data");
			iclist.hasNext=data.getInt("hasnext");
			if(data.get("info")instanceof JSONArray){
				JSONArray info=data.getJSONArray("info");
				for(int i=0;i<info.length();i++){
					JSONObject iobj=info.getJSONObject(i);
					Status s=new Status();
					s.id=iobj.getString("id");
					s.cCount=iobj.getInt("count");
					s.rCount=iobj.getInt("mcount");
					s.geo=null;
					s.platform=TENCENT_PLAT_FORM;
					s.source=iobj.getString("from");
					s.text=iobj.getString("origtext");
					s.timestamp=iobj.getLong("timestamp");
					s.user.city=iobj.getInt("city_code");
					s.user.head=iobj.getString("head");
					s.user.id=iobj.getString("openid");
					s.user.name=iobj.getString("name");
					s.user.nick=iobj.getString("nick");
					s.user.location=iobj.getString("location");
					if(iobj.get("image")instanceof JSONArray){
						JSONArray imgs=iobj.getJSONArray("image");
						for(int j=0;j<imgs.length();j++){			
							s.image.add(imgs.getString(j));
						}
					}

					if(iobj.get("source") instanceof JSONObject){
						JSONObject res=iobj.getJSONObject("source");
						Status reStatus=new Status();
						reStatus.cCount=res.getInt("count");
						reStatus.rCount=res.getInt("mcount");
						reStatus.geo=null;
						reStatus.id=res.getString("id");
						reStatus.source=res.getString("from");
						reStatus.platform=TENCENT_PLAT_FORM;
						reStatus.timestamp=res.getLong("timestamp");
						reStatus.text=res.getString("origtext");
						reStatus.user.city=res.getInt("city_code");
						reStatus.user.head=res.getString("head");
						reStatus.user.id=res.getString("openid");
						reStatus.user.name=res.getString("name");
						reStatus.user.nick=res.getString("nick");
						reStatus.user.location=res.getString("location");
						if(res.get("image")instanceof JSONArray){
							JSONArray image=res.getJSONArray("data");
							for(int j=0;j<image.length();j++){
								s.image.add(image.getString(j));
							}
						}
						s.reStatus=reStatus;
					}
					iclist.list.add(s);
					Log.v("第"+i+"个收藏微博：",s.toString());
				}
				task.result.add(iclist);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		fav.shutdownConnection();
	}
}
