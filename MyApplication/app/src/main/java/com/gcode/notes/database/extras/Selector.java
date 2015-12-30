package com.gcode.notes.database.extras;


import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.extras.queries.SelectQueries;
import com.gcode.notes.extras.values.Constants;

public class Selector {
    //TODO: REFACTOR AND OPTIMIZE
    //!NOTE first row in SQLite has value of 1, so when getLastRowIdFromTable() returns 0 (there are no records in table)
    //getFirstOrNextIdFromContent() with return 0 + 1, which will be the correct id for the next record
    //in case the table is not empty it will correctly, too (e.g. lastRow = 1 so next will be lastRow + 1 = 2;)

    public static int getFirstOrNextIdFromContent(SQLiteDatabase database) {
        int lastRowId = getLastRowIdFromTable(database, ContentEntry.TABLE_NAME);
        return lastRowId + 1;
    }

    public static int getLastRowIdFromTable(SQLiteDatabase database, String tableName) {
        int lastRowId = 0;
        Cursor cursor = database.rawQuery(SelectQueries.selectLastRowIdForTable(tableName), null);
        if (cursor.moveToFirst()) {
            lastRowId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
        }
        cursor.close();
        return lastRowId;
    }

    public static long getNextNumberForType(SQLiteDatabase database, int type) {
        return getCountFromContentTableForType(database, type) + 1;
    }

    public static long getCountFromContentTableForType(SQLiteDatabase database, int type) {
        return DatabaseUtils.queryNumEntries(database, ContentEntry.TABLE_NAME,
                ContentEntry.COLUMN_NAME_TYPE + SelectQueries.EQUALS_TO,
                new String[]{
                        Integer.toString(type)
                }
        );
    }
}
