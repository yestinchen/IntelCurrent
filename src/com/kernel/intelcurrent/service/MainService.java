﻿package com.kernel.intelcurrent.service;
import java.util.HashMap;
import java.util.Map;

import com.kernel.intelcurrent.activity.Updateable;
import com.kernel.intelcurrent.model.Group;
import com.kernel.intelcurrent.model.ICModel;
import com.kernel.intelcurrent.model.OAuthManager;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.tencent.weibo.oauthv2.OAuthV2;

import android.app.*;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
/**
 * classname:MainService.java
 * @author 许凌霄
 * */
public class MainService extends Service
{
    private static final String TAG=MainService.class.getSimpleName();
    private static MainService service;
    private ICModel model;
    private IBinder mBinder=new ICBinder();
    private Activity currentActivity;
    
	@Override
	public void onCreate() {
    	service=this;
    	model=ICModel.getICModel();
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		return mBinder;
	}
	public static MainService getInstance()
	{
		return service;
	}
	public void changeCurrentActivity(Activity current)
	{
		currentActivity=current;
	}
	
	/*=======================操纵ICModel的方法开始=======================================*/
	
	/**得到Timeline的信息
	 * @author sheling*/
	public void getTimeline(Group group,int pageflag, long pagetime,String lastid){
		OAuthV2 tencent_oauth = (OAuthV2) OAuthManager.getInstance().
			getOAuthKey(this, OAuthManager.TENCENT_PLATFORM).get(OAuthManager.TENCENT_WEIBO);
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("oauth", tencent_oauth);
		map.put("pageflag", pageflag);
		map.put("pagetime", pagetime);
		map.put("lastid", lastid);
		map.put("reqnum", 25);
		
		StringBuilder openids = new StringBuilder();
		for(User user:group.users){
			openids.append(user.id).append("_");
		}
		map.put("fopenids", openids.toString());
		Task t = new Task(Task.G_GET_GROUP_TIMELINE,map,null);
		model.doTask(t,this);
	}
	
	/**
	 * 获取用户的信息 //fopenids为0为用户自己（腾讯）
	 * @author allenjin
	 */
	public void getUserInfo(String openids){
		OAuthV2 tencent_oauth = (OAuthV2) OAuthManager.getInstance().
				getOAuthKey(this, OAuthManager.TENCENT_PLATFORM).get(OAuthManager.TENCENT_WEIBO);
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("oauth", tencent_oauth);
			map.put("openid", openids);
			Task t=new Task(Task.USER_INFO,map,null);
			model.doTask(t, this);
			Log.v(TAG,"getuserinfo in service");
	}
	/*=======================操纵ICModel的方法结束=======================================*/
	
	/**
	 * inner class:ICBinder
	 * 
	 * */
    public class ICBinder extends Binder
    {
    	/**
    	 * 让activity 获得MainService实例
    	 * */
    	public MainService getService()
 	   {
 	      return MainService.this;
 	   }
    }
    public void send(Task task)
    {
    	Message msg=new Message();
    	msg.obj=task;
    	msg.what=task.type;
    	handler.sendMessage(msg);
    }
    /**
     * 处理底层消息的内部类
     * */
    private Handler handler=new Handler()
    {
    	public void handleMessage(Message msg)
    	{
    		((Updateable)currentActivity).update(msg.what,msg.obj);
    	}
    };
}
