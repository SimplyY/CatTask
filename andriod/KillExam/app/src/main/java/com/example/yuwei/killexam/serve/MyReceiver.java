package com.example.yuwei.killexam.serve;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yuwei on 15/3/15.
 */
public class MyReceiver extends BroadcastReceiver{

    private static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent){

        if (intent.getAction().equals(BOOT_ACTION)){
            Log.i("MyReceiver", "onReceive");

            RemindService.startRemindService(context);

        }
    }


}
