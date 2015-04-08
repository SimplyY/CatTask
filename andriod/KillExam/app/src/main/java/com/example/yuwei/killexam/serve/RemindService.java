package com.example.yuwei.killexam.serve;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.tools.MyTime;
import com.example.yuwei.killexam.tools.Task;

import java.util.ArrayList;

/**
 * Created by yuwei on 15/3/15.
 */
public class RemindService extends Service{

    static Context MyContext;
    static int notifyId = 1;
    static ArrayList<Notification> notifications = new ArrayList<>();
    NotificationManager notificationManager;


    public static void startRemindService(Context context){
        Intent myIntent = new Intent(context, RemindService.class);
        MyContext = context;
        context.startService(myIntent);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("service", "RemindService onCreate");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                checkRemindTask();
                handler.postDelayed(this, 600 * 1000);
            }
        };
        handler.postDelayed(runnable, 600 * 1000);

        checkRemindTask();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(RemindService.class.toString(), "onDestroy");
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkRemindTask(){
        ArrayList<Task> tasks = MyDatabaseHelper.getTaskArray(this);

        for (Task task : tasks){
            if(isNeedRemind(task)){
                remind(task);
                Log.i("remind", "remind work");
                for (int i = 0; i < notifications.size(); i++) {
                    notificationManager.notify(notifyId, notifications.get(i));
                    Log.i("remind", i + " remind work");
                    notifyId++;

                }
                notifications.clear();
            }
        }
    }

    private void remind(Task task){
        final String REMIND_TITLE = "任务提醒";

        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this);

        String time = task.getFinishedDate().listShowString();
        Resources res = this.getResources();
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(task.getTagRes())
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.cat))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setTicker(REMIND_TITLE)
                .setContentTitle(task.getTaskName())
                .setContentText(time);


        Notification notification = builder.build();
        notifications.add(notification);



        task.setRecentestDayReminded(new MyDate());
        MyDatabaseHelper.updateTask(this, task);
    }


    //  这里针对
    private boolean isNeedRemind(Task task){
        MyDate recentestDayReminded = task.getRecentestDayReminded();
        MyDate finishDay = task.getFinishedDate();
        MyDate currentDate = new MyDate();

        MyTime currentTime = MyTime.getCurrentTime();
        MyTime beginRemindTime = new MyTime();
        MyTime endRemindTime = new MyTime();

        MyDatabaseHelper.getRemindTime(MyContext, beginRemindTime, endRemindTime);

        if (beginRemindTime.isLaterThan(currentTime) || currentTime.isLaterThan(endRemindTime)){
            return false;
        }

        if (currentDate.equals(recentestDayReminded)){
            return false;
        }

        if (task.getRemindMethod().getSelectedName().equals("不提醒")){
            return false;
        }

        switch (task.getRemindMethod().getSelectedName()){
            case "不提醒":
                return false;

            case "智能":
                return judgeByWise(task, recentestDayReminded, currentDate);

            case "每天":
                if (recentestDayReminded.isBefore(finishDay)){
                    return true;
                }
                break;
            case "每周":
                for (int i = 1; recentestDayReminded.isBefore(finishDay); i++) {
                    if (recentestDayReminded.addDay(i * 7).equals(finishDay)) {
                        return true;
                    }
                }
                break;
            case "每月":
                for (int i = 1; recentestDayReminded.isBefore(finishDay); i++){
                    if (recentestDayReminded.addMonth(i).equals(finishDay)){
                        return true;
                    }
                }
                break;
            case "每年":
                for (int i = 1; recentestDayReminded.isBefore(finishDay); i++){
                    if (recentestDayReminded.addYear(i).equals(finishDay)){
                        return true;
                    }
                }
                break;
        }

        return false;

    }

    private boolean judgeByWise(Task task, MyDate recentestDayReminded, MyDate current){
        if (recentestDayReminded.isBefore(task.getFinishedDate())){
            return true;
        }

        return false;
    }

}
