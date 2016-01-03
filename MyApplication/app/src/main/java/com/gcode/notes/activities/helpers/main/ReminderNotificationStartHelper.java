package com.gcode.notes.activities.helpers.main;

import android.content.Intent;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.data.note.NoteData;
import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.data.note.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.builders.IntentBuilder;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

public class ReminderNotificationStartHelper {
    public static void handleIfStartedFromReminderNotifcation(MainActivity mainActivity, Intent intent) {
        if (intent != null && intent.getBooleanExtra(Constants.EXTRA_FROM_REMINDER_NOTIFICATION, false)) {
            //!NOTE contentBase's reminder, so it wont duplicate
            //activity was started from reminder notification, start display activity according it extra data
            Intent displayActivityIntent;
            switch (intent.getIntExtra(Constants.EXTRA_TYPE, Constants.NO_VALUE)) {
                case Constants.TYPE_NOTE:
                    //It's note, parse noteData, remove it's reminder and start according display activity
                    NoteData noteData = Serializer.parseNoteData(intent.getStringExtra(Constants.EXTRA_NOTE_DATA));
                    if (noteData != null) {
                        //noteData has been parsed successfully, proceed forward
                        unsetReminderAndStartDisplayActivity(mainActivity, noteData);
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
                        unsetReminderAndStartDisplayActivity(mainActivity, listData);
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

    private static void unsetReminderAndStartDisplayActivity(MainActivity mainActivity, ContentBase contentBase) {
        contentBase.setReminder(Constants.NO_REMINDER); //unset contentBase's reminder
        MyApplication.getWritableDatabase().updateNoteReminder(contentBase); //apply reminder update to db
        Intent displayActivityIntent = IntentBuilder.buildStartDisplayActivity(mainActivity, contentBase);
        if (displayActivityIntent != null) {
            //IntentBuilder has successfully built intent, start it
            int requestCode = contentBase.getType() == Constants.TYPE_NOTE ? //set requestCode according contentBase type
                    Constants.NOTE_FROM_DISPLAY_REQUEST_CODE : Constants.LIST_FROM_DISPLAY_REQUEST_CODE;

            mainActivity.startActivityForResult(displayActivityIntent, requestCode);
        }
    }
}
