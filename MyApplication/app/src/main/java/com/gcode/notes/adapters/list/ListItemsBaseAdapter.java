package com.gcode.notes.adapters.list;


import android.support.v7.widget.RecyclerView;

import com.gcode.notes.adapters.viewholders.list.ListItemBaseViewHolder;
import com.gcode.notes.data.ListDataItem;

import java.util.ArrayList;

public abstract class ListItemsBaseAdapter extends RecyclerView.Adapter<ListItemBaseViewHolder> {
    protected ArrayList<ListDataItem> mData;

    public ListItemsBaseAdapter(ArrayList<ListDataItem> data) {
        mData = data;
    }

    @Override
    public void onBindViewHolder(ListItemBaseViewHolder holder, int position) {
        mData.get(position).display(holder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
