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

import java.util.ArrayList;

public class ComposeNoteSaveHelper {
    public static void saveNote(ComposeNoteActivity composeNoteActivity) {
        String title = composeNoteActivity.getTitleEditText().getText().toString();
        String description = composeNoteActivity.getDescriptionEditText().getText().toString();
        ArrayList<String> attachedImagesList = composeNoteActivity.mImagesAdapter.getData();
        if (isValidNote(title, description, attachedImagesList, composeNoteActivity.mAudioPath)) {
            int mode = composeNoteActivity.mIsStarred ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL;
            String reminderString = composeNoteActivity.getReminderTextView().getText().toString();
            if (reminderString.equals(composeNoteActivity.getResources().getString(R.string.compose_note_set_reminder_text))) {
                reminderString = Constants.NO_REMINDER;
            }

            //TODO: optimize passing by not passing empty array lists
            NoteData noteData = new NoteData(title, mode,
                    hasAttributes(description, attachedImagesList),
                    description, attachedImagesList, composeNoteActivity.mAudioPath, reminderString);

            Intent resultIntent = new Intent();
            if (!composeNoteActivity.mIsOpenedInEditMode) {
                //new note
                if (MyApplication.getWritableDatabase().insertNote(noteData) != Constants.DATABASE_ERROR) {
                    resultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mode);
                } else {
                    MyDebugger.log("Failed to save note.");
                }
            } else {
                //update existing note
                noteData.setId(composeNoteActivity.mEditNoteId);
                noteData.setTargetId(composeNoteActivity.mEditNoteTargetId);
                if (composeNoteActivity.mContentDetails != null) {
                    composeNoteActivity.mContentDetails.setLastModifiedDate(DateUtils.getCurrentTimeSQLiteFormatted());
                    noteData.setContentDetails(composeNoteActivity.mContentDetails);
                } else {
                    MyDebugger.log("ContentDetails are null");
                }
                if (MyApplication.getWritableDatabase().updateNote(noteData)) {
                    resultIntent.putExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(noteData));
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

    private static boolean hasAttributes(String description, ArrayList<String> attachedImagesList) {
        return description.trim().length() > 0 || attachedImagesList.size() > 0;
    }

    private static boolean isValidNote(String title, String description,
                                       ArrayList<String> attachedImagesList, String audioPath) {

        return title.trim().length() > 0 || description.trim().length() > 0 ||
                attachedImagesList.size() > 0 || !audioPath.equals(Constants.NO_AUDIO);
    }
}
