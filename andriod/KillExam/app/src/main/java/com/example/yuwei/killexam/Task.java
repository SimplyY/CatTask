package com.example.yuwei.killexam;

/**
 * Created by yuwei on 2014/12/30.
 */

public class Task {
    private String auther;
    private String finishTime;
    private String spendTime;
    private String content;
    private String belong;
    private int remindMethod;
    private int attribute;


    public Task(String auther, String finishTime, String spendTime, String content, String belong, int remindMethod, int attribute) {
        this.auther = auther;
        this.finishTime = finishTime;
        this.spendTime = spendTime;
        this.content = content;
        this.belong = belong;
        this.remindMethod = remindMethod;
        this.attribute = attribute;
    }

    public String getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(String spendTime) {
        this.spendTime = spendTime;
    }

    public void setRemindMethod(int remindMethod) {
        this.remindMethod = remindMethod;
    }

    public int getRemindMethod() {
        return remindMethod;
    }



    public String getAuther() {
        return auther;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public String getContent() {
        return content;
    }

    public String getBelong() {
        return belong;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }
}
