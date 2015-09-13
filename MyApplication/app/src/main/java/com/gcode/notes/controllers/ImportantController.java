package com.gcode.notes.controllers;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.adapters.NotesAdapter;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.notes.MyApplication;

public class ImportantController extends BaseController {
    public ImportantController(Toolbar toolbar, RecyclerView recyclerView) {
        super(toolbar, recyclerView);
    }

    @Override
    public void setContent() {
        mToolbar.setTitle("Important");
        setNewContent(MyApplication.getWritableDatabase().getAllImportantNotes());
    }

    @Override
    public void update(int mode) {
        if (mode == Constants.MODE_IMPORTANT) {
            applyUpdate(MyApplication.getWritableDatabase().getLastImportantNote());
        }
    }
}
