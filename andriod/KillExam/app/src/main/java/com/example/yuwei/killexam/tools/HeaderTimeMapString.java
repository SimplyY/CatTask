package com.example.yuwei.killexam.tools;

import android.content.Intent;

import java.util.HashMap;

/**
 * Created by yuwei on 15/2/28.
 */
public class HeaderTimeMapString extends HashMap<Integer, String>{
    private final String OVERDUE = "过期";
    private final String TODAY = "今天";
    private final String RECENTLY_THREE_DAY = "最近三天";
    private final String A_WEEK = "一周内";
    private final String FUTURE = "将来";

    public HeaderTimeMapString(){
        put(0, OVERDUE);
        put(1, TODAY);
        put(2, RECENTLY_THREE_DAY);
        put(3, A_WEEK);
        put(4, FUTURE);
    }

    public String getValue(Task task){

        return get(getKey(task));

    }

    public int getKey(Task task){
        MyDate myDate = task.getFinishedDate();
        MyDate current = new MyDate();
        if (myDate.isBefore(current)){
            return 0;
        }
        else if (myDate.equals(current)){
            return 1;
        }
        else if (current.isBefore(myDate, 3)){
            return 2;
        }
        else if (current.isBefore(myDate, 7)){
            return 3;
        }
        else{
            return 4;
        }
    }

}
