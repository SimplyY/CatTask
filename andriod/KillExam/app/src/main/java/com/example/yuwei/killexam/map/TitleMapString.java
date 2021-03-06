package com.example.yuwei.killexam.map;

import java.util.HashMap;

/**
 * Created by yuwei on 15/3/1.
 */
public class TitleMapString extends HashMap<Integer, String>{
    public static final String TASK_LIST = "任务清单";
    public static final String CREATE_TASK = "创建任务";
    public static final String RIMIND_TIME = "提醒时段";

    private String title;

    public TitleMapString(){
        put(0, TASK_LIST);
        put(1, CREATE_TASK);
        put(2, RIMIND_TIME);
        title = TASK_LIST;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
