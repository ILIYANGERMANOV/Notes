package com.gcode.notes.ui.helpers;


import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.extras.utils.MyUtils;
import com.gcode.notes.extras.values.Keys;

public class NavDrawerHelper {
    public static boolean isDrawerOpen(DrawerLayout drawerLayout) {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public static boolean didUserLearnedDrawer() {
        return MyUtils.readFromPreferences(Keys.PREF_USER_LEARNED_DRAWER, "false").equals("true");
    }

    public static void markDrawerSeen() {
        MyUtils.saveToPreferences(Keys.PREF_USER_LEARNED_DRAWER, "true");
    }

    public static void selectLabel(MainActivity mainActivity, MenuItem menuItem) {
        if (menuItem.getGroupId() == R.id.navigation_group_1) {
            menuItem.setChecked(true);
            closeDrawer(mainActivity.getDrawerLayout());
        }
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
