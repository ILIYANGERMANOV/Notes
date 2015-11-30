package com.gcode.notes.activities.helpers.compose.note;

import android.app.Activity;
import android.content.Intent;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

public class ComposeNoteSaveHelper {
    //TODO: OPTIMIZE and remove redundancy with ComposeListSaveHelper

    public static void saveNote(ComposeNoteActivity composeNoteActivity) {
        NoteData mNoteData = composeNoteActivity.mNoteData;

        String title = composeNoteActivity.getTitleEditText().getText().toString();
        String description = composeNoteActivity.getDescriptionEditText().getText().toString();

        mNoteData.setTitle(title);
        mNoteData.setDescription(description);
        //images are already added (adapter uses same list as adapter, so removing/adding will result mNoteData, too)
        //if has audio is already set in setupFromAudio or delete in DeleteAudioCallback

        if (mNoteData.isValidNote()) {
            //set noteData mode
            mNoteData.setMode(composeNoteActivity.mIsStarred ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL);

            //TODO: use real reminder
            String reminderString = composeNoteActivity.getReminderTextView().getText().toString();
            if (!reminderString.equals(composeNoteActivity.getResources().getString(R.string.compose_note_set_reminder_text))) {
                mNoteData.setReminder(reminderString);
            }

            //setting hasAttributes flag for database optimization
            mNoteData.setHasAttributesFlag(mNoteData.hasAttributes());

            Intent resultIntent = composeNoteActivity.mResultIntent;
            if (!composeNoteActivity.mIsOpenedInEditMode) {
                //new note
                mNoteData.setType(Constants.TYPE_NOTE);
                if (MyApplication.getWritableDatabase().insertNote(mNoteData) != Constants.DATABASE_ERROR) {
                    resultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mNoteData.getMode());
                } else {
                    MyDebugger.log("Failed to save note.");
                }
            } else {
                //update existing note
                mNoteData.setLastModifiedDate(DateUtils.getCurrentTimeSQLiteFormatted());
                if (MyApplication.getWritableDatabase().updateNote(mNoteData)) {
                    resultIntent.putExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(mNoteData));
                    resultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, composeNoteActivity.mNoteModeChanged);
                } else {
                    MyDebugger.log("Failed to update note.");
                }
            }
            composeNoteActivity.setResult(Activity.RESULT_OK, resultIntent);
        } else {
            MyDebugger.toast(composeNoteActivity, "Cannot save empty notes.");
        }
    }
}
