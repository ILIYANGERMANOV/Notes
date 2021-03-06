package com.gcode.notes.activities.helpers.display.note.editable;

import android.app.Activity;
import android.content.Intent;

import com.gcode.notes.activities.display.note.editable.DisplayNoteEditableActivity;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class DisplayNoteEditableResultHandler {
    public static void handleResult(DisplayNoteEditableActivity displayNoteEditableActivity, int requestCode,
                                    int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null && requestCode == Constants.COMPOSE_NOTE_REQUEST_CODE) {
            if (data.getBooleanExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, false)) {
                //note is saved from compose (updated), change mNoteData value and display it
                String serializedNoteData = data.getStringExtra(Constants.EXTRA_NOTE_DATA);
                if (serializedNoteData != null) {
                    NoteData noteData = Serializer.parseNoteData(serializedNoteData);
                    if (noteData != null) {
                        displayNoteEditableActivity.mNoteModeChanged = data.getBooleanExtra(Constants.EXTRA_NOTE_MODE_CHANGED, false);
                        displayNoteEditableActivity.mNoteData = noteData;
                        displayNoteEditableActivity.displayNoteData();
                    }
                }
            } else if (data.getBooleanExtra(Constants.EXTRA_DELETED_AUDIO, false)) {
                //note isn't saved from compose, but its audio is deleted
                //set mNoteData to NO_AUDIO and free audio utils
                displayNoteEditableActivity.mNoteData.setAttachedAudioPath(null);
                displayNoteEditableActivity.mAudioUtils.clearResources();
                displayNoteEditableActivity.mAudioUtils = null;
            }
        }
    }
}
