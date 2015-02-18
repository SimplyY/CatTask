package com.example.yuwei.killexam.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yuwei.killexam.tools.Task;

/**
 * Created by yuwei on 15/2/18.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper{


    private static String DATABASE_NAME;

    private static final String TABLE_NAME = "task";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String FINISH_TIME = "finish_time";
    private static final String SPEND_HOURS = "spend_time_hours";
    private static final String SPEND_MINUTES = "spend_time_minutes";
    private static final String CONTEXT = "context";
    private static final String REMIND_METHOD = "remind_method";
    private static final String ATTRIBUTE = "attribute";
    private static final String HAS_BELONG = "has_belong";
    private static final String BELONG = "belong";
    private static final String HAS_FINISHED = "has_finished";


    private static final String CREATE_TASK_TABLE = "create table task (" +
            ID + " integer primary key autoincrement, " +
            NAME + " text, " +
            FINISH_TIME + " integer, " +
            SPEND_HOURS + " integer, " +
            SPEND_MINUTES + " integer, " +
            CONTEXT + " text, " +
            REMIND_METHOD + " text, " +
            ATTRIBUTE + " text, " +
            HAS_BELONG + " integer, " +
            BELONG + " text, " +
            HAS_FINISHED + " integer);";


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        DATABASE_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static void writeNewTask(Context context, Task task){
        SQLiteOpenHelper databaseHelper = new MyDatabaseHelper(context,"task.db", null, 1);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        insertNewTask(database, task);
    }

    private static void insertNewTask(SQLiteDatabase database, Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, task.getTaskName());
        contentValues.put(FINISH_TIME, task.getFinishedTime().toString());
        contentValues.put(SPEND_HOURS, task.getSpendHours());
        contentValues.put(SPEND_MINUTES, task.getSpendMinutes());
        contentValues.put(REMIND_METHOD, task.getRemindMethod());
        contentValues.put(CONTEXT, task.getTaskContext());
        contentValues.put(REMIND_METHOD, task.getRemindMethod());
        contentValues.put(ATTRIBUTE, task.getTaskAttribute());

        if (task.isHasBelong()){
            contentValues.put(HAS_BELONG, 1);
            contentValues.put(BELONG, task.getBelong().toString());
        }
        else{
            contentValues.put(HAS_BELONG, 0);
            contentValues.put(BELONG, "NULL");
        }
        contentValues.put(HAS_FINISHED, 0);

        database.insert(TABLE_NAME, null, contentValues);


    }

}
