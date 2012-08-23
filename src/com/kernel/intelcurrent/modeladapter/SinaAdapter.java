package com.kernel.intelcurrent.modeladapter;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

import javax.net.ssl.SSLException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.kernel.intelcurrent.model.Comment;
import com.kernel.intelcurrent.model.ErrorEntry;
import com.kernel.intelcurrent.model.ICArrayList;
import com.kernel.intelcurrent.model.Status;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.tencent.weibo.api.FriendsAPI;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.beans.OAuth;
import com.weibo.net.Token;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboParameters;

/**sina Adapter
 * @author sheling*/
public class SinaAdapter extends ModelAdapter {

	private static final String TAG = SinaAdapter.class.getSimpleName();
	
	private Context context;
	
	private ErrorEntry error;
	private Weibo weibo;
	private Token accessToken;
	private Map<String,Object> map;
	
	public SinaAdapter(Context context,Task t) {
		super(t);
		this.context = context;
		map = t.param;
		weibo = (Weibo) map.get("weibo");
		error = new ErrorEntry();
		error.platform = User.PLATFORM_SINA_CODE;
		accessToken = weibo.getAccessToken();
	}

	@Override
	public void run() {
		try{
			switch(task.type){
//			case Task.G_GET_GROUP_TIMELINE:
//				getOtherWeiboList();
//				break;
			case Task.USER_INFO:
			case Task.USER_OTHER_INFO:
				getUserInfo();
				break;
			case Task.WEIBO_ADD:
				addWeibo();
				break;
//			case Task.WEIBO_COMMENTS_ADD:
//				addComment();
//				break;
//			case Task.WEIBO_REPOST:
//				repostWeibo();
//				break;
//			case Task.WEIBO_COMMENTS_RE:
//				replyWeibo();
//				break;
//			case Task.MSG_COMMENTS_MENTIONS:
//			case Task.MSG_COMMENTS_ME_LIST:
//				getMentionsWeiboList();
//				break;
//			case Task.MSG_PRIVATE_LIST:
//				getPrivateMsgList();
//				break;
//			case Task.USER_FANS_LIST:
//				getUserList(1);
//				break;
//			case Task.USER_FAV_LIST:
//				getFavWeiboList();
//				break;
//			case Task.USER_FRIENDS_LIST:
//				getUserList(2);
//				break;
			case Task.USER_WEIBO_LIST:
				getUserWeiboList();
				break;
			case Task.WEIBO_COMMENTS_BY_ID:
				getCommentList();
				break;
//			case Task.USER_FREINDS_DEL:
//				delFriend();
//				break;
//			case Task.USER_FRIENDS_ADD:
//				addFriend();
//				break;
			case Task.USER_OTHER_FANS_LIST:
				getOtherFanList();
				break;
			case Task.USER_OTHER_FRIENDS_LIST:
				getOtherIdolList();
				break;
//			case Task.USER_OTHER_WEIBO_LIST:
//				getOtherWeiboList();
//				break;
//			case Task.WEIBO_ADD_AT:
//				searchUser();
//				break;
//			case Task.USER_SIMPLE_INFO_LIST:
//				getSimUserInfoList();
//				break;
			case Task.USER_SEARCH:
				searchUser();
				break;
			}
		} catch(SSLException e){
			Log.e(TAG, "exception: "+e.toString()+e.getStackTrace());
			error.exception = ErrorEntry.EXCEPTION_SSL;
			e.printStackTrace();
		} catch (ConnectTimeoutException e){
			Log.e(TAG, "exception: "+e.toString()+e.getStackTrace());
			error.exception = ErrorEntry.EXCEPTION_CONNECT_TIME_OUT;
			e.printStackTrace();
		} catch (UnknownHostException e){
			Log.e(TAG, "exception: "+e.toString()+e.getStackTrace());
			error.exception = ErrorEntry.EXCEPTION_UNKNOWN_HOST;
			e.printStackTrace();
		} catch (JSONException e){
			Log.e(TAG, "exception: "+e.toString()+e.getStackTrace());
			e.printStackTrace();
			error.exception = ErrorEntry.EXCEPTION_JSON;
		} catch(Exception e){
			Log.e(TAG, "exception: "+e.toString()+e.getStackTrace());
			e.printStackTrace();
			error.exception = ErrorEntry.EXCEPTION_OTHER;
		}
		
		task.errors.put(error.platform, error);
		model.callBack(task);
	}

