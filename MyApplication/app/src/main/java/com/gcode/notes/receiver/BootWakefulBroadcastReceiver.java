package com.gcode.notes.receiver;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.gcode.notes.services.ResetAlarmsService;

public class BootWakefulBroadcastReceiver extends WakefulBroadcastReceiver {
    //!NOTE use WakefulBroadcastReceiver instead of normal receiver, cuz there is a little chance
    //where ResetAlarmsService won't be started

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            //boot completed, start resetAlarmService
            Intent resetAlarmsServiceIntent = new Intent(context, ResetAlarmsService.class);
            startWakefulService(context, resetAlarmsServiceIntent); //startWakefulService() guarantees that service will start
        }
    }
}
