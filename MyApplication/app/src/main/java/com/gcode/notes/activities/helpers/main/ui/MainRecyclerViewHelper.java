package com.gcode.notes.activities.helpers.main.ui;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.ui.listeners.RecyclerViewOnScrollListener;
import com.gcode.notes.adapters.main.MainAdapter;
import com.gcode.notes.data.base.ContentBase;
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

        MainAdapter adapter = new MainAdapter(mMainActivity, notesList);
        mMainActivity.mMainAdapter = adapter;

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mMainActivity, Constants.GRID_COLUMNS_COUNT);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);

        //shows / hides fab menu button on scrolling
        recyclerView.addOnScrollListener(new RecyclerViewOnScrollListener(mMainActivity.getFabMenu()));

        mMainActivity.mSimpleItemTouchHelperCallback = new SimpleItemTouchHelperCallback(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mMainActivity.mSimpleItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