	public void addWeibo() throws Exception{
		WeiboParameters params = new WeiboParameters();
		params.add("access_token",accessToken.getToken());
		params.add("status", (String)map.get("content"));
		String pic = (String)map.get("imgurl");
		String response = null;
		if(pic != null){
			Log.d(TAG, "add pic weibo: "+params.toString());
			params.add("pic", pic);
			response = weibo.request(context,"https://upload.api.weibo.com/2/statuses/upload.json",
					params,"POST",accessToken);	
		}else{
			response = weibo.request(context,"https://api.weibo.com/2/statuses/update.json",
					params,"POST",accessToken);	
		}
		JSONObject json = new JSONObject(response);
		if(json.has("error")){
			error.ret= 1;
			error.errorCode = json.getInt("error_code");
			error.detail = json.getString("error");
		}else{
			error.ret = 0;
			//success;
		}
		Log.d(TAG, "response: "+response);
	}
	
	public void getUserWeiboList() throws Exception{
		WeiboParameters params = new WeiboParameters();
		params.add("access_token",accessToken.getToken());
		JSONObject jsonObj = null;
		JSONArray jsonArr = null,arrTmp = null;
		ICArrayList arraylist = new ICArrayList();
		String response = null;
		response = weibo.request(context,"https://api.weibo.com/2/statuses/user_timeline.json",
					params,"GET",accessToken);
		JSONObject json = new JSONObject(response);
		if(json.has("error")){
			error.ret= 1;
			error.errorCode = json.getInt("error_code");
			error.detail = json.getString("error");
		}else{
			error.ret = 0;
			//success;
		}
		Log.d(TAG, "response: "+response);
		if(error.ret == 0){
			jsonArr = new JSONObject(response).getJSONArray("statuses");
			int length = jsonArr.length();
			for(int i=0;i<length;i++){
				jsonObj = jsonArr.getJSONObject(i);
				Status status = new Status();
				//TODO 封装为微博对象
				status.id = jsonObj.getString("id");
				status.text = jsonObj.getString("text");
				status.source = jsonObj.getString("source");
				if(jsonObj.has("original_pic") && jsonObj.get("original_pic") instanceof JSONArray){
					arrTmp = jsonObj.getJSONArray("original_pic");
					for(int j=0;j<arrTmp.length();j++){
						status.image.add(arrTmp.getString(j));	
					}
				}
				status.timestamp =new Date(jsonObj.getString("created_at")).getTime()/1000;
				status.rCount = jsonObj.getInt("reposts_count");
				status.cCount = jsonObj.getInt("comments_count");
				
				JSONObject jsonObjsub = jsonObj.getJSONObject("user");
				status.user.id = jsonObjsub.getString("id");
				status.user.nick = jsonObjsub.getString("name");
				status.user.head = jsonObjsub.getString("profile_image_url");
				status.platform = User.PLATFORM_SINA_CODE;
				arraylist.list.add(status);	
			}
			task.result.add(arraylist);
		}
	}
	
	public void getUserInfo() throws Exception{
		WeiboParameters params = new WeiboParameters();
		params.add("access_token",accessToken.getToken());
		params.add("screen_name","YellowSun东");
		JSONObject infoObj = null;
		User me = null;
		String response = null;
		response = weibo.request(context,"https://api.weibo.com/2/users/show.json",
					params,"GET",accessToken);
		JSONObject json = new JSONObject(response);
		if(json.has("error")){
			error.ret= 1;
			error.errorCode = json.getInt("error_code");
			error.detail = json.getString("error");
		}else{
			error.ret = 0;
			//success;
		}
		Log.d(TAG, "response: "+response);
		if(error.ret == 0){
			infoObj =new JSONObject(response);
			me = new User();
			me.id = infoObj.getString("id");
			me.name=infoObj.getString("screen_name");
			me.nick = infoObj.getString("name");
			me.province = infoObj.getInt("province");
			me.city = infoObj.getInt("city");
			me.location = infoObj.getString("location");
			me.description = infoObj.getString("description");
			me.homepage = infoObj.getString("url");
			me.head = infoObj.getString("profile_image_url");
			String sex=infoObj.getString("gender");
			if(sex=="m") me.gender = 1;
			else if(sex=="f") me.gender = 2;
			else me.gender = 0;
			me.fansnum = infoObj.getInt("followers_count");
			me.idolnum = infoObj.getInt("friends_count");
			me.favnum = infoObj.getInt("favourites_count");
			me.statusnum = infoObj.getInt("statuses_count");
			me.regTime = infoObj.getString("created_at");
			if(infoObj.getBoolean("following") ==true) 
				me.ismyfan = true;
			else
				me.ismyfan = false;
			me.platform = User.PLATFORM_SINA_CODE;
		}
		task.result.add(me);
	}
	
