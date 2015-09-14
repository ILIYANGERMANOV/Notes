package com.gcode.notes.database.extras;

import android.provider.BaseColumns;

import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.database.NotesContract.PictureEntry;
import com.gcode.notes.database.NotesContract.SoundEntry;

public class Queries {
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
                    ContentEntry.COLUMN_NAME_ATTRIBUTES + INT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_REMINDER + TEXT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_CREATION_DATE + TEXT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_EXPIRATION_DATE + TEXT_TYPE +
                    " )";

    public static final String SQL_DELETE_TABLE_CONTENT =
            "DROP TABLE IF EXISTS " + ContentEntry.TABLE_NAME;


    public static final String SQL_CREATE_TABLE_NOTES =
            "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
                    NoteEntry._ID + INT_TYPE + PRIMARY_KEY + COMMA_SEP +
                    NoteEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    NoteEntry.COLUMN_NAME_HAS_PICTURE + INT_TYPE + COMMA_SEP +
                    NoteEntry.COLUMN_NAME_HAS_SOUND + INT_TYPE +
                    " )";

    public static final String SQL_DELETE_TABLE_NOTES =
            "DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME;

    public static final String SQL_CREATE_TABLE_LISTS =
            "CREATE TABLE " + ListEntry.TABLE_NAME + " (" +
                    ListEntry._ID + INT_TYPE + PRIMARY_KEY + COMMA_SEP +
                    ListEntry.COLUMN_NAME_TASKS_SERIALIZED + TEXT_TYPE +
                    " )";

    public static final String SQL_DELETE_TABLE_LISTS =
            "DROP TABLE IF EXISTS " + ListEntry.TABLE_NAME;

    public static final String SQL_CREATE_TABLE_PICTURES =
            "CREATE TABLE " + PictureEntry.TABLE_NAME + " (" +
                    PictureEntry._ID + INT_TYPE + PRIMARY_KEY + COMMA_SEP +
                    PictureEntry.COLUMN_NAME_PATH + TEXT_TYPE + COMMA_SEP +
                    PictureEntry.COLUMN_NAME_NOTE_ID + INT_TYPE +
                    " )";

    public static final String SQL_DELETE_TABLE_PICTURES =
            "DROP TABLE IF EXISTS " + PictureEntry.TABLE_NAME;

    public static final String SQL_CREATE_TABLE_SOUNDS =
            "CREATE TABLE " + SoundEntry.TABLE_NAME + " (" +
                    SoundEntry._ID + INT_TYPE + PRIMARY_KEY + COMMA_SEP +
                    SoundEntry.COLUMN_NAME_PATH + TEXT_TYPE + COMMA_SEP +
                    SoundEntry.COLUMN_NAME_NOTE_ID + INT_TYPE +
                    " )";

    public static final String SQL_DELETE_TABLE_SOUNDS =
            "DROP TABLE IF EXISTS " + SoundEntry.TABLE_NAME;

    public static String selectLastRowIdForTable(String tableName) {
        return "SELECT " + BaseColumns._ID + " FROM " + tableName +
                " ORDER BY " + BaseColumns._ID + " DESC LIMIT 1";
    }


    public static String selectAllItemsFromContentForModes(int argsCount) {
        String returnQuery = "SELECT * FROM " + ContentEntry.TABLE_NAME + " WHERE " +
                ContentEntry.COLUMN_NAME_MODE + " IN(";

        for (int i = 0; i < argsCount - 1; ++i) {
            returnQuery += "?,";
        }
        returnQuery += "?) ORDER BY " +
                ContentEntry.COLUMN_NAME_ORDER_ID + " DESC";
        return returnQuery;
    }

    public static String selectLastItemFromContentForModes(int argsCount) {
        return selectAllItemsFromContentForModes(argsCount) + " LIMIT 1";
    }

    public static String[] buildSelectionArgs(int... modes) {
        int argsCount = modes.length;
        String[] selectionArgs = new String[argsCount];
        for (int i = 0; i < argsCount; ++i) {
            selectionArgs[i] = Integer.toString(modes[i]);
        }
        return selectionArgs;
    }

    public static String whereClauseContentId = ContentEntry._ID + " = ?";


    public static final String SELECT_ALL_FROM_NOTES_FOR_ID =
            "SELECT * FROM " + NoteEntry.TABLE_NAME + " WHERE " +
                    NoteEntry._ID + " = ?";

    public static final String SELECT_PATH_FROM_PICTURES_FOR_NOTE_ID =
            "SELECT " + PictureEntry.COLUMN_NAME_PATH + " FROM " + PictureEntry.TABLE_NAME +
                    " WHERE " + PictureEntry.COLUMN_NAME_NOTE_ID + " = ?";

    public static final String SELECT_PATH_FROM_SOUNDS_FOR_NOTE_ID =
            "SELECT " + SoundEntry.COLUMN_NAME_PATH + " FROM " + SoundEntry.TABLE_NAME +
                    " WHERE " + SoundEntry.COLUMN_NAME_NOTE_ID + " = ?";
}