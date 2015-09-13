package com.gcode.notes.data;


import android.support.annotation.Nullable;

import com.gcode.notes.extras.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class ContentBase {
    String title;
    int mode;
    int type;
    boolean hasAttributes;
    String reminderString;
    Date creationDate;
    String expirationDateString;

    int orderId;
    int id;
    int targetId;

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
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setExpirationDateString(String expirationDateString) {
        this.expirationDateString = expirationDateString;
    }

    public Date getCreationDate() {
        return creationDate;
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

    public Date getReminderDate() {
        Date reminderDate = null;
        try {
            reminderDate = DateFormat.getInstance().parse(reminderString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reminderDate;
    }

    public String getReminderString() {
        return hasReminder() ? reminderString : Constants.NO_REMINDER;
    }

    public int getMode() {
        return mode;
    }

    public String getTitle() {
        return title;
    }

    public boolean isValidTitle() {
        return title.trim().length() > 0;
    }

    public boolean isImportant() {
        return mode == Constants.MODE_IMPORTANT;
    }

    boolean hasReminder() {
        return !reminderString.equals(Constants.NO_REMINDER);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReminderString(String reminderString) {
        this.reminderString = reminderString;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}

