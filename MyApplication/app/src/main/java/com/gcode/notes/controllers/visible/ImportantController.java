package com.gcode.notes.controllers.visible;


import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.async.main.AddItemFromDbToMainTask;
import com.gcode.notes.tasks.async.main.RemoveItemFromMainTask;

public class ImportantController extends VisibleController {
    public ImportantController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean scrollToTop) {
        super.setContent(scrollToTop);
        mToolbar.setTitle(mMainActivity.getString(R.string.starred_label));
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
            new RemoveItemFromMainTask(mMainActivity.getString(R.string.note_moved_to_all_notes)).execute(item);
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
