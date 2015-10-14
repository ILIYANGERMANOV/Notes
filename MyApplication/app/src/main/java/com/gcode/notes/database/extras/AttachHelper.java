package com.gcode.notes.database.extras;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.ListData;
import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.database.NotesContract.PictureEntry;
import com.gcode.notes.database.NotesContract.SoundEntry;
import com.gcode.notes.serialization.Serializer;
import com.gcode.notes.extras.MyDebugger;

import java.net.URI;
import java.util.ArrayList;

public class AttachHelper {
    public static void attachNoteDataAttributes(SQLiteDatabase mDatabase, Cursor cursor, NoteData noteData) {
        Cursor notesCursor = mDatabase.rawQuery(Queries.SELECT_ALL_FROM_NOTES_FOR_ID,
                new String[]{
                        Integer.toString(cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TARGET_ID)))
                }
        );

        if (notesCursor.moveToFirst()) {
            String description = notesCursor.getString(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_DESCRIPTION));
            noteData.setDescription(description);
            if (noteHasAttachedPicture(notesCursor)) {
                //TODO: get picture path
                attachPictureToNote(mDatabase, cursor, noteData);
            }
            if (noteHasAttachedSound(notesCursor)) {
                //TODO: get audio path
                attachSoundToNote(mDatabase, cursor, noteData);
            }
        } else {
            MyDebugger.log("notesCursor.moveToFirst() with title", noteData.getTitle());
        }
        notesCursor.close();
    }

    public static void attachListDataAttributes(SQLiteDatabase mDatabase, Cursor cursor, ListData listData) {
        Cursor listCursor = mDatabase.rawQuery(Queries.SELECT_ALL_FROM_LISTS_FOR_ID,
                new String[]{
                        Integer.toString(cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TARGET_ID)))
                }
        );

        if (listCursor.moveToFirst()) {
            ArrayList<ListDataItem> mList = Serializer.parse(
                    listCursor.getString(listCursor.getColumnIndex(ListEntry.COLUMN_NAME_TASKS_SERIALIZED))
            );

            listData.setList(mList);
        } else {
            MyDebugger.log("listCursor.moveToFirst() with title", listData.getTitle());
        }

        listCursor.close();
    }

    private static boolean noteHasAttachedPicture(Cursor notesCursor) {
        return notesCursor.getInt(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_HAS_PICTURE)) != 0;
    }

    private static boolean noteHasAttachedSound(Cursor notesCursor) {
        return notesCursor.getInt(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_HAS_SOUND)) != 0;
    }

    private static void attachSoundToNote(SQLiteDatabase mDatabase, Cursor cursor, NoteData noteData) {
        Cursor soundCursor = mDatabase.rawQuery(Queries.SELECT_PATH_FROM_SOUNDS_FOR_NOTE_ID,
                new String[]{
                        Integer.toString(cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TARGET_ID)))
                }
        );

        if (soundCursor.moveToFirst()) {
            noteData.setAudioURI(URI.create(
                    soundCursor.getString(soundCursor.getColumnIndex(SoundEntry.COLUMN_NAME_PATH))
            ));
            MyDebugger.log("sound set");
        }

        soundCursor.close();
    }

    private static void attachPictureToNote(SQLiteDatabase mDatabase, Cursor cursor, NoteData noteData) {
        Cursor pictureCursor = mDatabase.rawQuery(Queries.SELECT_PATH_FROM_PICTURES_FOR_NOTE_ID,
                new String[]{
                        Integer.toString(cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TARGET_ID)))
                }
        );

        if (pictureCursor.moveToFirst()) {
            noteData.setImageURI(URI.create(
                    pictureCursor.getString(pictureCursor.getColumnIndex(PictureEntry.COLUMN_NAME_PATH))
            ));
            MyDebugger.log("picture set");
        }
        pictureCursor.close();
    }
}
