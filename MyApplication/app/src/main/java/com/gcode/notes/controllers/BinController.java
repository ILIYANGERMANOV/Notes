package com.gcode.notes.controllers;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.helper.SimpleItemTouchHelperCallback;
import com.gcode.notes.tasks.async.AddItemFromDbToMainTask;
import com.gcode.notes.tasks.async.LoadContentTask;
import com.github.clans.fab.FloatingActionMenu;

public class BinController extends BaseController {

    public BinController(Context context, Toolbar toolbar, RecyclerView recyclerView,
                         FloatingActionMenu fabMenu, AppBarLayout appBarLayout,
                         SimpleItemTouchHelperCallback simpleItemTouchHelperCallback) {

        super(context, toolbar, recyclerView, fabMenu, appBarLayout, simpleItemTouchHelperCallback);
    }

    @Override
    public void setContent(boolean notForFirstTime) {
        super.setContent(notForFirstTime);
        mToolbar.setTitle("Bin");
        new LoadContentTask(notForFirstTime).execute();
        mSimpleItemTouchHelperCallback.setLongPressDragEnabled(false);

    }

    @Override
    protected void onSetContentAnimation() {
        super.onSetContentAnimation();
        if (mPreviousControllerId != Constants.CONTROLLER_BIN) {
            mFabMenu.hideMenuButton(true);
        } else {
            mFabMenu.hideMenuButton(false);
        }
    }

    @Override
    public void onItemAdded(int mode) {
        if (mode == Constants.MODE_DELETED_NORMAL || mode == Constants.MODE_DELETED_IMPORTANT) {
            new AddItemFromDbToMainTask().execute();
        }
    }
}
