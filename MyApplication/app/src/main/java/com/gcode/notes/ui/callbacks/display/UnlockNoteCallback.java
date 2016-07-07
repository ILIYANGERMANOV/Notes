package com.gcode.notes.ui.callbacks.display;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

public class UnlockNoteCallback implements MaterialDialog.SingleButtonCallback {
    DisplayBaseActivity mDisplayBaseActivity;
    ContentBase mContentBase;

    public UnlockNoteCallback(DisplayBaseActivity displayBaseActivity, ContentBase contentBase) {
        mDisplayBaseActivity = displayBaseActivity;
        mContentBase = contentBase;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        //note should be moved from private to normal mode
        //!NOTE: note here is already decrypted, no need for decryption
        mDisplayBaseActivity.mNoteModeChanged = true; //main activity onItemModeChanged() should be called
        mContentBase.setMode(Constants.MODE_NORMAL); //changes note mode from private to normal
        //!NOTE: Must be used with update creation date flag, so will work
        if (!MyApplication.getWritableDatabase().updateNote(mContentBase, true)) {
            //failed to save to db decrypted note, log it and prevent further execution
            MyLogger.log("UnlockNoteCallback failed to update note.");
            return;
        }
        mDisplayBaseActivity.finish();
    }
}
