package com.gcode.notes.ui.callbacks.bin;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.async.main.RemoveItemFromMainTask;

public class RestoreNoteFromDisplayCallback implements MaterialDialog.SingleButtonCallback {
    Activity mActivity;
    ContentBase mNote;

    public RestoreNoteFromDisplayCallback(Activity activity, ContentBase note) {
        mActivity = activity;
        mNote = note;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        if (MyApplication.getWritableDatabase().restoreNoteFromBin(mNote)) {
            //note restored successfully
            new RemoveItemFromMainTask("Note restored successfully.").execute(mNote);
            mActivity.finish();
        } else {
            //failed to restore note
            MyDebugger.log("RestoreNoteFromDisplayCallback", "failed to restore note");
        }
    }
}
