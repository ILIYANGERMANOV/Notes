package com.gcode.notes.fragments.listeners.buttons;

import android.view.View;

import com.gcode.notes.fragments.ComposeReminderFragment;
import com.gcode.notes.ui.helpers.DialogBuilder;

public class TimeButtonOnClickListener implements View.OnClickListener {
    ComposeReminderFragment mComposeReminderFragment;

    public TimeButtonOnClickListener(ComposeReminderFragment composeReminderFragment) {
        mComposeReminderFragment = composeReminderFragment;
    }

    @Override
    public void onClick(View v) {
        DialogBuilder.buildTimePickerDialog(mComposeReminderFragment);
    }
}
