package com.example.yuwei.killexam;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.example.yuwei.killexam.map.TitleMapString;
import com.example.yuwei.killexam.taskFragments.CreateTaskFragment;
import com.example.yuwei.killexam.taskFragments.DevelopmentListFragment;
import com.example.yuwei.killexam.taskFragments.TaskListFragment;
import com.example.yuwei.killexam.tools.Task;


public class MainActivity extends ActionBarActivity
        implements com.example.yuwei.killexam.NavigationDrawerFragment.NavigationDrawerCallbacks{

    private NavigationDrawerFragment mNavigationDrawerFragment;

    static public TitleMapString mTitleMap = new TitleMapString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitleMap = new TitleMapString();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        restoreActionBar();
        getBelongInCreateTask();
    }

//  当从选择belong的activity里面后退时，会进入这个acitivity，这时需要进入createFragment。
    private void getBelongInCreateTask(){
        Intent intent = getIntent();
        String enterFragment = intent.getStringExtra("enterFragment");

        if (enterFragment != null) {
            switch (enterFragment) {
                case "CreateTaskFragment":
                    mTitleMap.setTitle(mTitleMap.CREATE_TASK);
                    enterCreateTask(intent);
                    break;
            }
        }
    }
    private void enterCreateTask(Intent intent){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment targetFragment = getTargetFragment();

        getNewTask((CreateTaskFragment)targetFragment, intent);
        fragmentManager.beginTransaction()
                .replace(R.id.container, targetFragment)
                .commit();
    }
    private void getNewTask(CreateTaskFragment fragment, Intent intent){
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            Task task = (Task) bundle.get("task");
            fragment.setNewTask(task);
        }
    }

//侧滑栏中的选项被选中时调用
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        setmTitle(position);
        replaceFragment();
    }
    public void setmTitle(int position) {
        mTitleMap.setTitle(mTitleMap.get(position));
    }
//  更换fragment
    public void replaceFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment targetFragment = getTargetFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.container, targetFragment)
                .commit();
    }
    private Fragment getTargetFragment(){
        Fragment targetFragment = new TaskListFragment(this);

        if (mTitleMap.getTitle().equals(mTitleMap.CREATE_TASK)){
            targetFragment = new CreateTaskFragment(this);
        }
        else if (mTitleMap.getTitle().equals(mTitleMap.DEVELOP_LIST)){
            targetFragment = new DevelopmentListFragment(this);
        }
        return targetFragment;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitleMap.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
