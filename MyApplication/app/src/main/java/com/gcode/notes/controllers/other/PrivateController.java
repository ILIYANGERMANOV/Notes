package com.gcode.notes.controllers.other;


import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.async.main.AddItemFromDbToMainTask;

public class PrivateController extends VisibleController {

    public PrivateController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean scrollToTop) {
        super.setContent(scrollToTop);
        mToolbar.setTitle(mContext.getString(R.string.private_label));
    }

    @Override
    public void onItemModeChanged(ContentBase item) {
        //TODO: implement method
    }

    @Override
    public void onItemChanged(ContentBase item) {
        int mode = item.getMode();
        if (mode == Constants.MODE_PRIVATE) {
            updateItem(item);
        }
    }

    @Override
    public void onItemAdded(int mode) {
        if (mode == Constants.MODE_PRIVATE) {
            new AddItemFromDbToMainTask().execute();
        }
    }
}
