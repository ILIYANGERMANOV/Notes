package com.gcode.notes.database.extras;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.database.NotesContract.PictureEntry;
import com.gcode.notes.database.NotesContract.SoundEntry;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.DateUtils;
import com.gcode.notes.extras.Serializer;
import com.gcode.notes.extras.MyDebugger;

import java.net.URI;

public class InsertHelper {

    public static long insertNote(SQLiteDatabase mDatabase, ContentBase contentBase) {
        if (contentBase.hasAttributes()) {
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

        if (contentBase.hasAttributes()) {
            //target id - the id corresponding in the relevant attribute table (Notes/Lists),
            // which is already inserted successfully for the current item
            String tableName = contentBase.getType() == Constants.TYPE_NOTE ? NoteEntry.TABLE_NAME : ListEntry.TABLE_NAME;
            contentValues.put(ContentEntry.COLUMN_NAME_TARGET_ID, Selector.getLastRowFromTable(mDatabase, tableName));
        }

        contentValues.put(ContentEntry.COLUMN_NAME_ORDER_ID, Selector.getFirstOrNextIdFromContent(mDatabase));
        contentValues.put(ContentEntry.COLUMN_NAME_TITLE, contentBase.getTitle());
        contentValues.put(ContentEntry.COLUMN_NAME_MODE, contentBase.getMode());
        contentValues.put(ContentEntry.COLUMN_NAME_TYPE, contentBase.getType());
        contentValues.put(ContentEntry.COLUMN_NAME_ATTRIBUTES, contentBase.hasAttributes());
        contentValues.put(ContentEntry.COLUMN_NAME_REMINDER, contentBase.getReminderString());
        contentValues.put(ContentEntry.COLUMN_NAME_CREATION_DATE, DateUtils.getCurrentTime());
        contentValues.putNull(ContentEntry.COLUMN_NAME_EXPIRATION_DATE);

        return mDatabase.insert(ContentEntry.TABLE_NAME, ContentEntry.COLUMN_NAME_EXPIRATION_DATE, contentValues);
    }

    private static void insertAttributesInLists(SQLiteDatabase mDatabase, ContentBase contentBase) {
        ListData listData = (ListData) contentBase;
        ContentValues contentValues = new ContentValues();

        contentValues.put(ListEntry.COLUMN_NAME_TASKS_SERIALIZED, Serializer.serialize(listData.getList()));

        if (mDatabase.insert(ListEntry.TABLE_NAME, null, contentValues) == Constants.DATABASE_ERROR) {
            contentBase.setAttributes(false);
            MyDebugger.log("ERROR INSERTING LIST ATTRIBUTES!");
        }
    }

    private static void insertAttributesInNotes(SQLiteDatabase mDatabase, ContentBase contentBase) {
        NoteData noteData = (NoteData) contentBase;
        ContentValues contentValues = new ContentValues();

        contentValues.put(NoteEntry.COLUMN_NAME_DESCRIPTION, noteData.getDescription());
        contentValues.put(NoteEntry.COLUMN_NAME_HAS_PICTURE, noteData.hasAttachedImage());
        contentValues.put(NoteEntry.COLUMN_NAME_HAS_SOUND, noteData.hasAttachedAudio());

        if (mDatabase.insert(NoteEntry.TABLE_NAME, null, contentValues) != Constants.DATABASE_ERROR) {
            if (noteData.hasAttachedImage()) {
                insertPicture(mDatabase, noteData.getImageURI());
            }
            if (noteData.hasAttachedAudio()) {
                insertAudio(mDatabase, noteData.getAudioURI());
            }
        } else {
            contentBase.setAttributes(false);
            MyDebugger.log("ERROR INSERTING NOTE ATTRIBUTES!");
        }
    }

    private static void insertPicture(SQLiteDatabase mDatabase, URI imageURI) {
        //TODO: insert picture
        ContentValues contentValues = new ContentValues();
        contentValues.put(PictureEntry.COLUMN_NAME_PATH, imageURI.toString());
        contentValues.put(PictureEntry.COLUMN_NAME_NOTE_ID, Selector.getFirstOrNextIdFromContent(mDatabase));

        mDatabase.insert(PictureEntry.TABLE_NAME, null, contentValues);
    }

    private static void insertAudio(SQLiteDatabase mDatabase, URI audioURI) {
        //TODO: insert audio
        ContentValues contentValues = new ContentValues();
        contentValues.put(SoundEntry.COLUMN_NAME_PATH, audioURI.toString());
        contentValues.put(SoundEntry.COLUMN_NAME_NOTE_ID, Selector.getFirstOrNextIdFromContent(mDatabase));

        mDatabase.insert(SoundEntry.TABLE_NAME, null, contentValues);
    }
}
