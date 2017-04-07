package com.example.rohit02kumar.smarttodonavigator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by rohit02.kumar on 4/7/2017.
 */
public class EventDataSource {
    private static final String DEFAULT_SORT_ORDER = "DESC";
    private SQLiteDatabase mDatabase;
    private EventsDatabaseHelper mDbHelper;
    private String[] mAllColumns = { EventTable.COLUMN_ID,
            EventTable.COLUMN_EVENTNAME, EventTable.COLUMN_EVENTTYPE,EventTable.COLUMN_FROM_DATE,
            EventTable.COLUMN_TO_DATE };
    private String[] mIdColumn = { EventTable.COLUMN_ID };

    public EventDataSource(Context context) {
        mDbHelper = new EventsDatabaseHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public long createEvent(Integer id, String eventname, String type, long from,long to
                                 ) {
        ContentValues values = new ContentValues();
        values.put(EventTable.COLUMN_ID, id);
        values.put(EventTable.COLUMN_EVENTNAME, eventname);
        values.put(EventTable.COLUMN_EVENTTYPE, type);
        values.put(EventTable.COLUMN_FROM_DATE, from);
        values.put(EventTable.COLUMN_TO_DATE,to );
        open();
        long insertId = mDatabase.insert(EventTable.TABLE_EVENTS, null, values);
        close();
        return insertId;
    }

//    public long updateEvent(Integer id, String filename, long modification_date,
//                                  String description) {
//        ContentValues values = new ContentValues();
//        values.put(Table.COLUMN_ID, id);
//        values.put(NotesTable.COLUMN_FILENAME, filename);
//        values.put(NotesTable.COLUMN_MODIFICATION_DATE, modification_date);
//        values.put(NotesTable.COLUMN_DESCRIPTION, description);
//        open();
//        long insertId = mDatabase.update(NotesTable.TABLE_NOTES, values, NotesTable.COLUMN_ID
//                + " = " + id, null);
//        Cursor cursor = mDatabase.query(NotesTable.TABLE_NOTES,
//                mAllColumns, NotesTable.COLUMN_ID + " = " + insertId, null,
//                null, null, null);
//        cursor.moveToFirst();
//        cursor.close();
//        close();
//        return insertId;
//    }

    public int deleteEvent(int id) {
        open();
        int deleteStatus = mDatabase.delete(EventTable.TABLE_EVENTS, EventTable.COLUMN_ID
                + " = " + id, null);
        close();
        return deleteStatus;
    }

    public ArrayList<Event> getAllNote_Objects() {
        ArrayList<Event> notes = new ArrayList<Event>();
        open();
        Cursor cursor = mDatabase.query(EventTable.TABLE_EVENTS,
                mAllColumns, null, null, null, null,
                EventTable.COLUMN_FROM_DATE+" "+ DEFAULT_SORT_ORDER);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event note = cursorToNote_Object(cursor);
            notes.add(note);

            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return notes;
    }

    public ArrayList<Integer> getAllIds() {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        open();
        Cursor cursor = mDatabase.query(EventTable.TABLE_EVENTS,
                mIdColumn, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            notes.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return notes;
    }

    private Event cursorToNote_Object(Cursor cursor) {
        Event note = new Event();
        note.setmId(cursor.getInt(0));
        note.setmEventName(cursor.getString(1));
        note.setmEvenType(cursor.getString(2));
        note.setmFromDate(cursor.getLong(3));
        note.setmToDate(cursor.getLong(4));
        return note;
    }
}
