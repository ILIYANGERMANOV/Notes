package com.gcode.notes.database.extras;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.ContentBase;
import com.gcode.notes.database.NotesContract.ContentEntry;

public class UpdateHelper {
    private static String whereClause = ContentEntry._ID + " = ?";

    public static int updateNoteMode(SQLiteDatabase mDatabase, ContentBase contentBase, int newMode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentEntry.COLUMN_NAME_MODE, newMode);
        String[] whereArgs = {
                Integer.toString(contentBase.getId())
        };
        return mDatabase.update(ContentEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
    }

    public static int updateNoteOrderId(SQLiteDatabase mDatabase, ContentBase contentBase, int newId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentEntry.COLUMN_NAME_ORDER_ID, newId);
        String[] whereArgs = {
                Integer.toString(contentBase.getId())
        };
        return mDatabase.update(ContentEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
    }
}
