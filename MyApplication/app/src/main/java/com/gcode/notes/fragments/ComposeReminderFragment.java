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
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.utils.AlarmUtils;
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
import java.util.Date;

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

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create picker listeners here in order to prevent memory leak, onCreate() is called once
        mDatePickerOnDateSetListener = new DatePickerOnDateSetListener(this);
        mTimePickerOnTimeSetListener = new TimePickerOnTimeSetListener(this);

        if (savedInstanceState == null) {
            //set default values, mIsReminderSet to false and year,month and etc to current datetime
            mIsReminderSet = false;
            Calendar calendar = Calendar.getInstance();
            try {
                //try to get the current datetime from calendar
                mYear = calendar.get(Calendar.YEAR);
                mMonthOfYear = calendar.get(Calendar.MONTH);
                mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);
            } catch (Exception e) {
                //exception while getting the current datetime, try to get them through Date
                Date date = new Date();
                mYear = date.getYear();
                mMonthOfYear = date.getMonth();
                mDayOfMonth = date.getDay();
                mHour = date.getHours();
                mMinute = date.getMinutes();
            }
        } else {
            //use values from previous state (fragment after screen rotation)
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
        setup(); //setups button listeners, style and texts; show/hide reminder layout
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        DatePickerDialog datePickerDialog =
                (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag(Tags.DATE_PICKER_DIALOG_TAG);
        TimePickerDialog timePickerDialog =
                (TimePickerDialog) getActivity().getFragmentManager().findFragmentByTag(Tags.TIME_PICKER_DIALOG_TAG);

        //MaterialPickerDialogs listeners (callbacks) are lost after pause() so reset them if dialogs are created
        if (datePickerDialog != null) {
            //dialog is probably showing, reset its listener
            datePickerDialog.setOnDateSetListener(mDatePickerOnDateSetListener); //reset listener
        }
        if (timePickerDialog != null) {
            //dialog is probably showing, reset its listener
            timePickerDialog.setOnTimeSetListener(mTimePickerOnTimeSetListener); //reset listener
        }
    }

    /**
     * @return returns reminders' date as String in SQLite format if set else null
     */
    public String getReminder() {
        if (mIsReminderSet) {
            //there is set reminder, return its date in SQLite format
            //!NOTE: reminder should be always in SQLite format cuz its directly inserted in db without reformatting
            return DateUtils.formatDateInSQLiteFormat(getCalendarForSelectedDateTime().getTime()); //return reminder date as string in SQLite format
        } else {
            //there is no reminder set, return null
            return null;
        }
    }

    public void setAlarmForReminder(ContentBase contentBase) {
        long when = getCalendarForSelectedDateTime().getTimeInMillis(); // notification time
        AlarmUtils.setAlarm(getContext(), contentBase, when);
    }

    public void updateButtonsText() {
        //sets buttons' text
        mTimeButton.setText(DateUtils.formatTime(getContext(), mHour, mMinute));
        mDateButton.setText(DateUtils.formatDate(mYear, mMonthOfYear, mDayOfMonth));
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

    private Calendar getCalendarForSelectedDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        //set seconds to 0 in order to start the alarm exactly at selected minute
        calendar.set(mYear, mMonthOfYear, mDayOfMonth, mHour, mMinute, 0);
        return calendar;
    }

    private void setup() {
        mSetReminderButton.setOnClickListener(new SetReminderButtonOnClickListener(this));
        mDateButton.setOnClickListener(new DateButtonOnClickListener(this));
        mTimeButton.setOnClickListener(new TimeButtonOnClickListener(this));
        mRemoveReminderButton.setOnClickListener(new RemoveReminderOnClickListener(this));

        //makes button's text underlined
        PaintFlagsHelper.setUnderline(mTimeButton);
        PaintFlagsHelper.setUnderline(mDateButton);

        updateButtonsText();

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