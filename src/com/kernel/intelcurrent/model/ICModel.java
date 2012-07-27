package com.kernel.intelcurrent.model;

import java.util.LinkedList;

import android.content.Context;
import android.util.Log;

import com.kernel.intelcurrent.service.MainService;

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
	   switch(type)
	   {
	   //添加任务分类执行
	   }
   }
   
   public int checkForThreadsNum(int type,Context context)
   {
	   int result=1;
	   //增加逻辑判断
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
