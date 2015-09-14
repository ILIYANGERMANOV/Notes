package com.gcode.notes.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.ContentBase;
import com.gcode.notes.database.extras.Builder;
import com.gcode.notes.database.extras.DeleteHelper;
import com.gcode.notes.database.extras.InsertHelper;
import com.gcode.notes.database.extras.Queries;
import com.gcode.notes.database.extras.UpdateHelper;
import com.gcode.notes.database.extras.Validator;
import com.gcode.notes.extras.Constants;

import java.util.ArrayList;

public class DatabaseController {
    private NotesDbHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DatabaseController(Context context) {
        mHelper = new NotesDbHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    //GETTERS ----------------------------------------------------------------------------------------------------------
    public ArrayList<ContentBase> getAllVisibleNotes() {
        Cursor cursor = getCursorForAllItemsFromContentForModes(Constants.MODE_NORMAL, Constants.MODE_IMPORTANT);
        return Builder.buildItemList(mDatabase, cursor);
    }

    public ContentBase getLastVisibleNote() {
        Cursor cursor = getCursorForLastItemFromContentForMode(Constants.MODE_NORMAL, Constants.MODE_IMPORTANT);
        return Builder.buildSingleItem(mDatabase, cursor);
    }

    public ArrayList<ContentBase> getAllImportantNotes() {
        Cursor cursor = getCursorForAllItemsFromContentForModes(Constants.MODE_IMPORTANT);
        return Builder.buildItemList(mDatabase, cursor);
    }

    public ContentBase getLastImportantNote() {
        Cursor cursor = getCursorForLastItemFromContentForMode(Constants.MODE_IMPORTANT);
        return Builder.buildSingleItem(mDatabase, cursor);
    }

    public ArrayList<ContentBase> getAllPrivateNotes() {
        Cursor cursor = getCursorForAllItemsFromContentForModes(Constants.MODE_PRIVATE);
        return Builder.buildItemList(mDatabase, cursor);
    }

    public ContentBase getLastPrivateNote() {
        Cursor cursor = getCursorForLastItemFromContentForMode(Constants.MODE_PRIVATE);
        return Builder.buildSingleItem(mDatabase, cursor);
    }

    public ArrayList<ContentBase> getAllDeletedNotes() {
        Cursor cursor = getCursorForAllItemsFromContentForModes(Constants.MODE_DELETED_NORMAL,
                Constants.MODE_DELETED_IMPORTANT);
        return Builder.buildItemList(mDatabase, cursor);
    }

    public ContentBase getLastDeletedNote() {
        Cursor cursor = getCursorForLastItemFromContentForMode(Constants.MODE_DELETED_NORMAL,
                Constants.MODE_DELETED_IMPORTANT);
        return Builder.buildSingleItem(mDatabase, cursor);
    }
    //GETTERS ----------------------------------------------------------------------------------------------------------

    //INSERTS--------------------------------------------------------------------------------------------------
    public long insertNote(ContentBase contentBase) {
        Validator.validateTitle(mDatabase, contentBase);
        mDatabase.beginTransaction();
        long newlyInsertedRow = InsertHelper.insertNote(mDatabase, contentBase);
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return newlyInsertedRow;
    }
    //INSERTS--------------------------------------------------------------------------------------------------

    //UPDATES------------------------------------------------------------------------------------------------------
    public boolean deleteNote(ContentBase contentBase) {
        int newMode;
        switch (contentBase.getMode()) {
            case Constants.MODE_NORMAL:
                newMode = Constants.MODE_DELETED_NORMAL;
                break;
            case Constants.MODE_IMPORTANT:
                newMode = Constants.MODE_DELETED_IMPORTANT;
                break;
            default:
                return false;
        }
        return UpdateHelper.updateNoteMode(mDatabase, contentBase, newMode) > 0;
    }

    public boolean revertNoteFromBin(ContentBase contentBase) {
        int newMode;
        switch (contentBase.getMode()) {
            case Constants.MODE_DELETED_NORMAL:
                newMode = Constants.MODE_NORMAL;
                break;
            case Constants.MODE_DELETED_IMPORTANT:
                newMode = Constants.MODE_IMPORTANT;
                break;
            default:
                return false;
        }
        return UpdateHelper.updateNoteMode(mDatabase, contentBase, newMode) > 0;
    }

    public void swapNotesPosition(ContentBase noteA, ContentBase noteB) {
        UpdateHelper.updateNoteOrderId(mDatabase, noteA, noteB.getOrderId());
        UpdateHelper.updateNoteOrderId(mDatabase, noteB, noteA.getOrderId());

        int orderIdHolder = noteA.getOrderId();
        noteA.setOrderId(noteB.getOrderId());
        noteB.setOrderId(orderIdHolder);
    }
    //UPDATES------------------------------------------------------------------------------------------------------


    //DELETE-------------------------------------------------------------------------------------------------------

    //DELETE-------------------------------------------------------------------------------------------------------
    public boolean emptyRecyclerBin() {
        return DeleteHelper.deleteNotesList(mDatabase, getAllDeletedNotes());
    }

    public boolean deleteNoteFromBin(ContentBase note) {
        return DeleteHelper.deleteNote(mDatabase, note) != 0;
    }
    //DELETE------------------------------------------------------------------------------------------------------------

    //PRIVATE----------------------------------------------------------------------------------------------------
    private Cursor getCursorForAllItemsFromContentForModes(int... modes) {
        return mDatabase.rawQuery(Queries.selectAllItemsFromContentForModes(modes.length), Queries.buildSelectionArgs(modes));
    }

    private Cursor getCursorForLastItemFromContentForMode(int... modes) {
        return mDatabase.rawQuery(Queries.selectLastItemFromContentForModes(modes.length), Queries.buildSelectionArgs(modes));
    }
}
