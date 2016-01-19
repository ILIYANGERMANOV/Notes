package com.gcode.notes.ui.callbacks.compose;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.adapters.note.compose.ComposeNoteImagesAdapter;

public class RemovePhotoCallback implements MaterialDialog.SingleButtonCallback {
    ComposeNoteImagesAdapter mAdapter;
    String mItem;

    public RemovePhotoCallback(ComposeNoteImagesAdapter adapter, String item) {
        mAdapter = adapter;
        mItem = item;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        mAdapter.remove(mItem);
        if(mAdapter.getCount() == 0) {
            mAdapter.hideListView();
        }
    }
}
