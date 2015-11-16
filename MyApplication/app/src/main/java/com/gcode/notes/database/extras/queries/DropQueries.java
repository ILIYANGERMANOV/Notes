package com.gcode.notes.database.extras.queries;

import com.gcode.notes.database.NotesContract;

public class DropQueries {
    public static final String SQL_DELETE_TABLE_CONTENT =
            "DROP TABLE IF EXISTS " + NotesContract.ContentEntry.TABLE_NAME;
    public static final String SQL_DELETE_TABLE_NOTES =
            "DROP TABLE IF EXISTS " + NotesContract.NoteEntry.TABLE_NAME;
    public static final String SQL_DELETE_TABLE_LISTS =
            "DROP TABLE IF EXISTS " + NotesContract.ListEntry.TABLE_NAME;
}
