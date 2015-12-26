package com.gcode.notes.fragments.listeners.buttons;

import android.view.View;

import com.gcode.notes.fragments.ComposeReminderFragment;

public class RemoveReminderOnClickListener implements View.OnClickListener {
    ComposeReminderFragment mComposeReminderFragment;

    public RemoveReminderOnClickListener(ComposeReminderFragment composeReminderFragment) {
        mComposeReminderFragment = composeReminderFragment;
    }

    @Override
    public void onClick(View v) {
        mComposeReminderFragment.hideReminder();
    }
}
