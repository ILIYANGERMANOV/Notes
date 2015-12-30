package com.gcode.notes.activities.helpers.compose;

import com.gcode.notes.activities.compose.ComposeBaseActivity;
import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.extras.utils.AlarmUtils;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.fragments.ComposeReminderFragment;
import com.gcode.notes.notes.MyApplication;

public class ComposeBaseSaveHelper {
    /**
     * Sets TITLE, REMINDER, MODE and ATTRIBUTES_FLAG
     *
     * @param composeBaseActivity - ComposeBaseActivity used to obtain TitleEditText, ReminderTextView and mIsStarred
     * @param contentBase         - used to set its title, reminder, mode, and attributes flag
     * @return whether the note had valid title before validation
     */
    public static boolean saveBase(ComposeBaseActivity composeBaseActivity, ContentBase contentBase) {
        contentBase.setTitle(composeBaseActivity.getTitleEditText().getText().toString());

        boolean hadValidTitleBeforeValidation = contentBase.hasValidTitle();

        //!NOTE: contentBase#setMode should be called before setting reminder,
        // cuz it will result in bug ('buildStartDisplayActivity() unknown mode: 0')
        contentBase.setMode(composeBaseActivity.mIsStarred ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL); //mode should be set before setting reminder, cuz it will result in bug
        contentBase.setHasAttributesFlag(contentBase.hasAttributes());

        //!NOTE: all attributes used in display must be set before ---save reminder---- so it can display correctly
        String currentTimeSQLiteFormatted = DateUtils.getCurrentTimeSQLiteFormatted();
        if (!contentBase.hasCreationDate()) {
            //its new note, set creation date
            contentBase.setCreationDate(currentTimeSQLiteFormatted);
        }
        contentBase.setLastModifiedDate(currentTimeSQLiteFormatted); //sets last modified date to current

        //validates contentBase title and if it is new note sets its id
        MyApplication.getWritableDatabase().validateNote(contentBase, composeBaseActivity.mIsOpenedInEditMode);

        //save reminder---------------------------------------------------------------------------------------------
        ComposeReminderFragment composeReminderFragment = composeBaseActivity.getComposeReminderFragment();
        if (composeReminderFragment != null) {
            //composeReminderFragment found successfully, set reminder to note
            if (contentBase.hasReminder() && !composeReminderFragment.mIsReminderSet) {
                //contentBase had reminder and now this reminder is removed, cancel alarm
                //!NOTE use this check before contentBase.setReminder(composeReminderFragment.getReminder()), cuz you will
                //NOT know if contentBase had reminder set
                AlarmUtils.cancelAlarm(composeBaseActivity, contentBase.getId());
            }
            contentBase.setReminder(composeReminderFragment.getReminder());
            if (contentBase.hasReminder()) {
                //contentBase has reminder after obtaining data from ComposeReminderFragment, set an alarm event
                composeReminderFragment.setAlarmForReminder(contentBase);
            }
        }
        //save reminder---------------------------------------------------------------------------------------------

        return hadValidTitleBeforeValidation;
    }
}
