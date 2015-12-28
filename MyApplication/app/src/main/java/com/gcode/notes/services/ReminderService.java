package com.gcode.notes.services;

import android.app.IntentService;
import android.content.Intent;

import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.ui.helpers.NotificationHelper;

public class ReminderService extends IntentService {
    public static String SERVICE_NAME = "ReminderService";

    public ReminderService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int type = intent.getIntExtra(Constants.EXTRA_TYPE, Constants.NO_VALUE);
        if (type == Constants.TYPE_NOTE) {
            //make notification with note data
            NotificationHelper.makeNoteDataReminderNotification(getApplicationContext(),
                    intent.getStringExtra(Constants.EXTRA_NOTE_DATA));
        } else if (type == Constants.TYPE_LIST) {
            //make notification with list data
            NotificationHelper.makeListDataReminderNotification(getApplicationContext(),
                    intent.getStringExtra(Constants.EXTRA_LIST_DATA));
        } else {
            //invalid type, log it
            MyDebugger.log(SERVICE_NAME + " onHandleIntent() invalid type", type);
        }
    }
}
