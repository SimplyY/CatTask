package com.example.yuwei.killexam.tools;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by yuwei on 15/2/18.
 */

//notice: 所有的belong通过belongName查询
public class Task implements Serializable {


    private String taskName;
    private MyDate finishedTime;
    private int spendHours = 0;
    private int spendMinutes = 0;
    private String taskContext;
    private String remindMethod = "不提醒";
    private String taskAttribute;

    private boolean hasBelong = false;
    private String belongName;

    public Task() {
    }

    public String toString(){
        return taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public MyDate getFinishedTime() {
        return finishedTime;
    }

    public int getSpendHours() {
        return spendHours;
    }

    public int getSpendMinutes() {
        return spendMinutes;
    }

    public String getTaskContext() {
        return taskContext;
    }

    public String getRemindMethod() {
        return remindMethod;
    }

    public String getTaskAttribute() {
        return taskAttribute;
    }

    public boolean isHasBelong() {
        return hasBelong;
    }

    public String getBelongName() {
        return belongName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setFinishedTime(MyDate finishedTime) {
        this.finishedTime = finishedTime;
    }

    public void setSpendHours(int spendHours) {
        this.spendHours = spendHours;
    }

    public void setSpendMinutes(int spentMinutes) {
        this.spendMinutes = spentMinutes;
    }

    public void setTaskContext(String taskContext) {
        this.taskContext = taskContext;
    }

    public void setRemindMethod(String remindMethod) {
        this.remindMethod = remindMethod;
    }

    public void setTaskAttribute(String taskAttribute) {
        this.taskAttribute = taskAttribute;
    }


    public void setHasBelong(boolean hasBelong) {
        this.hasBelong = hasBelong;
    }

    public void setBelongName(String belongName) {
        this.belongName = belongName;
    }
}
