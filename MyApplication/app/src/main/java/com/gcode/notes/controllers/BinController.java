package com.gcode.notes.controllers;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.animations.MyAnimator;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.AddItemFromDbToMainTask;
import com.gcode.notes.tasks.LoadContentTask;

public class BinController extends BaseController {

    public BinController(Context context, Toolbar toolbar, RecyclerView recyclerView,
                         FloatingActionButton fab, AppBarLayout appBarLayout) {

        super(context, toolbar, recyclerView, fab, appBarLayout);
    }

    @Override
    public void setContent() {
        super.setContent();
        mToolbar.setTitle("Bin");
        new LoadContentTask(this).execute();
    }

    @Override
    protected void onSetContentAnimation() {
        super.onSetContentAnimation();
        mFab.setVisibility(View.GONE);
        if (mPreviousControllerId != Constants.CONTROLLER_BIN) {
            MyAnimator.startAnimation(mContext, mFab, R.anim.collapse_anim);
        }
    }

    @Override
    public void onItemAdded(int mode) {
        if (mode == Constants.MODE_DELETED_NORMAL || mode == Constants.MODE_DELETED_IMPORTANT) {
            new AddItemFromDbToMainTask().execute();
        }
    }
}
