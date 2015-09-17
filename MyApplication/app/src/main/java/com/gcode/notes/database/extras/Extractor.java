package com.gcode.notes.database.extras;

import android.database.Cursor;

import com.gcode.notes.data.ListData;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.extras.DateUtils;

public class Extractor {
    public static NoteData extractNoteDataFromContent(Cursor cursor) {
        return new NoteData(
                cursor.getInt(cursor.getColumnIndex(ContentEntry._ID)),
                cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_ORDER_ID)),
                cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TARGET_ID)),
                cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TITLE)),
                cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_MODE)),
                cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_ATTRIBUTES)) != 0,
                cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_REMINDER)),
                DateUtils.parseString(cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_CREATION_DATE))),
                cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_EXPIRATION_DATE))
        );
    }

    public static ListData extractListDataFromContent(Cursor cursor) {
        return new ListData(
                cursor.getInt(cursor.getColumnIndex(ContentEntry._ID)),
                cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_ORDER_ID)),
                cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TARGET_ID)),
                cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TITLE)),
                cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_MODE)),
                cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_ATTRIBUTES)) != 0,
                cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_REMINDER)),
                DateUtils.parseString(cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_CREATION_DATE))),
                cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_EXPIRATION_DATE))
        );
    }

    public static int getItemType(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TYPE));
    }
}
