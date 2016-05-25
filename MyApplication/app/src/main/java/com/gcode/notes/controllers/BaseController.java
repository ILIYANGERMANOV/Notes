package com.gcode.notes.controllers;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.adapters.main.MainAdapter;
import com.gcode.notes.controllers.bin.BinController;
import com.gcode.notes.controllers.visible.AllNotesController;
import com.gcode.notes.controllers.visible.ImportantController;
import com.gcode.notes.controllers.visible.PrivateController;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.helper.SimpleItemTouchHelperCallback;
import com.gcode.notes.tasks.async.main.AddItemFromDbToMainTask;
import com.gcode.notes.tasks.async.main.RemoveItemFromMainTask;
import com.gcode.notes.ui.helpers.SearchViewHelper;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

public abstract class BaseController implements ControllerInterface {
    //TODO: REFACTOR AND OPTIMIZE
    private static BaseController mInstance;

    protected MainActivity mMainActivity;
    protected Toolbar mToolbar;
    protected RecyclerView mRecyclerView;
    protected FloatingActionMenu mFabMenu;
    protected AppBarLayout mAppBarLayout;
    protected MainAdapter mMainAdapter;
    protected SimpleItemTouchHelperCallback mSimpleItemTouchHelperCallback;

    protected BaseController(MainActivity mainActivity) {
        if (mainActivity == null) return; //protect from null pointer exception
        mMainActivity = mainActivity;
        mToolbar = mainActivity.getToolbar();
        mRecyclerView = mainActivity.getRecyclerView();
        mFabMenu = mainActivity.getFabMenu();
        mAppBarLayout = mainActivity.getAppBarLayout();
        mMainAdapter = mainActivity.mMainAdapter;
        mSimpleItemTouchHelperCallback = mainActivity.mSimpleItemTouchHelperCallback;
    }

    public synchronized static BaseController getInstance() {
        return mInstance;
    }

    public static void setInstance(BaseController controller) {
        mInstance = controller;
    }

    public void setNewContent(ArrayList<ContentBase> newContent, boolean scrollToTop) {
        MainAdapter mainAdapter = mMainAdapter;
        if (mainAdapter != null) {
            mainAdapter.updateContent(newContent, scrollToTop);
            if (scrollToTop) {
                mRecyclerView.scrollToPosition(0); //not smooth scroll, cuz there is bad UX
            }
        }
    }

    public void addItemAsFirst(ContentBase item) {
        MainAdapter mainAdapter = mMainAdapter;
        if (mainAdapter != null) {
            mainAdapter.addItem(0, item);
            mRecyclerView.smoothScrollToPosition(0);
            if (SearchViewHelper.isSearchViewOpened(mMainActivity)) {
                //search view is open, call searchHandler#onNewItemAdded() so list copy can be present
                mMainActivity.mSearchHandler.onNewItemAdded(item);
            }
        }
    }

    public void updateItem(ContentBase item) {
        MainAdapter mainAdapter = mMainAdapter;
        if (mainAdapter != null) {
            if (mainAdapter.updateItem(item)) {
                //item was updated successfully, if search is started call onItemChange()
                if (SearchViewHelper.isSearchViewOpened(mMainActivity)) {
                    //search view is opened, call searchHandler#onItemChanged() so list copy will be present
                    mMainActivity.mSearchHandler.onItemChanged(item);
                }
            } else {
                //item not updated, so it not exists, add it
                onAddNote(item);
            }
        } else {
            MyDebugger.log("Failed to update item, mainAdapter is null.");
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
        return mMainAdapter;
    }

    /**
     * Changes toolbar titles.
     * Sets recycler view's content, hides / shows FAB with animation.
     *
     * @param scrollToTop    whether recycler view should be scrolled to top
     * @param loadNewContent whether the controller should load new content
     */
    public void setContent(boolean scrollToTop, boolean loadNewContent) {
        SearchViewHelper.closeSearchView(mMainActivity);
        onSetContentAnimation();
    }

    protected void onSetContentAnimation() {
        mAppBarLayout.setExpanded(true, true);
    }

    @Override
    public void onNewNoteAdded(int mode) {
        if (shouldHandleMode(mode)) {
            new AddItemFromDbToMainTask().execute();
        }
    }

    @Override
    public void onAddNote(ContentBase contentBase) {
        if (mMainAdapter != null) {
            int itemPosition = mMainAdapter.addItemByOrderId(contentBase);
            mRecyclerView.smoothScrollToPosition(itemPosition);
            if (SearchViewHelper.isSearchViewOpened(mMainActivity)) {
                //search view is opened, call searchHandler#onItemAdded() so list copy will be present
                mMainActivity.mSearchHandler.onItemAdded(contentBase);
            }
        }
    }

    @Override
    public void onItemChanged(ContentBase item) {
        if (shouldHandleMode(item.getMode())) {
            updateItem(item);
        }
    }

    @Override
    public void onItemModeChanged(ContentBase item) {
        int mode = item.getMode();
        if (shouldHandleMode(mode)) {
            //controller should handle item change, call onItemChanged()
            onItemChanged(item);
        } else {
            //this item should not be handled by controller, remove it
            new RemoveItemFromMainTask(getRemoveMessageForMode(mode))
                    .execute(item);
            if (SearchViewHelper.isSearchViewOpened(mMainActivity)) {
                //search view is opened, call searchHandler#onItemRemoved() so list copy will be present
                mMainActivity.mSearchHandler.onItemRemoved(item);
            }
        }
    }

    private String getRemoveMessageForMode(int mode) {
        switch (mode) {
            case Constants.MODE_NORMAL:
            case Constants.MODE_IMPORTANT:
                return mMainActivity.getString(R.string.note_moved_to_all_notes);
            case Constants.MODE_DELETED_NORMAL:
            case Constants.MODE_DELETED_IMPORTANT:
                return mMainActivity.getString(R.string.note_moved_to_bin);
            case Constants.MODE_PRIVATE:
                return mMainActivity.getString(R.string.note_moved_to_private);
            case Constants.MODE_DELETED_FOREVER:
                return mMainActivity.getString(R.string.note_deleted_permanently);
            default:
                MyDebugger.log("BaseController getRemoveMessageForMode(): unknown mode!");
                return "Note moved to unknown mode";
        }
    }
}
