package com.gcode.notes.activities.helpers.main;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.FileUtils;
import com.gcode.notes.extras.utils.PhotoUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

import java.io.File;

public class MainActivityResultHandler {
    public static void handleResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == MainActivity.RESULT_OK) {
            if (data != null) {
                //result from compose/display activity
                switch (requestCode) {
                    case Constants.LIST_FROM_DISPLAY_REQUEST_CODE:
                        handleListFromDisplay(data);
                        break;
                    case Constants.COMPOSE_NOTE_REQUEST_CODE:
                        handleComposeResult(data);
                        break;
                    case Constants.NOTE_FROM_DISPLAY_REQUEST_CODE:
                        handleNoteFromDisplay(data);
                        break;
                    case Constants.REQUEST_OPEN_GALLERY:
                        handleSelectedPhotoFromGallery(activity, data);
                        break;
                }
            } else {
                //data is null, check for camera app result
                if (requestCode == Constants.REQUEST_TAKE_PHOTO) {
                    handleTakePhotoResult(activity);
                }
            }
        } else if (resultCode == MainActivity.RESULT_CANCELED && requestCode == Constants.REQUEST_TAKE_PHOTO) {
            //taking photo canceled, try to delete temp file
            if (PhotoUtils.pathToPhoto != null) {
                FileUtils.deleteFile(PhotoUtils.pathToPhoto);
            }
        }
    }

    private static void handleSelectedPhotoFromGallery(Activity activity, Intent data) {
        //photo selected from gallery
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = activity.getContentResolver().query(selectedImage, filePath, null, null, null);
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
        if(photoUri != null) {
            //selected photoUri obtained successfully, start compose note activity with it
            Intent intent = new Intent(activity, ComposeNoteActivity.class);
            intent.putExtra(Constants.EXTRA_PHOTO_URI, photoUri.toString());
            activity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
        } else {
            MyDebugger.log("handleSelectedPhotoFromGallery", "photoUri is null");
        }
    }

    private static void handleTakePhotoResult(Activity activity) {
        if (PhotoUtils.pathToPhoto != null) {
            //photo is taken successfully, add to gallery and launch compose activity with it
            PhotoUtils.addPhotoToGallery(MyApplication.getAppContext(), PhotoUtils.pathToPhoto);
            Intent intent = new Intent(activity, ComposeNoteActivity.class);
            intent.putExtra(Constants.EXTRA_PHOTO_URI, PhotoUtils.pathToPhoto.toString());
            activity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
        } else {
            MyDebugger.log("handleTakePhotoResult", "PhotoUtils.photoUri is null");
        }
    }

    private static void handleComposeResult(Intent data) {
        if (data.getBooleanExtra(Constants.NOTE_ADDED_SUCCESSFULLY, false)) {
            int mode = data.getIntExtra(Constants.COMPOSE_NOTE_MODE, Constants.ERROR);
            if (mode != Constants.ERROR) {
                BaseController.getInstance().onItemAdded(mode);
            } else {
                MyDebugger.log("onActivityResult() mode ERROR!");
            }
        }
    }

    private static void handleNoteFromDisplay(Intent data) {
        String serializedNoteData = data.getStringExtra(Constants.EXTRA_NOTE_DATA);
        if (serializedNoteData != null) {
            NoteData noteData = Serializer.parseNoteData(serializedNoteData);
            if (noteData != null) {
                notifyControllerForChanges(data, noteData);
            } else {
                MyDebugger.log("NOTE_FROM_DISPLAY noteData is null!");
            }
        } else {
            MyDebugger.log("NOTE_FROM_DISPLAY serializedNoteData is null!");
        }
    }

    private static void handleListFromDisplay(Intent data) {
        String serializedListData = data.getStringExtra(Constants.EXTRA_LIST_DATA);
        if (serializedListData != null) {
            ListData listData = Serializer.parseListData(serializedListData);
            if (listData != null) {
                notifyControllerForChanges(data, listData);
            } else {
                MyDebugger.log("LIST_FROM_DISPLAY listData is null!");
            }
        } else {
            MyDebugger.log("LIST_FROM_DISPLAY serializedListData is null!");
        }
    }

    private static void notifyControllerForChanges(Intent data, ContentBase contentBase) {
        BaseController controller = BaseController.getInstance();
        controller.onItemChanged(contentBase);
        if (data.getBooleanExtra(Constants.EXTRA_NOTE_MODE_CHANGED, false)) {
            controller.onItemModeChanged(contentBase);
        }
    }
}
