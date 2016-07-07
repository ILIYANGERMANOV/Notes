package com.gcode.notes.ui.callbacks.bin;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.adapters.main.MainAdapter;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.ui.helpers.SearchViewHelper;

public class DeleteNotePermanentlyCallback implements MaterialDialog.SingleButtonCallback {
    MainActivity mMainActivity;
    MainAdapter mAdapter;
    int mPosition;
    ContentBase mNote;

    public DeleteNotePermanentlyCallback(MainActivity mainActivity, int position, ContentBase note) {
        mMainActivity = mainActivity;
        mAdapter = mainActivity.mMainAdapter;
        mPosition = position;
        mNote = note;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        if (which == DialogAction.POSITIVE) {
            //positive option selected, delete note
            if (MyApplication.getWritableDatabase().deleteNotePermanently(mNote)) {
                //note successfully deleted
                if (SearchViewHelper.isSearchViewOpened(mMainActivity)) {
                    //search view is opened, call searchHandler#onItemRemoved() so list copy will be present
                    mMainActivity.mSearchHandler.onItemRemoved(mNote);
                }
            } else {
                //failed to delete note from db
                mAdapter.addItem(mPosition, mNote);
                mAdapter.getRecyclerView().smoothScrollToPosition(mPosition);
                MyLogger.log("Failed to delete note permanently!");
            }
        } else if (which == DialogAction.NEGATIVE) {
            //negative option selected, bring note back
            mAdapter.addItem(mPosition, mNote);
            mAdapter.getRecyclerView().smoothScrollToPosition(mPosition);
        }
    }
}
