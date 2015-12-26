package com.gcode.notes.database.extras;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.data.note.list.ListData;
import com.gcode.notes.data.note.NoteData;
import com.gcode.notes.extras.values.Constants;

import java.util.ArrayList;

public class Builder {
    public static ArrayList<ContentBase> buildItemList(SQLiteDatabase mDatabase, Cursor cursor) {
        ArrayList<ContentBase> mNotesList = new ArrayList<>();

        while (cursor.moveToNext()) {
            ContentBase currentItem;
            if (Extractor.getItemType(cursor) == Constants.TYPE_NOTE) {
                //NoteData
                currentItem = Builder.buildNoteData(mDatabase, cursor);
            } else {
                //ListData
                currentItem = Builder.buildListData(mDatabase, cursor);
            }
            mNotesList.add(currentItem);
        }

        cursor.close();

        return mNotesList;
    }

    public static ContentBase buildSingleItem(SQLiteDatabase database, Cursor cursor) {
        ContentBase contentBase = null;
        if (cursor.moveToFirst()) {
            if (Extractor.getItemType(cursor) == Constants.TYPE_NOTE) {
                contentBase = Builder.buildNoteData(database, cursor);
            } else {
                contentBase = Builder.buildListData(database, cursor);
            }
        }

        cursor.close();

        return contentBase;
    }

    private static ContentBase buildListData(SQLiteDatabase database, Cursor cursor) {
        ListData listData = Extractor.extractListDataFromContent(cursor);

        listData.setType(Constants.TYPE_LIST);

        if (listData.getHasAttributesFlag()) {
            AttachHelper.attachListDataAttributes(database, cursor, listData);
        }
        return listData;
    }


    private static ContentBase buildNoteData(SQLiteDatabase database, Cursor contentCursor) {
        NoteData noteData = Extractor.extractNoteDataFromContent(contentCursor);

        noteData.setType(Constants.TYPE_NOTE);

        if (noteData.getHasAttributesFlag()) {
            AttachHelper.attachNoteDataAttributes(database, noteData);
        }
        return noteData;
    }
}
