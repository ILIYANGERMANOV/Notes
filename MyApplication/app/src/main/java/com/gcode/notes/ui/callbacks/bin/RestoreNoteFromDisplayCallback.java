package com.gcode.notes.ui.callbacks.bin;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.notes.MyApplication;

public class RestoreNoteFromDisplayCallback implements MaterialDialog.SingleButtonCallback {
    DisplayBaseActivity mDisplayBaseActivity;
    ContentBase mNote;

    public RestoreNoteFromDisplayCallback(DisplayBaseActivity displayBaseActivity, ContentBase note) {
        mDisplayBaseActivity = displayBaseActivity;
        mNote = note;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        if (MyApplication.getWritableDatabase().restoreNoteFromBin(mNote)) {
            //note restored successfully
            mDisplayBaseActivity.mNoteModeChanged = true; //flag that note mode has changed,
            // so MainActivity can handle it
            mDisplayBaseActivity.finish();
        } else {
            //failed to restore note
            MyLogger.log("RestoreNoteFromDisplayCallback", "failed to restore note");
        }
    }
}
