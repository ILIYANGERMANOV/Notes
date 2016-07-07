package com.gcode.notes.activities.helpers.main.state;


import android.app.Activity;
import android.content.Intent;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyLogger;
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
                    case Constants.DISPLAY_LIST_REQUEST_CODE:
                        handleListFromDisplay(data);
                        break;
                    case Constants.COMPOSE_NOTE_REQUEST_CODE:
                        handleComposeResult(data);
                        break;
                    case Constants.DISPLAY_NOTE_REQUEST_CODE:
                        handleNoteFromDisplay(data);
                        break;
                    case Constants.REQUEST_OPEN_GALLERY:
                        handleCreateNoteWithPicture(activity, data);
                        break;
                    case Constants.SPEECH_INPUT_REQ_CODE:
                        handleSpeechInputOption(activity, data);
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

    private static void handleSpeechInputOption(Activity activity, Intent data) {
        String audioFilePath = FileUtils.createVoiceRecordFile(data.getData());
        if (audioFilePath != null) {
            Intent intent = IntentBuilder.buildStartComposeFromAudioIntent(activity, data, audioFilePath);
            activity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
        } else {
            MyLogger.log("handleSpeechInputOption()", "audioFilePath is null");
        }
    }

    private static void handleCreateNoteWithPicture(final Activity activity, Intent data) {
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
            MyLogger.log("handleTakePhotoResult", "PhotoUtils.photoUri is null");
        }
    }

    private static void handleComposeResult(Intent data) {
        if (data.getBooleanExtra(Constants.NOTE_ADDED_SUCCESSFULLY, false)) {
            int mode = data.getIntExtra(Constants.COMPOSE_NOTE_MODE, Constants.ERROR);
            if (mode != Constants.ERROR) {
                BaseController.getInstance().onNewNoteAdded(mode);
            } else {
                MyLogger.log("MainResultHandler handleComposeResult() mode ERROR!");
            }
        }
    }

    private static void handleNoteFromDisplay(Intent data) {
        NoteData noteData = Serializer.parseNoteData(data.getStringExtra(Constants.EXTRA_NOTE_DATA));
        if (noteData != null) {
            notifyControllerForChanges(data, noteData);
        } else {
            MyLogger.log("NOTE_FROM_DISPLAY noteData is null!");
        }
    }

    private static void handleListFromDisplay(Intent data) {
        ListData listData = Serializer.parseListData(data.getStringExtra(Constants.EXTRA_LIST_DATA));
        if (listData != null) {
            notifyControllerForChanges(data, listData);
        } else {
            MyLogger.log("LIST_FROM_DISPLAY listData is null!");
        }
    }

    private static void notifyControllerForChanges(Intent data, ContentBase contentBase) {
        //TODO: consider adding flags for note change so onItemChanged() won't be called every time, at least call only when open in edit mode
        BaseController controller = BaseController.getInstance();
        if (data.getBooleanExtra(Constants.EXTRA_NOTE_MODE_CHANGED, false)) {
            //item mode has changed, onItemModeChanged will handle properly
            controller.onItemModeChanged(contentBase);
        } else {
            //item mode has NOT changed, onItemChanged will update note
            controller.onItemChanged(contentBase);
        }
    }
}
