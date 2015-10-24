package com.gcode.notes.adapters.viewholders.list;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcode.notes.adapters.list.display.ListItemsDisplayBaseAdapter;
import com.gcode.notes.extras.MyDebugger;

public class ListItemDisplayViewHolder extends ListItemBaseViewHolder implements View.OnClickListener {
    ListItemsDisplayBaseAdapter mAdapter;

    public ListItemDisplayViewHolder(View itemView, ListItemsDisplayBaseAdapter adapter) {
        super(itemView);
        mCheckBox.setOnClickListener(this);
        mAdapter = adapter;
        if (adapter.isDeactivated()) {
            mCheckBox.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        int itemPosition = getAdapterPosition();
        if (itemPosition != RecyclerView.NO_POSITION) {
            ListItemsDisplayBaseAdapter otherAdapter = mAdapter.getOtherAdapter();
            otherAdapter.add(mAdapter.getItemAtPosition(itemPosition));
            mAdapter.remove(itemPosition);
        } else {
            MyDebugger.log("onClick() ListItemDisplayViewHolder NO_POSITION");
        }
    }
}
