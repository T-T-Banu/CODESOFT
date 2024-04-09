package com.example.demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "alarms.db";
        private static final int DATABASE_VERSION = 1;

        public static final String TABLE_ALARMS = "alarms";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_MINUTE = "minute";
        public static final String COLUMN_ENABLED = "enabled";

        private static final String CREATE_TABLE_ALARMS = "CREATE TABLE " + TABLE_ALARMS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_HOUR + " INTEGER," +
                COLUMN_MINUTE + " INTEGER," +
                COLUMN_ENABLED + " INTEGER DEFAULT 1" +
                ")";

    public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_ALARMS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
            onCreate(db);
        }
    }

