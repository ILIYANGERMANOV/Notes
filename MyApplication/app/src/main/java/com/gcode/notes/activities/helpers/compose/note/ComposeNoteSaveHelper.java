package com.gcode.notes.activities.helpers.compose.note;

import android.app.Activity;
import android.content.Intent;

import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.activities.helpers.compose.ComposeBaseSaveHelper;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;
import com.gcode.notes.tasks.async.encryption.EncryptNoteTask;
import com.gcode.notes.tasks.async.encryption.callbacks.EncryptTaskCallbacks;

public class ComposeNoteSaveHelper implements EncryptTaskCallbacks {
    //TODO: REFACTOR AND OPTIMIZE
    private ComposeNoteActivity mComposeNoteActivity;

    public ComposeNoteSaveHelper(ComposeNoteActivity composeNoteActivity) {
        mComposeNoteActivity = composeNoteActivity;
    }

    public void saveNote() {
        NoteData noteData = mComposeNoteActivity.mNoteData;

        if (!mComposeNoteActivity.mIsOpenedInEditMode) {
            //!NOTE contentBase type must be set before ComposeBaseSaveHelper#saveBase() cuz reminder won't be set correctly
            //when it is new note
            noteData.setType(Constants.TYPE_NOTE); //sets contentBase type to note
        }
        noteData.setDescription(mComposeNoteActivity.getDescriptionEditText().getText().toString()); //set description here, before its used in isValidNote()
        //images are already added (adapter uses same list as adapter, so removing/adding will result noteData, too)
        //if has audio is already set in setupFromAudio or delete in DeleteAudioCallback

        boolean hadValidTitleBeforeSaveBase = ComposeBaseSaveHelper.saveBase(mComposeNoteActivity, noteData);
        //!NOTE ComposeBaseSaveHelper#saveBase() returns boolean which indicates whether the title was valid
        //used cuz in ComposeBaseSaveHelper#saveBase()
        //if title isn't valid will be generated and this will result in saving not valid notes
        //cuz noteData#isValidNote will always return true

        if (noteData.isValidNote(hadValidTitleBeforeSaveBase)) {
            if (mComposeNoteActivity.mInPrivateMode) {
                //note is private mode, encrypt it before saving
                //!NOTE: onEncryptedSuccessfully or error callback will be called when ready
                //TODO: fix bug when editing private note (use copy constructor and setOldResult flag)
                new EncryptNoteTask(mComposeNoteActivity, this).execute(new NoteData(noteData));
                return;
            }

            saveToDbAndSetResult(noteData);
        } else {
            MyDebugger.toast(mComposeNoteActivity, "Cannot save empty notes.");
        }
    }


    private void saveToDbAndSetResult(NoteData noteData) {
        Intent resultIntent = mComposeNoteActivity.mResultIntent;
        if (!mComposeNoteActivity.mIsOpenedInEditMode) {
            //new note
            noteData.setType(Constants.TYPE_NOTE);
            if (MyApplication.getWritableDatabase().insertNote(noteData) != Constants.DATABASE_ERROR) {
                resultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                resultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, noteData.getMode());
            } else {
                MyDebugger.log("Failed to save note.");
                return;
            }
        } else {
            //update existing note
            if (MyApplication.getWritableDatabase().updateNote(noteData)) {
                resultIntent.putExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, true);
                NoteData resultNoteData = mComposeNoteActivity.mInPrivateMode ?
                        mComposeNoteActivity.mNoteData : noteData;
                resultIntent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(resultNoteData));
                resultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, mComposeNoteActivity.mNoteModeChanged);
            } else {
                MyDebugger.log("Failed to update note.");
                return;
            }
        }

        //note saved successfully, set alarm for reminder
        ComposeBaseSaveHelper.setAlarmIfHasReminder(mComposeNoteActivity.getComposeReminderFragment(), noteData);

        mComposeNoteActivity.setResult(Activity.RESULT_OK, resultIntent);
        mComposeNoteActivity.finish();
    }

    @Override
    public void onEncryptedSuccessfully(ContentBase contentBase) {
        //note encrypted successfully, save to db
        saveToDbAndSetResult(((NoteData) contentBase));
    }
}
