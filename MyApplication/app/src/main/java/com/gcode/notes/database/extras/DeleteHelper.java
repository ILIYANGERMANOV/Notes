package com.gcode.notes.database.extras;

import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.database.extras.queries.SelectQueries;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.AlarmUtils;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.async.delete.DeleteFileTask;

import java.util.ArrayList;

public class DeleteHelper {
    public static boolean deleteNotesList(SQLiteDatabase database, ArrayList<ContentBase> notesList) {
        for (ContentBase note : notesList) {
            if (deleteNote(database, note) == 0) {
                return false;
            }
        }
        return true;
    }

    public static int deleteNote(SQLiteDatabase database, ContentBase contentBase) {
        if (contentBase.getHasAttributesFlag()) {
            if (deleteAttributes(database, contentBase) == 0) {
                MyDebugger.log("Failed to delete attributes!");
            }
        }

        if (contentBase.hasReminder()) {
            //contentBase has reminder set, cancel its alarm
            AlarmUtils.cancelAlarm(MyApplication.getAppContext(), contentBase.getId());
        }

        return database.delete(ContentEntry.TABLE_NAME, SelectQueries.whereClauseContentId,
                new String[]{
                        Integer.toString(contentBase.getId())
                });
    }

    private static int deleteAttributes(SQLiteDatabase database, ContentBase note) {
        String[] whereArgs = new String[]{
                Integer.toString(note.getTargetId())
        };

        if (note instanceof NoteData) {
            //delete note attributes
            NoteData noteData = ((NoteData) note);
            String whereClause = NoteEntry._ID + " = ?";
            if (noteData.hasAttachedAudio()) {
                //!NOTE: in all when this method is called attachedAudioPath isn't encrypted
                new DeleteFileTask().execute(noteData.getAttachedAudioPath());
            }
            return database.delete(NoteEntry.TABLE_NAME, whereClause, whereArgs);
        } else {
            //delete list attributes
            String whereClause = ListEntry._ID + " = ?";
            return database.delete(ListEntry.TABLE_NAME, whereClause, whereArgs);
        }
    }
}
