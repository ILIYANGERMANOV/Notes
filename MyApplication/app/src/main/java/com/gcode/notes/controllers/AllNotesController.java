package com.gcode.notes.controllers;


import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.async.main.AddItemFromDbToMainTask;
import com.gcode.notes.tasks.async.main.LoadContentTask;


public class AllNotesController extends BaseController {

    public AllNotesController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean notForFirstTime) {
        super.setContent(notForFirstTime);
        mToolbar.setTitle("All Notes");
        new LoadContentTask(notForFirstTime).execute();
        mSimpleItemTouchHelperCallback.setLongPressDragEnabled(true);
    }

    @Override
    protected void onSetContentAnimation() {
        super.onSetContentAnimation();
        switch (mPreviousControllerId) {
            case Constants.ERROR:
            case Constants.CONTROLLER_BIN:
                mFabMenu.showMenuButton(true);
                break;
        }
    }

    @Override
    public void onItemAdded(int mode) {
        if (mode == Constants.MODE_NORMAL || mode == Constants.MODE_IMPORTANT) {
            new AddItemFromDbToMainTask().execute();
        }
    }

    @Override
    public void onItemModeChanged(ContentBase item) {
        int mode = item.getMode();
        if (mode == Constants.MODE_NORMAL || mode == Constants.MODE_IMPORTANT) {
            updateItemMode(item);
        }
    }

    @Override
    public void onItemChanged(ContentBase item) {
        int mode = item.getMode();
        if (mode == Constants.MODE_NORMAL || mode == Constants.MODE_IMPORTANT) {
            updateItem(item);
        }
    }
}
