package com.gcode.notes.activities.helpers.compose.base;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeBaseActivity;
import com.gcode.notes.data.main.ContentBase;
import com.gcode.notes.extras.values.Constants;

public class ComposeBaseSaveHelper {
    /**
     * Sets TITLE, REMINDER, MODE and ATTRIBUTES_FLAG
     *
     * @param composeBaseActivity - ComposeBaseActivity used to obtain TitleEditText, ReminderTextView and mIsStarred
     * @param contentBase         - used to set its title, reminder, mode, and attributes flag
     */
    public static void saveBase(ComposeBaseActivity composeBaseActivity, ContentBase contentBase) {
        contentBase.setTitle(composeBaseActivity.getTitleEditText().getText().toString());
        //TODO: use real reminder
        String reminderString = composeBaseActivity.getReminderTextView().getText().toString();
        if (!reminderString.equals(composeBaseActivity.getResources().getString(R.string.compose_note_set_reminder_text))) {
            contentBase.setReminder(reminderString);
        }
        contentBase.setMode(composeBaseActivity.mIsStarred ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL);
        contentBase.setHasAttributesFlag(contentBase.hasAttributes());
    }
}
