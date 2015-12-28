package com.gcode.notes.ui.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.gcode.notes.R;
import com.gcode.notes.data.note.NoteData;
import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.data.note.list.ListData;
import com.gcode.notes.data.note.list.ListDataItem;
import com.gcode.notes.extras.builders.IntentBuilder;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

import java.util.ArrayList;

public class NotificationHelper {

    public static void makeNoteDataReminderNotification(Context context, String noteDataSerialized) {
        NoteData noteData = Serializer.parseNoteData(noteDataSerialized); //parse noteData
        NotificationCompat.Builder notificationBuilder = getBaseNotificationBuilder(context, noteData); //get build with title and icon set
        if (noteData.hasDescription()) {
            //note has description, set it as notification content text
            notificationBuilder.setContentText(noteData.getDescription());
        }
        addPendingIntentAndShowNotification(context, notificationBuilder, noteDataSerialized, noteData);
    }

    public static void makeListDataReminderNotification(Context context, String listDataSerialized) {
        ListData listData = Serializer.parseListData(listDataSerialized);
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
        addPendingIntentAndShowNotification(context, builder, listDataSerialized, listData);
    }

    private static void addPendingIntentAndShowNotification(Context context, NotificationCompat.Builder notificationBuilder,
                                                            String objectSerialized, ContentBase contentBase) {

        //creates intent that starts MainActivity with the required extras to start display activity
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
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(contentBase.getTitle())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }
}
