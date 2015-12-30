package com.gcode.notes.data.note.base;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

public abstract class ContentBase {
    //have default value
    int id;
    int orderId;
    int targetId;
    boolean hasAttributesFlag;
    String reminder;
    ContentDetails contentDetails;

    //don't have default value
    String title;
    int mode;
    int type;


    public ContentBase() {
        //empty default constructor
        id = Constants.NO_VALUE;
        targetId = Constants.NO_VALUE;
        orderId = Constants.NO_VALUE;
        hasAttributesFlag = false;
        reminder = Constants.NO_REMINDER;
        contentDetails = new ContentDetails();
    }

    //this constructor is used by Extractor when collecting info from database
    public ContentBase(int id, int orderId, int targetId, String title, int mode, boolean hasAttributesFlag,
                       String reminder, String creationDate, String lastModifiedDate, String expirationDate) {

        this.id = id;
        this.orderId = orderId;
        this.targetId = targetId;
        this.title = title;
        this.mode = mode;
        this.reminder = reminder;
        this.hasAttributesFlag = hasAttributesFlag;
        contentDetails = new ContentDetails(creationDate, lastModifiedDate, expirationDate);
    }

    public abstract boolean hasAttributes();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public boolean hasCreationDate() {
        return !contentDetails.getCreationDate().equals(Constants.NO_DATE);
    }

    public String getCreationDate() {
        return contentDetails.getCreationDate();
    }

    public void setCreationDate(String creationDate) {
        contentDetails.setCreationDate(creationDate);
    }

    public String getLastModifiedDate() {
        return contentDetails.getLastModifiedDate();
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        contentDetails.setLastModifiedDate(lastModifiedDate);
    }

    public boolean hasExpirationDate() {
        return !contentDetails.getExpirationDate().trim().equals(Constants.NO_DATE);
    }

    public String getDateDetails() {
        String dateDetails = "";
        if (contentDetails == null) {
            //contentDetails are null, create dummy details to display
            MyDebugger.log("contentDetails are null, dummy details are created!");
            contentDetails = new ContentDetails();
        }

        Context context = MyApplication.getAppContext();
        final String NEW_LINE = "\n";

        dateDetails += context.getString(R.string.display_last_modified_date,
                DateUtils.formatDateTimeForDisplay(contentDetails.getLastModifiedDate())) + NEW_LINE;

        dateDetails += context.getString(R.string.display_creation_date,
                DateUtils.formatDateTimeForDisplay(contentDetails.getCreationDate()));
        if (hasExpirationDate()) {
            dateDetails += NEW_LINE + context.getString(R.string.display_expiration_date,
                    DateUtils.formatDateTimeForDisplay(contentDetails.getExpirationDate()));
        }
        return dateDetails;
    }

    public boolean getHasAttributesFlag() {
        return hasAttributesFlag;
    }

    public void setHasAttributesFlag(boolean hasAttributes) {
        this.hasAttributesFlag = hasAttributes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public boolean hasReminder() {
        return !reminder.equals(Constants.NO_REMINDER);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasValidTitle() {
        return title.trim().length() > 0;
    }

    public boolean isImportant() {
        return mode == Constants.MODE_IMPORTANT;
    }

    public void setModeImportant(boolean isImportant) {
        mode = isImportant ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL;
    }

    /**
     * Sets note's mode to MODE_DELETED_NORMAL / MODE_DELETED_IMPORTANT according its current mode.
     *
     * @return the mode of the note (MODE_DELETED_NORMAL / MODE_DELETED_IMPORTANT)
     */
    public int setAndReturnDeletedMode() {
        switch (mode) {
            case Constants.MODE_NORMAL:
                mode = Constants.MODE_DELETED_NORMAL;
                return mode;
            case Constants.MODE_IMPORTANT:
                mode = Constants.MODE_DELETED_IMPORTANT;
                return mode;
        }
        return Constants.ERROR;
    }

    /**
     * Displays content base's title and reminder
     *
     * @param titleTextView    - text view in which to display title
     * @param reminderTextView - text view in which to display reminder
     */
    public void displayBase(TextView titleTextView, TextView reminderTextView) {
        titleTextView.setText(title);
        displayReminder(reminderTextView);
    }

    private void displayReminder(TextView reminderTextView) {
        if (hasReminder()) {
            //there is reminder, format it for display and show it
            reminderTextView.setVisibility(View.VISIBLE);
            reminderTextView.setText(DateUtils.formatDateTimeForDisplay(reminder)); //reminder is in SQLite format
        } else {
            //there is no reminder, hide reminderTextView
            reminderTextView.setVisibility(View.GONE);
        }
    }
}

