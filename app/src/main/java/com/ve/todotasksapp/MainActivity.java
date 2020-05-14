package com.ve.todotasksapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int TASK_RQ =12;
    RecyclerView recycler;
    FloatingActionButton createTaskButton;
    Toolbar toolbar;
    ArrayList<Task> taskList;
    TaskAdapter taskAdapter;
    DBHelper helper;
    SwipeRefreshLayout  taskRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wireUpListeners();
        getDataFromDb();
    }

    private void getDataFromDb() {
        taskList=helper.getAllTasks();
        taskAdapter = new TaskAdapter(this, taskList);
        recycler.setAdapter(taskAdapter);

    }

    private void wireUpListeners() {
        recycler=(RecyclerView)findViewById(R.id.task_lists);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(this,2));
        createTaskButton=(FloatingActionButton)findViewById(R.id.task_create);
        createTaskButton.setOnClickListener(this);
        helper=new DBHelper(this);
        taskRefresh=findViewById(R.id.task_refresher);
        taskRefresh.setColorSchemeColors(ContextCompat.getColor(this,android.R.color.holo_blue_light));
        taskRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                taskList=helper.getAllTasks();
                taskAdapter.taskList=taskList;
                taskAdapter.taskListCopy=taskList;
                taskAdapter.notifyDataSetChanged();
                taskRefresh.setRefreshing(false);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.task_create:moveToCreateTaskActivity();
                                    break;
        }
    }

    private void moveToCreateTaskActivity() {
        Intent intent=new Intent(this,TaskCreation.class);
        startActivityForResult(intent,TASK_RQ);
    }
    void upDateTask(Task task)
    {
        Intent intent=new Intent(this,TaskCreation.class);
        intent.putExtra("task",task);
        startActivityForResult(intent,TASK_RQ);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==TASK_RQ && resultCode==RESULT_OK)
        {
            if(data!=null)
            {
                taskList.add((Task)data.getSerializableExtra("new_task"));
                taskAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem searchEvnt=menu.findItem(R.id.search_task);
        androidx.appcompat.widget.SearchView searchView= (androidx.appcompat.widget.SearchView) searchEvnt.getActionView();
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                taskAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;

    }
}
