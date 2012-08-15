package com.kernel.intelcurrent.model;

import java.util.LinkedList;
import java.util.Map;

import android.content.Context;
import android.text.style.ClickableSpan;
import android.util.Log;

import com.kernel.intelcurrent.modeladapter.TencentAdapter;
import com.kernel.intelcurrent.service.MainService;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.oauthv2.OAuthV2;

/**
 * classname:ICModel.java
 * @author 许凌霄
 * */
public class ICModel 
{
   private static final String TAG=ICModel.class.getSimpleName();
   private static ICModel model;
   private LinkedList<Task> tasks;
   private LinkedList<Group> group;
   private boolean sinaBound=false,
		           tencentBound=false;
   private int sinaAccessToken,tencentAccessToken;
   
   private OAuthV2 tencentOAuth = null;
   private String clientIp;
   private static int platformAvaliable = -1;
   private int total;//多平台一共能启动的线程数
   
   private ICModel()
   {
	   tasks=new LinkedList<Task>();
   }
   public static ICModel getICModel()
   {
	   if(model==null)
	   {
		   model=new ICModel();
	   }
	   return model;
   }
   /**
    * 任务执行，service调用该方法执行任务
    * @param task 任务
    * @param context 上下文环境，用来调用oAuthManager
    * */
   public void doTask(Task task,Context context)
   {
	   tasks.add(task);
	   int type=task.type;
	   if(platformAvaliable == -1){
		   checkForThreadsNum(task.type,context);
	   }
	   Log.v(TAG, "do task");Log.d(TAG, "tencentOauth:"+tencentOAuth);
	   switch(type)
	   {
	   //添加任务分类执行
	   //所有多平台任务
	   case Task.G_GET_GROUP_TIMELINE:
	   case Task.USER_INFO:
	   case Task.MSG_COMMENTS_MENTIONS:
	   case Task.MSG_COMMENTS_ME_LIST:
	   case Task.MSG_PRIVATE_LIST:
	   case Task.USER_FANS_LIST:
	   case Task.USER_FAV_LIST:
	   case Task.USER_FRIENDS_LIST:
	   case Task.USER_WEIBO_LIST:
	   case Task.USER_FREINDS_DEL:
	   case Task.USER_FRIENDS_ADD:
	   case Task.USER_OTHER_INFO:
	   case Task.USER_OTHER_FANS_LIST:
	   case Task.USER_OTHER_FRIENDS_LIST:
	   case Task.USER_OTHER_WEIBO_LIST:
		   total=checkForThreadsNum(type,context);
		   task.total=total;
		   if(platformAvaliable == OAuthManager.RESULT_BOTH_AVALIABLE
		   	|| platformAvaliable == OAuthManager.RESULT_ONLY_TENCENT_AVALIABLE){
			   Log.v(TAG, "new tencentadapter start");
			   task.param.put("oauth", tencentOAuth);
			   TencentAdapter ta=new TencentAdapter(task);
			   ta.start();
		   }
		   break;
		//所有单平台任务
	   case Task.WEIBO_ADD:
	   case Task.WEIBO_COMMENTS_ADD:
	   case Task.WEIBO_REPOST:
	   case Task.WEIBO_COMMENTS_BY_ID:
	   case Task.WEIBO_ADD_AT:
		   task.total = 1;
		   clientIp = OAuthManager.getInstance().getClientIP();
		   Log.v(TAG, "client ip:"+clientIp);
		   switch((Integer)task.param.get("platform")){
		   case Task.PLATFORM_ALL:
			   task.param.put("oauth", tencentOAuth);
			   task.param.put("clientip", clientIp);
			   TencentAdapter ta = new TencentAdapter(task);
			   ta.start();
		   case Task.PLATFORM_SINA:
			   break;
		   case Task.PLATFORM_TENCENT:
			   task.param.put("oauth", tencentOAuth);
			   task.param.put("clientip", clientIp);
			   TencentAdapter t = new TencentAdapter(task);
			   t.start();
			   break;
		   }
		   break;
	   }
    }
   
   public int checkForThreadsNum(int type,Context context)
   {
	   int result = 0;
	   //由于两个平台同时申请的情况较少，所以把这些挑出来，较检，其他return 1
//	   if(type==Task.G_GET_GROUP_TIMELINE){
		   //若从未较检过，去较检
		   if(platformAvaliable == -1){
			   platformAvaliable = OAuthManager.getInstance().oAuthCheck(context);
			   Log.v(TAG, "plat"+platformAvaliable);

			   //添加OAuth
			   if(platformAvaliable == OAuthManager.RESULT_ONLY_TENCENT_AVALIABLE ||
					   platformAvaliable == OAuthManager.RESULT_BOTH_AVALIABLE){
				   tencentOAuth = (OAuthV2) OAuthManager.getInstance().
						   getOAuthKey(context,OAuthManager.TENCENT_PLATFORM).get(OAuthManager.TENCENT_WEIBO);
				   Log.d(TAG, "tencentOauth:"+tencentOAuth);
			   }
		   }
		   if(platformAvaliable == OAuthManager.RESULT_ONLY_SINA_AVALIABLE ||
				   platformAvaliable == OAuthManager.RESULT_ONLY_TENCENT_AVALIABLE){
			   result =  1;
		   }else if (platformAvaliable == OAuthManager.RESULT_BOTH_AVALIABLE){
			   result =2;
		   }
		 total  = result;
//	   }
	   return result;
   }
   
   /**
    * 各线程完成任务时调用callback方法回调
    * */
   public void callBack(Task task)
   {
	   //deal with the result
	   //...
	   //如果任务种类明确不需要或不会产生不同SDK同时访问的状况，则直接交给service，model本身无需再处理
	   switch(task.type){
	   case Task.G_GET_GROUP_TIMELINE:
		   //do sth...
		   break;
		default :
			Log.v(TAG, "break task:"+task);
		   break;
	   }
	   MainService service=MainService.getInstance();
	   service.send(task);
   }
   
}
