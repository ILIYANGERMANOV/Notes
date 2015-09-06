package com.gcode.notes.controllers;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public abstract class BaseController {
    Toolbar mToolbar;
    RecyclerView mRecyclerView;

    BaseController(Toolbar toolbar, RecyclerView recyclerView) {
        mToolbar = toolbar;
        mRecyclerView = recyclerView;
    }

    public abstract void setContent();
}
