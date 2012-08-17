package com.kernel.intelcurrent.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.kernel.intelcurrent.activity.MainActivity;
import com.kernel.intelcurrent.activity.PushCenterActivity;
import com.kernel.intelcurrent.activity.R;
import com.kernel.intelcurrent.model.OAuthManager;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.modeladapter.TencentAdapter;
import com.tencent.weibo.oauthv2.OAuthV2;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

public class PushService extends Service {

	private static final String TAG = PushService.class.getSimpleName();
	
	private SharedPreferences prefs;
	private SoundPool pool;
	private AudioManager mAudioManager;
	private int soundID;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				Thread t = new Thread(){
					public void run(){
						checkMessage();				
					}
				};
				t.start();
				break;
			}
		}
	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Thread t = new Thread(){
			public void run(){
				Log.d(TAG, "service_task_running");
				checkMessage();
			}
		};
		t.start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void checkMessage(){
		prefs= getSharedPreferences(PushCenterActivity.PREFERENCE_FILE_PUSH_CENTER, 0);
		//若关闭则直接返回
		if(!prefs.getBoolean("push_enable", true)) return;
		//若时间选定，判断时间区间
		Calendar cal = Calendar.getInstance();
		if(!(prefs.getBoolean("time_enable", true) && 
				(cal.get(Calendar.HOUR_OF_DAY)>21 || cal.get(Calendar.HOUR_OF_DAY) <8) )){
			OAuthManager manager = OAuthManager.getInstance();
			int check = manager.oAuthCheck(this);
			OAuthV2 tencentOAuth = null;
			int result = 0;
			if(check == OAuthManager.RESULT_BOTH_AVALIABLE || check == OAuthManager.RESULT_ONLY_TENCENT_AVALIABLE){
				tencentOAuth = (OAuthV2) manager.getOAuthKey(this, OAuthManager.TENCENT_PLATFORM).get(OAuthManager.TENCENT_WEIBO);	
			}
			Map<String,Integer> resultMap = requestUpdateMsg(tencentOAuth);
			if(prefs.getBoolean("push_type_tiji", true)){
				result += checkTiji(resultMap);
			}
			if(prefs.getBoolean("push_type_sixin", true)){
				result += checkSixin(resultMap);
			}
			if(prefs.getBoolean("push_type_tingzhong", true)){
				result += checkTingzhong(resultMap);
			}
			
			if(result != 0){
				if(prefs.getBoolean("push_noti_type_music", true)){
					playNotifySound();
				}
				if(prefs.getBoolean("push_noti_type_shake", true)){
					vibrate();
				}
			}
		}
		Message msg = Message.obtain();
		msg.what = 1;
		long delayMillis = prefs.getInt("push_time_rate", 20) * 60 * 1000;
		handler.sendMessageDelayed(msg, delayMillis);
	}
	
	/**请求更新信息*/
	private Map<String,Integer> requestUpdateMsg(OAuthV2 tencentOAuth){
		if(tencentOAuth == null) return null;
		Map<String ,Object >map = new HashMap<String, Object>();
		map.put("oauth", tencentOAuth);
		map.put("op", 0);
		map.put("type", 0);
		Task t = new Task(Task.MSG_NUM_NOTIFICATION, map, null);
		TencentAdapter adapter = new TencentAdapter(t);
		adapter.getUpdateMsg();
		if(t.result != null && t.result.size() != 0){
			@SuppressWarnings("unchecked")
			HashMap<String,Integer> m = (HashMap<String, Integer>) t.result.get(0);
			return m;
		}
		return null;	
	}
	
	/**检查提及@*/
	private int checkTiji(Map<String,Integer> result){
		if(result == null || result.size() == 0) return 0 ;
		int num = result.get("mentions");
		if(num != 0){
			Intent intent = new Intent(this,MainActivity.class);
			notifyUser(intent,R.string.app_name +1,num+"条新提及", "点击查看详细");
		}
		return num;
	}

	/**检查私信*/
	private int checkSixin(Map<String,Integer> result){
		if(result == null || result.size() == 0) return 0 ;
		int num = result.get("private");
		if(num != 0){
			Intent intent = new Intent(this,MainActivity.class);
			notifyUser(intent,R.string.app_name +2,num+"条新私信", "点击查看详细");
		}
		return num;
	}

	/**检查听众*/
	private int checkTingzhong(Map<String,Integer> result){
		if(result == null || result.size() == 0) return 0 ;
		int num = result.get("fans");
		if(num != 0){
			Intent intent = new Intent(this,MainActivity.class);
			notifyUser(intent,R.string.app_name +3,num+"个新粉丝", "点击查看详细");
		}
		return num;
	}
	
	/**提醒用户*/
	private void notifyUser(Intent intent,int id,String title,String detail){
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,getResources().getString(R.string.app_name), System.currentTimeMillis());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, Notification.FLAG_SHOW_LIGHTS);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(this, title, detail,pi);
		nm.notify(id, notification);
	}
	
	
	/**播放提醒音频*/
	private void playNotifySound(){
		MediaPlayer mp = new MediaPlayer();
		try {
			AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.push_notify);
			mp.reset();
			mp.setAudioStreamType(AudioManager.STREAM_SYSTEM);
			mp.setLooping(false);
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mp.prepare();
			mp.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**震动*/
	private void vibrate(){
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[]{200,200}, -1);
	}
}
