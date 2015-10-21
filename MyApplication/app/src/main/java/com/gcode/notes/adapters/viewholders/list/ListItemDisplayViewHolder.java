package com.gcode.notes.adapters.viewholders.list;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcode.notes.adapters.list.display.ListItemsDisplayAdapter;
import com.gcode.notes.extras.MyDebugger;

public class ListItemDisplayViewHolder extends ListItemBaseViewHolder implements View.OnClickListener {
    ListItemsDisplayAdapter mAdapter;

    public ListItemDisplayViewHolder(View itemView, ListItemsDisplayAdapter adapter) {
        super(itemView);
        mCheckBox.setOnClickListener(this);
        mAdapter = adapter;
    }

    @Override
    public void onClick(View v) {
        int itemPosition = getAdapterPosition();
        if (itemPosition != RecyclerView.NO_POSITION) {
            ListItemsDisplayAdapter otherAdapter = mAdapter.getOtherAdapter();
            otherAdapter.add(mAdapter.getItemAtPosition(itemPosition));
            mAdapter.remove(itemPosition);
        } else {
            MyDebugger.log("onClick() ListItemDisplayViewHolder NO_POSITION");
        }
    }
}
