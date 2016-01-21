package com.gcode.notes.services;

import android.app.IntentService;
import android.content.Intent;

import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.database.DatabaseController;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;
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
            MyDebugger.log(SERVICE_NAME + "noteId has no value.");
            return;
        }
        ContentBase contentBase = new DatabaseController(this).getNoteFromId(noteId);
        switch (contentBase.getType()) {
            case Constants.TYPE_NOTE:
                //make notification with note data
                NotificationHelper.makeNoteDataReminderNotification(getApplicationContext(),
                        Serializer.serializeNoteData(((NoteData) contentBase)));
                break;
            case Constants.TYPE_LIST:
                //make notification with list data
                NotificationHelper.makeListDataReminderNotification(getApplicationContext(),
                        Serializer.serializeListData(((ListData) contentBase)));
                break;
            default:
                //invalid type, log it
                MyDebugger.log(SERVICE_NAME + " onHandleIntent() invalid type", contentBase.getType());
        }
    }
}
