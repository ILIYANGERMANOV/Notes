package com.gcode.notes.activities.helpers.main.ui.fab.listeners;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.motions.MyAnimator;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

public class FabMenuStateChangeListener implements FloatingActionMenu.MenuStateChangeListener {
    MainActivity mMainActivity;

    public FabMenuStateChangeListener(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
        mMainActivity.getFab().setImageResource(R.drawable.ic_close_white_24dp);
        MyAnimator.startAnimationOnView(mMainActivity, mMainActivity.getFab(), R.anim.open_rotate_anim);
        mMainActivity.mSubMenuOpened = true;
    }

    @Override
    public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
        mMainActivity.getFab().setImageResource(R.drawable.ic_open_white_24dp);
        MyAnimator.startAnimationOnView(mMainActivity, mMainActivity.getFab(), R.anim.close_rotate_anim);
        mMainActivity.mSubMenuOpened = false;
    }
}
