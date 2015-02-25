package com.example.yuwei.killexam.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yuwei.killexam.tools.Task;

import java.util.ArrayList;

/**
 * Created by yuwei on 15/2/18.
 */
//TODO: 将业务逻辑和数据库剥离
public class MyDatabaseHelper extends SQLiteOpenHelper{


    private static String DATABASE_NAME;

    private static final String TASK_TABLE_NAME = "task";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String COLOR_TAG = "color_tag";

    private static final String FINISH_TIME = "finish_time";
    private static final String SPEND_HOURS = "spend_time_hours";
    private static final String SPEND_MINUTES = "spend_time_minutes";
    private static final String REMIND_METHOD = "remind_method";
    private static final String ATTRIBUTE = "attribute";
    private static final String HAS_BELONG = "has_belong";
    private static final String BELONG_NAME = "belong";
    private static final String HAS_FINISHED = "has_finished";

    private static final String CREATE_TASK_TABLE = "create table task (" +
            ID + " integer primary key autoincrement, " +
            NAME + " text, " +
            FINISH_TIME + " integer, " +
            SPEND_HOURS + " integer, " +
            SPEND_MINUTES + " integer, " +
            REMIND_METHOD + " text, " +
            ATTRIBUTE + " text, " +
            HAS_BELONG + " integer, " +
            BELONG_NAME + " text, " +
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

    private static SQLiteDatabase getDatabase(Context context){
        SQLiteOpenHelper databaseHelper = new MyDatabaseHelper(context, "task.db", null, 1);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        return database;
    }

    private static Cursor getTaskCursor(SQLiteDatabase database, String name){
        String where = NAME + "='" + name + "'";

        Cursor cursor = database.query(TASK_TABLE_NAME, null, where, null, null, null, null);
        return cursor;
    }


    private static ContentValues getTaskContentValues(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, task.getTaskName());
        contentValues.put(COLOR_TAG, task.getTagColor().getSelectedName());
        contentValues.put(FINISH_TIME, task.getFinishedTime().toString());
        contentValues.put(SPEND_HOURS, task.getSpendHours());
        contentValues.put(SPEND_MINUTES, task.getSpendMinutes());
        contentValues.put(REMIND_METHOD, task.getRemindMethod().getSelectedName());
        contentValues.put(ATTRIBUTE, task.getTaskAttribute().getSelectedName());

        if (task.isHasBelong()){
            contentValues.put(HAS_BELONG, 1);
            contentValues.put(BELONG_NAME, task.getBelongName());
        }
        else{
            contentValues.put(HAS_BELONG, 0);
            contentValues.put(BELONG_NAME, "NULL");
        }

        if (task.getHasFinished() == 0){
            contentValues.put(HAS_FINISHED, 0);
        }
        else{
            contentValues.put(HAS_FINISHED, 1);
        }
        return contentValues;
    }

    public static void writeNewTask(Context context, Task task){
        SQLiteDatabase database = getDatabase(context);
        ContentValues contentValues = getTaskContentValues(task);

        database.insert(TASK_TABLE_NAME, null, contentValues);
    }



//核查任务名是否存在
    public static boolean checkNameHasExist(Context context, String name){
        SQLiteDatabase database = getDatabase(context);
        Cursor cursor = getTaskCursor(database, name);
        if (cursor.getCount()>0){
            return true;
        }
        cursor.close();
        return false;
    }

//得到多有上一级任务的名字
    public static ArrayList<String> getBelongTasksNames(Context context, String belongTasksAttribute){
        SQLiteDatabase database = getDatabase(context);
        ArrayList<String> belongTasksNames = new ArrayList<>();

        String where = ATTRIBUTE + "='" + belongTasksAttribute + "'";

        Cursor cursor = database.query(TASK_TABLE_NAME, new String[]{NAME}, where, null, null, null, null);

        while (cursor.moveToNext()){
            String belongTaskName = cursor.getString(cursor.getColumnIndex(NAME));

            belongTasksNames.add(belongTaskName);
        }
        cursor.close();
        return belongTasksNames;
    }

//更新某个任务是否完成
    public static void updateIsTaskFinished(Context context, Task theTask, boolean isChecked){
        int isFinished = isChecked?1:0;

        theTask.setHasFinished(isFinished);

        SQLiteDatabase database = getDatabase(context);

        ContentValues contentValues = getTaskContentValues(theTask);

        String where = NAME + "='" + theTask.getTaskName() + "'";

        int suc = database.update(TASK_TABLE_NAME, contentValues, where, null);


    }

}
