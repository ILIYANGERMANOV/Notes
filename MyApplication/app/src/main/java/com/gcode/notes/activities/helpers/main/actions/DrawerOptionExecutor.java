package com.gcode.notes.activities.helpers.main.actions;


import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.controllers.bin.BinController;
import com.gcode.notes.controllers.visible.AllNotesController;
import com.gcode.notes.controllers.visible.ImportantController;
import com.gcode.notes.controllers.visible.PrivateController;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.ui.helpers.NavDrawerHelper;

public class DrawerOptionExecutor implements NavigationView.OnNavigationItemSelectedListener {
    MainActivity mMainActivity;

    public DrawerOptionExecutor(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    /**
     * @param selectedId               the nav drawer id to switch on
     * @param preventExecutionIfSameId must be false when called onCreate() else true
     * @param loadNewContent           flag indicating whether the controller should load new content
     */
    public void applySelectedOption(int selectedId, boolean preventExecutionIfSameId,
                                    boolean loadNewContent) {
        if (mMainActivity.mSelectedId == selectedId) {
            //current option is already selected, scroll to top and expand appbar
            mMainActivity.getAppBarLayout().setExpanded(true, true); //animate appbar expanding
            mMainActivity.getRecyclerView().smoothScrollToPosition(0); //scroll to first item
            if (preventExecutionIfSameId) return; //prevent further execution if flag is up
        }

        if (preventExecutionIfSameId || selectedId == R.id.navigation_item_bin) {
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
                //mMainActivity.startActivity(new Intent(mMainActivity, ExploreActivity.class));
                MyDebugger.toast(mMainActivity, "Explore not implemented, yet!");
                return;
            case R.id.navigation_item_settings:
                //mMainActivity.startActivity(new Intent(mMainActivity, SettingsActivity.class));
                MyDebugger.toast(mMainActivity, "Settings not implemented, yet!");
                return;
            default:
                return;
        }


        if (preventExecutionIfSameId) {
            //preventExecutionIfSameId is false only when called by onCreate() in MainActivity
            //so if its true user has switched labels and there wasn't screen rotation in private

            mMainActivity.mLoadNewContentPrivate = false; //the controller should load new content
            //only when there was a screen rotation in private label
        }

        //!NOTE: preventExecutionIfSameId flag matches with scrollToTop in setContent(), cuz:
        //1.when method is called from onCreate() the flag is FALSE: the activity is fresh created or coming from
        //screen rotation in both cases content shouldn't be scrolled to top
        //2. when method is called from nav drawer on click the flag is TRUE: new controller should be chosen and
        //then new content should be scrolled (!if the current id is reselected code execution will be stopped at
        //the start on applySelectedOption()
        BaseController.getInstance().setContent(preventExecutionIfSameId, loadNewContent);

        if (mMainActivity.mMenu != null) {
            //mMenu is already created, prepare its new options according controller
            mMainActivity.onPrepareOptionsMenu(mMainActivity.mMenu);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        NavDrawerHelper.selectLabel(mMainActivity, menuItem);
        applySelectedOption(menuItem.getItemId(), true, true);
        return true;
    }
}
