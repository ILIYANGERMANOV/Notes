package com.gcode.notes.activities.helpers.main.ui.listeners;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.ui.FabMenuHelper;
import com.gcode.notes.extras.values.Constants;
import com.github.clans.fab.FloatingActionMenu;

public class FabMenuOnTouchListener implements View.OnTouchListener {
    MainActivity mMainActivity;
    FloatingActionMenu mFabMenu;
    //TODO: REFACTOR AND OPTIMIZE

    public FabMenuOnTouchListener(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mFabMenu = mainActivity.getFabMenu();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //fabMenu#isOpened() is added too, for extra secure if handler doesn't wait enough
        if (FabMenuHelper.isOpenStarted || (mFabMenu.isOpened() && !FabMenuHelper.isOpenedConsumed)) {
            //fab menu is opened, close it

            //!NOTE: flags must be reset here not in conditions, otherwise menu will be closed more than one
            FabMenuHelper.setTouchListenerFlagsDown(); //flags used to prevent screen flashing and open note when spamming

            if (mFabMenu.isOpened()) {
                //menu is already opened, you can close it safely w/o adding delay
                mFabMenu.close(true);
                mMainActivity.setContentAlpha(1f, true);
            } else {
                //menu is opening atm, close after the open anim is over
                //postDelayed, cuz must wait opening animation so fabMenu#close() can work
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFabMenu.close(true); //closing fab menu with animation
                        mMainActivity.setContentAlpha(1f, true);
                    }
                }, Constants.DELAY_SO_USER_CAN_SEE);
            }
            return true; //touch event is consumed return true
        }
        return false; //touch event isn't consumed return false
    }
}
