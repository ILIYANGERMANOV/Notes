package com.gcode.notes.ui.callbacks.bin;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;


public class DeleteNoteFromDisplayCallback extends MaterialDialog.ButtonCallback {
    Activity mActivity;
    ContentBase mNote;

    public DeleteNoteFromDisplayCallback(Activity activity, ContentBase note) {
        mActivity = activity;
        mNote = note;
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        if (MyApplication.getWritableDatabase().deleteNoteFromBin(mNote)) {
            mActivity.finish();
            MainAdapter adapter = BaseController.getInstance().getMainAdapter();
            if (adapter != null) {
                int itemPosition = adapter.getIndexOfItem(mNote);
                if (itemPosition != -1) {
                    adapter.removeItem(itemPosition);
                }
            }
        } else {
            MyDebugger.log("Failed to delete note");
        }
        dialog.cancel();
    }

    @Override
    public void onNegative(MaterialDialog dialog) {
        dialog.cancel();
    }
}
