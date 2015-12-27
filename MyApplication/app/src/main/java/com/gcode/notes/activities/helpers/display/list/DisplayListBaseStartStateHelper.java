package com.gcode.notes.activities.helpers.display.list;

import android.os.Bundle;

import com.gcode.notes.activities.display.list.DisplayListBaseActivity;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class DisplayListBaseStartStateHelper {
    DisplayListBaseActivity mDisplayListBaseActivity;

    public DisplayListBaseStartStateHelper(DisplayListBaseActivity displayListBaseActivity) {
        mDisplayListBaseActivity = displayListBaseActivity;
    }

    public void setupStartState(Bundle savedInstanceState) {
        Bundle extras = mDisplayListBaseActivity.getIntent().getExtras();
        if (extras != null && savedInstanceState == null) {
            //first time started
            setupFromBundle(extras);
        } else {
            //from saved instance state
            setupFromBundle(savedInstanceState);
            DisplayListBaseRotationHandler.handleScreenRotation(mDisplayListBaseActivity, savedInstanceState);
        }
    }

    private void setupFromBundle(Bundle bundle) {
        mDisplayListBaseActivity.mListData = Serializer.parseListData(bundle.getString(Constants.EXTRA_LIST_DATA));
        if (mDisplayListBaseActivity.mListData != null) {
            //mListData is successfully parsed, display it
            mDisplayListBaseActivity.displayListData();
        }
    }
}
