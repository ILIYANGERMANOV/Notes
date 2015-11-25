package com.gcode.notes.data.main;


import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gcode.notes.data.extras.ContentDetails;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.extras.values.Constants;

public class ContentBase {
    //TODO: REFACTOR AND OPTIMIZE
    int id;
    int orderId;
    int targetId;

    String title;
    int mode;
    int type;
    boolean hasAttributes;
    String reminder;

    ContentDetails contentDetails;

    public ContentBase() {
        //empty default constructor
    }

    //this constructor is used by Extractor when collecting info from database
    public ContentBase(int id, int orderId, int targetId, String title, int mode, boolean hasAttributes,
                       String reminder, String creationDate, String lastModifiedDate, String expirationDate) {

        this.id = id;
        this.orderId = orderId;
        this.targetId = targetId;
        this.title = title;
        this.mode = mode;
        this.reminder = reminder;
        this.hasAttributes = hasAttributes;
        contentDetails = new ContentDetails(creationDate, lastModifiedDate, expirationDate);
    }


    public ContentBase(String title, int mode, @Nullable String reminder) {
        this.title = title;
        this.mode = mode;
        this.reminder = reminder;
        this.targetId = Constants.ERROR;
    }

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

    public void setContentDetails(ContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    public ContentDetails getContentDetails() {
        return contentDetails != null ? contentDetails : new ContentDetails();
    }

    public String getLastModifiedDate() {
        return contentDetails.getLastModifiedDate();
    }

    public boolean hasExpirationDate() {
        return !contentDetails.getExpirationDate().trim().equals(Constants.NO_DATE);
    }

    public String getDateDetails() {
        String dateDetails = "";
        if (contentDetails == null) {
            //contentDetails are null, create dummy details to display
            contentDetails = new ContentDetails();
        }

        final String NEW_LINE = "\n";
        dateDetails += DateUtils.LAST_MODIFIED +
                DateUtils.formatDateForDisplay(contentDetails.getLastModifiedDate()) + NEW_LINE;

        dateDetails += DateUtils.CREATION_DATE + contentDetails.getCreationDate();
        if (hasExpirationDate()) {
            dateDetails += NEW_LINE + DateUtils.EXPIRATION_DATE +
                    DateUtils.formatDateForDisplay(contentDetails.getExpirationDate());
        }
        return dateDetails;
    }

    public void setAttributes(boolean hasAttributes) {
        this.hasAttributes = hasAttributes;
    }

    public boolean hasAttributes() {
        return hasAttributes;
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

    public boolean hasReminder() {
        return !reminder.equals(Constants.NO_REMINDER);
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
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

    public boolean isValidTitle() {
        return title.trim().length() > 0;
    }

    public boolean isImportant() {
        return mode == Constants.MODE_IMPORTANT;
    }

    public void setImportant(boolean isImportant) {
        if (mode == Constants.MODE_NORMAL || mode == Constants.MODE_IMPORTANT) {
            mode = isImportant ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL;
        }
    }

    public int setAndReturnDeletedMode() {
        switch (mode) {
            case Constants.MODE_NORMAL:
                mode = Constants.MODE_DELETED_NORMAL;
                return mode;
            case Constants.MODE_IMPORTANT:
                mode = Constants.MODE_DELETED_IMPORTANT;
                return mode;
            default:
                break;
        }
        return Constants.ERROR;
    }

    public void displayBase(TextView titleTextView) {
        titleTextView.setText(title);
    }

    public void displayBase(TextView titleTextView, TextView reminderTextView) {
        titleTextView.setText(title);
        displayReminder(reminderTextView);
    }

    public void displayReminder(TextView reminderTextView) {
        if (hasReminder()) {
            reminderTextView.setText(reminder);
        } else {
            reminderTextView.setVisibility(View.GONE);
        }
    }
}

