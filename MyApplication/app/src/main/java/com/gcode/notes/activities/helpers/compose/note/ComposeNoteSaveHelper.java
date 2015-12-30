package com.gcode.notes.activities.helpers.compose.note;

import android.app.Activity;
import android.content.Intent;

import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.activities.helpers.compose.ComposeBaseSaveHelper;
import com.gcode.notes.data.note.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

public class ComposeNoteSaveHelper {

    public static void saveNote(ComposeNoteActivity composeNoteActivity) {
        NoteData mNoteData = composeNoteActivity.mNoteData;

        if (!composeNoteActivity.mIsOpenedInEditMode) {
            //!NOTE contentBase type must be set before ComposeBaseSaveHelper#saveBase() cuz reminder won't be set correctly
            //when it is new note
            mNoteData.setType(Constants.TYPE_NOTE); //sets contentBase type to note
        }
        mNoteData.setDescription(composeNoteActivity.getDescriptionEditText().getText().toString()); //set description here, before its used in isValidNote()
        //images are already added (adapter uses same list as adapter, so removing/adding will result mNoteData, too)
        //if has audio is already set in setupFromAudio or delete in DeleteAudioCallback

        boolean hadValidTitleBeforeSaveBase = ComposeBaseSaveHelper.saveBase(composeNoteActivity, mNoteData);
        //!NOTE ComposeBaseSaveHelper#saveBase() returns boolean which indicates whether the title was valid
        //used cuz in ComposeBaseSaveHelper#saveBase()
        //if title isn't valid will be generated and this will result in saving not valid notes
        //cuz noteData#isValidNote will always return true

        if (mNoteData.isValidNote(hadValidTitleBeforeSaveBase)) {
            Intent resultIntent = composeNoteActivity.mResultIntent;
            if (!composeNoteActivity.mIsOpenedInEditMode) {
                //new note
                mNoteData.setType(Constants.TYPE_NOTE);
                if (MyApplication.getWritableDatabase().insertNote(mNoteData) != Constants.DATABASE_ERROR) {
                    resultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mNoteData.getMode());
                } else {
                    MyDebugger.log("Failed to save note.");
                    return;
                }
            } else {
                //update existing note
                if (MyApplication.getWritableDatabase().updateNote(mNoteData)) {
                    resultIntent.putExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(mNoteData));
                    resultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, composeNoteActivity.mNoteModeChanged);
                } else {
                    MyDebugger.log("Failed to update note.");
                    return;
                }
            }
            composeNoteActivity.setResult(Activity.RESULT_OK, resultIntent);
        } else {
            MyDebugger.toast(composeNoteActivity, "Cannot save empty notes.");
        }
    }
}
