package com.gcode.notes.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.ContentBase;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.extras.Builder;
import com.gcode.notes.database.extras.InsertHelper;
import com.gcode.notes.database.extras.Queries;
import com.gcode.notes.database.extras.Selector;
import com.gcode.notes.extras.Constants;

import java.util.ArrayList;

public class DatabaseController {
    private NotesDbHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DatabaseController(Context context) {
        mHelper = new NotesDbHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public ArrayList<ContentBase> getAllNotesForMode(int mode) {
        ArrayList<ContentBase> mNotesList = new ArrayList<>();

        String[] selectionArgs = {
                Integer.toString(mode)
        };

        Cursor cursor = mDatabase.rawQuery(Queries.SELECT_ALL_FROM_CONTENT_FOR_MODE, selectionArgs);

        while (cursor.moveToNext()) {
            ContentBase currentItem = null;
            if (cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TYPE)) == Constants.TYPE_NOTE) {
                //NoteData
                currentItem = Builder.buildNote(mDatabase, cursor);
            } else {
                //TODO: add ListData
            }
            mNotesList.add(currentItem);
        }

        cursor.close();

        return mNotesList;
    }

    public long insertNote(ContentBase contentBase) {
        if (!contentBase.isValidTitle()) {
            contentBase.setTitle(generateItemTitle());
        }
        mDatabase.beginTransaction();

        if (contentBase.hasAttributes()) {
            if (contentBase.getType() == Constants.TYPE_NOTE) {
                InsertHelper.insertAttributesInNotes(mDatabase, contentBase);
            } else {
                //TODO: insert list
            }
        }

        long newlyInsertedRow = InsertHelper.insertMainContent(mDatabase, contentBase);

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();

        return newlyInsertedRow;
    }

    private String generateItemTitle() {
        int noteNumber = Selector.getFirstOrNextIdFromContent(mDatabase);
        return "Note " + Integer.toString(noteNumber != 0 ? noteNumber : 1);
    }
}
