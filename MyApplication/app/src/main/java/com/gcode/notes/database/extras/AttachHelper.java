package com.gcode.notes.database.extras;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.main.ListData;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.database.extras.queries.SelectQueries;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.serialization.Serializer;

public class AttachHelper {
    public static void attachNoteDataAttributes(SQLiteDatabase database, NoteData noteData) {
        Cursor notesCursor = database.rawQuery(SelectQueries.SELECT_ALL_FROM_NOTES_FOR_ID,
                new String[]{
                        Integer.toString(noteData.getTargetId())
                }
        );

        if (notesCursor.moveToFirst()) {
            String description = notesCursor.getString(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_DESCRIPTION));
            noteData.setDescription(description);
            String columnValueHolder = notesCursor.getString(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_PHOTOS_PATHS));
            if (columnValueHolder != null) {
                //there are attached images, set them to note
                noteData.setAttachedImagesPaths(Serializer.parseImagesPathsList(columnValueHolder));
            }
            columnValueHolder = notesCursor.getString(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_AUDIO_PATH));
            noteData.setAttachedAudioPath(columnValueHolder);

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
            String tasksSerializedString = listCursor.getString(listCursor.getColumnIndex(ListEntry.COLUMN_NAME_TASKS_SERIALIZED));
            if (tasksSerializedString != null) {
                //list has serialized tasks, parse and attach them
                listData.setList(Serializer.parseListDataItems(tasksSerializedString));
            }
        } else {
            MyDebugger.log("listCursor.moveToFirst() with title", listData.getTitle());
        }

        listCursor.close();
    }
}
