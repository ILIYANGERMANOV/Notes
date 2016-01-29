package com.gcode.notes.controllers.visible;


import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.extras.values.Constants;

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
    public boolean shouldHandleMode(int mode) {
        return mode == Constants.MODE_IMPORTANT;
    }
}
