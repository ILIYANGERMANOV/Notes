package com.gcode.notes.controllers;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.animations.MyAnimator;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.LoadContentTask;

public class ImportantController extends BaseController {
    public ImportantController(Context context, Toolbar toolbar, RecyclerView recyclerView,
                               FloatingActionButton fab, AppBarLayout appBarLayout) {

        super(context, toolbar, recyclerView, fab, appBarLayout);
    }

    @Override
    public void setContent() {
        super.setContent();
        mToolbar.setTitle("Important");
        new LoadContentTask(this).execute(getControllerId());
    }

    @Override
    protected void onSetContentAnimation() {
        super.onSetContentAnimation();
        if(mPreviousControllerId == Constants.CONTROLLER_BIN) {
            MyAnimator.startAnimation(mContext, mFab, R.anim.expand_anim);
            mFab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemAdded(int mode) {
        if (mode == Constants.MODE_IMPORTANT) {
            addItem(MyApplication.getWritableDatabase().getLastImportantNote());
        }
    }
}
