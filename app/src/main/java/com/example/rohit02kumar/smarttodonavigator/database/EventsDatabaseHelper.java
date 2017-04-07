package com.example.rohit02kumar.smarttodonavigator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rohit02.kumar on 4/7/2017.
 */
public class EventsDatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "eventstable.db";
    private static final int DATABASE_VERSION = 1;

    public EventsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        EventTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        EventTable.onUpgrade(database, oldVersion, newVersion);
    }
}
