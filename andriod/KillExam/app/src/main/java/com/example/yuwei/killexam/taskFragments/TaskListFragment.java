package com.example.yuwei.killexam.taskFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.adapter.TaskListAdapter;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.map.HeaderTimeMapString;
import com.example.yuwei.killexam.map.TitleMapString;
import com.example.yuwei.killexam.tools.Task;
import com.example.yuwei.killexam.tools.TaskTree;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class TaskListFragment extends Fragment {

    public static TaskTree taskTree;

    private static MainActivity mMainActivity;

    public static StickyListHeadersListView taskListView;

    public static boolean isEditMode = false;

    static TaskListAdapter adapter;

    static ArrayList<Task> sortedTODOTaskArrayList;

    View mView;

    public TaskListFragment(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mMainActivity.currentFragment = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_task_list, container, false);


        initFloatButton();
        mMainActivity.initNewMenu(R.menu.main);

        taskListView = (StickyListHeadersListView)mView.findViewById(R.id.taskListView);

        initTaskList();

        if (sortedTODOTaskArrayList != null && sortedTODOTaskArrayList.size() != 0){
            initActionBar();
        }

        return mView;
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i("taskListFragment", "onStart");
        InputMethodManager inputMethodManager = (InputMethodManager)mMainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(taskListView.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

    }
//  更新taskList的数据（重新初始化，重新设置adapter），
//  调用方法更新ui线程，
//  设置之前选中的位置
    public static void renewListView(){
        initTaskList();

        int position = taskListView.getFirstVisiblePosition();

        adapter.notifyDataSetChanged();

        taskListView.setSelection(position);

        initActionBar();

    }

//  通过读取数据库获取最新的taskArray，来初始化taskTree
    public static void initTaskList(){
        ArrayList<Task> taskArrayList = MyDatabaseHelper.getAllTaskArray(mMainActivity);
        if (!taskArrayList.isEmpty()){
            taskTree = TaskTree.newInstance(taskArrayList, mMainActivity);

            sortedTODOTaskArrayList = TaskTree.getSortedTodoTaskArrayList();

            adapter = new TaskListAdapter(mMainActivity, R.layout.task_item, sortedTODOTaskArrayList);
            taskListView.setAdapter(adapter);

        }
    }


    private static void initActionBar(){
        if(!sortedTODOTaskArrayList.isEmpty()){
            HeaderTimeMapString headerTimeMapString = new HeaderTimeMapString();
            int imageId = headerTimeMapString.getImageId(sortedTODOTaskArrayList.get(0));
            mMainActivity.getSupportActionBar().setBackgroundDrawable(mMainActivity.getResources().getDrawable(imageId));

        }
    }


    private void initFloatButton(){

        final FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu) mView.findViewById(R.id.fa_menu);

        final String EXIT = "退出";

        final FloatingActionButton exitButton = (FloatingActionButton) mView.findViewById(R.id.exit_button);
        exitButton.setTitle(EXIT);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.finish();
            }
        });


        final String EDIT_MODE = "编辑";
        final FloatingActionButton editModeButton = (FloatingActionButton) mView.findViewById(R.id.edit_mode_button);
        editModeButton.setTitle(EDIT_MODE);
        editModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditMode = true;
                Toast.makeText(mMainActivity, "点击任务将编辑任务",Toast.LENGTH_SHORT).show();
                floatingActionsMenu.collapse();
            }
        });

        final String CREATE = "创建";

        final FloatingActionButton createTaskButton = (FloatingActionButton) mView.findViewById(R.id.create_task_button);
        createTaskButton.setTitle(CREATE);

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mTitleMap.setTitle(TitleMapString.CREATE_TASK);
                Fragment targetShowingFragment = mMainActivity.getTargetShowingFragmentByTitle();
                floatingActionsMenu.collapse();
                mMainActivity.replaceFragment(targetShowingFragment);
            }
        });

    }
}
