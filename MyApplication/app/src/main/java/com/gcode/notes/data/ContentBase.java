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
    int priority;
    String reminderString;
    Date creationDate;
    String expirationDateString;

    public ContentBase(String title, int mode, int priority, @Nullable String reminderString) {
        this.title = title;
        this.mode = mode;
        this.priority = priority;
        this.reminderString = reminderString;
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

    public Date getExpirationDate() {
        Date expirationDate = null;
        try {
            expirationDate = DateFormat.getInstance().parse(expirationDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return expirationDate;
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

    public int getPriority() {
        return priority;
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

    public void setPriority(int priority) {
        this.priority = priority;
    }
}

