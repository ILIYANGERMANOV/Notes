package com.gcode.notes.ui.callbacks.bin;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.adapters.main.MainAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.notes.MyApplication;


public class DeleteNoteFromDisplayBinCallback implements MaterialDialog.SingleButtonCallback {
    DisplayBaseActivity mDisplayBaseActivity;
    ContentBase mNote;

    public DeleteNoteFromDisplayBinCallback(DisplayBaseActivity displayBaseActivity, ContentBase note) {
        mDisplayBaseActivity = displayBaseActivity;
        mNote = note;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        if (MyApplication.getWritableDatabase().deleteNotePermanently(mNote)) {
            mDisplayBaseActivity.mNoteModeChanged = true; //main activity onItemModeChanged() should be called
            mDisplayBaseActivity.finish();
            MainAdapter adapter = BaseController.getInstance().getMainAdapter();
            if (adapter != null) {
                int itemPosition = adapter.getIndexOfItem(mNote);
                if (itemPosition != -1) {
                    adapter.removeItem(itemPosition);
                }
            }
        } else {
            MyLogger.log("Failed to delete note");
        }
    }
}
