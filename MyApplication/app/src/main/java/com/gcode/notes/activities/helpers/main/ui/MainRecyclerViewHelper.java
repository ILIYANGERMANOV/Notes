package com.gcode.notes.activities.helpers.main.ui;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.ui.listeners.RecyclerViewOnScrollListener;
import com.gcode.notes.adapters.main.MainAdapter;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.touch.SimpleItemTouchHelperCallback;

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

        int columnCount = mMainActivity.getResources().getInteger(R.integer.main_grid_column_count);
        final GridLayoutManager layoutManager =
                new GridLayoutManager(mMainActivity, columnCount);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //shows / hides fab menu button on scrolling
        recyclerView.addOnScrollListener(new RecyclerViewOnScrollListener(mMainActivity.getFabMenu()));

        mMainActivity.mSimpleItemTouchHelperCallback = new SimpleItemTouchHelperCallback(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mMainActivity.mSimpleItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
