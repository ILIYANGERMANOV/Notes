package com.gcode.notes.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.data.note.list.ListData;
import com.gcode.notes.database.extras.Builder;
import com.gcode.notes.database.extras.DeleteHelper;
import com.gcode.notes.database.extras.InsertHelper;
import com.gcode.notes.database.extras.UpdateHelper;
import com.gcode.notes.database.extras.Validator;
import com.gcode.notes.database.extras.queries.SelectQueries;
import com.gcode.notes.extras.values.Constants;

import java.util.ArrayList;

public class DatabaseController {
    private SQLiteDatabase mDatabase;

    public DatabaseController(Context context) {
        NotesDbHelper mHelper = new NotesDbHelper(context);
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
    public void updateNoteMode(ContentBase contentBase) {
        UpdateHelper.updateNoteMode(mDatabase, contentBase, contentBase.getMode());
    }

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
        return UpdateHelper.updateNoteModeAndExpirationDate(mDatabase, contentBase, newMode, true) > 0;
    }

    public boolean restoreNoteFromBin(ContentBase contentBase) {
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
        return UpdateHelper.updateNoteModeAndExpirationDate(mDatabase, contentBase, newMode, false) > 0;
    }

    public void swapNotesPosition(ContentBase noteA, ContentBase noteB) {
        UpdateHelper.updateNoteOrderId(mDatabase, noteA, noteB.getOrderId());
        UpdateHelper.updateNoteOrderId(mDatabase, noteB, noteA.getOrderId());

        int orderIdHolder = noteA.getOrderId();
        noteA.setOrderId(noteB.getOrderId());
        noteB.setOrderId(orderIdHolder);
    }

    public boolean updateNote(ContentBase contentBase) {
        return UpdateHelper.updateNote(mDatabase, contentBase) > 0;
    }

    public boolean updateListAttributes(ListData listData) {
        return UpdateHelper.updateListAttributes(mDatabase, listData) > 0;
    }

    public boolean removeAttachedAudioFromNote(int targetId) {
        return UpdateHelper.removeAttachedAudioFromNote(mDatabase, targetId) > 0;
    }
    //UPDATES------------------------------------------------------------------------------------------------------


    //DELETE-------------------------------------------------------------------------------------------------------
    public boolean emptyRecyclerBin() {
        return DeleteHelper.deleteNotesList(mDatabase, getAllDeletedNotes());
    }

    public boolean deleteExpiredNotes() {
        Cursor cursor = mDatabase.rawQuery(SelectQueries.SELECT_ALL_EXPIRED_NOTES, null);
        return DeleteHelper.deleteNotesList(mDatabase, Builder.buildItemList(mDatabase, cursor));
    }

    public boolean deleteNoteFromBin(ContentBase note) {
        return DeleteHelper.deleteNote(mDatabase, note) != 0;
    }
    //DELETE------------------------------------------------------------------------------------------------------------

    //PRIVATE----------------------------------------------------------------------------------------------------

    /**
     * @param modes - MUST CONTAIN AT LEAST ONE MODE; to order by expiration date pass MODE_DELETED_NORMAL as first argument
     * @return Cursor with the result of the query
     */
    private Cursor getCursorForAllItemsFromContentForModes(int... modes) {
        return mDatabase.rawQuery(
                SelectQueries.selectAllItemsFromContentForModes(modes.length, modes[0] != Constants.MODE_DELETED_NORMAL),
                SelectQueries.buildSelectionArgs(modes)
        );
    }

    /**
     * @param modes - MUST CONTAIN AT LEAST ONE MODE; to order by expiration date pass MODE_DELETED_NORMAL as first argument
     * @return Cursor with the result of the query
     */
    private Cursor getCursorForLastItemFromContentForMode(int... modes) {
        return mDatabase.rawQuery(
                SelectQueries.selectLastItemFromContentForModes(modes.length, modes[0] != Constants.MODE_DELETED_NORMAL),
                SelectQueries.buildSelectionArgs(modes)
        );
    }
}
