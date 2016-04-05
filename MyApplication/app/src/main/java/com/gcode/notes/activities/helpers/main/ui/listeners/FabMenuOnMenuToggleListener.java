package com.gcode.notes.activities.helpers.main.ui.listeners;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.extras.values.Constants;
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
        if (opened) {
            //menu is opened, lower views alpha
            mMainActivity.setContentAlpha(Constants.VIEWS_FADED_ALPHA, false);
        } else {
            //menu is closed, enable listeners and regain views alpha
            mMainActivity.mMainAdapter.setListenersDisabled(false);
            mMainActivity.setContentAlpha(1f, false);
        }
    }
}
