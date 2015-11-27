package com.gcode.notes.database.extras;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.main.ContentBase;
import com.gcode.notes.data.main.ListData;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class InsertHelper {

    public static long insertNote(SQLiteDatabase mDatabase, ContentBase contentBase) {
        if (contentBase.getHasAttributesFlag()) {
            if (contentBase.getType() == Constants.TYPE_NOTE) {
                insertAttributesInNotes(mDatabase, contentBase);
            } else {
                insertAttributesInLists(mDatabase, contentBase);
            }
        }

        return insertMainContent(mDatabase, contentBase);
    }

    private static long insertMainContent(SQLiteDatabase mDatabase, ContentBase contentBase) {
        ContentValues contentValues = new ContentValues();

        if (contentBase.getHasAttributesFlag()) {
            //target id - the id corresponding in the relevant attribute table (Notes/Lists),
            // which is already inserted successfully for the current item
            String tableName = contentBase.getType() == Constants.TYPE_NOTE ? NoteEntry.TABLE_NAME : ListEntry.TABLE_NAME;
            contentValues.put(ContentEntry.COLUMN_NAME_TARGET_ID, Selector.getLastRowFromTable(mDatabase, tableName));
        } else {
            //note hasn't attributes, set TARGET_ID to NO_VALUE
            contentValues.put(ContentEntry.COLUMN_NAME_TARGET_ID, Constants.NO_VALUE);
        }

        contentValues.put(ContentEntry.COLUMN_NAME_ORDER_ID, Selector.getFirstOrNextIdFromContent(mDatabase));
        contentValues.put(ContentEntry.COLUMN_NAME_TITLE, contentBase.getTitle());
        contentValues.put(ContentEntry.COLUMN_NAME_MODE, contentBase.getMode());
        contentValues.put(ContentEntry.COLUMN_NAME_TYPE, contentBase.getType());
        contentValues.put(ContentEntry.COLUMN_NAME_ATTRIBUTES, contentBase.getHasAttributesFlag());
        contentValues.put(ContentEntry.COLUMN_NAME_REMINDER, contentBase.getReminder());
        //TODO: add legit location
        contentValues.put(ContentEntry.COLUMN_NAME_LOCATION, Constants.NO_LOCATION);
        contentValues.put(ContentEntry.COLUMN_NAME_CREATION_DATE, DateUtils.getCurrentTimeSQLiteFormatted());
        contentValues.put(ContentEntry.COLUMN_NAME_LAST_MODIFIED_DATE, DateUtils.getCurrentTimeSQLiteFormatted());
        contentValues.put(ContentEntry.COLUMN_NAME_EXPIRATION_DATE, Constants.NO_DATE);

        return mDatabase.insert(ContentEntry.TABLE_NAME, null, contentValues);
    }

    public static void insertAttributesInLists(SQLiteDatabase mDatabase, ContentBase contentBase) {
        ListData listData = (ListData) contentBase;
        ContentValues contentValues = new ContentValues();

        contentValues.put(ListEntry.COLUMN_NAME_TASKS_SERIALIZED, Serializer.serializeListDataItems(listData.getList()));

        if (mDatabase.insert(ListEntry.TABLE_NAME, null, contentValues) == Constants.DATABASE_ERROR) {
            contentBase.setHasAttributesFlag(false);
            MyDebugger.log("ERROR INSERTING LIST ATTRIBUTES!");
        }
    }

    public static void insertAttributesInNotes(SQLiteDatabase mDatabase, ContentBase contentBase) {
        NoteData noteData = (NoteData) contentBase;
        ContentValues contentValues = new ContentValues();

        contentValues.put(NoteEntry.COLUMN_NAME_DESCRIPTION, noteData.getDescription());
        contentValues.put(NoteEntry.COLUMN_NAME_PHOTOS_PATHS, Serializer.serializeImagesPathsList(noteData.getAttachedImagesPaths()));
        contentValues.put(NoteEntry.COLUMN_NAME_AUDIO_PATH, noteData.getAttachedAudioPath());

        if (mDatabase.insert(NoteEntry.TABLE_NAME, null, contentValues) == Constants.DATABASE_ERROR) {
            //inserting note attributes failed, handle error
            contentBase.setHasAttributesFlag(false);
            MyDebugger.log("ERROR INSERTING NOTE ATTRIBUTES!");
        }
    }
}