	public void searchUser() throws Exception{
		WeiboParameters params = new WeiboParameters();
		params.add("access_token",accessToken.getToken());
		params.add("q","东");
		String response = null;
		response = weibo.request(context,"https://api.weibo.com/2/search/suggestions/users.json",
				params,"GET",accessToken);
		Log.v(TAG,response);
		if(error.ret == 0){
			ICArrayList list=new ICArrayList();
			JSONArray info=new JSONArray(response);
			for(int j=0;j<info.length();j++){
				User users=new User();
				JSONObject iobj=info.getJSONObject(j);
//				users.head=iobj.getString("profile_image_url");
				users.id=iobj.getString("uid");
				users.name=iobj.getString("screen_name");
//				users.nick=iobj.getString("nick");
//				users.location=iobj.getString("location");
				users.fansnum=iobj.getInt("followers_count");
//				users.idolnum=iobj.getInt("idolnum");
//				users.ismyidol=iobj.getBoolean("isidol");
				users.platform=User.PLATFORM_SINA_CODE;
				Log.v("第"+j+"个：用户", users.toString());
				list.list.add(users);
			}
			task.result.add(list);
		}
	}
	
	public void getOtherIdolList() throws Exception{
		WeiboParameters params = new WeiboParameters();
		params.add("access_token",accessToken.getToken());
		params.add("screen_name","YellowSun东");
		String response=null;
		response = weibo.request(context,"https://api.weibo.com/2/friendships/friends.json",
				params,"GET",accessToken);
		if(response == null) throw new ConnectTimeoutException();
		if(error.ret == 0){
			Log.v(TAG,response);
			ICArrayList ica=new ICArrayList();
			JSONArray infolist=new JSONObject(response).getJSONArray("users");
			for(int i=0;i<infolist.length();i++){
				JSONObject job=infolist.getJSONObject(i);
				User user=new User();
				user.head=job.getString("profile_image_url");
				user.id=job.getString("id");
				user.name=job.getString("screen_name");
				user.nick=job.getString("name");
				user.platform=User.PLATFORM_SINA_CODE;
				user.location=job.getString("location");
				user.fansnum=job.getInt("followers_count");
				ica.list.add(user);
				Log.v("第"+i+"个关注：", user.toString());		
			}
			task.result.add(ica);
		}
	}
	
	public void getOtherFanList() throws Exception{
		WeiboParameters params = new WeiboParameters();
		params.add("access_token",accessToken.getToken());
		params.add("screen_name","YellowSun东");
		String response=null;
		response = weibo.request(context,"https://api.weibo.com/2/friendships/followers.json",
				params,"GET",accessToken);
		if(response == null) throw new ConnectTimeoutException();
		
		Log.v(TAG,response);
		if(error.ret == 0){
			ICArrayList ica=new ICArrayList();
			JSONArray infolist=new JSONObject(response).getJSONArray("users");
			for(int i=0;i<infolist.length();i++){
				JSONObject job=infolist.getJSONObject(i);
				User user=new User();
				user.head=job.getString("profile_image_url");
				user.id=job.getString("id");
				user.name=job.getString("screen_name");
				user.nick=job.getString("name");
				user.platform=User.PLATFORM_SINA_CODE;
				user.location=job.getString("location");
				user.fansnum=job.getInt("followers_count");
				ica.list.add(user);
				Log.v("第"+i+"个关注：", user.toString());		
			}
			task.result.add(ica);
		}
	}
	
	public void getCommentList() throws Exception{
		WeiboParameters params = new WeiboParameters();
		params.add("access_token",accessToken.getToken());
		params.add("id","3480709601770432");
		String response=null;
		response = weibo.request(context,"https://api.weibo.com/2/comments/show.json",
				params,"GET",accessToken);
		if(response == null) throw new ConnectTimeoutException();
		
		Log.v(TAG,response);
		if(error.ret == 0){
			ICArrayList ica=new ICArrayList();
			JSONArray info=new JSONObject(response).getJSONArray("comments");
			Log.d(TAG,"comments length:"+info.length());
			for(int i=0;i<info.length();i++){
				Comment comment=new Comment();
				JSONObject infoobject=info.getJSONObject(i);
				comment.id=infoobject.getString("id");
				JSONObject user=infoobject.getJSONObject("user");
				JSONObject status=infoobject.getJSONObject("status");
				comment.openid = user.getString("id");
				comment.nick=user.getString("name");
				comment.text=status.getString("text");
				comment.name = user.getString("screen_name");
				comment.timestamp=new Date(infoobject.getString("created_at")).getTime()/1000;
				ica.list.add(comment);
			  }	
			task.result.add(ica);
		}
	}
}
