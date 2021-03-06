package com.gcode.notes.data.base;


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
        reminder = null;
        contentDetails = new ContentDetails();
    }

    public ContentBase(ContentBase other) {
        id = other.id;
        targetId = other.targetId;
        hasAttributesFlag = other.hasAttributesFlag;
        reminder = other.reminder;
        contentDetails = new ContentDetails(other.contentDetails);
        title = other.title;
        mode = other.mode;
        type = other.type;
    }

    //this constructor is used by Extractor when collecting info from database
    public ContentBase(int id, int orderId, int targetId, String title, int mode, boolean hasAttributesFlag,
                       String reminder, String creationDate, String lastModifiedDate, String expirationDate,
                       String myLocationSerialized) {

        this.id = id;
        this.orderId = orderId;
        this.targetId = targetId;
        this.title = title;
        this.mode = mode;
        this.reminder = reminder;
        this.hasAttributesFlag = hasAttributesFlag;
        contentDetails = new ContentDetails(creationDate, lastModifiedDate, expirationDate, myLocationSerialized);
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
        return contentDetails.getExpirationDate() != null;
    }

    public String getExpirationDate() {
        return contentDetails.getExpirationDate();
    }

    public void setExpirationDate(String expirationDate) {
        contentDetails.setExpirationDate(expirationDate);
    }

    public void displayDetails(TextView reminderTextView, TextView lastModifiedTextView,
                               TextView createdOnTextView, TextView expiresOnTextView) {
        if (contentDetails == null) {
            //contentDetails are null, create dummy details to display
            MyDebugger.log("contentDetails are null, dummy details are created!");
            contentDetails = new ContentDetails();
        }
        Context context = MyApplication.getAppContext();

        //display reminder
        if (hasReminder()) {
            reminderTextView.setVisibility(View.VISIBLE);
            //reminder is currently in SQLite format, format it for display
            reminderTextView.setText(context.getString(
                    R.string.display_reminder_text_and_date,
                    DateUtils.formatSQLiteDateForDisplay(reminder))
            );
        } else {
            //note hasn't reminder, hide it (this case is when note with reminder is modified and it is removed)
            reminderTextView.setVisibility(View.GONE);
        }

        //display last modified date
        lastModifiedTextView.setText(context.getString(R.string.display_last_modified_date,
                DateUtils.formatSQLiteDateForDisplay(getLastModifiedDate()))
        );

        //display creation date
        createdOnTextView.setText(context.getString(R.string.display_creation_date,
                DateUtils.formatSQLiteDateForDisplay(getCreationDate()))
        );

        //display expiration date
        if (hasExpirationDate()) {
            expiresOnTextView.setVisibility(View.VISIBLE);
            expiresOnTextView.setText(context.getString(
                    R.string.display_expiration_date,
                    DateUtils.formatSQLiteDateForDisplay(getExpirationDate()))
            );
        }
    }

    public boolean hasLocation() {
        return contentDetails.getMyLocation() != null;
    }

    public MyLocation getMyLocation() {
        return contentDetails.getMyLocation();
    }

    public double getLatitude() {
        return contentDetails.getMyLocation().getLatitude();
    }

    public double getLongitude() {
        return contentDetails.getMyLocation().getLongitude();
    }

    public void setLocation(double latitude, double longitude) {
        if (contentDetails.getMyLocation() == null) {
            //secure my location so there won't be null pointer exception
            contentDetails.setMyLocation(new MyLocation(latitude, longitude));
            return; // return cuz location is already set
        }
        contentDetails.getMyLocation().setLocation(latitude, longitude);
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
        return reminder != null;
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
     * Sets note's mode to MODE_NORMAL / MODE_IMPORTANT according its current mode.
     *
     * @return the mode of the note (MODE_NORMAL / MODE_IMPORTANT)
     */
    public int setAndReturnRestoredMode() {
        switch (mode) {
            case Constants.MODE_DELETED_NORMAL:
                mode = Constants.MODE_NORMAL;
                return mode;
            case Constants.MODE_DELETED_IMPORTANT:
                mode = Constants.MODE_IMPORTANT;
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
            reminderTextView.setText(DateUtils.formatSQLiteDateForDisplay(reminder)); //reminder is in SQLite format
        } else {
            //there is no reminder, hide reminderTextView
            reminderTextView.setVisibility(View.GONE);
        }
    }
}

