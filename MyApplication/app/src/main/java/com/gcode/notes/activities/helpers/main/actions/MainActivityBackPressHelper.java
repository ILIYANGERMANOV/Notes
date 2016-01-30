package com.gcode.notes.activities.helpers.main.actions;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.ui.helpers.NavDrawerHelper;
import com.gcode.notes.ui.helpers.SearchViewHelper;

public class MainActivityBackPressHelper {
    /**
     * Handles onBackPressed() when FAB menu, search view or navigation drawer is opened.
     * And return whether any of this events is handled.
     *
     * @return whether the activity should be minimized (call moveTaskToBack)
     */
    public static boolean shouldMoveTaskToBack(MainActivity mainActivity) {
        if (NavDrawerHelper.isDrawerOpen(mainActivity.getDrawerLayout())) {
            //drawer is opened, close and return false
            NavDrawerHelper.closeDrawer(mainActivity.getDrawerLayout());
            return false;
        } else if (mainActivity.getFabMenu().isOpened()) {
            //fab menu is opened, close it and return false
            mainActivity.getFabMenu().close(true); //closing should be animated
            return false;
        } else if (SearchViewHelper.isSearchViewOpened(mainActivity)) {
            //search view is opened, close it and return false
            SearchViewHelper.closeSearchView(mainActivity);
            return false;
        }
        return true; //any event wasn't handled, activity should be minimized
    }
}
