package com.gcode.notes.controllers;


import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.async.AddItemFromDbToMainTask;
import com.gcode.notes.tasks.async.LoadContentTask;
import com.gcode.notes.tasks.async.RemoveItemFromMainTask;

public class ImportantController extends BaseController {
    public ImportantController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean notForFirstTime) {
        super.setContent(notForFirstTime);
        mToolbar.setTitle("Starred");
        new LoadContentTask(notForFirstTime).execute();
        mSimpleItemTouchHelperCallback.setLongPressDragEnabled(true);
    }

    @Override
    protected void onSetContentAnimation() {
        super.onSetContentAnimation();
        if (mPreviousControllerId == Constants.CONTROLLER_BIN) {
            mFabMenu.showMenuButton(true);
        }
    }

    @Override
    public void onItemAdded(int mode) {
        if (mode == Constants.MODE_IMPORTANT) {
            new AddItemFromDbToMainTask().execute();
        }
    }

    @Override
    public void onItemModeChanged(ContentBase item) {
        if (item.getMode() != Constants.MODE_IMPORTANT) {
            new RemoveItemFromMainTask("Note moved to All notes.").execute(item);
        }
    }

    @Override
    public void onItemChanged(ContentBase item) {
        int mode = item.getMode();
        if (mode == Constants.MODE_IMPORTANT) {
            updateItem(item);
        }
    }
}
