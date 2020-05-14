package com.ve.todotasksapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="everyDayTask.db";
    public static final int CURRENT_DB_VERSION=1;
    public static final String TABLE_NAME="task";
    public static final String COLUMN1="task_name";
    public static final String COLUMN2="task_time";
    public static final String COLUMN3="task_likablity";
    public static final String COLUMN4="task_id";
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, CURRENT_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"("+COLUMN4+ " integer primary key,"+COLUMN1+" text,"+COLUMN2+" text,"+COLUMN3+" integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    boolean insertTask(Task t)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN1,t.getName());
        cv.put(COLUMN2,t.getTime());
        cv.put(COLUMN3,t.getTasklikeablity());
        cv.put(COLUMN4,t.getTaskId());
        long val=db.insert(TABLE_NAME,null,cv);
        if(val!=-1)
            return true;
        return false;
    }
    ArrayList<Task> getAllTasks()
    {
        ArrayList<Task> taskList=new ArrayList<Task>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor taskCursor=db.rawQuery("select * from "+TABLE_NAME,null);
        while(taskCursor.moveToNext())
        {
            Task newTask=new Task();
            newTask.setName(taskCursor.getString(taskCursor.getColumnIndex(COLUMN1)));
            newTask.setTime(taskCursor.getString(taskCursor.getColumnIndex(COLUMN2)));
            newTask.setTasklikeablity(taskCursor.getInt(taskCursor.getColumnIndex(COLUMN3)));
            newTask.setTaskId(taskCursor.getInt(taskCursor.getColumnIndex(COLUMN4)));
            taskList.add(newTask);
        }
        return taskList;
    }
    boolean deleteTask(int taskId)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        int val=db.delete(TABLE_NAME,COLUMN4+"= ?",new String[]{Integer.toString(taskId)});
        if(val!=-1)
            return true;
        return false;
    }
}
