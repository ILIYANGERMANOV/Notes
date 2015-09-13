package com.gcode.notes.database.extras;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.Constants;

import java.util.ArrayList;

public class Builder {
    public static ArrayList<ContentBase> buildItemList(SQLiteDatabase mDatabase, Cursor cursor) {
        ArrayList<ContentBase> mNotesList = new ArrayList<>();

        while (cursor.moveToNext()) {
            ContentBase currentItem = null;
            if (Extractor.getItemType(cursor) == Constants.TYPE_NOTE) {
                //NoteData
                currentItem = Builder.buildNoteData(mDatabase, cursor);
            } else {
                //TODO: add ListData
            }
            mNotesList.add(currentItem);
        }

        cursor.close();

        return mNotesList;
    }

    public static ContentBase buildSingleItem(SQLiteDatabase mDatabase, Cursor cursor) {
        ContentBase mNote = null;
        if (cursor.moveToFirst()) {
            if (Extractor.getItemType(cursor) == Constants.TYPE_NOTE) {
                mNote = Builder.buildNoteData(mDatabase, cursor);
            } else {
                //TODO: init List
            }
        }

        cursor.close();

        return mNote;
    }

    private static ContentBase buildNoteData(SQLiteDatabase mDatabase, Cursor cursor) {
        NoteData noteData = (NoteData) Extractor.extractNoteDataFromContent(cursor);

        noteData.setType(Constants.TYPE_NOTE);

        if (noteData.hasAttributes()) {
            AttachHelper.attachNoteAttributes(mDatabase, cursor, noteData);
        }
        return noteData;
    }
}
