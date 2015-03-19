package com.example.yuwei.killexam.tools;

import java.io.Serializable;

/**
 * Created by yuwei on 15/3/15.
 */
public class MyTime implements Serializable {
    public int minutes;
    public int hours;

    public MyTime() {
    }

    public MyTime(int hours, int minuts) {
        this.minutes = minuts;
        this.hours = hours;
    }
}
