package com.example.yuwei.killexam.map;

import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.tools.Task;

import java.util.HashMap;

/**
 * Created by yuwei on 15/2/28.
 */
public class HeaderTimeMapString extends HashMap<Integer, String>{
    private static final String OVERDUE = "过期";
    private static final String TODAY = "今天";
    private static final String A_WEEK = "一周内";
    private static final String FUTURE = "将来";


    private HashMap<Integer, Integer> imageId = new HashMap<>();

    public HeaderTimeMapString(){
        put(0, OVERDUE);
        put(1, TODAY);
        put(2, A_WEEK);
        put(3, FUTURE);

        imageId.put(0, R.drawable.header_overdue_red);
        imageId.put(1, R.drawable.header_today_silver);
        imageId.put(2, R.drawable.header_withinaweek_purple);
        imageId.put(3, R.drawable.header_future_blue);
    }

    public String getValue(Task task){

        return get(getKey(task));

    }

    public int getKey(Task task){
        MyDate headerDate = task.getHeaderDate();
        MyDate current = new MyDate();
        if (headerDate.isBefore(current)){
            return 0;
        }

        else if (headerDate.equals(current)){
            return 1;
        }
        else if (!current.addDay(6).isBefore(headerDate)){
            return 2;
        }
        else {
            return 3;
        }
    }

    public int getImageId(Task task){
        return imageId.get(getKey(task));
    }

}
