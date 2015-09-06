package com.gcode.notes.controllers;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class AllNotesController extends BaseController {

    public AllNotesController(Toolbar toolbar, RecyclerView recyclerView) {
        super(toolbar, recyclerView);
    }

    @Override
    public void setContent() {
        mToolbar.setTitle("All Notes");
    }
}
