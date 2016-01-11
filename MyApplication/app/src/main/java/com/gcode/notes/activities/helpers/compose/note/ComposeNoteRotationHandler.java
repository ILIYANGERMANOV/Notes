package com.gcode.notes.activities.helpers.compose.note;

import android.os.Bundle;

import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.activities.helpers.compose.ComposeBaseRotationHandler;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class ComposeNoteRotationHandler {
    public static void saveInstanceState(ComposeNoteActivity composeNoteActivity, Bundle outState) {
        ComposeBaseRotationHandler.saveInstanceState(composeNoteActivity, outState); //save base
        composeNoteActivity.getIntent().putExtra(Constants.EXTRA_SETUP_FROM, Constants.SETUP_FROM_SCREEN_ROTATION); //put it to int, cuz its extra we are checking in setupStartState()
        outState.putString(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(composeNoteActivity.mNoteData));
    }

    public static void handlerScreenRotation(ComposeNoteActivity composeNoteActivity, Bundle savedInstanceState) {
        ComposeBaseRotationHandler.handlerScreenRotation(composeNoteActivity, savedInstanceState); //handle base
        String serializedNoteData = savedInstanceState.getString(Constants.EXTRA_NOTE_DATA);
        NoteData noteData = Serializer.parseNoteData(serializedNoteData);
        if (noteData != null) {
            composeNoteActivity.mNoteData = noteData;
            if (noteData.hasAttachedImage()) {
                //images adapter uses the same list as mNoteData so no need to addAll(), cuz items will duplicate
                ComposeNotePhotoHelper.setupImagesAdapter(composeNoteActivity);
            }

            if (noteData.hasAttachedAudio()) {
                ComposeNoteAudioHelper.setupAudio(composeNoteActivity, noteData.getAttachedAudioPath());
            }
        } else {
            MyDebugger.log("handleScreenRotation noteData is null.");
        }
    }
}
