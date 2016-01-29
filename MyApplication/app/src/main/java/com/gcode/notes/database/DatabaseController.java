package com.gcode.notes.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.database.extras.DataBuilder;
import com.gcode.notes.database.extras.DeleteHelper;
import com.gcode.notes.database.extras.InsertHelper;
import com.gcode.notes.database.extras.Selector;
import com.gcode.notes.database.extras.UpdateHelper;
import com.gcode.notes.database.extras.Validator;
import com.gcode.notes.database.extras.queries.SelectQueries;
import com.gcode.notes.extras.values.Constants;

import java.util.ArrayList;

public class DatabaseController {
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseController(Context context) {
        mContext = context;
        NotesDbHelper mHelper = new NotesDbHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    //GETTERS ----------------------------------------------------------------------------------------------------------
    public ArrayList<ContentBase> getAllVisibleNotes() {
        Cursor cursor = getCursorForAllNoteForModes(Constants.MODE_NORMAL, Constants.MODE_IMPORTANT);
        return DataBuilder.buildItemList(mDatabase, cursor);
    }

    public ContentBase getLastVisibleNote() {
        Cursor cursor = getCursorForLastItemFromContentForMode(Constants.MODE_NORMAL, Constants.MODE_IMPORTANT);
        return DataBuilder.buildSingleItem(mDatabase, cursor);
    }

    public ArrayList<ContentBase> getAllImportantNotes() {
        Cursor cursor = getCursorForAllNoteForModes(Constants.MODE_IMPORTANT);
        return DataBuilder.buildItemList(mDatabase, cursor);
    }

    public ContentBase getLastImportantNote() {
        Cursor cursor = getCursorForLastItemFromContentForMode(Constants.MODE_IMPORTANT);
        return DataBuilder.buildSingleItem(mDatabase, cursor);
    }

    public ArrayList<ContentBase> getAllPrivateNotes() {
        Cursor cursor = getCursorForAllNoteForModes(Constants.MODE_PRIVATE);
        return DataBuilder.buildItemList(mDatabase, cursor);
    }

    public ArrayList<ContentBase> getAllNotesWithReminder() {
        Cursor cursor = getCursorForAllNoteWithReminder();
        return DataBuilder.buildItemList(mDatabase, cursor);
    }

    public ContentBase getLastPrivateNote() {
        Cursor cursor = getCursorForLastItemFromContentForMode(Constants.MODE_PRIVATE);
        return DataBuilder.buildSingleItem(mDatabase, cursor);
    }

    public ArrayList<ContentBase> getAllDeletedNotes() {
        Cursor cursor = getCursorForAllNoteForModes(Constants.MODE_DELETED_NORMAL,
                Constants.MODE_DELETED_IMPORTANT);
        return DataBuilder.buildItemList(mDatabase, cursor);
    }

    public ContentBase getLastDeletedNote() {
        Cursor cursor = getCursorForLastItemFromContentForMode(Constants.MODE_DELETED_NORMAL,
                Constants.MODE_DELETED_IMPORTANT);
        return DataBuilder.buildSingleItem(mDatabase, cursor);
    }

    public ContentBase getNoteFromId(int noteId) {
        Cursor cursor = mDatabase.rawQuery(
                SelectQueries.SELECT_NOTE_WITH_ID,
                new String[]{
                        Integer.toString(noteId)
                }
        );
        return DataBuilder.buildSingleItem(mDatabase, cursor);
    }
    //GETTERS ----------------------------------------------------------------------------------------------------------

    //INSERTS--------------------------------------------------------------------------------------------------

    /**
     * Inserts note in database. !WARN: Note order id and target id are NOT set to contentBase
     *
     * @param contentBase note to be inserted
     * @return the newly inserted row id or Constants.DATABASE_ERROR
     */
    public long insertNote(ContentBase contentBase) {
        mDatabase.beginTransaction();
        long newlyInsertedRow = InsertHelper.insertNote(mDatabase, contentBase);
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return newlyInsertedRow;
    }
    //INSERTS--------------------------------------------------------------------------------------------------

    //UPDATES------------------------------------------------------------------------------------------------------
    public void updateNoteReminder(ContentBase contentBase) {
        UpdateHelper.updateNoteReminder(mDatabase, contentBase);
    }

    public void updateNoteMode(ContentBase contentBase) {
        UpdateHelper.updateNoteMode(mDatabase, contentBase);
    }

    /**
     * Sets mode to deleted in both contentBase and database.
     *
     * @param contentBase note which mode will be set to deleted
     * @return whether the operation was successful
     */
    public boolean deleteNote(ContentBase contentBase) {
        int newMode = contentBase.setAndReturnDeletedMode();
        return newMode != Constants.ERROR &&
                UpdateHelper.updateNoteModeAndExpirationDate(mDatabase, contentBase, newMode, true) > 0;
    }

    /**
     * Sets note mode to restored (normal/important) in both contentBase and database.
     *
     * @param contentBase note which mode will be set to MODE_NORMAL / MODE_IMPORTANT
     * @return whether the operation was successful
     */
    public boolean restoreNoteFromBin(ContentBase contentBase) {
        int newMode = contentBase.setAndReturnRestoredMode();
        return newMode != Constants.ERROR &&
                UpdateHelper.updateNoteModeAndExpirationDate(mDatabase, contentBase, newMode, false) > 0;
    }

    public void swapNotesPosition(ContentBase noteA, ContentBase noteB) {
        UpdateHelper.updateNoteOrderId(mDatabase, noteA, noteB.getOrderId());
        UpdateHelper.updateNoteOrderId(mDatabase, noteB, noteA.getOrderId());

        int orderIdHolder = noteA.getOrderId();
        noteA.setOrderId(noteB.getOrderId());
        noteB.setOrderId(orderIdHolder);
    }

    public boolean updateNote(ContentBase contentBase) {
        return UpdateHelper.updateNote(mContext, mDatabase, contentBase, false) > 0;
    }

    public boolean updateNote(ContentBase contentBase, boolean updateCreationDate) {
        return UpdateHelper.updateNote(mContext, mDatabase, contentBase, updateCreationDate) > 0;
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
        return DeleteHelper.deleteNotesList(mDatabase, DataBuilder.buildItemList(mDatabase, cursor));
    }

    public boolean deleteNotePermanently(ContentBase note) {
        return DeleteHelper.deleteNote(mDatabase, note) != 0;
    }

    public boolean deleteAllPrivateNotes() {
        return DeleteHelper.deleteNotesList(mDatabase, getAllPrivateNotes());
    }
    //DELETE------------------------------------------------------------------------------------------------------------


    //OTHER-------------------------------------------------------------------------------------------------------------

    /**
     * Validates note's title and if it is not okay, valid one is generate.
     * For new notes (not existing) id is set accordingly.
     *
     * @param contentBase  note which id and title are validated
     * @param existingNote whether the note already exists (is opened in edit mode)
     */
    public void validateNote(ContentBase contentBase, boolean existingNote) {
        if (!existingNote) {
            //new note, which is note inserted yet, so if everything runs correctly
            //its id will be the next, get it and set it to content base so reminder can work correctly
            contentBase.setId(Selector.getFirstOrNextIdFromContent(mDatabase));
        }

        //Title must be always validated, cuz even when updating note it can be deleted
        //and this will result in reminder notification with empty title
        Validator.validateTitle(mDatabase, contentBase, existingNote);
    }
    //OTHER-------------------------------------------------------------------------------------------------------------

    //PRIVATE----------------------------------------------------------------------------------------------------

    /**
     * @param modes - MUST CONTAIN AT LEAST ONE MODE; to order by expiration date pass MODE_DELETED_NORMAL as first argument
     * @return Cursor with the result of the query
     */
    private Cursor getCursorForAllNoteForModes(int... modes) {
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

    private Cursor getCursorForAllNoteWithReminder() {
        return mDatabase.rawQuery(
                SelectQueries.SELECT_ALL_NOTES_WITH_REMINDER,
                null
        );
    }
}
