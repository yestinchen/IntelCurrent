package com.kernel.intelcurrent.model;

import java.util.LinkedList;

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
    * */
   public void doTask(Task task)
   {
	   tasks.add(task);
	   int type=task.type;
	   int total=checkForThreadsNum(type);
	   task.total=total;
	   switch(type)
	   {
	   //添加任务分类执行
	   }
   }
   public int checkForThreadsNum(int type)
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
	   
	   MainService service=MainService.getInstance();
	   service.send(task);
   }
   
}
