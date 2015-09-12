package com.gcode.notes.database.extras;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.Constants;

public class Builder {
    public static ContentBase buildNote(SQLiteDatabase mDatabase, Cursor cursor) {
        NoteData noteData = (NoteData) Extractor.extractNoteDataFromContent(cursor);

        noteData.setType(Constants.TYPE_NOTE);

        if (noteData.hasAttributes()) {
            AttachHelper.attachNoteAttributes(mDatabase, cursor, noteData);
        }
        return noteData;
    }
}
