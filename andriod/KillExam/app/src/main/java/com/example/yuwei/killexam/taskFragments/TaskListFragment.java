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

import java.util.ArrayList;


public class TaskListFragment extends Fragment {

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
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_task_list, container, false);

        initTaskListView();

        return mView;
    }

    private void initTaskListView(){
        ArrayList<Task> taskArrayList = MyDatabaseHelper.getTaskArray();

        TaskListAdapter adapter = new TaskListAdapter(getActivity(), R.layout.task_item, taskArrayList);

        ListView taskListView = (ListView)this.getActivity().findViewById(R.id.taskListView);

        taskListView.setAdapter(adapter);
    }


}
