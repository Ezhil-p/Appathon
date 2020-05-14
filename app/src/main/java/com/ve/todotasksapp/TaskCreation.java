package com.ve.todotasksapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TaskCreation extends AppCompatActivity implements View.OnClickListener {

    private static final String TASK_PREFERENCE = "TASK_UNIQUE_ID";
    EditText taskNameTxt;
    SeekBar likablityMeasure;
    ImageView taskTimeImg;
    Task newTask;
    TextView taskTimeTxt;
    Button taskScheduleBtn;
    Toolbar toolbar;
    DBHelper helper;
    ProgressBar pb;
    TextView progressText;
    TaskAlarmCreator creator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);
        wireUpListeners();
        if(savedInstanceState!=null)
            taskTimeTxt.setText(savedInstanceState.getString("selected_time"));
        Task taskToUpdate=(Task)getIntent().getSerializableExtra("task");
        if(taskToUpdate!=null) {
            newTask = taskToUpdate;
            fillWidgetsWithData();
        }
        else
            newTask = new Task();
            creator=new TaskAlarmCreator(this,newTask);
            helper=new DBHelper(this);

    }

    private void fillWidgetsWithData() {
        taskNameTxt.setText(newTask.getName());
        taskTimeTxt.setText(newTask.getTime());
        likablityMeasure.setProgress(newTask.getTasklikeablity());
    }

    private void wireUpListeners() {
        taskNameTxt=(EditText)findViewById(R.id.task_name);
        taskTimeImg=(ImageView)findViewById(R.id.task_image_time);
        taskScheduleBtn=(Button)findViewById(R.id.task_schedule);
        taskTimeTxt=(TextView)findViewById(R.id.task_time);
        likablityMeasure=(SeekBar)findViewById(R.id.task_likablity);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Task Scheduling");
        taskTimeTxt.setOnClickListener(this);
        taskTimeImg.setOnClickListener(this);
        taskScheduleBtn.setOnClickListener(this);
        pb=(ProgressBar)findViewById(R.id.progress_bar);
        progressText=(TextView)findViewById(R.id.creating_alarm_label);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("selected_time",taskTimeTxt.getText().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.task_schedule:sheduleTask();
                                    break;
            case R.id.task_time:
            case R.id.task_image_time:createTimeDialog();
                                    break;
        }
    }

    private void createTimeDialog() {
        TimePickerFragment fragment=new TimePickerFragment();
        fragment.setTime(taskTimeTxt);
        fragment.show(getSupportFragmentManager(),"time_dialog");
    }

    private void sheduleTask() {
        pb.setVisibility(View.VISIBLE);
        progressText.setVisibility(View.VISIBLE);
        if(validateTask())
        {
            creator.createAlarm();
            if(helper.insertTask(newTask))
            {
                Toast.makeText(this,"task scheduled successfully",Toast.LENGTH_LONG).show();
                finishAcitivity();
            }
        }
        pb.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);

    }

    private void finishAcitivity() {
        Intent intent=new Intent();
        intent.putExtra("new_task",newTask);
        setResult(RESULT_OK,intent);
        finish();
    }


    private boolean validateTask() {
        if(taskNameTxt.getText().toString().length()==0 || taskNameTxt.getText().toString().trim().length()==0)
        {
            taskNameTxt.setError("please enter a valid taskName");
            return false;
        }
        if(taskTimeTxt.getText().toString().length()==0)
        {
            taskTimeTxt.setError("please enter a valid time");
            return false;
        }
        newTask.setTaskId(getUniqueTaskId());
        newTask.setName(taskNameTxt.getText().toString());
        newTask.setTime(taskTimeTxt.getText().toString());
        newTask.setTasklikeablity(likablityMeasure.getProgress());
        return true;
    }

    private int getUniqueTaskId() {
       SharedPreferences shared=getSharedPreferences(TASK_PREFERENCE, Context.MODE_PRIVATE);
       int val=shared.getInt("taskUniqueId",0);
       SharedPreferences.Editor edit=shared.edit();
       edit.putInt("taskUniqueId",val+1);
       edit.commit();
       return val;
    }

}
