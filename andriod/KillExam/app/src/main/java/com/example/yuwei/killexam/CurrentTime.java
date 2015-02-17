package com.example.yuwei.killexam;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
/**
 * Created by yuwei on 15/2/16.
 */
public class CurrentTime {
    Date now;
    Calendar calendar;
    int year;
    int month;
    int day;
    public CurrentTime() {
        calendar = Calendar.getInstance();
        now = calendar.getTime();

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
    }

    public CurrentTime(int year, int month, int day) {
        calendar = Calendar.getInstance();
        now = calendar.getTime();

        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public String toString() {
        return (String)DateFormat.format("MMMM, d, yyyy ", calendar.getTime());
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
