package com.gcode.notes.database.extras;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.main.ListData;
import com.gcode.notes.data.extras.ListDataItem;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.database.extras.queries.SelectQueries;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.serialization.Serializer;

import java.util.ArrayList;

public class AttachHelper {
    public static void attachNoteDataAttributes(SQLiteDatabase mDatabase, NoteData noteData) {
        Cursor notesCursor = mDatabase.rawQuery(SelectQueries.SELECT_ALL_FROM_NOTES_FOR_ID,
                new String[]{
                        Integer.toString(noteData.getTargetId())
                }
        );

        if (notesCursor.moveToFirst()) {
            String description = notesCursor.getString(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_DESCRIPTION));
            noteData.setDescription(description);
            String stringHolder = notesCursor.getString(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_PHOTOS_PATHS));
            noteData.setAttachedImagesPaths(Serializer.parseImagesPathsList(stringHolder));
            stringHolder = notesCursor.getString(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_AUDIO_PATH));
            noteData.setAttachedAudioPath(stringHolder);

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
}
