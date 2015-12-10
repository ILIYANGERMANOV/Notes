package com.gcode.notes.database.extras.queries;

import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;

public class CreateQueries {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_TABLE_CONTENT =
            "CREATE TABLE " + ContentEntry.TABLE_NAME + " (" +
                    ContentEntry._ID + INT_TYPE + PRIMARY_KEY + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_ORDER_ID + INT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_TYPE + INT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_TARGET_ID + INT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_MODE + INT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_HAS_ATTRIBUTES + INT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_REMINDER + TEXT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_CREATION_DATE + TEXT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_LAST_MODIFIED_DATE + TEXT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_EXPIRATION_DATE + TEXT_TYPE +
                    " )";

    public static final String SQL_CREATE_TABLE_NOTES =
            "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
                    NoteEntry._ID + INT_TYPE + PRIMARY_KEY + COMMA_SEP +
                    NoteEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    NoteEntry.COLUMN_NAME_PHOTOS_PATHS + TEXT_TYPE + COMMA_SEP +
                    NoteEntry.COLUMN_NAME_AUDIO_PATH + TEXT_TYPE +
                    " )";

    public static final String SQL_CREATE_TABLE_LISTS =
            "CREATE TABLE " + ListEntry.TABLE_NAME + " (" +
                    ListEntry._ID + INT_TYPE + PRIMARY_KEY + COMMA_SEP +
                    ListEntry.COLUMN_NAME_TASKS_SERIALIZED + TEXT_TYPE +
                    " )";
}
