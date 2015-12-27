package com.gcode.notes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.gcode.notes.R;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.values.Tags;
import com.gcode.notes.fragments.listeners.buttons.DateButtonOnClickListener;
import com.gcode.notes.fragments.listeners.buttons.RemoveReminderOnClickListener;
import com.gcode.notes.fragments.listeners.buttons.SetReminderButtonOnClickListener;
import com.gcode.notes.fragments.listeners.buttons.TimeButtonOnClickListener;
import com.gcode.notes.fragments.listeners.pickers.DatePickerOnDateSetListener;
import com.gcode.notes.fragments.listeners.pickers.TimePickerOnTimeSetListener;
import com.gcode.notes.ui.helpers.PaintFlagsHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ComposeReminderFragment extends Fragment {
    @Bind(R.id.compose_set_reminder_button)
    Button mSetReminderButton;

    @Bind(R.id.compose_reminder_relative_layout)
    RelativeLayout mReminderLayout;

    @Bind(R.id.compose_reminder_pick_date_button)
    Button mDateButton;

    @Bind(R.id.compose_reminder_pick_time_button)
    Button mTimeButton;

    @Bind(R.id.compose_reminder_remove_button)
    ImageButton mRemoveReminderButton;

    //getters for layout components--------------------------------------------------------------------------------------
    public Button getDateButton() {
        return mDateButton;
    }

    public Button getTimeButton() {
        return mTimeButton;
    }
    //getters for layout components--------------------------------------------------------------------------------------

    public boolean mIsReminderSet;

    public int mYear;
    public int mMonthOfYear;
    public int mDayOfMonth;
    public int mHour;
    public int mMinute;

    public DatePickerOnDateSetListener mDatePickerOnDateSetListener;
    public TimePickerOnTimeSetListener mTimePickerOnTimeSetListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create picker listeners here in order to prevent memory leak, onCreate() is called once
        mDatePickerOnDateSetListener = new DatePickerOnDateSetListener(this);
        mTimePickerOnTimeSetListener = new TimePickerOnTimeSetListener(this);

        if (savedInstanceState == null) {
            //set default values
            Calendar calendar = Calendar.getInstance();
            mIsReminderSet = false;
            mYear = calendar.get(Calendar.YEAR);
            mMonthOfYear = calendar.get(Calendar.MONTH);
            mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR);
            mMinute = calendar.get(Calendar.MINUTE);
        } else {
            //use values from previous state
            mIsReminderSet = savedInstanceState.getBoolean(Constants.EXTRA_IS_REMINDER_SET, false);
            mYear = savedInstanceState.getInt(Constants.EXTRA_YEAR);
            mMonthOfYear = savedInstanceState.getInt(Constants.EXTRA_MONTH);
            mDayOfMonth = savedInstanceState.getInt(Constants.EXTRA_DAY);
            mHour = savedInstanceState.getInt(Constants.EXTRA_HOUR);
            mMinute = savedInstanceState.getInt(Constants.EXTRA_MINUTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compose_reminder_layout, container, true);
        ButterKnife.bind(this, view);
        setup();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //MaterialPickerDialogs listeners (callbacks) are lost after pause() so reset them if dialogs are created
        DatePickerDialog datePickerDialog =
                (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag(Tags.DATE_PICKER_DIALOG_TAG);
        TimePickerDialog timePickerDialog =
                (TimePickerDialog) getActivity().getFragmentManager().findFragmentByTag(Tags.TIME_PICKER_DIALOG_TAG);

        if (datePickerDialog != null) {
            //dialog is created
            datePickerDialog.setOnDateSetListener(mDatePickerOnDateSetListener); //reset listener
        }
        if (timePickerDialog != null) {
            //dialog is created
            timePickerDialog.setOnTimeSetListener(mTimePickerOnTimeSetListener); //reset listener
        }
    }

    /**
     * @return returns reminders' date as String in SQLite format if set else Constants.NO_REMINDER
     */
    public String getReminder() {
        if (mIsReminderSet) {
            //there is set reminder, return its date
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(mYear, mMonthOfYear, mDayOfMonth, mHour, mMinute);
            return DateUtils.parseDateInSQLiteFormat(calendar.getTime()); //return reminder date as string in SQLite format
        } else {
            //there is no reminder set, return null
            return Constants.NO_REMINDER;
        }
    }

    public void showReminder() {
        mIsReminderSet = true;
        mSetReminderButton.setVisibility(View.GONE);
        mReminderLayout.setVisibility(View.VISIBLE);
    }

    public void hideReminder() {
        mIsReminderSet = false;
        mReminderLayout.setVisibility(View.GONE);
        mSetReminderButton.setVisibility(View.VISIBLE);
    }

    private void setup() {
        mSetReminderButton.setOnClickListener(new SetReminderButtonOnClickListener(this));
        mDateButton.setOnClickListener(new DateButtonOnClickListener(this));
        mTimeButton.setOnClickListener(new TimeButtonOnClickListener(this));
        mRemoveReminderButton.setOnClickListener(new RemoveReminderOnClickListener(this));

        //makes button's text underlined
        PaintFlagsHelper.setUnderline(mTimeButton);
        PaintFlagsHelper.setUnderline(mDateButton);

        //sets buttons' text
        mTimeButton.setText(DateUtils.formatTime(getContext(), mHour, mMinute));
        mDateButton.setText(DateUtils.formatDate(mYear, mMonthOfYear, mDayOfMonth));

        if (mIsReminderSet) {
            //reminder is set, show it
            showReminder();
        } else {
            //reminder is not set, hide it
            hideReminder();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.EXTRA_IS_REMINDER_SET, mIsReminderSet);
        outState.putInt(Constants.EXTRA_YEAR, mYear);
        outState.putInt(Constants.EXTRA_MONTH, mMonthOfYear);
        outState.putInt(Constants.EXTRA_DAY, mDayOfMonth);
        outState.putInt(Constants.EXTRA_HOUR, mHour);
        outState.putInt(Constants.EXTRA_MINUTE, mMinute);
    }
}
