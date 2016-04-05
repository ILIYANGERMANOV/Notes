package com.gcode.notes.controllers.bin;


import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.async.main.LoadContentTask;

public class BinController extends BaseController {

    public BinController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean scrollToTop, boolean loadNewContent) {
        super.setContent(scrollToTop, loadNewContent);
        mToolbar.setTitle(mMainActivity.getString(R.string.bin_label));
        if (loadNewContent) {
            new LoadContentTask(scrollToTop).execute();
        }
        mSimpleItemTouchHelperCallback.setLongPressDragEnabled(false);

    }

    @Override
    protected void onSetContentAnimation() {
        super.onSetContentAnimation();
        if (mMainActivity.mPreviousSelectedId != R.id.navigation_item_bin) {
            //selected bin, button disappear should be animated
            mFabMenu.hideMenuButton(true);
        } else {
            //bin coming from screen rotation, menu button disappear should not be animated
            mFabMenu.hideMenuButton(false);
        }
    }

    @Override
    public boolean shouldHandleMode(int mode) {
        return mode == Constants.MODE_DELETED_NORMAL || mode == Constants.MODE_DELETED_IMPORTANT;
    }

    @Override
    public void setupEmptyView(TextView emptyView) {
        emptyView.setText(mMainActivity.getString(R.string.bin_empty_view_text));
        emptyView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bin_empty_drawable, 0, 0);
    }
}
