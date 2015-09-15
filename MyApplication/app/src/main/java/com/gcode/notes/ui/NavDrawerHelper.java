package com.gcode.notes.ui;


import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.gcode.notes.extras.Keys;
import com.gcode.notes.extras.Utils;

public class NavDrawerHelper {
    public static boolean didUserLearnedDrawer() {
        return Utils.readFromPreferences(Keys.PREF_USER_LEARNED_DRAWER, "false").equals("true");

    }

    public static void markDrawerSeen() {
        Utils.saveToPreferences(Keys.PREF_USER_LEARNED_DRAWER, "true");
    }

    public static void showDrawer(DrawerLayout mDrawerLayout) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public static void hideDrawer(DrawerLayout mDrawerLayout) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
}
