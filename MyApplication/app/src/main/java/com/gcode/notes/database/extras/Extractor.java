package com.gcode.notes.database.extras;

import android.database.Cursor;

import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.database.NotesContract;
import com.gcode.notes.extras.DateUtils;

public class Extractor {
    public static ContentBase extractNoteDataFromContent(Cursor cursor) {
        return new NoteData(
                cursor.getString(cursor.getColumnIndex(NotesContract.ContentEntry.COLUMN_NAME_TITLE)),
                cursor.getInt(cursor.getColumnIndex(NotesContract.ContentEntry.COLUMN_NAME_MODE)),
                cursor.getInt(cursor.getColumnIndex(NotesContract.ContentEntry.COLUMN_NAME_PRIORITY)),
                cursor.getInt(cursor.getColumnIndex(NotesContract.ContentEntry.COLUMN_NAME_ATTRIBUTES)) != 0,
                cursor.getString(cursor.getColumnIndex(NotesContract.ContentEntry.COLUMN_NAME_REMINDER)),
                DateUtils.parseString(cursor.getString(cursor.getColumnIndex(NotesContract.ContentEntry.COLUMN_NAME_CREATION_DATE))),
                cursor.getString(cursor.getColumnIndex(NotesContract.ContentEntry.COLUMN_NAME_EXPIRATION_DATE))
        );
    }
}
