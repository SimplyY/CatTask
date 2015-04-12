package com.example.yuwei.killexam.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.map.SpinnerValue;
import com.example.yuwei.killexam.tools.MyTime;
import com.example.yuwei.killexam.tools.Task;

import java.util.ArrayList;

/**
 * Created by yuwei on 15/2/18.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper{


    private static int VERSION = 3;

    private static String DATABASE_NAME = "task.db";

    private static final String TASK_TABLE_NAME = "task";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String COLOR_TAG = "color_tag";

    private static final String FINISH_DATE = "finish_time";
    private static final String SPEND_HOURS = "spend_time_hours";
    private static final String SPEND_MINUTES = "spend_time_minutes";
    private static final String REMIND_METHOD = "remind_method";
    private static final String ATTRIBUTE = "attribute";
    private static final String HAS_BELONG = "has_belong";
    private static final String BELONG_NAME = "belong";
    private static final String HAS_FINISHED = "has_finished";

    private static final String RECENTEST_DAY_REMINDED = "recentest_day_reminded";

    private static final String CREATE_TASK_TABLE = "create table " + TASK_TABLE_NAME + " ( " +
            ID + " integer primary key autoincrement, " +
            NAME + " text, " +
            COLOR_TAG + " text, " +
            FINISH_DATE + " integer, " +
            SPEND_HOURS + " integer, " +
            SPEND_MINUTES + " integer, " +
            REMIND_METHOD + " text, " +
            ATTRIBUTE + " text, " +
            HAS_BELONG + " integer, " +
            BELONG_NAME + " text, " +
            RECENTEST_DAY_REMINDED + " integer, "+
            HAS_FINISHED + " integer);";


    private static final String REMIND_TIME_TABLE_NAME = "remind_time";

    private static final String BEGIN_REMIND_HOURS = "begin_remind_hours";
    private static final String BEGIN_REMIND_MINUTES = "begin_remind_minutes";
    private static final String END_REMIND_HOURS = "end_remind_hours";
    private static final String END_REMIND_MINUTES = "end_remind_minutes";

    private static final String CREATE_REMIND_TIME_TABLE = "create table " + REMIND_TIME_TABLE_NAME +" ( " +
            ID + " integer, " +
            BEGIN_REMIND_HOURS + " integer, " +
            BEGIN_REMIND_MINUTES + " integer, " +
            END_REMIND_HOURS + " integer, " +
            END_REMIND_MINUTES + " integer);";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        DATABASE_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK_TABLE);

        db.execSQL(CREATE_REMIND_TIME_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static SQLiteDatabase getDatabase(Context context){
        SQLiteOpenHelper databaseHelper = new MyDatabaseHelper(context, DATABASE_NAME, null, VERSION);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        return database;
    }

    private static Cursor getTaskCursor(SQLiteDatabase database, String name){
        String where = NAME + "='" + name + "'";

        Cursor cursor = database.query(TASK_TABLE_NAME, null, where, null, null, null, null);
        return cursor;
    }

    private static String getStringFromCursor(Cursor cursor, String columnName){
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private static int getIntFromCursor(Cursor cursor, String columnName){
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    private static Task getCompleteTask(Cursor cursor, Context context){
        Task task = new Task();

        String attribute = getStringFromCursor(cursor, ATTRIBUTE);
        SpinnerValue taskAttribute = SpinnerValue.initSpinnerValue(R.array.task_attribute_array, context.getResources());
        taskAttribute.setSelectedName(attribute);
        task.setTaskAttribute(taskAttribute);

        String name = getStringFromCursor(cursor, NAME);
        task.setTaskName(name);

        String colorTag = getStringFromCursor(cursor, COLOR_TAG);
        SpinnerValue tagColor = SpinnerValue.initSpinnerValue(R.array.tag_color_array, context.getResources());
        tagColor.setSelectedName(colorTag);
        task.setTagColor(tagColor);

        String finishDateString = getStringFromCursor(cursor, FINISH_DATE);
        MyDate finishDate =  new MyDate(finishDateString);
        task.setFinishedDate(finishDate);

        MyDate recentestDayReminded = new MyDate(getStringFromCursor(cursor, RECENTEST_DAY_REMINDED));
        task.setRecentestDayReminded(recentestDayReminded);

        int spendHours = getIntFromCursor(cursor, SPEND_HOURS);
        int spendMinutes = getIntFromCursor(cursor, SPEND_MINUTES);
        task.setSpendTime(spendHours, spendMinutes);

        String remindMethodString = getStringFromCursor(cursor, REMIND_METHOD);
        SpinnerValue remindMethod = SpinnerValue.initSpinnerValue(R.array.task_remind_method_array, context.getResources());
        remindMethod.setSelectedName(remindMethodString);
        task.setRemindMethod(remindMethod);

        int hasBelong = getIntFromCursor(cursor, HAS_BELONG);
        task.setHasBelong(hasBelong==1);

        String belongName = getStringFromCursor(cursor, BELONG_NAME);
        task.setBelongName(hasBelong==1 ? belongName:null);

        int hasFinished = getIntFromCursor(cursor, HAS_FINISHED);
        task.setHasFinished(hasFinished);

        return task;

    }


    private static ContentValues getTaskContentValues(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ATTRIBUTE, task.getTaskAttribute().getSelectedName());
        contentValues.put(NAME, task.getTaskName());
        contentValues.put(COLOR_TAG, task.getTagColor().getSelectedName());
        contentValues.put(FINISH_DATE, task.getFinishedDate().toString());
        contentValues.put(RECENTEST_DAY_REMINDED,task.getRecentestDayReminded().toString());
        contentValues.put(SPEND_HOURS, task.getSpendHours());
        contentValues.put(SPEND_MINUTES, task.getSpendMinutes());
        contentValues.put(REMIND_METHOD, task.getRemindMethod().getSelectedName());


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
        database.close();
    }

//  核查任务名是否存在
    public static boolean checkNameHasExist(Context context, String name){
        SQLiteDatabase database = getDatabase(context);
        Cursor cursor = getTaskCursor(database, name);
        if (cursor.getCount()>0){
            return true;
        }
        cursor.close();
        database.close();
        return false;
    }

//  得到多有上一级任务
    public static ArrayList<Task> getBelongTasks(Context context, String belongTasksAttribute){
        SQLiteDatabase database = getDatabase(context);
        ArrayList<Task> belongTasks = new ArrayList<>();

        String where = ATTRIBUTE + "='" + belongTasksAttribute + "'";

        Cursor cursor = database.query(TASK_TABLE_NAME, null, where, null, null, null, null);

        while (cursor.moveToNext()){
            Task belongTask = getCompleteTask(cursor, context);

            belongTasks.add(belongTask);
        }
        cursor.close();
        database.close();
        return belongTasks;
    }

//  取出所有的task
    public static ArrayList<Task> getAllTaskArray(Context context){
        SQLiteDatabase database = getDatabase(context);
        ArrayList<Task> taskArrayList = new ArrayList<>();

        Cursor cursor = database.query(TASK_TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()){
            Task theTask = getCompleteTask(cursor, context);
            taskArrayList.add(theTask);
        }
        cursor.close();
        database.close();
        return taskArrayList;
    }



//  更新任务
    public static void updateTask(Context context, Task theTask){
        SQLiteDatabase database = getDatabase(context);

        ContentValues contentValues = getTaskContentValues(theTask);

        String where = NAME + "='" + theTask.getTaskName() + "'";

        database.update(TASK_TABLE_NAME, contentValues, where, null);
        database.close();
    }

    //  更新任务
    public static void updateTask(Context context, Task theTask, String oldTaskName){
        SQLiteDatabase database = getDatabase(context);

        ContentValues contentValues = getTaskContentValues(theTask);

        String where = NAME + "='" + oldTaskName + "'";

        database.update(TASK_TABLE_NAME, contentValues, where, null);
        database.close();
    }

    public static void deleteTask(Context context, Task task){
        SQLiteDatabase database = getDatabase(context);

        String where = NAME + "='" + task.getTaskName() + "'";
        database.delete(TASK_TABLE_NAME, where, null);
        database.close();
    }


    public static void writeRemindTime(Context context, MyTime beginTime, MyTime endTime){
        SQLiteDatabase database = getDatabase(context);

        String where = ID + "= 1";
        database.delete(REMIND_TIME_TABLE_NAME, where, null);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, 1);
        contentValues.put(BEGIN_REMIND_HOURS, beginTime.hours);
        contentValues.put(BEGIN_REMIND_MINUTES, beginTime.minutes);
        contentValues.put(END_REMIND_HOURS, endTime.hours);
        contentValues.put(END_REMIND_MINUTES, endTime.minutes);

        database.insert(REMIND_TIME_TABLE_NAME, null, contentValues);
        database.close();
    }

    public static void getRemindTime(Context context, MyTime beginTime, MyTime endTime){
        SQLiteDatabase database = getDatabase(context);

        String where = ID + "= 1";
        Cursor cursor = database.query(REMIND_TIME_TABLE_NAME, null, where, null, null, null, null);

        if (cursor.moveToNext()){


            beginTime.hours = cursor.getInt(cursor.getColumnIndex(BEGIN_REMIND_HOURS));
            beginTime.minutes = cursor.getInt(cursor.getColumnIndex(BEGIN_REMIND_MINUTES));

            endTime.hours = cursor.getInt(cursor.getColumnIndex(END_REMIND_HOURS));
            endTime.minutes = cursor.getInt(cursor.getColumnIndex(END_REMIND_MINUTES));
        }
        database.close();
    }

}
