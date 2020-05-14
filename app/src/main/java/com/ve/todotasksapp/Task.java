package com.ve.todotasksapp;

import java.io.Serializable;

public class Task implements Serializable {
    int taskId;
    String Name;
    String time;
    int Tasklikeablity;

    public Task(int id, String notificationText, String time, int likablity) {
        this.taskId=id;
        this.Name=notificationText;
        this.time=time;
        this.Tasklikeablity=likablity;
    }

    public Task() {
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTasklikeablity() {
        return Tasklikeablity;
    }

    public void setTasklikeablity(int tasklikeablity) {
        Tasklikeablity = tasklikeablity;
    }
}
