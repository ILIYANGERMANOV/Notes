package com.gcode.notes.data;


import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gcode.notes.extras.values.Constants;

import java.util.Date;

public class ContentBase {
    int id;
    int orderId;
    int targetId;

    String title;
    int mode;
    int type;
    boolean hasAttributes;
    String reminderString;
    Date creationDate;
    //TODO: make legit expirationDate, add lastModified
    String expirationDateString;


    public ContentBase(int id, int orderId, int targetId, String title, int mode, boolean hasAttributes,
                       String reminderString, Date creationDate, String expirationDateString) {

        this.id = id;
        this.orderId = orderId;
        this.targetId = targetId;
        this.title = title;
        this.mode = mode;
        this.reminderString = reminderString;
        this.hasAttributes = hasAttributes;
        this.creationDate = creationDate;
        this.expirationDateString = expirationDateString;
    }


    public ContentBase(String title, int mode, @Nullable String reminderString) {
        this.title = title;
        this.mode = mode;
        this.reminderString = reminderString;
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

    public void setExpirationDateString(String expirationDateString) {
        this.expirationDateString = expirationDateString;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getExpirationDate() {
        return expirationDateString;
    }

    public boolean hasExpirationDate() {
        return expirationDateString != null;
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

    public String getReminderString() {
        return hasReminder() ? reminderString : Constants.NO_REMINDER;
    }

    public void setReminderString(String reminderString) {
        this.reminderString = reminderString;
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

    boolean hasReminder() {
        return !reminderString.equals(Constants.NO_REMINDER);
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
            reminderTextView.setText(reminderString);
        } else {
            reminderTextView.setVisibility(View.GONE);
        }
    }
}

