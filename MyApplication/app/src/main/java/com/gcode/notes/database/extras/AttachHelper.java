package com.gcode.notes.database.extras;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.NoteData;
import com.gcode.notes.database.NotesContract;
import com.gcode.notes.extras.MyDebugger;

import java.net.URI;

public class AttachHelper {
    public static void attachNoteAttributes(SQLiteDatabase mDatabase, Cursor cursor, NoteData noteData) {
        Cursor notesCursor = mDatabase.rawQuery(Queries.SELECT_ALL_FROM_NOTES_FOR_ID,
                new String[]{
                        Integer.toString(cursor.getInt(cursor.getColumnIndex(NotesContract.ContentEntry.COLUMN_NAME_TARGET_ID)))
                }
        );

        if (notesCursor.moveToFirst()) {
            String description = notesCursor.getString(notesCursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_NAME_DESCRIPTION));
            noteData.setDescription(description);
            if (noteHasAttachedPicture(notesCursor)) {
                //TODO: get picture path
                attachPictureToNote(mDatabase, cursor, noteData);
            }
            if (noteHasAttachedSound(notesCursor)) {
                //TODO: get audio path
                attachSoundToNote(mDatabase, cursor, noteData);
            }
            notesCursor.close();
        } else {
            MyDebugger.log("notesCursor.moveToFirst() with title", noteData.getTitle());
        }
    }

    private static boolean noteHasAttachedPicture(Cursor notesCursor) {
        return notesCursor.getInt(notesCursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_NAME_HAS_PICTURE)) != 0;
    }

    private static boolean noteHasAttachedSound(Cursor notesCursor) {
        return notesCursor.getInt(notesCursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_NAME_HAS_SOUND)) != 0;
    }

    private static void attachSoundToNote(SQLiteDatabase mDatabase, Cursor cursor, NoteData noteData) {
        Cursor soundCursor = mDatabase.rawQuery(Queries.SELECT_PATH_FROM_SOUNDS_FOR_NOTE_ID,
                new String[]{
                        Integer.toString(cursor.getInt(cursor.getColumnIndex(NotesContract.ContentEntry.COLUMN_NAME_TARGET_ID)))
                }
        );

        if (soundCursor.moveToFirst()) {
            noteData.setAudioURI(URI.create(
                    soundCursor.getString(soundCursor.getColumnIndex(NotesContract.SoundEntry.COLUMN_NAME_PATH))
            ));
            MyDebugger.log("sound set");
        }

        soundCursor.close();
    }

    private static void attachPictureToNote(SQLiteDatabase mDatabase, Cursor cursor, NoteData noteData) {
        Cursor pictureCursor = mDatabase.rawQuery(Queries.SELECT_PATH_FROM_PICTURES_FOR_NOTE_ID,
                new String[]{
                        Integer.toString(cursor.getInt(cursor.getColumnIndex(NotesContract.ContentEntry.COLUMN_NAME_TARGET_ID)))
                }
        );

        if (pictureCursor.moveToFirst()) {
            noteData.setImageURI(URI.create(
                    pictureCursor.getString(pictureCursor.getColumnIndex(NotesContract.PictureEntry.COLUMN_NAME_PATH))
            ));
            MyDebugger.log("picture set");
        }
        pictureCursor.close();
    }
}
