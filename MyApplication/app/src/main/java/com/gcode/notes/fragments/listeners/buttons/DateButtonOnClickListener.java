package com.gcode.notes.fragments.listeners.buttons;

import android.view.View;

import com.gcode.notes.fragments.ComposeReminderFragment;
import com.gcode.notes.ui.helpers.DialogBuilder;

public class DateButtonOnClickListener implements View.OnClickListener {
    ComposeReminderFragment mComposeReminderFragment;

    public DateButtonOnClickListener(ComposeReminderFragment composeReminderFragment) {
        mComposeReminderFragment = composeReminderFragment;
    }

    @Override
    public void onClick(View v) {
        DialogBuilder.buildDatePickerDialog(mComposeReminderFragment);
    }
}
