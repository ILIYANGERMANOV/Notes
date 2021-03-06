package com.gcode.notes.activities.helpers.compose;

import android.os.Bundle;

import com.gcode.notes.activities.compose.ComposeBaseActivity;
import com.gcode.notes.extras.values.Constants;

public class ComposeBaseRotationHandler {
    /**
     * Saves EXTRA_IS_OPENED_IN_EDIT_MODE, EXTRA_IS_STARRED, EXTRA_NOTE_MODE_CHANGED.
     *
     * @param composeBaseActivity - ComposeNoteActivity
     * @param outState            - Bundle from onSavedInstanceState
     */
    public static void saveInstanceState(ComposeBaseActivity composeBaseActivity, Bundle outState) {
        outState.putBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE, composeBaseActivity.mIsOpenedInEditMode);
        outState.putBoolean(Constants.EXTRA_IS_STARRED, composeBaseActivity.mIsStarred);
        outState.putBoolean(Constants.EXTRA_NOTE_MODE_CHANGED, composeBaseActivity.mNoteModeChanged);
        outState.putBoolean(Constants.EXTRA_IS_IN_PRIVATE_MODE, composeBaseActivity.mInPrivateMode);
    }

    /**
     * Init EXTRA_IS_OPENED_IN_EDIT_MODE, EXTRA_IS_STARRED, EXTRA_NOTE_MODE_CHANGED from passed bundle.
     *
     * @param composeBaseActivity - ComposeNoteActivity
     * @param savedInstanceState  - Bundle from onCreate (savedInstanceState)
     */
    public static void handlerScreenRotation(ComposeBaseActivity composeBaseActivity, Bundle savedInstanceState) {
        composeBaseActivity.mIsOpenedInEditMode = savedInstanceState.getBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE);
        if (savedInstanceState.getBoolean(Constants.EXTRA_IS_STARRED)) {
            composeBaseActivity.setStarredState();
        }
        composeBaseActivity.mNoteModeChanged = savedInstanceState.getBoolean(Constants.EXTRA_NOTE_MODE_CHANGED);
        composeBaseActivity.mInPrivateMode = savedInstanceState.getBoolean(Constants.EXTRA_IS_IN_PRIVATE_MODE);
        if(composeBaseActivity.mInPrivateMode) {
            composeBaseActivity.setInPrivateMode();
        }
    }
}
