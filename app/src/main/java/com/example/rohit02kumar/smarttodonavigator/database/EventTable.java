package com.example.rohit02kumar.smarttodonavigator.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by rohit02.kumar on 4/7/2017.
 */
public class EventTable {

    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EVENTNAME = "eventname";
    public static final String COLUMN_EVENTTYPE="eventype";
    public static final String COLUMN_FROM_DATE = "fromdate";
    public static final String COLUMN_TO_DATE = "todate";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_EVENTS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_EVENTNAME + " text not null, "
            + COLUMN_EVENTTYPE + " text not null, "
            + COLUMN_FROM_DATE + " long not null,"
            + COLUMN_TO_DATE + " long not null"
            + ");";

    /* will be used to create database and notes table */
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    /* will be used when we need to update database is required in future */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(EventTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(database);
    }
}
