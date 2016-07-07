package com.gcode.notes.ui.callbacks.display;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.notes.MyApplication;

public class DeletePrivateNoteFromDisplayCallback implements MaterialDialog.SingleButtonCallback {

    DisplayBaseActivity mDisplayBaseActivity;
    ContentBase mContentBase;

    public DeletePrivateNoteFromDisplayCallback(DisplayBaseActivity displayBaseActivity, ContentBase contentBase) {
        mDisplayBaseActivity = displayBaseActivity;
        mContentBase = contentBase;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        //note should be deleted from db

        //updates note mode both on contentBase and in db
        if (!MyApplication.getWritableDatabase().deleteNotePermanently(mContentBase)) {
            //failed to send note to bin, log and prevent further execution
            MyLogger.log("DeletePrivateNoteCallback failed to delete permanently note.");
            return;
        }
        mDisplayBaseActivity.mNoteModeChanged = true; //main activity onItemModeChanged() should be called
        mDisplayBaseActivity.finish();
    }
}
