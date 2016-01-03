package com.gcode.notes.activities.helpers.main.ui;

import com.gcode.notes.activities.MainActivity;

public class MainToolbarHelper {
    MainActivity mMainActivity;

    public MainToolbarHelper(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void setupToolbar() {
        if (mMainActivity.getToolbar() != null) {
            mMainActivity.setSupportActionBar(mMainActivity.getToolbar());
        }
    }
}
