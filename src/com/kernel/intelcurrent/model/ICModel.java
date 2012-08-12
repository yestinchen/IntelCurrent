package com.kernel.intelcurrent.model;

import java.util.LinkedList;

import android.content.Context;
import android.util.Log;

import com.kernel.intelcurrent.modeladapter.TencentAdapter;
import com.kernel.intelcurrent.service.MainService;
import com.tencent.weibo.beans.OAuth;

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
   
   private static int platformAvaliable = -1;
   
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
	   int total=checkForThreadsNum(type,context);
	   task.total=total;
	   Log.v(TAG, "do task");
	   if(platformAvaliable == OAuthManager.RESULT_BOTH_AVALIABLE
	   	|| platformAvaliable == OAuthManager.RESULT_ONLY_TENCENT_AVALIABLE){
		   Log.v(TAG, "new tencentadapter start");
		   TencentAdapter ta=new TencentAdapter(task);
		   ta.start();
	   }
//	   switch(type)
//	   {
//	   //添加任务分类执行
//	   case Task.G_GET_GROUP_TIMELINE:
//		   if(platformAvaliable == OAuthManager.RESULT_BOTH_AVALIABLE
//		   	|| platformAvaliable == OAuthManager.RESULT_ONLY_TENCENT_AVALIABLE){
//			   TencentAdapter ta = new TencentAdapter(task);
//			   ta.start();		  
//		   }
//		   break;
//	   case Task.USER_INFO:
//		   if(platformAvaliable == OAuthManager.RESULT_BOTH_AVALIABLE
//		   	|| platformAvaliable == OAuthManager.RESULT_ONLY_TENCENT_AVALIABLE){
//			   Log.v(TAG, "new tencentadapter start");
//			   TencentAdapter ta=new TencentAdapter(task);
//			   ta.start();
//		   }
//		   break;
//	   }
    }
   
   public int checkForThreadsNum(int type,Context context)
   {
	   int result = 0;
	   //由于两个平台同时申请的情况较少，所以把这些挑出来，较检，其他return 1
	   if(type==Task.G_GET_GROUP_TIMELINE){
		   //若从未较检过，去较检
		   if(platformAvaliable == -1){
			   platformAvaliable = OAuthManager.getInstance().oAuthCheck(context);
		   }
		   if(platformAvaliable == OAuthManager.RESULT_ONLY_SINA_AVALIABLE ||
				   platformAvaliable == OAuthManager.RESULT_ONLY_TENCENT_AVALIABLE){
			   result =  1;
		   }else if (platformAvaliable == OAuthManager.RESULT_BOTH_AVALIABLE){
			   result =2;
		   }
	   }
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
