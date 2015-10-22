package com.gcode.notes.controllers;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.animations.MyAnimator;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.tasks.AddItemFromDbToMainTask;
import com.gcode.notes.tasks.LoadContentTask;

public class AllNotesController extends BaseController {

    public AllNotesController(Context context, Toolbar toolbar, RecyclerView recyclerView,
                              FloatingActionButton fab, AppBarLayout appBarLayout) {

        super(context, toolbar, recyclerView, fab, appBarLayout);
    }

    @Override
    public void setContent() {
        super.setContent();
        mToolbar.setTitle("All Notes");
        new LoadContentTask(this).execute();
    }

    @Override
    protected void onSetContentAnimation() {
        super.onSetContentAnimation();
        switch (mPreviousControllerId) {
            case Constants.ERROR:
            case Constants.CONTROLLER_BIN:
                MyAnimator.startAnimation(mContext, mFab, R.anim.expand_anim);
                mFab.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onItemAdded(int mode) {
        if (mode == Constants.MODE_NORMAL || mode == Constants.MODE_IMPORTANT) {
            new AddItemFromDbToMainTask().execute();
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
