package com.gcode.notes.extras.builders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.activities.display.list.DisplayListBinActivity;
import com.gcode.notes.activities.display.list.DisplayListNormalActivity;
import com.gcode.notes.activities.display.note.DisplayNoteBinActivity;
import com.gcode.notes.activities.display.note.DisplayNoteNormalActivity;
import com.gcode.notes.data.note.NoteData;
import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.data.note.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class IntentBuilder {
    public static Intent buildStartComposeFromPhotoIntent(Activity activity, Uri photoUri) {
        return buildStartComposeFromPhotoIntent(activity, photoUri.toString());
    }

    public static Intent buildStartComposeFromPhotoIntent(Activity activity, String photoPath) {
        Intent intent = new Intent(activity, ComposeNoteActivity.class);
        intent.putExtra(Constants.EXTRA_PHOTO_URI, photoPath);
        intent.putExtra(Constants.EXTRA_SETUP_FROM, Constants.SETUP_FROM_PHOTO);
        return intent;
    }

    public static Intent buildStartComposeFromAudioIntent(Activity activity, Intent data, String audioFilePath) {
        Intent intent = new Intent(activity, ComposeNoteActivity.class);
        intent.putExtra(Constants.EXTRA_SETUP_FROM, Constants.SETUP_FROM_AUDIO);
        intent.putExtra(Constants.EXTRA_AUDIO_PATH, audioFilePath);
        intent.putExtra(Constants.EXTRA_RECOGNIZED_SPEECH_TEXT,
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        return intent;
    }

    public static Intent buildStartMainActivityFromReminder(Context context, String objectSerialized, int type) {
        Intent intent = new Intent(context, MainActivity.class); //start MainActivity
        intent.putExtra(Constants.EXTRA_FROM_REMINDER_NOTIFICATION, true); //flag that help MainActivity recognize that it was started from notification
        intent.putExtra(Constants.EXTRA_TYPE, type); //int flag that helps MainActivity to recognize if object is note/list
        switch (type) {
            case Constants.TYPE_NOTE:
                //put objectSerialized as EXTRA_NOTE_DATA
                intent.putExtra(Constants.EXTRA_NOTE_DATA, objectSerialized);
                break;
            case Constants.TYPE_LIST:
                //put objectSerialized as EXTRA_LIST_DATA
                intent.putExtra(Constants.EXTRA_LIST_DATA, objectSerialized);
                break;
            default:
                //unknown type, log it
                MyDebugger.log("buildStartMainActivityFromReminder() unknown type", type);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); //makes intent start/backtrace to
        //MainActivity and if already created onNewIntent() is called
        return intent;
    }

    public static Intent buildStartDisplayActivity(Activity activity, ContentBase contentBase) {
        Intent intent = null;
        int mode = contentBase.getMode();
        int type = contentBase.getType();

        switch (mode) {
            case Constants.MODE_NORMAL:
            case Constants.MODE_IMPORTANT:
                //start normal display activity
                if (type == Constants.TYPE_NOTE) {
                    //it's note, start display note activity
                    intent = new Intent(activity, DisplayNoteNormalActivity.class);
                } else {
                    //it's list start display list activity
                    intent = new Intent(activity, DisplayListNormalActivity.class);
                }
                break;
            case Constants.MODE_PRIVATE:
                //TODO: private
                break;
            case Constants.MODE_DELETED_NORMAL:
            case Constants.MODE_DELETED_IMPORTANT:
                //start bin display activity
                if (type == Constants.TYPE_NOTE) {
                    //it's note, start display note activity
                    intent = new Intent(activity, DisplayNoteBinActivity.class);
                } else {
                    //it's list start display list activity
                    intent = new Intent(activity, DisplayListBinActivity.class);
                }
                break;
            default:
                //unknown note mode, log it
                MyDebugger.log("buildStartDisplayActivity() unknown mode", mode);
        }

        if (intent != null) {
            //intent has been instantiated successfully, add its extras and start it
            if (type == Constants.TYPE_NOTE) {
                //note, add noteData extra
                intent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData((NoteData) contentBase));
            } else {
                //list, add listData extra
                intent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(((ListData) contentBase)));
            }
        }

        return intent;
    }
}
