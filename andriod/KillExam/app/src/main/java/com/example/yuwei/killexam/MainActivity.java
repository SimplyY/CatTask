package com.example.yuwei.killexam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.example.yuwei.killexam.map.TitleMapString;
import com.example.yuwei.killexam.taskFragments.CreateTaskFragment;
import com.example.yuwei.killexam.taskFragments.DevelopmentListFragment;
import com.example.yuwei.killexam.taskFragments.TaskListFragment;
import com.example.yuwei.killexam.tools.Task;
import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import android.support.v7.app.ActionBarDrawerToggle;


public class MainActivity extends ActionBarActivity {

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

        replaceFragment();
        getBelongInCreateTask();

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
                        .setAvatar(getResources().getDrawable(R.drawable.cat_head_picture))
                        .setBackground(getResources().getDrawable(R.drawable.cat_background))
                        .setName("a cat")
                        .setDescription("look for task~")
                        .setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
                            @Override
                            public void onClick(DrawerProfile drawerProfile) {
                            }
                        })
        );

        drawer.addItem(
                new DrawerItem()
                        .setTextPrimary(mTitleMap.TASK_LIST)

                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                setmTitle(position);
                                replaceFragment();
                            }
                        })
        );

        drawer.addItem(
                new DrawerItem()
                        .setTextPrimary(mTitleMap.CREATE_TASK)
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                setmTitle(position);
                                replaceFragment();
                            }
                        })
        );

        drawer.addItem(
                new DrawerItem()
                        .setTextPrimary(mTitleMap.FINISHED_LIST)
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                setmTitle(position);
                                replaceFragment();
                            }
                        })
        );

    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.logo);
    }


    public void setmTitle(int position) {
        mTitleMap.setTitle(mTitleMap.get(position));
    }

    //  当从选择belong的activity里面后退时，会进入这个activity，这时需要进入createFragment。
    private void getBelongInCreateTask() {
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

    private void enterCreateTask(Intent intent) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment targetFragment = getTargetFragment();

        getNewTask((CreateTaskFragment) targetFragment, intent);
        fragmentManager.beginTransaction()
                .replace(R.id.container, targetFragment)
                .commit();
    }

    private void getNewTask(CreateTaskFragment fragment, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            Task task = (Task) bundle.get("task");
            fragment.setNewTask(task);
        }
    }


    //  更换fragment
    public void replaceFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment targetFragment = getTargetFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.container, targetFragment)
                .commit();
        initDrawerArrow();


    }

    private Fragment getTargetFragment() {
        Fragment targetFragment = new TaskListFragment(this);

        if (mTitleMap.getTitle().equals(mTitleMap.TASK_LIST)) {
            targetFragment = new TaskListFragment(this);
        }
        if (mTitleMap.getTitle().equals(mTitleMap.CREATE_TASK)) {
            targetFragment = new CreateTaskFragment(this);
        } else if (mTitleMap.getTitle().equals(mTitleMap.FINISHED_LIST)) {
            targetFragment = new DevelopmentListFragment(this);
        }
        return targetFragment;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
