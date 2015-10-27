package com.gcode.notes.database.extras;

import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.database.NotesContract.PictureEntry;
import com.gcode.notes.database.NotesContract.SoundEntry;
import com.gcode.notes.database.extras.queries.SelectQueries;
import com.gcode.notes.extras.MyDebugger;

import java.util.ArrayList;

public class DeleteHelper {
    public static boolean deleteNotesList(SQLiteDatabase mDatabase, ArrayList<ContentBase> notesList) {
        for (ContentBase note : notesList) {
            if (deleteNote(mDatabase, note) == 0) {
                return false;
            }
        }
        return true;
    }

    public static int deleteNote(SQLiteDatabase mDatabase, ContentBase note) {
        if (note.hasAttributes()) {
            if (deleteAttributes(mDatabase, note) == 0) {
                MyDebugger.log("Failed to delete attributes!");
            }
        }

        return mDatabase.delete(ContentEntry.TABLE_NAME, SelectQueries.whereClauseContentId, new String[]{
                Integer.toString(note.getId())
        });
    }

    private static int deleteAttributes(SQLiteDatabase mDatabase, ContentBase note) {
        String[] whereArgs = new String[]{
                Integer.toString(note.getTargetId())
        };

        if (note instanceof NoteData) {
            NoteData noteData = (NoteData) note;
            if (noteData.hasAttachedImage()) {
                if (deleteAttachedImage(mDatabase, note.getId()) == 0) {
                    MyDebugger.log("Failed to delete attached image!");
                }
            }
            if (noteData.hasAttachedAudio()) {
                if (deleteAttachedSound(mDatabase, note.getId()) == 0) {
                    MyDebugger.log("Failed to delete attached sound!");
                }
            }
            String whereClause = NoteEntry._ID + " = ?";
            return mDatabase.delete(NoteEntry.TABLE_NAME, whereClause, whereArgs);
        } else {
            String whereClause = ListEntry._ID + " = ?";
            return mDatabase.delete(ListEntry.TABLE_NAME, whereClause, whereArgs);
        }
    }

    private static int deleteAttachedSound(SQLiteDatabase mDatabase, int noteId) {
        String whereClause = SoundEntry.COLUMN_NAME_NOTE_ID + " = ?";
        return mDatabase.delete(SoundEntry.TABLE_NAME, whereClause, new String[]{
                Integer.toString(noteId)
        });
    }

    private static int deleteAttachedImage(SQLiteDatabase mDatabase, int noteId) {
        String whereClause = PictureEntry.COLUMN_NAME_NOTE_ID + " = ?";
        return mDatabase.delete(PictureEntry.TABLE_NAME, whereClause, new String[]{
                Integer.toString(noteId)
        });
    }
}
