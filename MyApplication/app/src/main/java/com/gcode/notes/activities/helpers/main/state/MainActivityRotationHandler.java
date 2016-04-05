package com.gcode.notes.activities.helpers.main.state;

import android.os.Bundle;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.controllers.visible.PrivateController;
import com.gcode.notes.extras.values.Keys;
import com.gcode.notes.tasks.async.delete.DeleteExpiredNotesTask;

public class MainActivityRotationHandler {
    //TODO: OPTIMIZE sometimes skipping frames on screen rotation (40max)
    public static void saveInstanceState(MainActivity mainActivity, Bundle outState) {
        //saves selected item id in nav drawer
        outState.putInt(Keys.EXTRA_SELECTED_ID, mainActivity.mSelectedId);
        //saves previous selected item id in nav drawer
        outState.putInt(Keys.EXTRA_PREVIOUS_SELECTED_ID, mainActivity.mPreviousSelectedId);
        //saves whether FAB menu is opened
        outState.putBoolean(Keys.EXTRA_FAB_MENU_OPENED, mainActivity.mIsFabMenuOpened);
    }

    /**
     * Sets mSelectedId, mIsFabMenuOpened and activity's intent properly.
     * Start DeleteExpiredNotesTask if app is ran for first time.
     * !NOTE: MUST BE CALLED before setup()!
     *
     * @param mainActivity       MainActivity
     * @param savedInstanceState Saved instance state bundle
     */
    public static void handleScreenRotation(MainActivity mainActivity, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //cannot be clicked
            BaseController controller = BaseController.getInstance();
            if (controller instanceof PrivateController) {
                //screen rotation in private, it should load new content
                mainActivity.mLoadNewContentPrivate = true;
            }

            mainActivity.setIntent(null); //start from notification has been consumed, set intent to null, so getIntent() will be null
            //and it won't start display activity again

            mainActivity.mSelectedId = savedInstanceState.getInt(Keys.EXTRA_SELECTED_ID); //handle previous selected item id in nav drawer
            mainActivity.mPreviousSelectedId = savedInstanceState.getInt(Keys.EXTRA_PREVIOUS_SELECTED_ID);
            mainActivity.mIsFabMenuOpened = savedInstanceState.getBoolean(Keys.EXTRA_FAB_MENU_OPENED); //handle whether FAB menu was opened
        } else {
            //app is ran for first time delete expired notes
            new DeleteExpiredNotesTask().execute();
        }
    }
}
