package com.gcode.notes.database.extras;

import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.main.ContentBase;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
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
        if (note.getHasAttributesFlag()) {
            if (deleteAttributes(mDatabase, note) == 0) {
                MyDebugger.log("Failed to delete attributes!");
            }
        }

        return mDatabase.delete(ContentEntry.TABLE_NAME, SelectQueries.whereClauseContentId,
                new String[]{
                        Integer.toString(note.getId())
                });
    }

    private static int deleteAttributes(SQLiteDatabase mDatabase, ContentBase note) {
        String[] whereArgs = new String[]{
                Integer.toString(note.getTargetId())
        };

        if (note instanceof NoteData) {
            //delete note attributes
            String whereClause = NoteEntry._ID + " = ?";
            return mDatabase.delete(NoteEntry.TABLE_NAME, whereClause, whereArgs);
        } else {
            //delete list attributes
            String whereClause = ListEntry._ID + " = ?";
            return mDatabase.delete(ListEntry.TABLE_NAME, whereClause, whereArgs);
        }
    }
}
