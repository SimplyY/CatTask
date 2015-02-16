package com.example.yuwei.killexam;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by yuwei on 15/2/16.
 */
public class DateTime {
    int year;
    int monthOfYear;
    int dayOfMonth;

    public DateTime() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        year = now.getYear() + 1900;
        monthOfYear = now.getMonth() + 1;
        dayOfMonth = now.getDay();
    }

    public int getYear() {
        return year;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    static DateTime now(){
        return new DateTime();
    }
}
