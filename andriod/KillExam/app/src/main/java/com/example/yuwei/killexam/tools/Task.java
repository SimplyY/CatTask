package com.example.yuwei.killexam.tools;


import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.map.SpinnerValue;

import java.io.Serializable;

/**
 * Created by yuwei on 15/2/18.
 */

//notice: 所有的belong通过belongName查询
public class Task implements Serializable {

    private SpinnerValue tagColor;
    private String taskName;
    private MyDate finishedDate;
    private MyTime spendTime = new MyTime();
    private SpinnerValue remindMethod;
    private SpinnerValue taskAttribute;

    private boolean hasBelong = false;
    private String belongName;
    private MyDate headerDate;
    private MyDate recentestDayReminded = new MyDate(-1);

    public MyDate getRecentestDayReminded() {
        return recentestDayReminded;
    }

    public void setRecentestDayReminded(MyDate recentestDayReminded) {
        this.recentestDayReminded = recentestDayReminded;
    }

    private int hasFinished = 0;

    public Task() {
    }

    public int getTagRes(){
        String color = getTagColor().getSelectedName();

        int tagResId = R.drawable.tag_color_silver;
        switch (color) {
            case "银色":
                tagResId = R.drawable.tag_color_silver;
                break;
            case "绿色":
                tagResId = R.drawable.tag_color_green;
                break;
            case "蓝色":
                tagResId = R.drawable.tag_color_blue;
                break;
            case "紫色":
                tagResId = R.drawable.tag_color_purple;
                break;
        }
        return tagResId;
    }

    public String toString(){
        return taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public SpinnerValue getTagColor() {
        return tagColor;
    }

    public MyDate getFinishedDate() {
        return finishedDate;
    }

    public int getSpendHours() {
        return spendTime.hours;
    }

    public int getSpendMinutes() {
        return spendTime.minutes;
    }


    public SpinnerValue getRemindMethod() {
        return remindMethod;
    }

    public SpinnerValue getTaskAttribute() {
        return taskAttribute;
    }

    public boolean isHasBelong() {
        return hasBelong;
    }

    public String getBelongName() {
        return belongName;
    }

    public int getHasFinished() {
        return hasFinished;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTagColor(SpinnerValue tagColor) {
        this.tagColor = tagColor;
    }

    public void setFinishedDate(MyDate finishedDate) {
        this.finishedDate = finishedDate;
    }

    public void setSpendTime(int hours, int minutes){
        spendTime = new MyTime(hours, minutes);
    }

    public void setRemindMethod(SpinnerValue remindMethod) {
        this.remindMethod = remindMethod;
    }

    public void setTaskAttribute(SpinnerValue taskAttribute) {
        this.taskAttribute = taskAttribute;
    }


    public void setHasBelong(boolean hasBelong) {
        this.hasBelong = hasBelong;
    }

    public void setBelongName(String belongName) {
        this.belongName = belongName;
    }

    public void setHasFinished(int hasFinished) {
        this.hasFinished = hasFinished;
    }

    public MyDate getHeaderDate() {
        return headerDate;
    }

    public void setHeaderDate(MyDate headerDate) {
        this.headerDate = headerDate;
    }
}
