package com.gcode.notes.activities.helpers.compose;

import com.gcode.notes.activities.compose.ComposeBaseActivity;
import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.extras.utils.AlarmUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.fragments.ComposeReminderFragment;

public class ComposeBaseSaveHelper {
    /**
     * Sets TITLE, REMINDER, MODE and ATTRIBUTES_FLAG
     *
     * @param composeBaseActivity - ComposeBaseActivity used to obtain TitleEditText, ReminderTextView and mIsStarred
     * @param contentBase         - used to set its title, reminder, mode, and attributes flag
     */
    public static void saveBase(ComposeBaseActivity composeBaseActivity, ContentBase contentBase) {
        contentBase.setTitle(composeBaseActivity.getTitleEditText().getText().toString());
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
        contentBase.setMode(composeBaseActivity.mIsStarred ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL);
        contentBase.setHasAttributesFlag(contentBase.hasAttributes());
    }
}
