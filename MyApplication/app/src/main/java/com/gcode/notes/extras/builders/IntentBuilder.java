package com.gcode.notes.extras.builders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.activities.display.list.bin.DisplayListBinActivity;
import com.gcode.notes.activities.display.list.editable.DisplayListNormalActivity;
import com.gcode.notes.activities.display.list.editable.DisplayListPrivateActivity;
import com.gcode.notes.activities.display.note.bin.DisplayNoteBinActivity;
import com.gcode.notes.activities.display.note.editable.DisplayNoteNormalActivity;
import com.gcode.notes.activities.display.note.editable.DisplayNotePrivateActivity;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.AuthenticationUtils;
import com.gcode.notes.extras.utils.EncryptionUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
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
        //TODO: REFACTOR AND OPTIMIZE
        //TODO: fix possible bugs when lock note is implemented (consider always getting updated note from db)
        Intent intent = null;

        int type = contentBase.getType();

        if (type == Constants.TYPE_LIST) {
            //note is list, get latest update list from db
            ContentBase updatedList = getUpdatedList(contentBase);
            if(updatedList != null) {
                //secure updated list
                contentBase = updatedList;
            }
        } else {
            //note is not list, get updated mode
            try {
                contentBase.setMode(MyApplication.getWritableDatabase().getNoteModeFromId(contentBase.getId()));
            } catch (Exception e) {
                e.printStackTrace();
                MyDebugger.log("buildStartDisplayActivity() exception while getting updated note mode",
                        e.getMessage());
            }
        }

        int mode = contentBase.getMode();

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
                //start private display activity (note is already decrypted here)
                if (type == Constants.TYPE_NOTE) {
                    //it's note, start display note activity
                    intent = new Intent(activity, DisplayNotePrivateActivity.class);
                } else {
                    //it's list start display list activity
                    intent = new Intent(activity, DisplayListPrivateActivity.class);
                }
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

    private static ContentBase getUpdatedList(ContentBase contentBase) {
        ContentBase targetList = MyApplication.getWritableDatabase().getNoteFromId(contentBase.getId());
        if (targetList != null) {
            //updated list obtained successfully
            if (targetList.getMode() == Constants.MODE_PRIVATE) {
                //target list is private and needs decryption
                try {
                    //TODO: possible skipping frames (very low chance)
                    EncryptionUtils.getInstance(AuthenticationUtils.getInstance(null, null).getPassword())
                            .decryptListData(((ListData) targetList));
                } catch (Exception e) {
                    //exception while decrypting, log it and set not updated list
                    MyDebugger.log("buildStartDisplayActivity() decryption exception", e.getMessage());
                    targetList = contentBase;
                    try {
                        EncryptionUtils.getInstance(AuthenticationUtils.getInstance(null, null).getPassword())
                                .decryptListData(((ListData) targetList));
                    } catch (Exception e1) {
                        //fatal decryption exception, cannot proceed forward, log it and return null
                        MyDebugger.log("buildStartDisplayActivity() 2nd level decryption exception", e1.getMessage());
                        return null;
                    }
                }
            }
            contentBase = targetList;
        } else {
            //failed to find updated list, log it
            MyDebugger.log("buildStartDisplayActivity() failed to find updated list.");
        }

        return contentBase;
    }
}
