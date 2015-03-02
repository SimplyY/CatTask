package com.example.yuwei.killexam.taskFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.adapter.TaskListAdapter;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.tools.Task;
import com.example.yuwei.killexam.tools.TaskTree;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class TaskListFragment extends Fragment {

    static public TaskTree taskTree;

    static private MainActivity mMainActivity;

    static StickyListHeadersListView taskListView;
    static TaskListAdapter adapter;

    View mView;

    public TaskListFragment(){

    }

    public TaskListFragment(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_task_list, container, false);
        taskListView = (StickyListHeadersListView)mView.findViewById(R.id.taskListView);

        renewTaskList();

        return mView;
    }

//  通过读取数据库获取最新的taskArray，来初始化taskTree
    public static void renewTaskList(){
        ArrayList<Task> taskArrayList = MyDatabaseHelper.getTaskArray(mMainActivity);
//TODO: 检查taskTree的hasFinished的属性
        if (taskArrayList.isEmpty() == false){
            initSortedTaskTreeAdapter(taskArrayList);
            taskListView.setAdapter(adapter);
        }
    }

    private static void initSortedTaskTreeAdapter(ArrayList<Task> taskArrayList){
        taskTree = TaskTree.newInstance(taskArrayList);

        ArrayList<Task> sortedTaskArrayList = TaskTree.getSortedTaskArrayList();
        adapter = new TaskListAdapter(mMainActivity, R.layout.task_item, sortedTaskArrayList);
    }
}
