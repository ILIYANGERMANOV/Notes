package com.gcode.notes.activities.helpers.compose.note;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.FileUtils;
import com.gcode.notes.extras.utils.PhotoUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

import java.io.File;

public class ComposeNoteResultHandler {
    //TODO: Refactor and reduce redundancy with MainActivityResultHandler
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
            if (composeNoteActivity.mOpenImageInGalleryProgressDialog != null) {
                composeNoteActivity.mOpenImageInGalleryProgressDialog.dismiss();
            }
        }
    }

    //TODO; failing to add second/third on pictures which are saved with 0 photos
    private static void handleSelectedPhotoFromGallery(ComposeNoteActivity composeNoteActivity, Intent data) {
        //photo selected from gallery
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = composeNoteActivity.getContentResolver().query(selectedImage, filePath, null, null, null);
        if (c == null) {
            MyDebugger.log("handleSelectedPhotoFromGallery", "cursor is null, abort operation");
            return;
        }
        Uri photoUri = null;
        if (c.moveToFirst()) {
            int columnIndex = c.getColumnIndex(filePath[0]);
            photoUri = Uri.fromFile(new File(c.getString(columnIndex)));
        } else {
            MyDebugger.log("handleSelectedPhotoFromGallery", "Cursor is empty!");
        }
        c.close();
        if (photoUri != null) {
            //selected photoUri obtained successfully, add it to adapter in order to display
            composeNoteActivity.mImagesAdapter.add(photoUri.toString());
        } else {
            MyDebugger.log("handleSelectedPhotoFromGallery", "photoUri is null");
        }
    }

    private static void handleTakePhotoResult(ComposeNoteActivity composeNoteActivity) {
        if (PhotoUtils.pathToPhoto != null) {
            //photo is taken successfully, add to gallery and to adapter
            PhotoUtils.addPhotoToGallery(MyApplication.getAppContext(), PhotoUtils.pathToPhoto);
            composeNoteActivity.mImagesAdapter.add(PhotoUtils.pathToPhoto.toString());
        } else {
            MyDebugger.log("handleTakePhotoResult", "PhotoUtils.photoUri is null");
        }
    }
}
