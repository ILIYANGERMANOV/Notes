package com.gcode.notes.controllers;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class ImportantController extends BaseController {
    public ImportantController(Toolbar toolbar, RecyclerView recyclerView) {
        super(toolbar, recyclerView);
    }

    @Override
    public void setContent() {
        mToolbar.setTitle("Important");
    }
}
