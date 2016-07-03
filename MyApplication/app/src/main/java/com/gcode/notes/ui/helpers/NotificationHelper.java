package com.gcode.notes.ui.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.gcode.notes.R;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.data.list.ListDataItem;
import com.gcode.notes.database.DatabaseController;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.builders.IntentBuilder;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

import java.util.ArrayList;

public class NotificationHelper {

    public static void makeNoteDataReminderNotification(Context context, NoteData noteData) {
        NotificationCompat.Builder notificationBuilder = getBaseNotificationBuilder(context, noteData); //get build with title and icon set
        if (noteData.hasDescription()) {
            //note has description, set it as notification content text
            notificationBuilder.setContentText(noteData.getDescription());
        }
        fireReminder(context, notificationBuilder, noteData);
    }

    public static void makeListDataReminderNotification(Context context, ListData listData) {
        NotificationCompat.Builder builder = getBaseNotificationBuilder(context, listData);
        if (listData.hasAttachedList()) {
            //list has list items, summarize them and show them as notification content text
            String listDataItemsString = "";
            ArrayList<ListDataItem> listDataItems = listData.getList();
            for (int i = 0; i < listDataItems.size() && i < Constants.MAX_LIST_ITEMS_TO_DISPLAY; ++i) {
                listDataItemsString += listDataItems.get(i).getContent() + ", ";
            }
            builder.setContentText(listDataItemsString);
        }
        fireReminder(context, builder, listData);
    }

    private static void fireReminder(Context context, NotificationCompat.Builder notificationBuilder, ContentBase contentBase) {

        contentBase.setReminder(null); //unset contentBase's reminder

        //try to obtain db controller instance
        DatabaseController databaseController;
        try {
            databaseController = MyApplication.getWritableDatabase();
        } catch (RuntimeException ex) {
            //there is no scenario where service is alive and application is killed (if they run in the same process, by default all components of the app run is same process)
            //this is added for extra security for future versions of android
            MyDebugger.log("Application is dead when firing reminder:", ex.getLocalizedMessage());
            //try to reinitialize db and update reminder
            databaseController = new DatabaseController(context);
        }
        databaseController.updateNoteReminder(contentBase); //apply reminder update to db

        //creates intent that starts MainActivity with the required extras to start display activity
        String objectSerialized = contentBase.getType() == Constants.TYPE_NOTE ?
                Serializer.serializeNoteData(((NoteData) contentBase)) : Serializer.serializeListData(((ListData) contentBase));

        Intent intent = IntentBuilder.buildStartMainActivityFromReminder(context, objectSerialized, contentBase.getType());

        //creates required pending intent to handle the click event
        PendingIntent pendingIntent = PendingIntent.getActivity(context, contentBase.getId(),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent); //sets the pending intent to the notification

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE); //get NotificationManager

        Notification notification = notificationBuilder.build(); //build notification
        notification.flags |= Notification.FLAG_AUTO_CANCEL; //make notification to cancel itself when the user clicks it
        notificationManager.notify(contentBase.getId(), notification); //show notification (1st argument is its unique id)
    }

    private static NotificationCompat.Builder getBaseNotificationBuilder(Context context, ContentBase contentBase) {
        String title;
        if (contentBase.getMode() == Constants.MODE_PRIVATE) {
            title = context.getString(R.string.private_label);
        } else {
            title = contentBase.getTitle();
        }

        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }
}
