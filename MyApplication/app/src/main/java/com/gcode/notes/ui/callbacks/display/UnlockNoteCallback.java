package com.gcode.notes.ui.callbacks.display;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.R;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.async.main.RemoveItemFromMainTask;

public class UnlockNoteCallback implements MaterialDialog.SingleButtonCallback {
    //TODO: SAME AS LockNote todo conception and !!mNoteChanged optimization for all display actions
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
        mDisplayBaseActivity.mNoteModeChanged = false; //main activity onItemModeChanged() shouldn't be called
        mContentBase.setMode(Constants.MODE_NORMAL); //changes note mode from private to normal
        //!NOTE: Must be used with update creation date flag, so will work
        if (!MyApplication.getWritableDatabase().updateNote(mContentBase, true)) {
            //failed to save to db decrypted note, log it and prevent further execution
            MyDebugger.log("UnlockNoteCallback failed to update note.");
            return;
        }
        mDisplayBaseActivity.finish();
        new RemoveItemFromMainTask(mDisplayBaseActivity.getString(R.string.note_moved_to_all_notes)).execute(mContentBase);
    }
}
