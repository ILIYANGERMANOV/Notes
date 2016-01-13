package com.gcode.notes.controllers.other;


import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.tasks.async.main.LoadContentTask;

public class VisibleController extends BaseController {
    protected VisibleController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean scrollToTop) {
        super.setContent(scrollToTop);
        new LoadContentTask(scrollToTop).execute();
        mSimpleItemTouchHelperCallback.setLongPressDragEnabled(true);
    }

    @Override
    protected void onSetContentAnimation() {
        super.onSetContentAnimation();
        if (mFabMenu.isMenuButtonHidden()) {
            mFabMenu.showMenuButton(true);
        }
    }
}
