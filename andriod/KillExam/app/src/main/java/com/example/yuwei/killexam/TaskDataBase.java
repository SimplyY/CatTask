package com.example.yuwei.killexam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yuwei on 15/2/18.
 */
public class TaskDataBase extends SQLiteOpenHelper{

    private static final String CREATE_TASK_TABLE = "create task table (" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "";


    public TaskDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
