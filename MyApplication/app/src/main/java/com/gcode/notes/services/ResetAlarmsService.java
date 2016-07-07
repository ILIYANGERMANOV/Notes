package com.gcode.notes.services;

import android.app.IntentService;
import android.content.Intent;

import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.extras.utils.AlarmUtils;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.receivers.BootWakefulBroadcastReceiver;

import java.util.Date;

public class ResetAlarmsService extends IntentService {
    public static String RESET_ALARM_SERVICE_TAG = "reset_alarm_service";
    //!NOTE because the service was started by WakefulBroadcastReceiver, the wake lock must be released
    // by calling completeWakefulIntent(). AND IMPORTANT: the service might restart in the middle of the work
    //but in our case won't a problem, AlarmUtils#setAlarms() will just update the alarm


    public ResetAlarmsService() {
        super(RESET_ALARM_SERVICE_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //builds list with all notes, which has reminder
        for (ContentBase contentBase : MyApplication.getWritableDatabase().getAllNotesWithReminder()) {
            Date reminderDate = DateUtils.parseDateFromSQLiteFormat(contentBase.getReminder()); //parse reminderDate
            if (reminderDate == null) {
                //failed to parse reminder date, log it and skip this alarm
                MyLogger.log(RESET_ALARM_SERVICE_TAG + " onHandleIntent() failed to parse reminder date.");
                continue;
            }
            AlarmUtils.setAlarm(this, contentBase.getId(), reminderDate.getTime()); //reset alarm for current contentBase
        }
        BootWakefulBroadcastReceiver.completeWakefulIntent(intent);//releases the wake lock
    }
}
