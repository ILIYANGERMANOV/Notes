package com.gcode.notes.fragments.listeners.pickers;


import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.fragments.ComposeReminderFragment;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

public class TimePickerOnTimeSetListener implements TimePickerDialog.OnTimeSetListener {
    ComposeReminderFragment mComposeReminderFragment;

    public TimePickerOnTimeSetListener(ComposeReminderFragment composeReminderFragment) {
        mComposeReminderFragment = composeReminderFragment;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        mComposeReminderFragment.getTimeButton().setText(
                DateUtils.formatTime(mComposeReminderFragment.getContext(), hourOfDay, minute)
        );
        mComposeReminderFragment.mHour = hourOfDay;
        mComposeReminderFragment.mMinute = minute;
    }
}
