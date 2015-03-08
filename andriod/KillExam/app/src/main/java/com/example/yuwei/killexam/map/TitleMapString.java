package com.example.yuwei.killexam.map;

import com.google.android.gms.gcm.Task;

import java.util.HashMap;

/**
 * Created by yuwei on 15/3/1.
 */
public class TitleMapString extends HashMap<Integer, String>{
    public final String TASK_LIST = "任务清单";
    public final String CREATE_TASK = "创建任务";
    public final String FINISHED_LIST = "完成清单";

    private String title;

    public TitleMapString(){
        put(0, TASK_LIST);
        put(1, CREATE_TASK);
        put(2, FINISHED_LIST);
        title = TASK_LIST;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
