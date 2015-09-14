package com.gcode.notes.controllers;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.animations.MyAnimator;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.notes.MyApplication;

public class BinController extends BaseController {
    FloatingActionButton mFab;
    Context mContext;

    boolean animate;

    public BinController(Context context, Toolbar toolbar, RecyclerView recyclerView, FloatingActionButton fab,
                         boolean animate) {

        super(toolbar, recyclerView);
        mContext = context;
        mFab = fab;
        this.animate = animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    public boolean hasAnimation() {
        return animate;
    }

    @Override
    public void setContent() {
        mToolbar.setTitle("Bin");
        mFab.clearAnimation();
        if (animate) {
            MyAnimator.startAnimation(mContext, mFab, R.anim.collapse_anim);
        }
        mFab.setVisibility(View.INVISIBLE);
        setNewContent(MyApplication.getWritableDatabase().getAllDeletedNotes());
    }

    @Override
    public void update(int mode) {
        if (mode == Constants.MODE_DELETED_NORMAL || mode == Constants.MODE_DELETED_IMPORTANT) {
            applyUpdate(MyApplication.getWritableDatabase().getLastDeletedNote());
        }
    }
}
