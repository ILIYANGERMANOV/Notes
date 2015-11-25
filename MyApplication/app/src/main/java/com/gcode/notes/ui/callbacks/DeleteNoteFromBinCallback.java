package com.gcode.notes.ui.callbacks;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.data.main.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;

public class DeleteNoteFromBinCallback extends MaterialDialog.ButtonCallback {
    MainAdapter mAdapter;
    int mPosition;
    ContentBase mNote;

    public DeleteNoteFromBinCallback(MainAdapter adapter, int position, ContentBase note) {
        mAdapter = adapter;
        mPosition = position;
        mNote = note;
    }

    @Override
    public void onNegative(MaterialDialog dialog) {
        mAdapter.addItem(mPosition, mNote);
        mAdapter.getRecyclerView().smoothScrollToPosition(mPosition);
        dialog.cancel();
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        if (!MyApplication.getWritableDatabase().deleteNoteFromBin(mNote)) {
            //failed to delete note from db
            mAdapter.addItem(mPosition, mNote);
            mAdapter.getRecyclerView().smoothScrollToPosition(mPosition);
            MyDebugger.log("Failed to delete note from recycler bin!");
        }
        dialog.cancel();
    }
}
