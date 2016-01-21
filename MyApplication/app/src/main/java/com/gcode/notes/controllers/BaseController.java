package com.gcode.notes.controllers;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.controllers.bin.BinController;
import com.gcode.notes.controllers.visible.AllNotesController;
import com.gcode.notes.controllers.visible.ImportantController;
import com.gcode.notes.controllers.visible.PrivateController;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.helper.SimpleItemTouchHelperCallback;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

public class BaseController {
    //TODO: REFACTOR AND OPTIMIZE
    private static BaseController mInstance;

    protected MainActivity mMainActivity;
    protected Toolbar mToolbar;
    protected RecyclerView mRecyclerView;
    protected FloatingActionMenu mFabMenu;
    protected AppBarLayout mAppBarLayout;
    protected SimpleItemTouchHelperCallback mSimpleItemTouchHelperCallback;

    public synchronized static BaseController getInstance() {
        if (mInstance == null) {
            //TODO: handle problems when clear memory
            mInstance = new BaseController(null);
            MyDebugger.log("BaseController is null, fake instance created.");
        }
        return mInstance;
    }

    public static void setInstance(BaseController controller) {
        mInstance = controller;
    }

    protected BaseController(MainActivity mainActivity) {
        if (mainActivity == null) return; //protect from null pointer exception
        mMainActivity = mainActivity;
        mToolbar = mainActivity.getToolbar();
        mRecyclerView = mainActivity.getRecyclerView();
        mFabMenu = mainActivity.getFabMenu();
        mAppBarLayout = mainActivity.getAppBarLayout();
        mSimpleItemTouchHelperCallback = mainActivity.mSimpleItemTouchHelperCallback;
    }

    public void setNewContent(ArrayList<ContentBase> newContent, boolean scrollToTop) {
        MainAdapter mainAdapter = getMainAdapter();
        if (mainAdapter != null) {
            mainAdapter.updateContent(newContent);
            //mRecyclerView.invalidate();
            if (scrollToTop) {
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

    public MainActivity getMainActivity() {
        return mMainActivity;
    }

    public MainAdapter getMainAdapter() {
        if (mRecyclerView == null) return null;
        RecyclerView.Adapter mAdapter = mRecyclerView.getAdapter();
        if (mAdapter instanceof MainAdapter) {
            return (MainAdapter) mAdapter;
        }
        return null;
    }

    /**
     * Changes toolbar titles.
     * Sets recycler view's content, hides / shows FAB with animation.
     * @param scrollToTop - whether recycler view should be scrolled to top
     */
    public void setContent(boolean scrollToTop) {
        onSetContentAnimation();
    }

    protected void onSetContentAnimation() {
        mAppBarLayout.setExpanded(true, true);
    }

    public void onItemAdded(int mode) {

    }

    public void onItemChanged(ContentBase item) {

    }

    public void onItemModeChanged(ContentBase item) {
    }
}
