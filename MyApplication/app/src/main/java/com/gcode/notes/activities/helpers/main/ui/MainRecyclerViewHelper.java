package com.gcode.notes.activities.helpers.main.ui;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

public class MainRecyclerViewHelper {
    MainActivity mMainActivity;

    public MainRecyclerViewHelper(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void setupRecyclerView() {
        ArrayList<ContentBase> notesList = new ArrayList<>();

        RecyclerView recyclerView = mMainActivity.getRecyclerView();

        MainAdapter adapter = new MainAdapter(mMainActivity, mMainActivity.getRecyclerView(), notesList,
                mMainActivity.getCoordinatorLayout(), mMainActivity.getRecyclerViewEmptyView());

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(mMainActivity, Constants.GRID_COLUMNS_COUNT);
        RecyclerView.ItemAnimator mItemAnimator = new DefaultItemAnimator();

        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setItemAnimator(mItemAnimator);
        recyclerView.setAdapter(adapter);

        mMainActivity.mSimpleItemTouchHelperCallback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mMainActivity.mSimpleItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
