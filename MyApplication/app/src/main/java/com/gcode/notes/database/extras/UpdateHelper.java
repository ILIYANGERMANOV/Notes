package com.gcode.notes.database.extras;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.NoteData;
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
                                                      int newMode, boolean hasExpirationDate) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentEntry.COLUMN_NAME_MODE, newMode);
        String expirationDate = hasExpirationDate ? DateUtils.getExpirationDate() : Constants.NO_DATE;
        contentValues.put(ContentEntry.COLUMN_NAME_EXPIRATION_DATE, expirationDate);
        return database.update(ContentEntry.TABLE_NAME, contentValues, SelectQueries.whereClauseContentId, getContentBaseIdStringArray(contentBase));
    }

    public static int updateNoteMode(SQLiteDatabase database, ContentBase contentBase, int newMode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentEntry.COLUMN_NAME_MODE, newMode);
        return database.update(ContentEntry.TABLE_NAME, contentValues, SelectQueries.whereClauseContentId, getContentBaseIdStringArray(contentBase));
    }

    public static int updateNoteOrderId(SQLiteDatabase mDatabase, ContentBase contentBase, int newId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentEntry.COLUMN_NAME_ORDER_ID, newId);
        return mDatabase.update(ContentEntry.TABLE_NAME, contentValues, SelectQueries.whereClauseContentId, getContentBaseIdStringArray(contentBase));
    }

    public static int updateNote(SQLiteDatabase database, ContentBase contentBase) {
        int affectedRows = 0;
        affectedRows += updateBaseContent(database, contentBase);
        if (affectedRows == 0) {
            MyDebugger.log("updatingBaseContent failed.");
        }
        if (contentBase.hasAttributes()) {
            if (contentBase.getType() == Constants.TYPE_NOTE) {
                //onItemAdded note attributes
                affectedRows += updateNoteAttributes(database, (NoteData) contentBase);
            } else {
                //onItemAdded list attributes
                affectedRows += updateListAttributes(database, (ListData) contentBase);
            }
        }
        return affectedRows;
    }

    private static int updateBaseContent(SQLiteDatabase database, ContentBase contentBase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentEntry.COLUMN_NAME_TITLE, contentBase.getTitle());
        contentValues.put(ContentEntry.COLUMN_NAME_MODE, contentBase.getMode());
        contentValues.put(ContentEntry.COLUMN_NAME_ATTRIBUTES, contentBase.hasAttributes());
        contentValues.put(ContentEntry.COLUMN_NAME_LAST_MODIFIED_DATE, contentBase.getLastModifiedDate());
        contentValues.put(ContentEntry.COLUMN_NAME_REMINDER, contentBase.getReminder());
        if (contentBase.getTargetId() == Constants.ERROR) {
            //targetId isn't set so, there is now row for attributes; insert it now
            if (contentBase.getType() == Constants.TYPE_NOTE) {
                InsertHelper.insertAttributesInNotes(database, contentBase);
            } else {
                InsertHelper.insertAttributesInLists(database, contentBase);
            }
            if (contentBase.hasAttributes()) {
                //target id - the id corresponding in the relevant attribute table (Notes/Lists),
                // which is already inserted successfully for the current item
                String tableName = contentBase.getType() == Constants.TYPE_NOTE ? NoteEntry.TABLE_NAME : ListEntry.TABLE_NAME;
                contentBase.setTargetId(Selector.getLastRowFromTable(database, tableName));
                contentValues.put(ContentEntry.COLUMN_NAME_TARGET_ID, contentBase.getTargetId());
            }
        }

        return database.update(ContentEntry.TABLE_NAME, contentValues,
                SelectQueries.whereClauseContentId, getContentBaseIdStringArray(contentBase));
    }

    private static int updateNoteAttributes(SQLiteDatabase database, NoteData noteData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteEntry.COLUMN_NAME_DESCRIPTION, noteData.getDescription());
        contentValues.put(NoteEntry.COLUMN_NAME_PHOTOS_PATHS, Serializer.serializePathsList(noteData.getAttachedImagesPaths()));
        contentValues.put(NoteEntry.COLUMN_NAME_SOUNDS_PATHS, Serializer.serializePathsList(noteData.getAttachedAudioPaths()));

        return database.update(NoteEntry.TABLE_NAME, contentValues,
                SelectQueries.whereClauseNoteId, getContentBaseTargetId(noteData));
    }

    public static int updateListAttributes(SQLiteDatabase database, ListData listData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ListEntry.COLUMN_NAME_TASKS_SERIALIZED, Serializer.serializeListDataItems(listData.getList()));

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
