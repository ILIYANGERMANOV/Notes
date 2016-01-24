package com.gcode.notes.activities.helpers.main.state;


import android.app.Activity;
import android.content.Intent;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.builders.IntentBuilder;
import com.gcode.notes.extras.utils.FileUtils;
import com.gcode.notes.extras.utils.PhotoUtils;
import com.gcode.notes.extras.utils.callbacks.PhotoSelectedCallback;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

public class MainActivityResultHandler {
    public static void handleResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
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
                    case Constants.SPEECH_INPUT_REQ_CODE:
                        handleSpeechInput(activity, data);
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

    private static void handleSpeechInput(Activity activity, Intent data) {
        String audioFilePath = FileUtils.createVoiceRecordFile(data.getData());
        if (audioFilePath != null) {
            Intent intent = IntentBuilder.buildStartComposeFromAudioIntent(activity, data, audioFilePath);
            activity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
        } else {
            MyDebugger.log("handleSpeechInput()", "audioFilePath is null");
        }
    }

    private static void handleSelectedPhotoFromGallery(final Activity activity, Intent data) {
        //photo selected from gallery
        PhotoSelectedCallback photoSelectedCallback = new PhotoSelectedCallback() {
            @Override
            public void onPhotoSelected(String photoPath) {
                //selected photoUri obtained successfully, start compose note activity with it
                Intent intent = IntentBuilder.buildStartComposeFromPhotoIntent(activity, photoPath);
                activity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
            }
        };
        PhotoUtils.handleSelectedPhotoFromGallery(activity, data, photoSelectedCallback);
    }


    private static void handleTakePhotoResult(Activity activity) {
        if (PhotoUtils.pathToPhoto != null) {
            //photo is taken successfully, add to gallery and launch compose activity with it
            PhotoUtils.addPhotoToGallery(MyApplication.getAppContext(), PhotoUtils.pathToPhoto);
            Intent intent = IntentBuilder.buildStartComposeFromPhotoIntent(activity, PhotoUtils.pathToPhoto);
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
        //TODO: same issues as BaseController#onItemModeChanged
        controller.onItemChanged(contentBase);
        if (data.getBooleanExtra(Constants.EXTRA_NOTE_MODE_CHANGED, false)) {
            controller.onItemModeChanged(contentBase);
        }
    }
}
