package com.gcode.notes.activities.helpers.main.state;

import android.content.Intent;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.visible.callbacks.AuthenticationCallbacks;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.builders.IntentBuilder;
import com.gcode.notes.extras.utils.AuthenticationUtils;
import com.gcode.notes.extras.utils.MyUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.values.Keys;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;
import com.gcode.notes.tasks.async.encryption.DecryptNoteTask;
import com.gcode.notes.tasks.async.encryption.callbacks.DecryptTaskCallbacks;
import com.gcode.notes.tasks.async.main.RemoveItemFromMainTask;

public class ReminderNotificationStartHelper implements AuthenticationCallbacks,
        DecryptTaskCallbacks {

    private MainActivity mMainActivity;

    private ContentBase mContentBase;

    public ReminderNotificationStartHelper(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void handleIfStartedFromReminderNotification(Intent intent) {
        if (intent != null && intent.getBooleanExtra(Constants.EXTRA_FROM_REMINDER_NOTIFICATION, false)) {
            //!NOTE contentBase's reminder, so it wont duplicate
            //activity was started from reminder notification, start display activity according it extra data
            switch (intent.getIntExtra(Constants.EXTRA_NOTE_ID, Constants.NO_VALUE)) {
                case Constants.TYPE_NOTE:
                    //It's note, parse noteData, remove it's reminder and start according display activity
                    NoteData noteData = Serializer.parseNoteData(intent.getStringExtra(Constants.EXTRA_NOTE_DATA));
                    if (noteData != null) {
                        //noteData has been parsed successfully, proceed forward
                        unsetReminderAndStartDisplayActivity(noteData);
                    } else {
                        //failed to parse noteData, log it
                        MyDebugger.log("ReminderNotificationStartHelper", "failed to parse noteData");
                    }
                    break;
                case Constants.TYPE_LIST:
                    //It's list, parse listData, remove it's reminder and start according display activity
                    ListData listData = Serializer.parseListData(intent.getStringExtra(Constants.EXTRA_LIST_DATA));
                    if (listData != null) {
                        //listData has been parsed successfully, proceed forward
                        unsetReminderAndStartDisplayActivity(listData);
                    } else {
                        //failed to parse listData, log it
                        MyDebugger.log("ReminderNotificationStartHelper", "failed to parse listData");
                    }
                    break;
                default:
                    //there is no note type set, log it and prevent further execution
                    MyDebugger.log("ReminderNotificationStartHelper unknown content base type");
            }
        }
    }

    private void unsetReminderAndStartDisplayActivity(ContentBase contentBase) {
        contentBase.setReminder(null); //unset contentBase's reminder
        MyApplication.getWritableDatabase().updateNoteReminder(contentBase); //apply reminder update to db

        if (contentBase.getMode() == Constants.MODE_PRIVATE) {
            //its private note, authenticate access
            mContentBase = contentBase;
            AuthenticationUtils.getInstance(mMainActivity, this).authenticate();
            return;
        }
        startDisplayActivity(contentBase);
    }

    private void startDisplayActivity(ContentBase contentBase) {
        Intent displayActivityIntent = IntentBuilder.buildStartDisplayActivityIntent(mMainActivity, contentBase);
        if (displayActivityIntent != null) {
            //IntentBuilder has successfully built intent, start it
            int requestCode = contentBase.getType() == Constants.TYPE_NOTE ? //set requestCode according contentBase type
                    Constants.DISPLAY_NOTE_REQUEST_CODE : Constants.DISPLAY_LIST_REQUEST_CODE;

            mMainActivity.startActivityForResult(displayActivityIntent, requestCode);
        }
    }

    @Override
    public void onAuthenticated(String password) {
        new DecryptNoteTask(mMainActivity, this).execute(mContentBase);
    }

    @Override
    public void onPasswordTriesEnded() {
        //delete private note and reset password tries
        if (!MyApplication.getWritableDatabase().deleteNotePermanently(mContentBase)) {
            //failed to delete note, log it
            MyDebugger.log("ReminderNotificationStartHelper onPasswordTriesEnded() failed to delete note");
        }
        new RemoveItemFromMainTask(mMainActivity.getString(
                R.string.private_note_deleted_due_to_wrong_pass)).execute(mContentBase);

        MyUtils.saveToPreferences(Keys.PREF_PASS_TRIES, Constants.PASS_MAX_TRIES);
    }

    @Override
    public void onExitPrivate() {

    }

    @Override
    public void onDecryptedSuccessfully(ContentBase contentBase) {
        startDisplayActivity(contentBase);
    }
}
