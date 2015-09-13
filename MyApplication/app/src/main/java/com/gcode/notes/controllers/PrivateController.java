package com.gcode.notes.controllers;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.extras.Constants;
import com.gcode.notes.notes.MyApplication;

public class PrivateController extends BaseController {

    public PrivateController(Toolbar toolbar, RecyclerView recyclerView) {
        super(toolbar, recyclerView);
    }

    @Override
    public void setContent() {
        mToolbar.setTitle("Private");
        setNewContent(MyApplication.getWritableDatabase().getAllPrivateNotes());
    }

    @Override
    public void update(int mode) {
        if (mode == Constants.MODE_PRIVATE) {
            applyUpdate(MyApplication.getWritableDatabase().getLastPrivateNote());
        }
    }
}
