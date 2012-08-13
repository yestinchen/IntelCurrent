package com.kernel.intelcurrent.activity;
import com.kernel.intelcurrent.service.MainService;
import com.kernel.intelcurrent.service.MainService.ICBinder;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.Log;
/**
 * classname:BaseActivity.java
 * @author 许凌霄
 * */
public abstract class BaseActivity extends Activity
{
    private static final String TAG=BaseActivity.class.getSimpleName();
    protected MainService mService;
    boolean isBound=false;
    
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
 	    setContentView(R.layout.activity_base);
    }
    protected void onStart()
   	{
   	   super.onStart();
   	   Intent intent=new Intent(this,MainService.class);
   	   bindService(intent,connection, Context.BIND_AUTO_CREATE);
   	   Log.v(TAG, "mService bind");
   	   if(mService!=null){
   		   Log.v(TAG,"mService is not null");
   	   }
   	}
    protected void onStop()
	{
	   super.onStop();
	   unbindService(connection);
	   isBound=false;
	}
   
    /**
     * inner class:ServiceConnection
     * */
    private ServiceConnection connection= new ServiceConnection()
    {

		public void onServiceConnected(ComponentName className, IBinder service)
		{
			ICBinder binder=(ICBinder)service;
			mService=binder.getService();
			isBound=true;
			mService.changeCurrentActivity(BaseActivity.this);	
			onConnectionFinished();		
		}

		public void onServiceDisconnected(ComponentName name) 
		{
			isBound = false;
			onConnectionDisConnected();
		}
    	
    };
    public abstract void onConnectionFinished();
    public abstract void onConnectionDisConnected();
}
