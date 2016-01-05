package com.gcode.notes.activities.helpers.main.ui.listeners;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.gcode.notes.activities.helpers.main.ui.FabMenuHelper;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.github.clans.fab.FloatingActionMenu;

public class FabMenuOnTouchListener implements View.OnTouchListener {
    FloatingActionMenu mFabMenu;
    //TODO: REFACTOR AND OPTIMIZE

    public FabMenuOnTouchListener(FloatingActionMenu fabMenu) {
        mFabMenu = fabMenu;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //MyDebugger.log("touch");
        //fabMenu#isOpened() is added too, for extra secure if handler doesn't wait enough
        if (FabMenuHelper.isOpenStarted || mFabMenu.isOpened()) {
            //MyDebugger.log("fab menu consumed the event");
            //fab menu is opened, close it
            FabMenuHelper.isOpenStarted = false;

            //postDelayed, cuz must wait opening animation so fabMenu#close() can work
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFabMenu.close(true); //closing fab menu with animation
                }
            }, Constants.DELAY_SO_USER_CAN_SEE);
            return true; //touch event is consumed return true
        }
        return false; //touch event isn't consumed return false
    }
}
