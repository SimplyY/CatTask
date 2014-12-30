package com.example.yuwei.killexam;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks
        ,CreateTaskFragment.OnFragmentInteractionListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private Fragment targetFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private MyDatabaseHelper tasksDataBase;
    private Task newTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createTasksDatabase();
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void createTasksDatabase(){
        tasksDataBase = new MyDatabaseHelper(this, "MyTasks.db", null, 1);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment targetFragment = getTargetFragment(position);
        fragmentManager.beginTransaction()
                .replace(R.id.container, targetFragment)
                .commit();
    }

    private Fragment getTargetFragment(int position){
        int sectionNumber = position + 1;
        targetFragment = TaskListFragment.newInstance(sectionNumber);
        if(sectionNumber == 1){
            targetFragment = CreateTaskFragment.newInstance(sectionNumber);
        }
        else if(sectionNumber == 2){
            targetFragment = TaskListFragment.newInstance(sectionNumber);
        }
        else if(sectionNumber == 3){
            targetFragment = DevelopmentListFragment.newInstance(sectionNumber);
        }
        return targetFragment;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    private void onClick_CreateTask(View view){
        if(isTaskComplete()){
            addTaskToDatebase();
        }else{
            Toast.makeText(this,"任务信息不完整，请填写完整", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getTask(Task newTask){
        this.newTask = newTask;
    }

    private boolean isTaskComplete(){
        CreateTaskFragment createTaskFragment = (CreateTaskFragment) targetFragment;
        createTaskFragment.createTask(newTask);

        return true;

    }


    private void addTaskToDatebase(){
        SQLiteDatabase db = tasksDataBase.getWritableDatabase();

        ContentValues newTaskValues = new ContentValues();

        newTaskValues.put("auther", newTask.getAuther());
        newTaskValues.put("finish_time", newTask.getFinishTime());
        newTaskValues.put("spend_time", newTask.getSpendTime());
        newTaskValues.put("task_content", newTask.getContent());
        newTaskValues.put("task_belong", newTask.getBelong());
        newTaskValues.put("task_remind_method", newTask.getRemindMethod());
        newTaskValues.put("task_attribute",newTask.getAttribute());

        db.insert("tasks", null, newTaskValues);

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
