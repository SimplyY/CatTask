package com.example.yuwei.killexam.tools;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
/**
 * Created by yuwei on 15/2/16.
 */
public class MyDate {
    private Calendar calendar = Calendar.getInstance();
    private int year;
    private int month;
    private int day;

//不带参数的构造函数得到为当前时间的对象
    public MyDate() {
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
    }

    public MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public MyDate(String date){
        int indexYearChar = date.indexOf("年");
        int indexMonthChar = date.indexOf("月");
        int indexDayChar = date.indexOf("日");

        year = Integer.valueOf(date.substring(0,indexYearChar));
        month = Integer.valueOf(date.substring(indexYearChar + 1, indexMonthChar));
        day = Integer.valueOf(date.substring(indexMonthChar + 1, indexDayChar));
    }

    @Override
    public String toString() {
        return year + "年" + month + "月" + day + "日";
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

    public String listString(){
        return this.toString().substring(5);
    }
}
