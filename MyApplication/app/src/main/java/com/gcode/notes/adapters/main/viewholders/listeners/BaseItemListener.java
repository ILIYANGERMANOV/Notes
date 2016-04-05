package com.gcode.notes.adapters.main.viewholders.listeners;

import android.app.Activity;
import android.view.View;

public abstract class BaseItemListener implements View.OnClickListener {
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
