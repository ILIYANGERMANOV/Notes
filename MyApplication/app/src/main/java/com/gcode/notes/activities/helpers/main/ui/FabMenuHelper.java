package com.gcode.notes.activities.helpers.main.ui;

import com.gcode.notes.activities.MainActivity;
import com.github.clans.fab.FloatingActionMenu;

public class FabMenuHelper {
    //TODO: REFACTOR AND OPTIMIZE

    public static void setupFabMenu(final MainActivity mainActivity) {
        final FloatingActionMenu fabMenu = mainActivity.getFabMenu(); //reference for easier access
        fabMenu.setClosedOnTouchOutside(true);

        if (mainActivity.mSubMenuOpened) {
            fabMenu.open(false);
        }

        fabMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                mainActivity.mSubMenuOpened = opened;
            }
        });
    }
}
