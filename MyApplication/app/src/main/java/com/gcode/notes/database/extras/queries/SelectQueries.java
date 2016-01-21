package com.gcode.notes.database.extras.queries;

import android.provider.BaseColumns;

import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.extras.utils.DateUtils;

public class SelectQueries {
    //TODO: refactor and optimize
    private static final String SELECT = "SELECT ";
    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    private static final String WHERE = " WHERE ";
    private static final String FROM = " FROM ";
    private static final String ORDER_BY_EXPIRATION_DATE =
            "datetime(" + ContentEntry.COLUMN_NAME_EXPIRATION_DATE + ")";

    private static final String ORDER_BY = " ORDER BY ";
    private static final String DESC_LIMIT_1 = " DESC LIMIT 1";

    public static final String EQUALS_TO = " = ?";
    public static final String IS_NOT_NULL = " IS NOT NULL";
    public static final String AND = " AND ";
    public static final String LESS_THAN = " < ";

    public static final String SELECT_ALL_FROM_NOTES_FOR_ID =
            SELECT_ALL_FROM + NoteEntry.TABLE_NAME + WHERE +
                    NoteEntry._ID + EQUALS_TO;

    public static final String SELECT_ALL_FROM_LISTS_FOR_ID =
            SELECT_ALL_FROM + ListEntry.TABLE_NAME + WHERE +
                    ListEntry._ID + EQUALS_TO;

    public static final String SELECT_ALL_EXPIRED_NOTES =
            SELECT_ALL_FROM + ContentEntry.TABLE_NAME + WHERE +
                    ContentEntry.COLUMN_NAME_EXPIRATION_DATE + IS_NOT_NULL +
                    AND + ContentEntry.COLUMN_NAME_EXPIRATION_DATE + LESS_THAN +
                    "'" + DateUtils.getCurrentTimeSQLiteFormatted() + "'";

    public static final String SELECT_ALL_NOTES_WITH_REMINDER =
            SELECT_ALL_FROM + ContentEntry.TABLE_NAME + WHERE +
                    ContentEntry.COLUMN_NAME_REMINDER + IS_NOT_NULL;

    public static final String SELECT_NOTE_WITH_ID =
            SELECT_ALL_FROM + ContentEntry.TABLE_NAME + WHERE +
                    ContentEntry._ID + EQUALS_TO;

    public static final String SELECT_NOTE_MODE_WITH_ID =
            SELECT + ContentEntry.COLUMN_NAME_MODE +
                    FROM + ContentEntry.TABLE_NAME + WHERE +
                    ContentEntry._ID + EQUALS_TO;

    public static String whereClauseContentId = ContentEntry._ID + EQUALS_TO;
    public static String whereClauseNoteId = NoteEntry._ID + EQUALS_TO;
    public static String whereClauseListId = ListEntry._ID + EQUALS_TO;

    public static String selectLastRowIdForTable(String tableName) {
        return SELECT + BaseColumns._ID + FROM + tableName +
                ORDER_BY + BaseColumns._ID + DESC_LIMIT_1;
    }

    public static String selectLastItemFromContentForModes(int argsCount, boolean orderByOrderId) {
        return selectAllItemsFromContentForModes(argsCount, orderByOrderId) + " LIMIT 1";
    }

    public static String selectAllItemsFromContentForModes(int argsCount, boolean orderByROrderId) {
        String returnQuery = SELECT_ALL_FROM + ContentEntry.TABLE_NAME + WHERE +
                ContentEntry.COLUMN_NAME_MODE + " IN(";

        for (int i = 0; i < argsCount - 1; ++i) {
            returnQuery += "?,";
        }

        String orderValue = orderByROrderId ? ContentEntry.COLUMN_NAME_ORDER_ID : ORDER_BY_EXPIRATION_DATE;

        returnQuery += "?) ORDER BY " + orderValue + " DESC";
        return returnQuery;
    }

    public static String[] buildSelectionArgs(int... modes) {
        int argsCount = modes.length;
        String[] selectionArgs = new String[argsCount];
        for (int i = 0; i < argsCount; ++i) {
            selectionArgs[i] = Integer.toString(modes[i]);
        }
        return selectionArgs;
    }
}