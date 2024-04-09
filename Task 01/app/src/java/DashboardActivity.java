package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton fabAddTask;
    private static final int ADD_TASK_REQUEST_CODE = 1;
    private TextView todayDateTextView;
    LinearLayout btnHome, btnCalendar, btnSetting;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fabAddTask = findViewById(R.id.addTaskButton);
        btnHome=findViewById(R.id.btnHome);
        btnSetting=findViewById(R.id.btnSettings);
        btnCalendar = findViewById(R.id.calendarIcon);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        // Initialize TextViews
        todayDateTextView = findViewById(R.id.todayDate);


        // Set current date
        setCurrentDate();
        displayTasks();

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddTaskActivity
                Intent intent = new Intent(DashboardActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
            }
        });
        btnCalendar.setOnClickListener(view -> {
            Intent i=new Intent(this,CalendarActivity.class);
            startActivity(i);
        });


    }


    private void setCurrentDate() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(calendar.getTime());

        // Set current date to TextView
        todayDateTextView.setText(currentDate);
    }




    private void displayTasks() {
        Cursor cursor = databaseHelper.getAllTasks();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                    int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
                    int dateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);
                    int timeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME);
                    int contentIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTENT);

                    int id = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String date = cursor.getString(dateIndex);
                    String time = cursor.getString(timeIndex);
                    String content = cursor.getString(contentIndex);

                    Task task = new Task(id, title, date, time, content);
                    taskList.add(task);
                }
                cursor.close();

                taskAdapter = new TaskAdapter(taskList, databaseHelper, this);
                recyclerView.setAdapter(taskAdapter);
            } else {
                Toast.makeText(this, "No tasks found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Cursor is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TASK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refresh the task list
                taskList.clear();
                displayTasks();
            }
        }
    }



}