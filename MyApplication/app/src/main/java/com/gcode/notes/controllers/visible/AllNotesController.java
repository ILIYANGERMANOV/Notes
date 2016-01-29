package com.gcode.notes.controllers.visible;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.extras.values.Constants;

public class AllNotesController extends VisibleController {

    public AllNotesController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean scrollToTop) {
        super.setContent(scrollToTop);
        mToolbar.setTitle(mMainActivity.getString(R.string.all_notes_label));
    }

    @Override
    public boolean shouldHandleMode(int mode) {
        return mode == Constants.MODE_NORMAL || mode == Constants.MODE_IMPORTANT;
    }
}
