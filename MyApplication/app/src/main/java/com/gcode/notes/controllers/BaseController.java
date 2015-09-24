package com.gcode.notes.controllers;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.adapters.NotesAdapter;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.Constants;

import java.util.ArrayList;

public class BaseController {
    private static BaseController mInstance;

    Context mContext;
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    FloatingActionButton mFab;
    AppBarLayout mAppBarLayout;

    static int mPreviousControllerId;

    public static BaseController getInstance() {
        if (mInstance == null) {
            mInstance = new BaseController(null, null, null, null, null);
        }
        return mInstance;
    }

    public static void setInstance(BaseController controller) {
        if (mInstance != null) {
            mPreviousControllerId = mInstance.getControllerId();
        } else {
            mPreviousControllerId = Constants.ERROR;
        }
        mInstance = controller;
    }

    BaseController(Context context, Toolbar toolbar, RecyclerView recyclerView,
                   FloatingActionButton fab, AppBarLayout appBarLayout) {

        mContext = context;
        mToolbar = toolbar;
        mRecyclerView = recyclerView;
        mFab = fab;
        mAppBarLayout = appBarLayout;
    }

    public void setNewContent(ArrayList<ContentBase> newContent) {
        NotesAdapter mNotesAdapter = getNotesAdapter();
        if (mNotesAdapter != null) {
            mNotesAdapter.updateContent(newContent);
            mRecyclerView.invalidate();
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
        } else if (this instanceof ImportantController) {
            return Constants.CONTROLLER_IMPORTANT;
        } else if (this instanceof PrivateController) {
            return Constants.CONTROLLER_PRIVATE;
        } else if (this instanceof BinController) {
            return Constants.CONTROLLER_BIN;
        }
        return Constants.ERROR;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public NotesAdapter getNotesAdapter() {
        if (mRecyclerView == null) return null;
        RecyclerView.Adapter mAdapter = mRecyclerView.getAdapter();
        if (mAdapter instanceof NotesAdapter) {
            return (NotesAdapter) mAdapter;
        }
        return null;
    }

    public void setContent() {
        onSetContentAnimation();
    }

    protected void onSetContentAnimation() {
        mAppBarLayout.setExpanded(true, mFab.getTranslationY() != 0);
    }

    public void update(int mode) {

    }
}
