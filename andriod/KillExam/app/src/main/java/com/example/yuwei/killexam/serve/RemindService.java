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
                handler.postDelayed(this, 300 * 1000);
            }
        };
        handler.postDelayed(runnable, 300 * 1000);

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

//  if check right notify it
    private void checkRemindTask(){
        ArrayList<Task> tasks = MyDatabaseHelper.getAllTaskArray(this);
        for (Task task : tasks){
            if(isNeedRemind(task)){
                buildNotification(task);

                Log.i("buildNotification", "buildNotification work");
                task.setRecentestDayReminded(new MyDate());
                MyDatabaseHelper.updateTask(this, task);
            }
        }

        for (int i = 0; i < notifications.size(); i++) {
            notificationManager.notify(notifyId, notifications.get(i));
            notifyId ++;
        }

        notifications.clear();
    }

    private void buildNotification(Task task){
        final String REMIND_TITLE = "任务提醒";

        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this);

        String time = task.getFinishedDate().listShowString();
        Resources res = this.getResources();
        final String title = task.getBelongName() == null ? task.getTaskName() :
                task.getBelongName() + ":" + task.getTaskName();
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(task.getTagRes())
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.cat))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setTicker(REMIND_TITLE)
                .setContentTitle(title)
                .setContentText(time);

        Notification notification = builder.build();
        notifications.add(notification);
    }

    //  这里针对
    private boolean isNeedRemind(Task task){
        MyDate recentestDayReminded = task.getRecentestDayReminded();
        MyDate finishDay = task.getFinishedDate();
        MyDate currentDate = new MyDate();

        if(!isTimeInRemindTime()){
            return false;
        }

        if (currentDate.equals(recentestDayReminded)){
            return false;
        }

        if (currentDate.equals(finishDay)){
            return true;
        }

        switch (task.getRemindMethod().getSelectedName()){
            case "不提醒":
                return false;
            case "智能":
                return judgeByWise(task);
            case "每天":
                if (recentestDayReminded.isBefore(finishDay)){
                    return true;
                }
                break;
            case "每周":
                return judgeByDays(task, 7);
            case "每月":
                return judgeEveryMonth(task);
            case "每年":
                return remindEveryYear(task);

        }
        return false;

    }

    private boolean isTimeInRemindTime(){
        MyTime beginRemindTime = new MyTime();
        MyTime endRemindTime = new MyTime();
        MyTime currentTime = MyTime.getCurrentTime();

        MyDatabaseHelper.getRemindTime(MyContext, beginRemindTime, endRemindTime);

        if (beginRemindTime.isLaterThan(currentTime) || currentTime.isLaterThan(endRemindTime)){
            return false;
        }
        return true;
    }

    private boolean judgeByWise(Task task){
        if (task.getRecentestDayReminded().isBefore(task.getFinishedDate())){
            switch (task.getTagColor().getSelectedName()){
                case "银色":
                    return judgeEveryMonth(task);
                case "绿色":
                    return judgeByDays(task, 14);
                case "蓝色":
                    return judgeByDays(task, 7);
                case "紫色":
                    return judgeByDays(task, 3);
            }
        }
        return false;
    }

    private boolean judgeByDays(Task task, int days){
        MyDate finishDay = task.getFinishedDate();
        MyDate currentDate = new MyDate();
        for (int i = 1; currentDate.addDay(i * days).isBefore(finishDay.addDay(1)); i++) {
            if (currentDate.addDay(i * days).equals(finishDay)) {
                return true;
            }
        }
        return false;
    }


    private boolean judgeEveryMonth(Task task){
        MyDate finishDay = task.getFinishedDate();
        MyDate currentDate = new MyDate();
        for (int i = 1; currentDate.addMonth(i).isBefore(finishDay.addDay(1)); i++) {
            if (currentDate.addMonth(i).equals(finishDay)) {
                return true;
            }
        }
        return false;
    }

    private boolean remindEveryYear(Task task){
        MyDate finishDay = task.getFinishedDate();
        MyDate currentDate = new MyDate();
        for (int i = 1; currentDate.addYear(i).isBefore(finishDay.addDay(1)); i++) {
            if (currentDate.addYear(i).equals(finishDay)) {
                return true;
            }
        }
        return false;
    }

}
