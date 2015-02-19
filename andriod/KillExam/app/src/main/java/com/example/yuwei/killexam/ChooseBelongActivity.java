package com.example.yuwei.killexam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.logging.Logger;


public class ChooseBelongActivity extends ActionBarActivity {

    String newTaskName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent newTaskIntent = getIntent();
        Bundle newTaskBundle = newTaskIntent.getExtras();
        if (newTaskBundle != null){
            newTaskName = (String)newTaskBundle.get("taskName");
        }
        else{
            Log.e("danger", "chooseBelongActivity has no newTask");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_belong);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_belong, menu);
        return true;
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
