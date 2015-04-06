package com.example.yuwei.killexam.serve;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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

    int notiId = 1;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("service", "RemindService onCreate");

        checkRemindTask();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
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
            }
        }
    }

    private void remind(Task task){
        final String REMIND_TITLE = "任务提醒";

        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String time = task.getFinishedDate().listShowString();
        Resources res = this.getResources();
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(task.getTagRes())
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.cat))
                .setWhen(System.currentTimeMillis())
                .setTicker(REMIND_TITLE)
                .setAutoCancel(true)
                .setContentTitle(task.getTaskName())
                .setContentText(time);

        Notification notification = builder.build();
        notificationManager.notify(notiId, notification);

        task.setRecentestDayReminded(new MyDate());
        MyDatabaseHelper.updateTask(this, task);
    }


    //  这里针对
    private boolean isNeedRemind(Task task){
        MyDate recentestDayReminded = task.getRecentestDayReminded();
        MyDate finishDay = task.getFinishedDate();
        MyDate current = new MyDate();

        if (current.equals(recentestDayReminded)){
            return false;
        }

        if (task.getRemindMethod().getSelectedName().equals("不提醒")){
            return false;
        }

        switch (task.getRemindMethod().getSelectedName()){
            case "不提醒":
                return false;

            case "智能":
                return judgeByWise(task, recentestDayReminded, current);

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
