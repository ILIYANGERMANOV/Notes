package com.gcode.notes.activities.helpers.compose.note;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.adapters.note.compose.ComposeNoteImagesAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;
import com.linearlistview.LinearListView;

import java.util.ArrayList;

public class ComposeNoteStartStateHelper {
    ComposeNoteActivity mComposeNoteActivity;

    public ComposeNoteStartStateHelper(ComposeNoteActivity composeNoteActivity) {
        mComposeNoteActivity = composeNoteActivity;
    }

    public void setupStartState(Bundle savedInstanceState) {
        Intent intent = mComposeNoteActivity.getIntent();
        setupLayout();
        //TODO: consider adding EXTRA_SETUP_FROM
        if (savedInstanceState == null) {
            if (intent.getStringExtra(Constants.EXTRA_NOTE_DATA) != null) {
                //Note opened in edit mode
                mComposeNoteActivity.mIsOpenedInEditMode = true;
                setupFromEditMode(intent.getStringExtra(Constants.EXTRA_NOTE_DATA));
            } else if (intent.getStringExtra(Constants.EXTRA_PHOTO_URI) != null) {
                setupFromPhoto(intent.getStringExtra(Constants.EXTRA_PHOTO_URI));
            } else if (intent.getStringExtra(Constants.EXTRA_AUDIO_PATH) != null) {
                ComposeNoteAudioHelper.setupFromAudio(mComposeNoteActivity, intent.getStringExtra(Constants.EXTRA_AUDIO_PATH),
                        intent.getStringExtra(Constants.EXTRA_RECOGNIZED_SPEECH_TEXT));
            } else {
                //New note
                mComposeNoteActivity.mIsOpenedInEditMode = false;
                setupFromZero();
            }
        } else {
            //Saved instance state
            ComposeNoteRotationHandler.handlerScreenRotation(mComposeNoteActivity, savedInstanceState);
        }
    }

    private void setupLayout() {
        EditText titleEditText = mComposeNoteActivity.getTitleEditText();
        titleEditText.setHorizontallyScrolling(false);
        titleEditText.setMaxLines(3);

        LinearListView imagesLinearListView = mComposeNoteActivity.getImagesLinearListView();
        mComposeNoteActivity.mImagesAdapter = new ComposeNoteImagesAdapter(mComposeNoteActivity,
                new ArrayList<String>(), imagesLinearListView);
        imagesLinearListView.setAdapter(mComposeNoteActivity.mImagesAdapter);
    }

    private void setupFromZero() {
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
            mComposeNoteActivity.mEditNoteId = noteData.getId();
            mComposeNoteActivity.mEditNoteTargetId = noteData.getTargetId();
            mComposeNoteActivity.getTitleEditText().setText(noteData.getTitle());
            if (noteData.isImportant()) {
                ComposeNoteImportanceHelper.setStarredState(mComposeNoteActivity);
            }
            if (noteData.hasReminder()) {
                mComposeNoteActivity.getReminderTextView().setText(noteData.getReminder());
            }
            mComposeNoteActivity.mContentDetails = noteData.getContentDetails();
            if (noteData.hasAttachedAudio()) {
                ComposeNoteAudioHelper.setupAudio(mComposeNoteActivity, noteData.getAttachedAudioPath());
            }
            if (noteData.hasAttachedImage()) {
                //adapter's list is still empty, no need to clear
                mComposeNoteActivity.mImagesAdapter.addAll(noteData.getAttachedImagesPaths());
            }
            mComposeNoteActivity.getDescriptionEditText().setText(noteData.getDescription());
        }
    }

    private void setupFromPhoto(String photoUriString) {
        mComposeNoteActivity.mImagesAdapter.add(photoUriString);
    }
}
