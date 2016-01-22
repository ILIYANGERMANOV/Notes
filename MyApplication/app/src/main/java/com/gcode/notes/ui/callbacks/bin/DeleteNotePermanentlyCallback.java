package com.gcode.notes.ui.callbacks.bin;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.adapters.main.MainAdapter;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;

public class DeleteNotePermanentlyCallback implements MaterialDialog.SingleButtonCallback {
    MainAdapter mAdapter;
    int mPosition;
    ContentBase mNote;

    public DeleteNotePermanentlyCallback(MainAdapter adapter, int position, ContentBase note) {
        mAdapter = adapter;
        mPosition = position;
        mNote = note;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        if(which == DialogAction.POSITIVE) {
            //positive option selected, delete note
            if (!MyApplication.getWritableDatabase().deleteNoteFromBin(mNote)) {
                //failed to delete note from db
                mAdapter.addItem(mPosition, mNote);
                mAdapter.getRecyclerView().smoothScrollToPosition(mPosition);
                MyDebugger.log("Failed to delete note permanently!");
            }
        } else if(which == DialogAction.NEGATIVE) {
            //negative option selected, bring note back
            mAdapter.addItem(mPosition, mNote);
            mAdapter.getRecyclerView().smoothScrollToPosition(mPosition);
        }
    }
}
