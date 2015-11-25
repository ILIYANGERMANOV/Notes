package com.gcode.notes.activities.helpers.compose.note;

import android.os.Bundle;

import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

import java.util.ArrayList;

public class ComposeNoteRotationHandler {
    public static void saveInstanceState(ComposeNoteActivity composeNoteActivity, Bundle outState) {
        outState.putBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE, composeNoteActivity.mIsOpenedInEditMode);
        if (composeNoteActivity.mIsOpenedInEditMode) {
            outState.putInt(Constants.EXTRA_EDIT_NOTE_ID, composeNoteActivity.mEditNoteId);
            outState.putInt(Constants.EXTRA_EDIT_NOTE_TARGET_ID, composeNoteActivity.mEditNoteTargetId);
        }
        outState.putBoolean(Constants.EXTRA_IS_STARRED, composeNoteActivity.mIsStarred);
        outState.putBoolean(Constants.EXTRA_NOTE_MODE_CHANGED, composeNoteActivity.mNoteModeChanged);
        outState.putString(Constants.EXTRA_CONTENT_DETAILS, Serializer.serializeContentDetails(composeNoteActivity.mContentDetails));
        //TODO: optimize always passing images list
        outState.putString(Constants.EXTRA_ATTACHED_IMAGES_LIST, Serializer.serializePathsList(composeNoteActivity.mImagesAdapter.getData()));
        outState.putString(Constants.EXTRA_ATTACHED_AUDIO_PATH, composeNoteActivity.mAudioPath);
    }

    public static void handlerScreenRotation(ComposeNoteActivity composeNoteActivity, Bundle savedInstanceState) {
        composeNoteActivity.mIsOpenedInEditMode = savedInstanceState.getBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE);
        if (composeNoteActivity.mIsOpenedInEditMode) {
            composeNoteActivity.mEditNoteId = savedInstanceState.getInt(Constants.EXTRA_EDIT_NOTE_ID);
            composeNoteActivity.mEditNoteTargetId = savedInstanceState.getInt(Constants.EXTRA_EDIT_NOTE_TARGET_ID);
        }
        if (savedInstanceState.getBoolean(Constants.EXTRA_IS_STARRED)) {
            ComposeNoteImportanceHelper.setStarredState(composeNoteActivity);
        }
        composeNoteActivity.mNoteModeChanged = savedInstanceState.getBoolean(Constants.EXTRA_NOTE_MODE_CHANGED);
        composeNoteActivity.mContentDetails = Serializer.parseContentDetails(savedInstanceState.getString(Constants.EXTRA_CONTENT_DETAILS));
        ArrayList<String> pathsList = Serializer.parseStringPathsList(
                savedInstanceState.getString(Constants.EXTRA_ATTACHED_IMAGES_LIST));

        if (pathsList != null) {
            //adapter's list is still empty, no need to clear
            composeNoteActivity.mImagesAdapter.addAll(pathsList);
        }
        String audioPath = savedInstanceState.getString(Constants.EXTRA_ATTACHED_AUDIO_PATH);
        if (audioPath != null && !audioPath.equals(Constants.NO_AUDIO)) {
            ComposeNoteAudioHelper.setupAudio(composeNoteActivity, audioPath);
        }
    }
}
