package com.ve.todotasksapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*this class creates alarm for every task*/
public class TaskAlarmCreator {
    private static final int TASK_NOTIFICATION =12 ;
    Task currentTask;
    Context context;
    TaskAlarmCreator(Context context,Task task)
    {
        this.context=context;
        this.currentTask=task;
    }
    TaskAlarmCreator(Context context)
    {
        this.context=context;
    }

    void createAlarm()
    {
        long desTime=0;
        try {
            desTime=extractTime(currentTask.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*use broadcast reciever to recieve alarm and create a notification with task name*/
        AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context,AlarmReciever.class);
        intent.putExtra("task_name",currentTask.getName());
        intent.putExtra("task_time",currentTask.getTime());
        intent.putExtra("task_unique_id", currentTask.getTaskId());
        intent.putExtra("task_likablity",currentTask.getTasklikeablity());
        PendingIntent pi=PendingIntent.getBroadcast(context,currentTask.getTaskId(),intent,0);
        alarm.setExact(AlarmManager.RTC_WAKEUP,desTime,pi);
        /*creates alarm that notifies at specifiedd hour and minute*/
    }

    /*extract hour and minute from given time to setAlarm*/
    long extractTime(String time) throws ParseException {
        Date dest=new SimpleDateFormat("HH:mm").parse(time);
        Calendar currentTime=Calendar.getInstance();
        Calendar tempTime=Calendar.getInstance();
        tempTime.setTime(dest);
        Calendar destTime=Calendar.getInstance();
        destTime.set(Calendar.HOUR_OF_DAY,tempTime.get(Calendar.HOUR_OF_DAY));
        destTime.set(Calendar.MINUTE,tempTime.get(Calendar.MINUTE));
        destTime.set(Calendar.SECOND,0);
        if(destTime.before(currentTime))                    //if hour is passed,say current time is 14:00 and
            destTime.add(Calendar.DATE,1);          //task is at 13:00  create alarm for nextDay
        return destTime.getTimeInMillis();
        //return time in millis as this time is used by alarm manager
    }
    static void cancelAlarm(Context context,int id)
    {
        AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context,AlarmReciever.class);
        PendingIntent pi=PendingIntent.getBroadcast(context,id,intent,PendingIntent.FLAG_NO_CREATE);
        if(pi!=null)
        {
            alarm.cancel(pi);
        }
    }
    void createNextDayAlarm()
    {
        long desTime=0;
        try {
            desTime=extractTimeForNextDay(currentTask.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*use broadcast reciever to recieve alarm and create a notification with task name*/
        AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context,AlarmReciever.class);
        intent.putExtra("task_name",currentTask.getName());
        intent.putExtra("task_time",currentTask.getTime());
        intent.putExtra("task_unique_id", currentTask.getTaskId());
        intent.putExtra("task_likablity",currentTask.getTasklikeablity());
        PendingIntent pi=PendingIntent.getBroadcast(context,currentTask.getTaskId(),intent,0);
        alarm.setExact(AlarmManager.RTC_WAKEUP,desTime,pi);
        /*creates alarm that notifies at specifiedd hour and minute*/
    }

    private long extractTimeForNextDay(String time) throws ParseException {
        Date dest=new SimpleDateFormat("HH:mm").parse(time);
        Calendar tempTime=Calendar.getInstance();
        tempTime.setTime(dest);
        Calendar destTime=Calendar.getInstance();
        destTime.set(Calendar.HOUR_OF_DAY,tempTime.get(Calendar.HOUR_OF_DAY));
        destTime.set(Calendar.MINUTE,tempTime.get(Calendar.MINUTE));
        destTime.set(Calendar.SECOND,0);
        destTime.add(Calendar.DATE,1);
        return destTime.getTimeInMillis();

    }


}
