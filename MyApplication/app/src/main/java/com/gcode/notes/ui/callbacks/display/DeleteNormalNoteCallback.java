package com.gcode.notes.ui.callbacks.display;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.R;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.async.main.RemoveItemFromMainTask;

public class DeleteNormalNoteCallback implements MaterialDialog.SingleButtonCallback {
    //TODO: Same issue as LockNoteCallback

    DisplayBaseActivity mDisplayBaseActivity;
    ContentBase mContentBase;

    public DeleteNormalNoteCallback(DisplayBaseActivity displayBaseActivity, ContentBase contentBase) {
        mDisplayBaseActivity = displayBaseActivity;
        mContentBase = contentBase;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        //note should be moved to bin

        //updates note mode both on contentBase and in db
        if (!MyApplication.getWritableDatabase().deleteNote(mContentBase)) {
            //failed to send note to bin, log and prevent further execution
            MyDebugger.log("DeleteNormalNoteCallback failed to send note to bin");
            return;
        }
        mDisplayBaseActivity.mNoteModeChanged = false; //main activity onItemModeChanged() shouldn't be called
        mDisplayBaseActivity.finish();

        new RemoveItemFromMainTask(mDisplayBaseActivity.
                getString(R.string.note_moved_to_bin)).execute(mContentBase);
    }
}
