package com.gcode.notes.activities.helpers.compose.note;

import android.content.Intent;
import android.os.Bundle;

import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.activities.helpers.compose.base.ComposeBaseStartStateHelper;
import com.gcode.notes.data.note.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class ComposeNoteStartStateHelper extends ComposeBaseStartStateHelper {
    ComposeNoteActivity mComposeNoteActivity;

    public ComposeNoteStartStateHelper(ComposeNoteActivity composeNoteActivity) {
        mComposeNoteActivity = composeNoteActivity;
    }

    public void setupStartState(Bundle savedInstanceState) {
        Intent intent = mComposeNoteActivity.getIntent();
        switch (intent.getIntExtra(Constants.EXTRA_SETUP_FROM, Constants.ERROR)) {
            case Constants.SETUP_FROM_ZERO:
                //New note
                mComposeNoteActivity.mIsOpenedInEditMode = false;
                setupFromZero();
                break;
            case Constants.SETUP_FROM_EDIT_MODE:
                //Note opened in edit mode
                setupFromEditMode(intent.getStringExtra(Constants.EXTRA_NOTE_DATA));
                break;
            case Constants.SETUP_FROM_SCREEN_ROTATION:
                //handling screen rotation
                ComposeNoteRotationHandler.handlerScreenRotation(mComposeNoteActivity, savedInstanceState);
                break;
            case Constants.SETUP_FROM_PHOTO:
                //Creating note from attached image
                ComposeNotePhotoHelper.setupFromPhoto(mComposeNoteActivity, intent.getStringExtra(Constants.EXTRA_PHOTO_URI));
                break;
            case Constants.SETUP_FROM_AUDIO:
                //Creating audio note (from voice recognition)
                ComposeNoteAudioHelper.setupFromAudio(mComposeNoteActivity, intent.getStringExtra(Constants.EXTRA_AUDIO_PATH),
                        intent.getStringExtra(Constants.EXTRA_RECOGNIZED_SPEECH_TEXT));
                break;
            default:
                MyDebugger.log("EXTRA_SETUP_FROM not passed!");
                break;
        }
    }

    protected void setupFromZero() {
        super.setupFromZero(mComposeNoteActivity);
        mComposeNoteActivity.mNoteData = new NoteData();
    }

    private void setupFromEditMode(String serializedNoteData) {
        NoteData noteData = Serializer.parseNoteData(serializedNoteData);
        if (noteData != null) {
            //passed noteData is OK, setup the activity from it
            super.setupFromEditMode(mComposeNoteActivity, noteData); //setup base (title, importance, reminder, mOpenedInEditMode)
            mComposeNoteActivity.mNoteData = noteData;
            if (noteData.hasAttachedAudio()) {
                ComposeNoteAudioHelper.setupAudio(mComposeNoteActivity, noteData.getAttachedAudioPath());
            }
            if (noteData.hasAttachedImage()) {
                //images adapter uses the same list as mNoteData so no need to addAll(), cuz items will duplicate
                ComposeNotePhotoHelper.setupImagesAdapter(mComposeNoteActivity);
            }
            mComposeNoteActivity.getDescriptionEditText().setText(noteData.getDescription());
        } else {
            MyDebugger.log("EditMode launched with null note, finish activity in order to prevent errors");
            mComposeNoteActivity.finish();
        }
    }
}
