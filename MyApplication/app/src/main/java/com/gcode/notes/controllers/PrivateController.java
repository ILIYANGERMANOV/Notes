package com.gcode.notes.controllers;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.helper.SimpleItemTouchHelperCallback;
import com.gcode.notes.tasks.async.AddItemFromDbToMainTask;
import com.gcode.notes.tasks.async.LoadContentTask;
import com.github.clans.fab.FloatingActionMenu;

public class PrivateController extends BaseController {

    public PrivateController(Context context, Toolbar toolbar, RecyclerView recyclerView,
                             FloatingActionMenu fabMenu, AppBarLayout appBarLayout,
                             SimpleItemTouchHelperCallback simpleItemTouchHelperCallback) {

        super(context, toolbar, recyclerView, fabMenu, appBarLayout, simpleItemTouchHelperCallback);
    }

    @Override
    public void setContent(boolean notForFirstTime) {
        super.setContent(notForFirstTime);
        mToolbar.setTitle("Private");
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
