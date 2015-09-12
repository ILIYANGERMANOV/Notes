package com.gcode.notes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gcode.notes.database.extras.Queries;

public class NotesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Notes.db";
    public static final int DATABASE_VERSION = 2;

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Queries.SQL_CREATE_TABLE_CONTENT);
        db.execSQL(Queries.SQL_CREATE_TABLE_NOTES);
        db.execSQL(Queries.SQL_CREATE_TABLE_LISTS);
        db.execSQL(Queries.SQL_CREATE_TABLE_PICTURES);
        db.execSQL(Queries.SQL_CREATE_TABLE_SOUNDS);
    }

    public void dropDatabase(SQLiteDatabase db) {
        db.execSQL(Queries.SQL_DELETE_TABLE_CONTENT);
        db.execSQL(Queries.SQL_DELETE_TABLE_NOTES);
        db.execSQL(Queries.SQL_DELETE_TABLE_LISTS);
        db.execSQL(Queries.SQL_DELETE_TABLE_PICTURES);
        db.execSQL(Queries.SQL_DELETE_TABLE_SOUNDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropDatabase(db);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
