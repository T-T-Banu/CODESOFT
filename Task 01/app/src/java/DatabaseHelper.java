package com.example.todoapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "tasks.db";
    public static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_TASKS = "tasks";

    // Column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_CONTENT = "content";

    //  table query
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_TITLE + " TEXT," +
            COLUMN_DATE + " TEXT," +
            COLUMN_TIME + " TEXT," +
            COLUMN_CONTENT + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Insert a task into the database
    public long insertTask(String title, String date, String time, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_CONTENT, content);
        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    // Get all tasks from the database
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TASKS, null, null, null, null, null, null);
    }

    // Update a task in the database
    public int updateTask(long id, String title, String date, String time, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_CONTENT, content);
        return db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Delete a task from the database
    public int deleteTask(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public long insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DATE, task.getDate());
        values.put(COLUMN_TIME, task.getTime());
        values.put(COLUMN_CONTENT, task.getContent());
        // Insert the task into the database
        return db.insert(TABLE_TASKS, null, values);
    }

    public Task getTaskById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Task task = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_TASKS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTitle = cursor.getColumnIndex(COLUMN_TITLE);
                int columnIndexDate = cursor.getColumnIndex(COLUMN_DATE);
                int columnIndexTime = cursor.getColumnIndex(COLUMN_TIME);
                int columnIndexContent = cursor.getColumnIndex(COLUMN_CONTENT);

                if (columnIndexTitle != -1 && columnIndexDate != -1 && columnIndexTime != -1 && columnIndexContent != -1) {
                    String title = cursor.getString(columnIndexTitle);
                    String date = cursor.getString(columnIndexDate);
                    String time = cursor.getString(columnIndexTime);
                    String content = cursor.getString(columnIndexContent);
                    task = new Task(id, title, date, time, content);
                } else {

                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return task;
    }


}