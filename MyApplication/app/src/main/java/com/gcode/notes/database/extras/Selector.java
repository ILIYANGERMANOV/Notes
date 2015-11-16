package com.gcode.notes.database.extras;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.extras.queries.SelectQueries;

public class Selector {
    public static int getFirstOrNextIdFromContent(SQLiteDatabase mDatabase) {
        int lastRowId = getLastRowFromTable(mDatabase, ContentEntry.TABLE_NAME);
        return lastRowId + 1;
    }

    public static int getLastRowFromTable(SQLiteDatabase mDatabase, String tableName) {
        int targetId = 0;
        Cursor cursor = mDatabase.rawQuery(SelectQueries.selectLastRowIdForTable(tableName), null);
        if (cursor.moveToFirst()) {
            targetId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
        }
        cursor.close();
        return targetId;
    }
}
