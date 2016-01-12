package com.gcode.notes.controllers;


import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.async.main.AddItemFromDbToMainTask;
import com.gcode.notes.tasks.async.main.LoadContentTask;

public class BinController extends BaseController {

    public BinController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean notForFirstTime) {
        super.setContent(notForFirstTime);
        mToolbar.setTitle("Bin");
        new LoadContentTask(notForFirstTime).execute();
        mSimpleItemTouchHelperCallback.setLongPressDragEnabled(false);

    }

    @Override
    protected void onSetContentAnimation() {
        super.onSetContentAnimation();
        if (mPreviousControllerId != Constants.CONTROLLER_BIN) {
            mFabMenu.hideMenuButton(true);
        } else {
            mFabMenu.hideMenuButton(false);
        }
    }

    @Override
    public void onItemAdded(int mode) {
        if (mode == Constants.MODE_DELETED_NORMAL || mode == Constants.MODE_DELETED_IMPORTANT) {
            new AddItemFromDbToMainTask().execute();
        }
    }
}
