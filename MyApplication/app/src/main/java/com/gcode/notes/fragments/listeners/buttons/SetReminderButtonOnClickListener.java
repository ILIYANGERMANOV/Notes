package com.gcode.notes.fragments.listeners.buttons;


import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.gcode.notes.fragments.ComposeReminderFragment;

public class SetReminderButtonOnClickListener implements View.OnClickListener {
    ComposeReminderFragment mComposeReminderFragment;

    public SetReminderButtonOnClickListener(ComposeReminderFragment composeReminderFragment) {
        mComposeReminderFragment = composeReminderFragment;
    }

    @Override
    public void onClick(View v) {
        mComposeReminderFragment.showReminder();
    }
}
