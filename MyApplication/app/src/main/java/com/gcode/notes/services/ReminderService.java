package com.gcode.notes.services;

import android.app.IntentService;
import android.content.Intent;

import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.database.DatabaseController;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.ui.helpers.NotificationHelper;

public class ReminderService extends IntentService {
    public static String SERVICE_NAME = "ReminderService";

    public ReminderService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int noteId = intent.getIntExtra(Constants.EXTRA_NOTE_ID, Constants.NO_VALUE);
        if (noteId == Constants.NO_VALUE) {
            MyLogger.log(SERVICE_NAME + "noteId has no value.");
            return;
        }
        //try to obtain db controller instance
        DatabaseController databaseController;
        try {
            databaseController = MyApplication.getWritableDatabase();
        } catch (RuntimeException ex) {
            //there is no scenario where service is alive and application is killed (if they run in the same process, by default all components of the app run is same process)
            //this is added for extra security for future versions of android
            MyLogger.log("Application is dead in " + SERVICE_NAME + ":", ex.getLocalizedMessage());
            //try to reinitialize db and update reminder
            databaseController = new DatabaseController(this);
        }
        ContentBase contentBase = databaseController.getNoteFromId(noteId);
        switch (contentBase.getType()) {
            case Constants.TYPE_NOTE:
                //make notification with note data
                NotificationHelper.makeNoteDataReminderNotification(getApplicationContext(), ((NoteData) contentBase));
                break;
            case Constants.TYPE_LIST:
                //make notification with list data
                NotificationHelper.makeListDataReminderNotification(getApplicationContext(), ((ListData) contentBase));
                break;
            default:
                //invalid type, log it
                MyLogger.log(SERVICE_NAME + " onHandleIntent() invalid type", contentBase.getType());
        }
    }
}
