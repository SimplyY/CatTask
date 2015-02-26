package com.example.yuwei.killexam.taskFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.adapter.TaskListAdapter;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.tools.Task;
import com.example.yuwei.killexam.tools.TaskTree;

import java.util.ArrayList;


public class TaskListFragment extends Fragment {

    private TaskTree taskTrees;

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        return fragment;
    }

    View mView;

    public TaskListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_task_list, container, false);

        initTaskListView();

        return mView;
    }

    private void initTaskListView(){
        ArrayList<Task> taskArrayList = MyDatabaseHelper.getTaskArray(getActivity());
        taskTrees = TaskTree.newInstance(taskArrayList);

        taskTrees.sort();
        if (taskArrayList.isEmpty() == false){
            TaskListAdapter adapter = new TaskListAdapter(getActivity().getApplicationContext(), R.layout.task_item, taskArrayList);

            ListView taskListView = (ListView)mView.findViewById(R.id.taskListView);

            taskListView.setAdapter(adapter);
        }
    }
}
