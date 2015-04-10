package com.example.yuwei.killexam;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.example.yuwei.killexam.map.TitleMapString;
import com.example.yuwei.killexam.serve.MyReceiver;
import com.example.yuwei.killexam.serve.RemindService;
import com.example.yuwei.killexam.taskFragments.CreateTaskFragment;
import com.example.yuwei.killexam.taskFragments.ChooseRemindTimeFragment;
import com.example.yuwei.killexam.taskFragments.EditTaskFragment;
import com.example.yuwei.killexam.taskFragments.EditableTaskFragment;
import com.example.yuwei.killexam.taskFragments.TaskListFragment;
import com.example.yuwei.killexam.tools.Task;
import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import android.support.v7.app.ActionBarDrawerToggle;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static final String ENTER_FRAGMENT = "enterFragment";
    public static final String TASK = "task";

    public Menu mMenu;
    public Fragment currentFragment;

    public Fragment targetShowingFragment;

    private DrawerFrameLayout drawer;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;

    static public TitleMapString mTitleMap = new TitleMapString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        initToolBar();

        initDrawer();
        initDrawerArrow();

        enterFragment();
    }

    @Override
    public void onStart(){
        super.onStart();
        if (!isServiceRunning(this, "RemindService")){
            RemindService.startRemindService(this.getApplicationContext());
            Log.i("MainActivity", "start remindService from MainActivity");
        }
    }

    private void enterFragment() {
        if (!hasEnterFragmentByIntent()) {
//          正常情况进入taskList
            mTitleMap.setTitle(TitleMapString.TASK_LIST);
            targetShowingFragment = getTargetShowingFragmentByTitle();
            replaceFragment(targetShowingFragment);
        }
    }

    private boolean hasEnterFragmentByIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
//          确认进入createTaskFragment的信息
//          当从选择belong的activity里面后退时，会进入这个activity，这时需要进入createFragment。
            if (bundle.getString(ENTER_FRAGMENT) != null && bundle.getSerializable(TASK) != null) {
                mTitleMap.setTitle(bundle.getString(ENTER_FRAGMENT));
                enterCreateTask((Task) bundle.getSerializable(TASK));
                return true;
            }
        }
        return false;
    }

    //  arrow animation
    private void initDrawerArrow() {

        drawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.app_name, R.string.app_name
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    private void initDrawer() {
        drawer = (DrawerFrameLayout) findViewById(R.id.drawer);
        drawer.setProfile(
                new DrawerProfile()
                        .setBackground(getResources().getDrawable(R.drawable.cat_background))
                        .setName("A cat")
                        .setDescription("looks for task~")
                        .setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
                            @Override
                            public void onClick(DrawerProfile drawerProfile) {
                            }
                        })
        );

        drawer.addItem(
                new DrawerItem()
                        .setTextPrimary(TitleMapString.TASK_LIST)
                        .setTextMode(DrawerItem.SINGLE_LINE)
                        .setImage(getResources().getDrawable(R.drawable.icon_task_list))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                setmTitleByDrawerClickedItem(position);
                                replaceFragmentFromDrawer();
                            }
                        })
        );

        drawer.addItem(
                new DrawerItem()
                        .setTextPrimary(TitleMapString.CREATE_TASK)
                        .setImage(getResources().getDrawable(R.drawable.icon_create))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                setmTitleByDrawerClickedItem(position);
                                replaceFragmentFromDrawer();
                            }
                        })
        );

        drawer.addItem(
                new DrawerItem()
                        .setTextPrimary(TitleMapString.RIMIND_TIME)
                        .setImage(getResources().getDrawable(R.drawable.icon_remind))
                        .setOnItemClickListener(
                                new DrawerItem.OnItemClickListener() {
                                    @Override
                                    public void onClick(DrawerItem drawerItem, int id, int position) {
                                        setmTitleByDrawerClickedItem(position);
                                        replaceFragmentFromDrawer();
                                    }
                                })
        );

    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.logo);
    }


    public void setmTitleByDrawerClickedItem(int position) {
        mTitleMap.setTitle(mTitleMap.get(position));
    }

    private void enterCreateTask(Task task) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CreateTaskFragment createTaskFragment = (CreateTaskFragment) getTargetShowingFragmentByTitle();

        createTaskFragment.setNewTask(task);

        fragmentManager.beginTransaction()
                .replace(R.id.container, createTaskFragment)
                .commit();

    }


    //  更换fragment
    private void replaceFragmentFromDrawer() {

        drawer.closeDrawer();

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(this);
                targetShowingFragment = getTargetShowingFragmentByTitle();
                replaceFragment(targetShowingFragment);
            }
        };
        handler.postDelayed(runnable, 320);
    }


    public void replaceFragment(Fragment targetShowingFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, targetShowingFragment);

        if (!targetShowingFragment.getClass().equals(TaskListFragment.class)){
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();


        TaskListFragment.isEditMode = false;
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }


    public Fragment getTargetShowingFragmentByTitle() {
        Fragment targetFragment = new TaskListFragment(this);

        if (mTitleMap.getTitle().equals(TitleMapString.TASK_LIST)) {
            targetFragment = new TaskListFragment(this);
        } else if (mTitleMap.getTitle().equals(TitleMapString.CREATE_TASK)) {
            targetFragment = new CreateTaskFragment(this);
        } else if (mTitleMap.getTitle().equals(TitleMapString.RIMIND_TIME)) {
            targetFragment = new ChooseRemindTimeFragment(this);
        }
        return targetFragment;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        if (currentFragment != null) {
            //给右上角的menu设置创建功能
            if (currentFragment.getClass() == CreateTaskFragment.class) {
                initNewMenu(R.menu.create_fragment);
                return true;
            } else if (currentFragment.getClass() == EditTaskFragment.class) {
                initNewMenu(R.menu.edit_fragment);
                return true;
            }
        }

        initNewMenu(R.menu.main);
        return true;
    }

    public void initNewMenu(int menuId) {
        if (mMenu != null) {

            mMenu.clear();
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(menuId, mMenu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_action_quit) {
            finish();
            return true;
        }
        if (id == R.id.menu_action_create) {
            ((EditableTaskFragment) currentFragment).createTask();
        }
        if (id == R.id.menu_action_delete) {
            ((EditTaskFragment) currentFragment).deleteTask();
        }

        return super.onOptionsItemSelected(item);
    }
    public static boolean isServiceRunning(Context mContext,String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);

        if (!(serviceList.size()>0)) {
            return false;
        }

        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
