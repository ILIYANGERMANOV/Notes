package com.gcode.notes.activities.helpers.main.ui.listeners;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.ui.FabMenuHelper;
import com.github.clans.fab.FloatingActionMenu;

public class FabMenuOnMenuToggleListener implements FloatingActionMenu.OnMenuToggleListener {
    MainActivity mMainActivity;
    //TODO: CRUCIAL optimize listeners enabled is called many times

    public FabMenuOnMenuToggleListener(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public void onMenuToggle(boolean opened) {
        mMainActivity.mIsFabMenuOpened = opened; //toggle whether fab menu is opened
        if (!opened) {
            //menu is closed, enable listeners
            FabMenuHelper.setRecyclerViewListenersDisabled(mMainActivity.getRecyclerView(), false);
        }
    }
}
