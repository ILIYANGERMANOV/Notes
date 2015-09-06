package com.gcode.notes.controllers;


import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class BinController extends BaseController {
    FloatingActionButton mFab;

    public BinController(Toolbar toolbar, RecyclerView recyclerView, FloatingActionButton fab) {
        super(toolbar, recyclerView);
        mFab = fab;
    }

    @Override
    public void setContent() {
        mToolbar.setTitle("Bin");
        mFab.setVisibility(View.INVISIBLE);
    }
}
