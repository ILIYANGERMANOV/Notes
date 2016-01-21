package com.gcode.notes.activities.helpers.main.actions;


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.extra.ExploreActivity;
import com.gcode.notes.activities.extra.SettingsActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.controllers.bin.BinController;
import com.gcode.notes.controllers.visible.AllNotesController;
import com.gcode.notes.controllers.visible.ImportantController;
import com.gcode.notes.controllers.visible.PrivateController;
import com.gcode.notes.ui.helpers.NavDrawerHelper;

public class DrawerOptionExecutor implements NavigationView.OnNavigationItemSelectedListener {
    MainActivity mMainActivity;

    public DrawerOptionExecutor(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    /**
     * @param selectedId               the nav drawer id to switch on
     * @param preventExecutionIfSameId must be false when called onCreate() else true
     */
    public void applySelectedOption(int selectedId, boolean preventExecutionIfSameId) {
        if (mMainActivity.mSelectedId == selectedId && preventExecutionIfSameId) return;


        if(preventExecutionIfSameId || selectedId == R.id.navigation_item_bin) {
            //private controller is selected or we are bin (both selected or screen rotation),
            //save previous selected id cuz bin fab animation will bug
            mMainActivity.mPreviousSelectedId = mMainActivity.mSelectedId;
        }

        //!NOTE: this is executing only if the selected id is not already selected or
        //if applySelectedOption() is called from OnCreate(), in this case it should be executed
        //because MainActivity#mSelectedId is set to all_notes_id but the selected option isn't actually applied
        switch (selectedId) {
            case R.id.navigation_item_all_notes:
                mMainActivity.mSelectedId = selectedId;
                BaseController.setInstance(new AllNotesController(mMainActivity));
                break;
            case R.id.navigation_item_starred:
                mMainActivity.mSelectedId = selectedId;
                BaseController.setInstance(new ImportantController(mMainActivity));
                break;
            case R.id.navigation_item_private:
                mMainActivity.mSelectedId = selectedId;
                BaseController.setInstance(new PrivateController(mMainActivity));
                break;
            case R.id.navigation_item_bin:
                mMainActivity.mSelectedId = selectedId;
                BaseController.setInstance(new BinController(mMainActivity));
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

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.getGroupId() == R.id.navigation_group_1) {
            menuItem.setChecked(true);
            NavDrawerHelper.closeDrawer(mMainActivity.getDrawerLayout());
        }
        applySelectedOption(menuItem.getItemId(), true);
        return true;
    }
}
