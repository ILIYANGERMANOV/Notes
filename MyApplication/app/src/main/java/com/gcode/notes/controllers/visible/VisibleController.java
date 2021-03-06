package com.gcode.notes.controllers.visible;


import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.tasks.async.main.LoadContentTask;

public abstract class VisibleController extends BaseController {
    protected VisibleController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean scrollToTop, boolean loadNewContent) {
        super.setContent(scrollToTop, loadNewContent); //calls onSetContentAnimation
        if (loadNewContent) {
            new LoadContentTask(scrollToTop).execute();
        }
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
