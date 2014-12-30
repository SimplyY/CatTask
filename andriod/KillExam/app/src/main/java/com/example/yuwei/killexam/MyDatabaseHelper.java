package com.example.yuwei.killexam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yuwei on 2014/12/30.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_TASKS_TABLE = "create table tasks("
            + "id integer primary key autoincrement,"
            + "auther text"
            + "finish_time text"
            + "spend_time text"
            + "task_content text"
            + "task_belong text"
            + "task_remind_method"
            + "task_attribute integer";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASKS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
