package com.gcode.notes.activities.helpers.compose.note;

import android.os.Bundle;

import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class ComposeNoteRotationHandler {
    public static void saveInstanceState(ComposeNoteActivity composeNoteActivity, Bundle outState) {
        composeNoteActivity.getIntent().putExtra(Constants.EXTRA_SETUP_FROM, Constants.SETUP_FROM_SCREEN_ROTATION); //put it to int, cuz its extra we are checking in setupStartState()
        outState.putBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE, composeNoteActivity.mIsOpenedInEditMode);
        outState.putBoolean(Constants.EXTRA_IS_STARRED, composeNoteActivity.mIsStarred);
        outState.putBoolean(Constants.EXTRA_NOTE_MODE_CHANGED, composeNoteActivity.mNoteModeChanged);
        composeNoteActivity.mNoteData.setAttachedImagesPaths(composeNoteActivity.mImagesAdapter.getData()); //preserves added/removed images before screen rotation
        outState.putString(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(composeNoteActivity.mNoteData));
    }

    public static void handlerScreenRotation(ComposeNoteActivity composeNoteActivity, Bundle savedInstanceState) {
        composeNoteActivity.mIsOpenedInEditMode = savedInstanceState.getBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE);
        if (savedInstanceState.getBoolean(Constants.EXTRA_IS_STARRED)) {
            ComposeNoteImportanceHelper.setStarredState(composeNoteActivity);
        }
        composeNoteActivity.mNoteModeChanged = savedInstanceState.getBoolean(Constants.EXTRA_NOTE_MODE_CHANGED);
        String serializedNoteData = savedInstanceState.getString(Constants.EXTRA_NOTE_DATA);
        NoteData noteData = Serializer.parseNoteData(serializedNoteData);
        if (noteData != null) {
            composeNoteActivity.mNoteData = noteData;
            if (noteData.hasAttachedImage()) {
                //adapter's list is still empty, no need to clear
                composeNoteActivity.mImagesAdapter.addAll(noteData.getAttachedImagesPaths());
            }

            if (noteData.hasAttachedAudio()) {
                ComposeNoteAudioHelper.setupAudio(composeNoteActivity, noteData.getAttachedAudioPath());
            }
        } else {
            MyDebugger.log("handleScreenRotation noteData is null.");
        }
    }
}
