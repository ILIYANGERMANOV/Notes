package com.gcode.notes.activities.helpers.display.note.listeners;

import android.view.View;

import com.gcode.notes.activities.display.note.DisplayNoteBaseActivity;
import com.gcode.notes.extras.utils.PhotoUtils;
import com.gcode.notes.ui.helpers.DialogBuilder;
import com.linearlistview.LinearListView;

public class DisplayNoteImageOnItemClickListener implements LinearListView.OnItemClickListener {
    DisplayNoteBaseActivity mDisplayNoteBaseActivity;

    public DisplayNoteImageOnItemClickListener(DisplayNoteBaseActivity displayNoteBaseActivity) {
        mDisplayNoteBaseActivity = displayNoteBaseActivity;
    }

    @Override
    public void onItemClick(LinearListView parent, View view, int position, long id) {
        if (mDisplayNoteBaseActivity.mOpenInGalleryProgressDialog == null) {
            mDisplayNoteBaseActivity.mOpenInGalleryProgressDialog =
                    DialogBuilder.buildOpenImageProgressDialog(mDisplayNoteBaseActivity);
        }
        if (mDisplayNoteBaseActivity.mOpenInGalleryProgressDialog.isShowing()) {
            //another open is already in progress, prevent further execution
            return;
        }

        mDisplayNoteBaseActivity.mOpenInGalleryProgressDialog.show();
        //TODO: add on click effect on image
        PhotoUtils.openPhotoInGallery(mDisplayNoteBaseActivity,
                (String) parent.getAdapter().getItem(position));
    }
}
