package com.gcode.notes.extras.utils;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.services.ReminderService;

public class AlarmUtils {
    public static void setAlarm(Context context, int noteId, long when) {
        Intent intent = new Intent(context, ReminderService.class);
        intent.putExtra(Constants.EXTRA_NOTE_ID, noteId);

        PendingIntent pendingIntent = PendingIntent.getService(context, noteId, //use contentBase's id, cuz it is unique and it used later for canceling alarms
                intent, PendingIntent.FLAG_CANCEL_CURRENT); //FLAG_CANCEL_CURRENT, cuz when updating reminders' date it must cancel previous intent

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (MyUtils.isPreApi19()) {
            //its pre19 and AlarmManager#set() is interpreted as exact time
            setAlarmPre19(alarmManager, when, pendingIntent);
        } else {
            //its API 19 or greater and AlarmManager#set() is NOT interpreted as exact time, use #setExact()
            setAlarm19(alarmManager, when, pendingIntent);
        }
    }

    private static void setAlarmPre19(AlarmManager alarmManager, long when, PendingIntent pendingIntent) {
        alarmManager.set(AlarmManager.RTC_WAKEUP, when, pendingIntent); //RTC_WAKEUP so ReminderService will be executed even when the phone fell asleep
        //using RTC, cuz it measures elapsed time using clock, not like ELAPSED_REAL_TIME which measures the time after the device has booted
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setAlarm19(AlarmManager alarmManager, long when, PendingIntent pendingIntent) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, when, pendingIntent); //RTC_WAKEUP so ReminderService will be executed even when the phone fell asleep
        //using RTC, cuz it measures elapsed time using clock, not like ELAPSED_REAL_TIME which measures the time after the device has booted
    }

    public static void cancelAlarm(Context context, int noteId) {
        //To work pendingIntent must be equivalent with the pendingIntent used for setting the alarm, filterEquals() used for check
        Intent intent = new Intent(context, ReminderService.class); //note sure if this intent is checked by filterEquals()
        //EXTRAS aren't check for sure 100%
        PendingIntent pendingIntent = PendingIntent.getService(context, noteId,
                intent, PendingIntent.FLAG_CANCEL_CURRENT); //the intent must match INTENT_ACTION and INTENT_DATA, so filterEquals() can pass

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
