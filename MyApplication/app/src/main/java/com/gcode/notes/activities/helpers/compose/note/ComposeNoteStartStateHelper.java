package com.gcode.notes.activities.helpers.compose.note;

import android.content.Intent;
import android.os.Bundle;

import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class ComposeNoteStartStateHelper {
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
                mComposeNoteActivity.mIsOpenedInEditMode = true;
                setupFromEditMode(intent.getStringExtra(Constants.EXTRA_NOTE_DATA));
                break;
            case Constants.SETUP_FROM_SCREEN_ROTATION:
                //handling screen rotation
                ComposeNoteRotationHandler.handlerScreenRotation(mComposeNoteActivity, savedInstanceState);
                break;
            case Constants.SETUP_FROM_PHOTO:
                //Creating note from attached image
                setupFromPhoto(intent.getStringExtra(Constants.EXTRA_PHOTO_URI));
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

    private void setupFromZero() {
        mComposeNoteActivity.mNoteData = new NoteData();
        switch (BaseController.getInstance().getControllerId()) {
            case Constants.CONTROLLER_ALL_NOTES:

                break;
            case Constants.CONTROLLER_IMPORTANT:
                ComposeNoteImportanceHelper.setStarredState(mComposeNoteActivity);
                break;
            case Constants.CONTROLLER_PRIVATE:
                //TODO: PRIVATE: set mStarImageButton to sth
                break;
            default:
                break;
        }
    }

    private void setupFromEditMode(String serializedNoteData) {
        NoteData noteData = Serializer.parseNoteData(serializedNoteData);
        if (noteData != null) {
            mComposeNoteActivity.mNoteData = noteData;
            mComposeNoteActivity.getTitleEditText().setText(noteData.getTitle());
            if (noteData.isImportant()) {
                ComposeNoteImportanceHelper.setStarredState(mComposeNoteActivity);
            }
            if (noteData.hasReminder()) {
                mComposeNoteActivity.getReminderTextView().setText(noteData.getReminder());
            }
            if (noteData.hasAttachedAudio()) {
                ComposeNoteAudioHelper.setupAudio(mComposeNoteActivity, noteData.getAttachedAudioPath());
            }
            if (noteData.hasAttachedImage()) {
                //adapter's list is still empty, no need to clear
                mComposeNoteActivity.mImagesAdapter.addAll(noteData.getAttachedImagesPaths());
            }
            mComposeNoteActivity.getDescriptionEditText().setText(noteData.getDescription());
        } else {
            MyDebugger.log("EditMode launched with null note, finish activity in order to prevent errors");
            mComposeNoteActivity.finish();
        }
    }

    private void setupFromPhoto(String photoUriString) {
        mComposeNoteActivity.mImagesAdapter.add(photoUriString);
        mComposeNoteActivity.mNoteData = new NoteData();
        mComposeNoteActivity.mNoteData.addAttachedImagePath(photoUriString);
    }
}
