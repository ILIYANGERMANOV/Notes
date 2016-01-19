package com.gcode.notes.activities.helpers.main.actions;


import android.content.Intent;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.extra.ExploreActivity;
import com.gcode.notes.activities.extra.SettingsActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.controllers.bin.BinController;
import com.gcode.notes.controllers.visible.AllNotesController;
import com.gcode.notes.controllers.visible.ImportantController;
import com.gcode.notes.controllers.visible.PrivateController;

public class DrawerOptionExecutor {
    MainActivity mMainActivity;

    public DrawerOptionExecutor(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void applySelectedOption(int selectedId, boolean preventExecutionIfSameId) {
        if (mMainActivity.mSelectedId == selectedId && preventExecutionIfSameId) return;

        //!NOTE: this is executing only if the selected id is not already selected or
        //if applySelectedOption() is called from OnCreate(), in this case it should be executed
        //because MainActivity#mSelectedId is set to all_notes_id but the selected option isn't actually applied
        switch (selectedId) {
            case R.id.navigation_item_all_notes:
                mMainActivity.mSelectedId = selectedId;
                AllNotesController allNotesController = new AllNotesController(mMainActivity);

                BaseController.setInstance(allNotesController);
                break;
            case R.id.navigation_item_starred:
                mMainActivity.mSelectedId = selectedId;
                ImportantController importantController = new ImportantController(mMainActivity);

                BaseController.setInstance(importantController);
                break;
            case R.id.navigation_item_private:
                mMainActivity.mSelectedId = selectedId;
                PrivateController privateController = new PrivateController(mMainActivity);

                BaseController.setInstance(privateController);
                break;
            case R.id.navigation_item_bin:
                mMainActivity.mSelectedId = selectedId;
                BinController binController = new BinController(mMainActivity);

                BaseController.setInstance(binController);
                break;
            case R.id.navigation_item_explore:
                mMainActivity.startActivity(new Intent(mMainActivity, ExploreActivity.class));
                return;
            case R.id.navigation_item_settings:
                mMainActivity.startActivity(new Intent(mMainActivity, SettingsActivity.class));
                return;
            default:
                return;
        }

        //!NOTE: preventExecutionIfSameId flag matches with scrollToTop in setContent(), cuz:
        //1.when method is called from onCreate() the flag is FALSE: the activity is fresh created or coming from
        //screen rotation in both cases content shouldn't be scrolled to top
        //2. when method is called from nav drawer on click the flag is TRUE: new controller should be chosen and
        //then new content should be scrolled (!if the current id is reselected code execution will be stopped at
        //the start on applySelectedOption()
        BaseController.getInstance().setContent(preventExecutionIfSameId);

        if (mMainActivity.mMenu != null) {
            //mMenu is already created, prepare its new options according controller
            mMainActivity.onPrepareOptionsMenu(mMainActivity.mMenu);
        }
    }
}
