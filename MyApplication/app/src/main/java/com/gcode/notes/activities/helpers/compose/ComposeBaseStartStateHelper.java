package com.gcode.notes.activities.helpers.compose;

import com.gcode.notes.activities.compose.ComposeBaseActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.fragments.ComposeReminderFragment;

public class ComposeBaseStartStateHelper {
    /**
     * Sets activity mode.
     * Set action button image and action.
     *
     * @param composeBaseActivity activity which modes and states will be set
     */
    protected void setupFromZero(ComposeBaseActivity composeBaseActivity) {
        composeBaseActivity.mIsOpenedInEditMode = false;
        switch (BaseController.getInstance().getControllerId()) {
            case Constants.CONTROLLER_ALL_NOTES:
                composeBaseActivity.setNotStarredState();
                break;
            case Constants.CONTROLLER_IMPORTANT:
                composeBaseActivity.setStarredState();
                break;
            case Constants.CONTROLLER_PRIVATE:
                //TODO: PRIVATE: set mStarImageButton to sth
                composeBaseActivity.mInPrivateMode = true;
                break;
            default:
                break;
        }
    }

    protected void setupFromEditMode(ComposeBaseActivity composeBaseActivity, ContentBase contentBase) {
        composeBaseActivity.mIsOpenedInEditMode = true;
        composeBaseActivity.getTitleEditText().setText(contentBase.getTitle());
        if (contentBase.isImportant()) {
            composeBaseActivity.setStarredState();
        }
        if (contentBase.hasReminder()) {
            ComposeReminderFragment composeReminderFragment = composeBaseActivity.getComposeReminderFragment();
            if (composeReminderFragment != null) {
                //composeReminderFragment found successfully, display reminder
                composeReminderFragment.showReminder();
                String reminder = contentBase.getReminder(); //reminder is always in SQLiteFormat '2015-12-31 02:16:00'
                String[] reminderSplit = reminder.split(" "); //first element is date, second is time
                String[] date = reminderSplit[0].split("-"); ///now date is ["year", "month", "date"]
                String[] time = reminderSplit[1].split(":"); // now time is ["hour, "minute", "seconds"]

                composeReminderFragment.mYear = Integer.parseInt(date[0]); //year
                composeReminderFragment.mMonthOfYear = Integer.parseInt(date[1]) - 1; //month (-1 cuz was months start from 0)
                composeReminderFragment.mDayOfMonth = Integer.parseInt(date[2]); //day

                composeReminderFragment.mHour = Integer.parseInt(time[0]); //hour
                composeReminderFragment.mMinute = Integer.parseInt(time[1]); //minute

                composeReminderFragment.updateButtonsText(); //update buttons' text to above setted values
            }
        }
        if (contentBase.getMode() == Constants.MODE_PRIVATE) {
            //note is private mode, rise mInPrivateMode flag
            composeBaseActivity.mInPrivateMode = true;
        }
    }
}
