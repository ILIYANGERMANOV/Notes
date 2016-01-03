package com.gcode.notes.activities.helpers.main;

import android.os.Bundle;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.extras.values.Keys;
import com.gcode.notes.tasks.async.DeleteExpiredNotesTask;

public class MainActivityRotationHandler {
    public static void saveInstanceState(MainActivity mainActivity, Bundle outState) {
        outState.putInt(Keys.STATE_SELECTED_POSITION, mainActivity.mSelectedId); //saves selected item id in nav drawer
        outState.putBoolean(Keys.STATE_SUB_MENU_OPENED, mainActivity.mSubMenuOpened); //saves whether FAB menu is opened
    }

    /**
     * Sets mSelectedId, mSubMenuOpened and activity's intent properly.
     * Start DeleteExpiredNotesTask if app is ran for first time.
     * !NOTE: MUST BE CALLED before setup()!
     *
     * @param mainActivity       MainActivity
     * @param savedInstanceState Saved instance state bundle
     */
    public static void handleScreenRotation(MainActivity mainActivity, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mainActivity.setIntent(null); //start from notification has been consumed, set intent to null, so getIntent() will be null
            //and it won't start display activity again

            mainActivity.mSelectedId = savedInstanceState.getInt(Keys.STATE_SELECTED_POSITION); //handle previous selected item id in nav drawer
            mainActivity.mSubMenuOpened = savedInstanceState.getBoolean(Keys.STATE_SUB_MENU_OPENED); //handle whether FAB menu was opened
        } else {
            //app is ran for first time delete expired notes
            new DeleteExpiredNotesTask().execute();
        }
    }
}
