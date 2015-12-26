package com.gcode.notes.controllers;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

public class BaseController {
    private static BaseController mInstance;

    Context mContext;
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    FloatingActionButton mFab;
    AppBarLayout mAppBarLayout;
    SimpleItemTouchHelperCallback mSimpleItemTouchHelperCallback;

    static int mPreviousControllerId;

    public synchronized static BaseController getInstance() {
        if (mInstance == null) {
            //TODO: handle problems when clear memory
            mInstance = new BaseController(null, null, null, null, null, null);
            MyDebugger.log("BaseController is null, fake instance created.");
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
                   FloatingActionButton fab, AppBarLayout appBarLayout,
                   SimpleItemTouchHelperCallback simpleItemTouchHelperCallback) {

        mContext = context;
        mToolbar = toolbar;
        mRecyclerView = recyclerView;
        mFab = fab;
        mAppBarLayout = appBarLayout;
        mSimpleItemTouchHelperCallback = simpleItemTouchHelperCallback;
    }

    public void setNewContent(ArrayList<ContentBase> newContent, boolean notForFirstTime) {
        MainAdapter mMainAdapter = getMainAdapter();
        if (mMainAdapter != null) {
            mMainAdapter.updateContent(newContent);
            mRecyclerView.invalidate();
            if (notForFirstTime) {
                mRecyclerView.smoothScrollToPosition(0);
            }
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
        } else {
            MyDebugger.log("Failed to update item, mMainAdapter is null.");
        }
    }

    public void updateItemMode(ContentBase item) {
        MainAdapter mMainAdapter = getMainAdapter();
        if (mMainAdapter != null) {
            mMainAdapter.updateItemMode(item);
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

    public void setContent(boolean fromSavedInstanceState) {
        onSetContentAnimation();
    }

    protected void onSetContentAnimation() {
        //TODO: fix bug when deleted snackbar is showing and FAB goes invis and then is upper on main
        mAppBarLayout.setExpanded(true, mFab.getTranslationY() != 0);
    }

    public void onItemAdded(int mode) {

    }

    public void onItemChanged(ContentBase item) {

    }

    public void onItemModeChanged(ContentBase item) {
    }
}
