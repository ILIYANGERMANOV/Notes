package com.gcode.notes.adapters.list.display;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gcode.notes.R;
import com.gcode.notes.adapters.list.ListItemsBaseAdapter;
import com.gcode.notes.adapters.viewholders.list.ListItemBaseViewHolder;
import com.gcode.notes.adapters.viewholders.list.ListItemDisplayViewHolder;
import com.gcode.notes.data.ListDataItem;

import java.util.ArrayList;

public class ListItemsDisplayBaseAdapter extends ListItemsBaseAdapter {
    ListItemsDisplayBaseAdapter mOtherAdapter;
    boolean mIsDeactivated;
    RecyclerView mRecyclerView;

    public ListItemsDisplayBaseAdapter(ArrayList<ListDataItem> data, RecyclerView recyclerView) {
        super(data);
        mRecyclerView = recyclerView;
    }

    public boolean isDeactivated() {
        return mIsDeactivated;
    }

    public void setDeactivated(boolean mIsDeactivated) {
        this.mIsDeactivated = mIsDeactivated;
    }

    @Override
    public ListItemBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ListItemDisplayViewHolder(inflater.inflate(R.layout.list_data_item_display, parent, false), this);
    }

    public ListDataItem getItemAtPosition(int position) {
        return mData.get(position);
    }

    public void setOtherAdapter(ListItemsDisplayBaseAdapter otherAdapter) {
        this.mOtherAdapter = otherAdapter;
    }

    public ListItemsDisplayBaseAdapter getOtherAdapter() {
        return mOtherAdapter;
    }

    public void add(ListDataItem item) {

    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
}
