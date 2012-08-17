package com.kernel.intelcurrent.service;

import com.kernel.intelcurrent.activity.PushCenterActivity;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	private static final String DEBUG_TAG = "BootReceiver";
    private PendingIntent mAlarmSender;
    @Override
    public void onReceive(Context context, Intent intent) {
        // 启动提醒服务，每30分钟检查是否启动
        mAlarmSender = PendingIntent.getService(context, 0, new Intent(context,
                PushService.class), 0);
        long firstTime = SystemClock.elapsedRealtime();
        SharedPreferences prefs = context.getSharedPreferences(PushCenterActivity.PREFERENCE_FILE_PUSH_CENTER, 0);
        if(prefs.getBoolean("push_enable", true)){
            AlarmManager am = (AlarmManager) context
                    .getSystemService(Activity.ALARM_SERVICE);
            am.cancel(mAlarmSender);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                    30 * 60 * 1000, mAlarmSender);
        }
        Log.v(DEBUG_TAG, "booted ,starting service");
    }
}