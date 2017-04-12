package com.example.rohit02kumar.smarttodonavigator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rohit02.kumar on 4/7/2017.
 */
public class EventDataSource {
    private static final String DEFAULT_SORT_ORDER = "DESC";
    private SQLiteDatabase mDatabase;
    private EventsDatabaseHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = {
            EventTable.COLUMN_EVENTNAME, EventTable.COLUMN_EVENTTYPE,EventTable.COLUMN_FROM_DATE,
            EventTable.COLUMN_TO_DATE,EventTable.COLUMN_TO_COMPLETE };
  //  private String[] mIdColumn = { EventTable.COLUMN_ID };

    public EventDataSource(Context context) {
        mDbHelper = new EventsDatabaseHelper(context);
        mContext=context;
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public long createEvent(String eventname, String type, String from,String to,
                                 int isComplete) {
        ContentValues values = new ContentValues();
        values.put(EventTable.COLUMN_EVENTNAME, eventname);
        values.put(EventTable.COLUMN_EVENTTYPE, type);
        values.put(EventTable.COLUMN_FROM_DATE, from);
        values.put(EventTable.COLUMN_TO_DATE,to );
        values.put(EventTable.COLUMN_TO_COMPLETE,isComplete);
        open();
        long insertId = mDatabase.insert(EventTable.TABLE_EVENTS, null, values);
        if(insertId!=-1)
        {
            Toast.makeText(mContext,"Event added successfully",Toast.LENGTH_SHORT);
        }

        close();
        return insertId;
    }

    public long updateEvent(String name,int isComplete) {
        ContentValues values = new ContentValues();
        values.put(EventTable.COLUMN_TO_COMPLETE, isComplete);
        open();
        long insertId = mDatabase.update(EventTable.TABLE_EVENTS, values, EventTable.COLUMN_EVENTNAME
                + " = " + name, null);

        return insertId;
    }

    public int deleteEvent(String name) {
        open();
        int deleteStatus = mDatabase.delete(EventTable.TABLE_EVENTS, EventTable.COLUMN_EVENTNAME
                + " = " + name, null);
        close();
        return deleteStatus;
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<Event>();
        open();
        Cursor cursor = mDatabase.query(EventTable.TABLE_EVENTS,
                mAllColumns, null, null, null, null,
                EventTable.COLUMN_FROM_DATE+" "+ DEFAULT_SORT_ORDER);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent_Object(cursor);
            try {
                Date toDate=new SimpleDateFormat("dd/MM/yyyy").parse(event.getmFromDate());
                String currentDate=new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                Date current= new SimpleDateFormat("dd/MM/yyyy").parse(currentDate);
                if(toDate.compareTo(current)<=0)
                    events.add(event);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return events;
    }

    private Event cursorToEvent_Object(Cursor cursor) {
        Event event = new Event();
        event.setmEventName(cursor.getString(0));
        event.setmEvenType(cursor.getString(1));
        event.setmFromDate(cursor.getString(2));
        event.setmToDate(cursor.getString(3));
        event.setIsComplete(cursor.getInt(4));
        return event;
    }
}
