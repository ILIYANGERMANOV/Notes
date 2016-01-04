package com.gcode.notes.activities.helpers.main.actions;

import android.support.v4.widget.DrawerLayout;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.ui.helpers.NavDrawerHelper;

public class MainActivityBackPressHelper {
    /**
     * Handles onBackPressed() when FAB sub menu or navigation drawer is opened.
     * And return whether any of this events is handled.
     *
     * @return whether the activity should be minimized
     */
    public static boolean handleBackPressedWhenDrawerOrFABMenuOpened(MainActivity mainActivity) {
        DrawerLayout drawerLayout = mainActivity.getDrawerLayout(); //get reference for easier access
        if (NavDrawerHelper.isDrawerOpen(drawerLayout) ||
                (MainActivity.mActionMenu != null && MainActivity.mActionMenu.isOpen())) {
            //FAB menu or nav drawer is opened, check which one and close it (both can't be opened at the same time)

            //check with nav drawer, cuz it has higher priority (elevation)
            if (NavDrawerHelper.isDrawerOpen(drawerLayout)) {
                //Navigation drawer is opened, close it
                NavDrawerHelper.closeDrawer(drawerLayout);
            } else {
                //FAB menu is opened, close it
                MainActivity.mActionMenu.close(true);
            }
            return false; //onBackPressed() was handled by closing FAB menu or nav drawer
        }
        return true; //any event wasn't handled, activity should be minimized
    }
}
