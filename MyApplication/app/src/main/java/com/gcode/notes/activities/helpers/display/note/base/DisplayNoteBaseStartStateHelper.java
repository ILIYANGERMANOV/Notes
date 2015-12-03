package com.gcode.notes.activities.helpers.display.note.base;


import android.os.Bundle;

import com.gcode.notes.activities.display.note.DisplayNoteBaseActivity;
import com.gcode.notes.activities.helpers.display.DisplayToolbarHelper;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class DisplayNoteBaseStartStateHelper {
    DisplayNoteBaseActivity mDisplayNoteActivity;

    public DisplayNoteBaseStartStateHelper(DisplayNoteBaseActivity displayNoteBaseActivity) {
        mDisplayNoteActivity = displayNoteBaseActivity;
    }

    public void setupStartState(Bundle savedInstanceState) {
        DisplayToolbarHelper.setupToolbar(mDisplayNoteActivity, mDisplayNoteActivity.getToolbar()); //setup activity's toolbar

        Bundle extras = mDisplayNoteActivity.getIntent().getExtras();
        if (extras != null && savedInstanceState == null) {
            //first time started
            setupFromBundle(extras);
        } else {
            //from saved instance state
            mDisplayNoteActivity.mNoteModeChanged = savedInstanceState.getBoolean(Constants.EXTRA_NOTE_MODE_CHANGED); //restore mNoteModeChanged flag from previous state
            setupFromBundle(savedInstanceState);
        }
    }

    private void setupFromBundle(Bundle bundle) {
        if (bundle != null) {
            String serializedNoteData = bundle.getString(Constants.EXTRA_NOTE_DATA);
            if (serializedNoteData != null) {
                mDisplayNoteActivity.mNoteData = Serializer.parseNoteData(serializedNoteData);
                if (mDisplayNoteActivity.mNoteData != null) {
                    mDisplayNoteActivity.displayNoteData();
                }
            }
        }
    }
}
