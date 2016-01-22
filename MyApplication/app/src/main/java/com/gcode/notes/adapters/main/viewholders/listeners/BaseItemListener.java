package com.gcode.notes.adapters.main.viewholders.listeners;

import android.app.Activity;

public class BaseItemListener {
    Activity mActivity;
    boolean mDisabled;

    public BaseItemListener(Activity activity) {
        mActivity = activity;
        mDisabled = false;
    }

    public void setDisabled(boolean disabled) {
        mDisabled = disabled;
    }
}
