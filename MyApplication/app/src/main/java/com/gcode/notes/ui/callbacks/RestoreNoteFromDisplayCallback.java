package com.gcode.notes.ui.callbacks;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.RemoveItemFromMainTask;

public class RestoreNoteFromDisplayCallback extends MaterialDialog.ButtonCallback {
    Activity mActivity;
    ContentBase mNote;

    public RestoreNoteFromDisplayCallback(Activity activity, ContentBase note) {
        mActivity = activity;
        mNote = note;
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        if (MyApplication.getWritableDatabase().restoreNoteFromBin(mNote)) {
            //note restored successfully
            new RemoveItemFromMainTask().execute(mNote);
            mActivity.finish();
        } else {
            //failed to restore note
            MyDebugger.log("RestoreNoteFromDisplayCallback", "failed to restore note");
        }
        dialog.cancel();
    }

    @Override
    public void onNegative(MaterialDialog dialog) {
        dialog.cancel();
    }
}
