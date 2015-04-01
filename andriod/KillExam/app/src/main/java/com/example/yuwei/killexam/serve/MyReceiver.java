package com.example.yuwei.killexam.serve;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by yuwei on 15/3/15.
 */
public class MyReceiver extends BroadcastReceiver{

    private static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals(BOOT_ACTION)){

            //获取AlarmManager系统服务
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            //包装需要执行Service的Intent

            Intent myIntent = new Intent("com.yuwei.REMIND_SERVICE");
            PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                    myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //触发服务的起始时间,elapsedRealtime()得到上一次触发距离现在的时间
            long triggerAtTime = SystemClock.elapsedRealtime();

            //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
                    1 * 600, pendingIntent);
        }
    }
}
