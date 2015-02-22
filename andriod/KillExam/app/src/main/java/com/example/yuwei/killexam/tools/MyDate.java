package com.example.yuwei.killexam.tools;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
/**
 * Created by yuwei on 15/2/16.
 */
public class MyDate {
    Date now;
    Calendar calendar = Calendar.getInstance();
    int year;
    int month;
    int day;
    public MyDate() {
        now = calendar.getTime();

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
    }

    public MyDate(int year, int month, int day) {
        now = calendar.getTime();

        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public String toString() {
        return year + "." + month + "." + day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
