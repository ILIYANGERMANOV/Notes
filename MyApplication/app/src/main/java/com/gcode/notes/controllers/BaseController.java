package com.gcode.notes.controllers;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.adapters.NotesAdapter;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.notes.MyApplication;

import java.util.ArrayList;

public abstract class BaseController {
    Toolbar mToolbar;
    RecyclerView mRecyclerView;

    BaseController(Toolbar toolbar, RecyclerView recyclerView) {
        mToolbar = toolbar;
        mRecyclerView = recyclerView;
    }

    public void setNewContent(ArrayList<ContentBase> newContent) {
        NotesAdapter mNotesAdapter = getNotesAdapter();
        if (mNotesAdapter != null) {
            mNotesAdapter.updateContent(newContent);
        }
    }

    public void applyUpdate(ContentBase item) {
        NotesAdapter mNotesAdapter = getNotesAdapter();
        if (mNotesAdapter != null) {
            mNotesAdapter.addItem(item);
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    public int getControllerId() {
        if (this instanceof AllNotesController) {
            return Constants.CONTROLLER_ALL_NOTES;
        } else if(this instanceof ImportantController) {
            return Constants.CONTROLLER_IMPORTANT;
        } else if(this instanceof PrivateController) {
            return Constants.CONTROLLER_PRIVATE;
        } else if(this instanceof BinController) {
            return Constants.CONTROLLER_BIN;
        }
        return Constants.ERROR;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public NotesAdapter getNotesAdapter() {
        RecyclerView.Adapter mAdapter = mRecyclerView.getAdapter();
        if (mAdapter instanceof NotesAdapter) {
            return (NotesAdapter) mAdapter;
        }
        return null;
    }

    public abstract void setContent();

    public abstract void update(int mode);
}
