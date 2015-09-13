package com.gcode.notes.controllers;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.adapters.NotesAdapter;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.notes.MyApplication;

public class AllNotesController extends BaseController {

    public AllNotesController(Toolbar toolbar, RecyclerView recyclerView) {
        super(toolbar, recyclerView);
    }

    @Override
    public void setContent() {
        setNewContent(MyApplication.getWritableDatabase().getAllVisibleNotes());
        mToolbar.setTitle("All Notes");
    }

    @Override
    public void update(int mode) {
        if (mode == Constants.MODE_NORMAL || mode == Constants.MODE_IMPORTANT) {
            applyUpdate(MyApplication.getWritableDatabase().getLastVisibleNote());
        }
    }
}
