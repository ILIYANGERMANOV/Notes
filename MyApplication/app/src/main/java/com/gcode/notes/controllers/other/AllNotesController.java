package com.gcode.notes.controllers.other;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.async.main.AddItemFromDbToMainTask;

public class AllNotesController extends VisibleController {

    public AllNotesController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean scrollToTop) {
        super.setContent(scrollToTop);
        mToolbar.setTitle(mContext.getString(R.string.all_notes_label));
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
