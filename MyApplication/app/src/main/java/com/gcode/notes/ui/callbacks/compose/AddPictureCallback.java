package com.gcode.notes.ui.callbacks.compose;


import android.app.Activity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.gcode.notes.R;
import com.gcode.notes.extras.utils.PhotoUtils;

public class AddPictureCallback implements MaterialDialog.ListCallback {
    Activity mActivity;
    MaterialSimpleListAdapter mAdapter;

    public AddPictureCallback(Activity activity, MaterialSimpleListAdapter adapter) {
        mActivity = activity;
        mAdapter = adapter;
    }


    @Override
    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        MaterialSimpleListItem item = mAdapter.getItem(which);
        if (item.getContent() == mActivity.getResources().getString(R.string.add_picture_dialog_take_photo)) {
            //take photo selected, start camera app
            PhotoUtils.dispatchTakePictureIntent(mActivity);
        } else {
            //choose image selected, start image choosing intent
            PhotoUtils.choosePhotoFromGallery(mActivity);
        }
        //item selected cancel dialog
        dialog.cancel();
    }
}
