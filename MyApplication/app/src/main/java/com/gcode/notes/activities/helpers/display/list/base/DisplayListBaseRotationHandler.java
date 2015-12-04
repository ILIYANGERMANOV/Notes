package com.gcode.notes.activities.helpers.display.list.base;

import android.os.Bundle;

import com.gcode.notes.activities.display.list.DisplayListBaseActivity;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class DisplayListBaseRotationHandler {
    /**
     * Hide done tasks if they were hidden, and preserves the value on mNoteModeChanged.
     *
     * @param displayListBaseActivity- DisplayBaseActivity
     * @param savedInstanceState       - instance state before screen rotation
     */
    public static void handleScreenRotation(DisplayListBaseActivity displayListBaseActivity, Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean(Constants.EXTRA_IS_DONE_HIDDEN)) {
            //done tasks were hidden in previous state, hide them
            DisplayListBaseTasksHelper.hideDoneTasks(displayListBaseActivity);
        }
        displayListBaseActivity.mNoteModeChanged = savedInstanceState.getBoolean(Constants.EXTRA_NOTE_MODE_CHANGED);
    }

    /**
     * Saves EXTRA_LIST_DATA, EXTRA_IS_DONE_HIDDEN and EXTRA_NOTE_MODE_CHANGED
     *
     * @param displayListBaseActivity - DisplayListBaseActivity
     * @param outState                - bundle in which properties will be saved
     */
    public static void saveInstanceState(DisplayListBaseActivity displayListBaseActivity, Bundle outState) {
        outState.putString(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(displayListBaseActivity.mListData));
        outState.putBoolean(Constants.EXTRA_IS_DONE_HIDDEN, displayListBaseActivity.mIsDoneTasksHidden);
        outState.putBoolean(Constants.EXTRA_NOTE_MODE_CHANGED, displayListBaseActivity.mNoteModeChanged);
    }
}
