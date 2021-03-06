package com.gcode.notes.database.extras;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class InsertHelper {

    public static long insertNote(SQLiteDatabase database, ContentBase contentBase) {
        if (contentBase.getHasAttributesFlag()) {
            if (contentBase.getType() == Constants.TYPE_NOTE) {
                insertAttributesInNote(database, contentBase);
            } else {
                insertAttributesInList(database, contentBase);
            }
        }

        return insertMainContent(database, contentBase);
    }

    private static long insertMainContent(SQLiteDatabase database, ContentBase contentBase) {
        ContentValues contentValues = new ContentValues();

        if (contentBase.getHasAttributesFlag()) {
            //target id - the id corresponding in the relevant attribute table (Notes/Lists),
            // which is already inserted successfully for the current item
            String tableName = contentBase.getType() == Constants.TYPE_NOTE ?
                    NoteEntry.TABLE_NAME : ListEntry.TABLE_NAME;

            contentValues.put(ContentEntry.COLUMN_NAME_TARGET_ID,
                    Selector.getLastRowIdFromTable(database, tableName));
        } else {
            //note hasn't attributes, set TARGET_ID to NO_VALUE
            contentValues.put(ContentEntry.COLUMN_NAME_TARGET_ID, Constants.NO_VALUE);
        }

        contentValues.put(ContentEntry.COLUMN_NAME_ORDER_ID, Selector.getFirstOrNextIdFromContent(database));
        contentValues.put(ContentEntry.COLUMN_NAME_TITLE, contentBase.getTitle());
        contentValues.put(ContentEntry.COLUMN_NAME_MODE, contentBase.getMode());
        contentValues.put(ContentEntry.COLUMN_NAME_TYPE, contentBase.getType());
        contentValues.put(ContentEntry.COLUMN_NAME_HAS_ATTRIBUTES, contentBase.getHasAttributesFlag());
        contentValues.put(ContentEntry.COLUMN_NAME_CREATION_DATE, contentBase.getCreationDate());
        contentValues.put(ContentEntry.COLUMN_NAME_LAST_MODIFIED_DATE, contentBase.getLastModifiedDate());

        //!NOTE: COLUMN_EXPIRATION_DATE will be always null when creation new note
        if(contentBase.hasReminder()) {
            //note has reminder, insert it
            //!NOTE: reminder is already is in SQLite format
            contentValues.put(ContentEntry.COLUMN_NAME_REMINDER, contentBase.getReminder());
        }
        if (contentBase.hasLocation()) {
            //note has location, serialize it and put it
            contentValues.put(ContentEntry.COLUMN_NAME_LOCATION,
                    Serializer.serializeMyLocation(contentBase.getMyLocation()));
        }

        return database.insert(ContentEntry.TABLE_NAME, ContentEntry.COLUMN_NAME_REMINDER, contentValues);
    }

    public static void insertAttributesInNote(SQLiteDatabase database, ContentBase contentBase) {
        NoteData noteData = (NoteData) contentBase;
        ContentValues contentValues = new ContentValues();

        contentValues.put(NoteEntry.COLUMN_NAME_DESCRIPTION, noteData.getDescription());
        if (noteData.hasAttachedImage()) {
            //note has attached image, insert it
            contentValues.put(NoteEntry.COLUMN_NAME_PHOTOS_PATHS,
                    Serializer.serializeImagesPathsList(noteData.getAttachedImagesPaths()));
        }
        if(noteData.hasAttachedAudio()) {
            //note has attached audio, insert it
            contentValues.put(NoteEntry.COLUMN_NAME_AUDIO_PATH, noteData.getAttachedAudioPath());
        }

        if (database.insert(NoteEntry.TABLE_NAME,
                NoteEntry.COLUMN_NAME_PHOTOS_PATHS, contentValues) == Constants.DATABASE_ERROR) {
            //inserting note attributes failed, handle error
            contentBase.setHasAttributesFlag(false);
            MyDebugger.log("ERROR INSERTING NOTE ATTRIBUTES!");
        }
    }

    public static void insertAttributesInList(SQLiteDatabase database, ContentBase contentBase) {
        ListData listData = (ListData) contentBase;
        ContentValues contentValues = new ContentValues();

        //!NOTE: insertListAttributesInList() is only called when there is attached list with items
        contentValues.put(ListEntry.COLUMN_NAME_TASKS_SERIALIZED, Serializer.serializeListDataItems(listData.getList()));

        if (database.insert(ListEntry.TABLE_NAME, ListEntry.COLUMN_NAME_TASKS_SERIALIZED, contentValues) == Constants.DATABASE_ERROR) {
            contentBase.setHasAttributesFlag(false);
            MyDebugger.log("ERROR INSERTING LIST ATTRIBUTES!");
        }
    }
}
