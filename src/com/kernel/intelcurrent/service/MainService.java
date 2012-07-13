package com.kernel.intelcurrent.service;
import com.kernel.intelcurrent.model.ICModel;
import com.kernel.intelcurrent.model.Task;

import android.app.*;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
    public MainService()
    {
    	super();
    	service=this;
    	model=ICModel.getICModel();
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
    	Activity target=task.target;
    	if(target!=currentActivity)
    	{
    		//记录任务被终止
    		//...
    		return;
    	}
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
    	public void handlerMessage(Message msg)
    	{
    		switch(msg.what)
    		{
    		
    		}
    	}
    };
}
