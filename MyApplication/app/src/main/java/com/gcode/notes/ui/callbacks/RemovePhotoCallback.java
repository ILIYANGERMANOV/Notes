package com.gcode.notes.ui.callbacks;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.adapters.note.compose.ComposeNoteImagesAdapter;

public class RemovePhotoCallback extends MaterialDialog.ButtonCallback {
    ComposeNoteImagesAdapter mAdapter;
    String mItem;

    public RemovePhotoCallback(ComposeNoteImagesAdapter adapter, String item) {
        mAdapter = adapter;
        mItem = item;
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        mAdapter.remove(mItem);
        if(mAdapter.getCount() == 0) {
            mAdapter.hideListView();
        }
        dialog.cancel();
    }

    @Override
    public void onNegative(MaterialDialog dialog) {
        dialog.cancel();
    }
}
