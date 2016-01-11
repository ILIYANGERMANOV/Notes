package com.gcode.notes.database.extras;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.database.extras.queries.SelectQueries;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class UpdateHelper {

    public static int updateNoteModeAndExpirationDate(SQLiteDatabase database, ContentBase contentBase,
                                                      int newMode, boolean setExpire) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentEntry.COLUMN_NAME_MODE, newMode);
        String expirationDate = setExpire ? DateUtils.getExpirationDate() : Constants.NO_DATE;
        contentValues.put(ContentEntry.COLUMN_NAME_EXPIRATION_DATE, expirationDate);
        return database.update(ContentEntry.TABLE_NAME, contentValues, SelectQueries.whereClauseContentId, getContentBaseIdStringArray(contentBase));
    }

    public static int updateNoteReminder(SQLiteDatabase database, ContentBase contentBase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentEntry.COLUMN_NAME_REMINDER, contentBase.getReminder());
        return database.update(ContentEntry.TABLE_NAME, contentValues,
                SelectQueries.whereClauseContentId, getContentBaseIdStringArray(contentBase));
    }

    public static int updateNoteMode(SQLiteDatabase database, ContentBase contentBase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentEntry.COLUMN_NAME_MODE, contentBase.getMode());
        return database.update(ContentEntry.TABLE_NAME, contentValues,
                SelectQueries.whereClauseContentId, getContentBaseIdStringArray(contentBase));
    }

    public static int updateNoteOrderId(SQLiteDatabase database, ContentBase contentBase, int newId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentEntry.COLUMN_NAME_ORDER_ID, newId);
        return database.update(ContentEntry.TABLE_NAME, contentValues,
                SelectQueries.whereClauseContentId, getContentBaseIdStringArray(contentBase));
    }

    public static int updateNote(Context context, SQLiteDatabase database, ContentBase contentBase) {
        int affectedRows = 0;
        affectedRows += updateBaseContent(database, contentBase); //returns <= 0 on fail
        if (affectedRows <= 0) {
            //unrecoverable error happened while updating base content, log it, inform the user and cancel update
            MyDebugger.log("updatingBaseContent failed.");
            MyDebugger.toast(context, "Fatal error while updating note.");
            return Constants.DATABASE_ERROR;
        }

        if (contentBase.getType() == Constants.TYPE_NOTE) {
            //update note attributes
            affectedRows += updateNoteAttributes(database, (NoteData) contentBase);
        } else {
            //update list attributes
            affectedRows += updateListAttributes(database, (ListData) contentBase);
        }
        return affectedRows;
    }

    public static int removeAttachedAudioFromNote(SQLiteDatabase database, int targetId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteEntry.COLUMN_NAME_AUDIO_PATH, Constants.NO_AUDIO);
        return database.update(NoteEntry.TABLE_NAME, contentValues, SelectQueries.whereClauseNoteId,
                new String[]{
                        Integer.toString(targetId)
                }
        );
    }

    private static int updateBaseContent(SQLiteDatabase database, ContentBase contentBase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentEntry.COLUMN_NAME_TITLE, contentBase.getTitle());
        contentValues.put(ContentEntry.COLUMN_NAME_MODE, contentBase.getMode());
        contentValues.put(ContentEntry.COLUMN_NAME_HAS_ATTRIBUTES, contentBase.getHasAttributesFlag());
        contentValues.put(ContentEntry.COLUMN_NAME_LAST_MODIFIED_DATE, contentBase.getLastModifiedDate());
        contentValues.put(ContentEntry.COLUMN_NAME_REMINDER, contentBase.getReminder());
        if (contentBase.hasLocation()) {
            //note has location, update it
            contentValues.put(ContentEntry.COLUMN_NAME_LOCATION, Serializer.serializeMyLocation(contentBase.getMyLocation()));
        } else {
            //note hasn't location, set NO_LOCATION
            contentValues.put(ContentEntry.COLUMN_NAME_LOCATION, Constants.NO_LOCATION);
        }

        if (contentBase.getTargetId() == Constants.NO_VALUE) {
            if (contentBase.getHasAttributesFlag()) {
                //targetId isn't set so, there is no row for attributes; insert it now
                if (contentBase.getType() == Constants.TYPE_NOTE) {
                    InsertHelper.insertAttributesInNote(database, contentBase); //on fail setHasAttributesFlag to false
                } else {
                    InsertHelper.insertAttributesInList(database, contentBase); //on fail setHasAttributesFlag to false
                }

                if (contentBase.getHasAttributesFlag()) {
                    //Check whether attributes were inserted successfully
                    //target id - the id corresponding in the relevant attribute table (Notes/Lists),
                    // which attribute is already inserted successfully into attribute table for the current item,
                    //so there is no chance to have issues with wrong value from getLastRowIdFromTable() for empty table
                    String tableName = contentBase.getType() == Constants.TYPE_NOTE ? NoteEntry.TABLE_NAME : ListEntry.TABLE_NAME;
                    contentBase.setTargetId(Selector.getLastRowIdFromTable(database, tableName));
                    contentValues.put(ContentEntry.COLUMN_NAME_TARGET_ID, contentBase.getTargetId());
                } else {
                    //unrecoverable error, log it and prevent further procedure
                    MyDebugger.log("updateBaseContent() failed to insert attributes!");
                    return Constants.DATABASE_ERROR;
                }
            }
        }

        return database.update(ContentEntry.TABLE_NAME, contentValues,
                SelectQueries.whereClauseContentId, getContentBaseIdStringArray(contentBase));
    }

    private static int updateNoteAttributes(SQLiteDatabase database, NoteData noteData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteEntry.COLUMN_NAME_DESCRIPTION, noteData.getDescription());
        if (noteData.hasAttachedImage()) {
            //note has attached image, serialize list and update row
            contentValues.put(NoteEntry.COLUMN_NAME_PHOTOS_PATHS,
                    Serializer.serializeImagesPathsList(noteData.getAttachedImagesPaths()));
        } else {
            //note has no attached images, put null for the column
            contentValues.putNull(NoteEntry.COLUMN_NAME_PHOTOS_PATHS);
        }
        contentValues.put(NoteEntry.COLUMN_NAME_AUDIO_PATH, noteData.getAttachedAudioPath());

        return database.update(NoteEntry.TABLE_NAME, contentValues,
                SelectQueries.whereClauseNoteId, getContentBaseTargetId(noteData));
    }

    public static int updateListAttributes(SQLiteDatabase database, ListData listData) {
        ContentValues contentValues = new ContentValues();

        if (listData.hasAttachedList()) {
            //list has attached items, update them
            contentValues.put(ListEntry.COLUMN_NAME_TASKS_SERIALIZED, Serializer.serializeListDataItems(listData.getList()));
        } else {
            //list hasn't attached items, put null for optimization
            contentValues.putNull(ListEntry.COLUMN_NAME_TASKS_SERIALIZED);
        }

        return database.update(ListEntry.TABLE_NAME, contentValues,
                SelectQueries.whereClauseListId, getContentBaseTargetId(listData));
    }

    private static String[] getContentBaseIdStringArray(ContentBase contentBase) {
        return new String[]{
                Integer.toString(contentBase.getId())
        };
    }

    private static String[] getContentBaseTargetId(ContentBase contentBase) {
        return new String[]{
                Integer.toString(contentBase.getTargetId())
        };
    }
}
