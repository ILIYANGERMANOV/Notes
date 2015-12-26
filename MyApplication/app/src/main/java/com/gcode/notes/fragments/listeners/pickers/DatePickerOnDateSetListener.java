package com.gcode.notes.fragments.listeners.pickers;

import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.fragments.ComposeReminderFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

public class DatePickerOnDateSetListener implements DatePickerDialog.OnDateSetListener {
    ComposeReminderFragment mComposeReminderFragment;

    public DatePickerOnDateSetListener(ComposeReminderFragment composeReminderFragment) {
        mComposeReminderFragment = composeReminderFragment;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mComposeReminderFragment.getDateButton().setText(
                DateUtils.formatDate(year, monthOfYear, dayOfMonth)
        );
        mComposeReminderFragment.mYear = year;
        mComposeReminderFragment.mMonthOfYear = monthOfYear;
        mComposeReminderFragment.mDayOfMonth = dayOfMonth;
    }
}
