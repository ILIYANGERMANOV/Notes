package com.gcode.notes.controllers;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.animations.MyAnimator;

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

    @Override
    public void setContent() {
        mToolbar.setTitle("Bin");
        mFab.clearAnimation();
        if(animate) {
            MyAnimator.startAnimation(mContext, mFab, R.anim.collapse_anim);
        }
        mFab.setVisibility(View.INVISIBLE);
    }
}
