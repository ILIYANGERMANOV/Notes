package com.gcode.notes.activities.helpers.display.list.base;

import android.app.Activity;

import com.gcode.notes.activities.display.list.DisplayListBaseActivity;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class DisplayListBaseResultHandler {
    /**
     * Puts EXTRA_LIST_DATA, EXTRA_NOTE_MODE_CHANGED and sets results of activity.
     *
     * @param displayListBaseActivity - DisplayListBaseActivity which result will be set
     */
    public static void setResult(DisplayListBaseActivity displayListBaseActivity) {
        displayListBaseActivity.mResultIntent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(displayListBaseActivity.mListData));
        displayListBaseActivity.mResultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, displayListBaseActivity.mNoteModeChanged);
        displayListBaseActivity.setResult(Activity.RESULT_OK, displayListBaseActivity.mResultIntent); //set result
    }
}
