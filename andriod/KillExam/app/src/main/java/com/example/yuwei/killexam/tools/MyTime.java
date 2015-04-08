package com.example.yuwei.killexam.tools;

import android.text.format.Time;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by yuwei on 15/3/15.
 */
public class MyTime implements Serializable {
    public int minutes;
    public int hours;

    public MyTime() {
    }

    public static MyTime getCurrentTime(){
//        long time=System.currentTimeMillis();
//
//        final Calendar mCalendar=Calendar.getInstance();
//        mCalendar.setTimeInMillis(time);
//        int hours=mCalendar.get(Calendar.HOUR);
//        int minutes=mCalendar.get(Calendar.MINUTE);


        Time current = new Time();
        current.setToNow();
        int minutes = current.minute;
        int hours = current.hour;
        return new MyTime(hours, minutes);
    }

    public MyTime(int hours, int minutes) {
        this.minutes = minutes;
        this.hours = hours;
    }

    public boolean isLaterThan(MyTime otherTime){
        return this.getValue() > otherTime.getValue();
    }

    private int getValue(){
        return (hours * 60) + minutes;
    }
}
