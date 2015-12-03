package com.gcode.notes.activities.helpers.compose.note;

import android.content.Intent;

import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.FileUtils;
import com.gcode.notes.extras.utils.PhotoUtils;
import com.gcode.notes.extras.utils.callbacks.PhotoSelectedCallback;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

public class ComposeNoteResultHandler {
    public static void handleResult(ComposeNoteActivity composeNoteActivity, int requestCode, int resultCode, Intent data) {
        if (resultCode == ComposeNoteActivity.RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case Constants.REQUEST_OPEN_GALLERY:
                        handleSelectedPhotoFromGallery(composeNoteActivity, data);
                        break;
                }
            } else {
                //data is null, check for camera app result
                if (requestCode == Constants.REQUEST_TAKE_PHOTO) {
                    handleTakePhotoResult(composeNoteActivity);
                }
            }
        } else if (resultCode == ComposeNoteActivity.RESULT_CANCELED && requestCode == Constants.REQUEST_TAKE_PHOTO) {
            //taking photo canceled, try to delete temp file
            if (PhotoUtils.pathToPhoto != null) {
                FileUtils.deleteFile(PhotoUtils.pathToPhoto);
            }
        }

        if (requestCode == Constants.OPEN_PHOTO_IN_GALLERY_REQ_CODE) {
            //comeback from image opened in gallery, dismiss openImageInGallery progress dialog
            if (composeNoteActivity.mOpenImageInGalleryProgressDialog != null) {
                composeNoteActivity.mOpenImageInGalleryProgressDialog.dismiss();
            }
        }
    }

    private static void handleSelectedPhotoFromGallery(final ComposeNoteActivity composeNoteActivity, Intent data) {
        PhotoSelectedCallback photoSelectedCallback = new PhotoSelectedCallback() {
            @Override
            public void onPhotoSelected(String photoPath) {
                //photo selected successfully, add it
                ComposeNotePhotoHelper.addPhoto(composeNoteActivity, photoPath);
            }
        };
        PhotoUtils.handleSelectedPhotoFromGallery(composeNoteActivity, data, photoSelectedCallback);
    }

    private static void handleTakePhotoResult(ComposeNoteActivity composeNoteActivity) {
        if (PhotoUtils.pathToPhoto != null) {
            //photo is taken successfully, add to gallery and to adapter
            PhotoUtils.addPhotoToGallery(MyApplication.getAppContext(), PhotoUtils.pathToPhoto);
            ComposeNotePhotoHelper.addPhoto(composeNoteActivity, PhotoUtils.pathToPhoto.toString());
        } else {
            MyDebugger.log("handleTakePhotoResult", "PhotoUtils.photoUri is null");
        }
    }
}
