package com.gcode.notes.database.extras;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.gcode.notes.data.ListData;
import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.database.NotesContract.PictureEntry;
import com.gcode.notes.database.NotesContract.SoundEntry;
import com.gcode.notes.database.extras.queries.SelectQueries;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.serialization.Serializer;

import java.util.ArrayList;

public class AttachHelper {
    public static void attachNoteDataAttributes(SQLiteDatabase mDatabase, NoteData noteData) {
        MyDebugger.log("attaching note attributes");
        Cursor notesCursor = mDatabase.rawQuery(SelectQueries.SELECT_ALL_FROM_NOTES_FOR_ID,
                new String[]{
                        Integer.toString(noteData.getTargetId())
                }
        );

        if (notesCursor.moveToFirst()) {
            String description = notesCursor.getString(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_DESCRIPTION));
            noteData.setDescription(description);
            if (noteHasAttachedPicture(notesCursor)) {
                attachPictureToNote(mDatabase, noteData);
            }
            if (noteHasAttachedSound(notesCursor)) {
                attachSoundToNote(mDatabase, noteData);
            }
        } else {
            MyDebugger.log("notesCursor.moveToFirst() with title", noteData.getTitle());
        }
        notesCursor.close();
    }

    public static void attachListDataAttributes(SQLiteDatabase mDatabase, Cursor cursor, ListData listData) {
        Cursor listCursor = mDatabase.rawQuery(SelectQueries.SELECT_ALL_FROM_LISTS_FOR_ID,
                new String[]{
                        Integer.toString(cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TARGET_ID)))
                }
        );

        if (listCursor.moveToFirst()) {
            ArrayList<ListDataItem> mList = Serializer.parseListDataItems(
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

    private static void attachSoundToNote(SQLiteDatabase mDatabase, NoteData noteData) {
        //TODO: get audio path
        Cursor soundCursor = mDatabase.rawQuery(SelectQueries.SELECT_PATH_FROM_SOUNDS_FOR_NOTE_ID,
                new String[]{
                        Integer.toString(noteData.getId())
                }
        );

        if (soundCursor.moveToFirst()) {
            noteData.setAudioUri(Uri.parse(
                    soundCursor.getString(soundCursor.getColumnIndex(SoundEntry.COLUMN_NAME_PATH))
            ));
            MyDebugger.log("sound set");
        }

        soundCursor.close();
    }

    private static void attachPictureToNote(SQLiteDatabase mDatabase, NoteData noteData) {
        MyDebugger.log("attach note picture ID", noteData.getId());
        Cursor pictureCursor = mDatabase.rawQuery(SelectQueries.SELECT_PATH_FROM_PICTURES_FOR_NOTE_ID,
                new String[]{
                        Integer.toString(noteData.getId())
                }
        );

        if (pictureCursor.moveToFirst()) {
            String serializedPathsList = pictureCursor.getString(pictureCursor.getColumnIndex(PictureEntry.COLUMN_NAME_PATHS_LIST));
            ArrayList<String> attachedImagesPaths = Serializer.parseAttachedImagesList(serializedPathsList);
            noteData.setAttachedImagesPaths(attachedImagesPaths);
            MyDebugger.log("picture set");
        } else {
            MyDebugger.log("pictureCursor is empty");
        }
        pictureCursor.close();
    }
}
