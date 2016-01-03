package com.gcode.notes.activities.helpers.main.actions;


import android.content.Intent;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.extra.ExploreActivity;
import com.gcode.notes.activities.extra.SettingsActivity;
import com.gcode.notes.controllers.AllNotesController;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.controllers.BinController;
import com.gcode.notes.controllers.ImportantController;
import com.gcode.notes.controllers.PrivateController;

public class DrawerOptionExecutor {
    MainActivity mMainActivity;

    public DrawerOptionExecutor(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void applySelectedOption(int selectedId, boolean notForFirstTime) {
        if (mMainActivity.mSelectedId == selectedId && notForFirstTime) return;

        switch (selectedId) {
            case R.id.navigation_item_all_notes:
                mMainActivity.mSelectedId = selectedId;
                AllNotesController allNotesController = new AllNotesController(mMainActivity, mMainActivity.getToolbar(),
                        mMainActivity.getRecyclerView(), mMainActivity.getFab(),
                        mMainActivity.getAppBarLayout(), mMainActivity.mSimpleItemTouchHelperCallback);

                BaseController.setInstance(allNotesController);
                break;
            case R.id.navigation_item_important:
                mMainActivity.mSelectedId = selectedId;
                ImportantController importantController = new ImportantController(mMainActivity, mMainActivity.getToolbar(),
                        mMainActivity.getRecyclerView(), mMainActivity.getFab(),
                        mMainActivity.getAppBarLayout(), mMainActivity.mSimpleItemTouchHelperCallback);

                BaseController.setInstance(importantController);
                break;
            case R.id.navigation_item_private:
                mMainActivity.mSelectedId = selectedId;
                PrivateController privateController = new PrivateController(mMainActivity, mMainActivity.getToolbar(),
                        mMainActivity.getRecyclerView(), mMainActivity.getFab(),
                        mMainActivity.getAppBarLayout(), mMainActivity.mSimpleItemTouchHelperCallback);

                BaseController.setInstance(privateController);
                break;
            case R.id.navigation_item_bin:
                mMainActivity.mSelectedId = selectedId;
                BinController binController = new BinController(mMainActivity, mMainActivity.getToolbar(),
                        mMainActivity.getRecyclerView(), mMainActivity.getFab(),
                        mMainActivity.getAppBarLayout(), mMainActivity.mSimpleItemTouchHelperCallback);

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

        BaseController.getInstance().setContent(notForFirstTime); //changes main activity's recycler view, FAB according controller set
        if (mMainActivity.mMenu != null) {
            mMainActivity.onPrepareOptionsMenu(mMainActivity.mMenu);
        }
    }
}
