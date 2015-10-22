package com.gcode.notes.controllers;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.adapters.MainAdapter;
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

    public synchronized static BaseController getInstance() {
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
        MainAdapter mMainAdapter = getMainAdapter();
        if (mMainAdapter != null) {
            mMainAdapter.updateContent(newContent);
            mRecyclerView.invalidate();
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    public void addItem(ContentBase item) {
        MainAdapter mainAdapter = getMainAdapter();
        if (mainAdapter != null) {
            mainAdapter.addItem(0, item);
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    public void updateItem(ContentBase item) {
        MainAdapter mMainAdapter = getMainAdapter();
        if (mMainAdapter != null) {
            mMainAdapter.updateItem(item);
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

    public MainAdapter getMainAdapter() {
        if (mRecyclerView == null) return null;
        RecyclerView.Adapter mAdapter = mRecyclerView.getAdapter();
        if (mAdapter instanceof MainAdapter) {
            return (MainAdapter) mAdapter;
        }
        return null;
    }

    public void setContent() {
        onSetContentAnimation();
    }

    protected void onSetContentAnimation() {
        mAppBarLayout.setExpanded(true, mFab.getTranslationY() != 0);
    }

    public void onItemAdded(int mode) {

    }

    public void onItemChanged(ContentBase item) {

    }
}
