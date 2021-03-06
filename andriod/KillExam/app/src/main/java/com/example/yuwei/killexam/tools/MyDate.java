package com.example.yuwei.killexam.tools;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by yuwei on 15/2/16.
 */
public class MyDate implements Serializable {
    private static Calendar calendar = Calendar.getInstance();
    private int year;
    private int month;
    private int day;

    //不带参数的构造函数得到为当前时间的对象
    public MyDate() {
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;//java 月份从零开始算
        year = calendar.get(Calendar.YEAR);
    }

    public MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public MyDate(int i) {
        day = calendar.get(Calendar.DAY_OF_MONTH) + i;
        month = calendar.get(Calendar.MONTH) + 1;//java 月份从零开始算
        year = calendar.get(Calendar.YEAR);
        this.addDay(i);
    }

    public MyDate(String date) {
        if (date.equals("")) {
            year = 0;
            month = 0;
            day = 0;
            return;
        }
        int indexYearChar = date.indexOf("年");
        int indexMonthChar = date.indexOf("月");
        int indexDayChar = date.indexOf("日");

        year = Integer.valueOf(date.substring(0, indexYearChar));
        month = Integer.valueOf(date.substring(indexYearChar + 1, indexMonthChar));
        day = Integer.valueOf(date.substring(indexMonthChar + 1, indexDayChar));
    }

    @Override
    public String toString() {
        if (year == 0) {
            return "";
        }

        return year + "年" + month + "月" + day + "日";
    }


    public boolean isBefore(MyDate theDate) {
        if (getYear() < theDate.getYear()) {
            return true;
        }
        if (getYear() == theDate.getYear()) {
            if (getMonth() < theDate.getMonth()) {
                return true;
            }
            if (getMonth() == theDate.getMonth()) {
                if (getDay() < theDate.getDay()) {
                    return true;
                }
            }
        }

        return false;
    }


    public MyDate addDay(int increaseDay) {
        MyDate myDate = new MyDate(getYear(), getMonth(), getDay());

        int amountDayOfMonth = myDate.getAmountDayOfMonth();
        int newDay = myDate.getDay() + increaseDay;

        while (newDay > amountDayOfMonth) {
            myDate = myDate.addMonth(1);
            newDay -= amountDayOfMonth;
        }
        myDate.setDay(newDay);
        return myDate;
    }

    public MyDate addMonth(int increaseMonth) {
        MyDate myDate = new MyDate(getYear(), getMonth(), getDay());
        int newMonth = myDate.getMonth() + increaseMonth;

        while (newMonth > 12){
            myDate = myDate.addYear(1);
            newMonth -= 12;
        }
        myDate.setMonth(newMonth);
        return myDate;
    }

    public MyDate addYear(int increaseYear) {
        MyDate myDate = new MyDate(getYear() + increaseYear, getMonth(), getDay());
        return myDate;
    }

    public int getAmountDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.getYear());
        cal.set(Calendar.MONTH, this.getMonth() - 1);

        return cal.getActualMaximum(Calendar.DATE);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyDate myDate = (MyDate) o;

        if (day != myDate.day) return false;
        if (month != myDate.month) return false;
        if (year != myDate.year) return false;

        return true;
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

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String listShowString() {
        return this.toString().substring(5);
    }
}
