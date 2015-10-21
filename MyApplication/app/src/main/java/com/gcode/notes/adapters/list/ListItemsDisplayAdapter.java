package com.gcode.notes.adapters.list;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.list.ListItemBaseViewHolder;
import com.gcode.notes.adapters.viewholders.list.ListItemDisplayViewHolder;
import com.gcode.notes.data.ListDataItem;

import java.util.ArrayList;

public class ListItemsDisplayAdapter extends ListItemsBaseAdapter {
    ListItemsDisplayAdapter mOtherAdapter;
    boolean mIsTickedModeOn;

    public ListItemsDisplayAdapter(ArrayList<ListDataItem> data, boolean isTickedModeOn) {
        super(data);
        mIsTickedModeOn = isTickedModeOn;
    }

    @Override
    public ListItemBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ListItemDisplayViewHolder(inflater.inflate(R.layout.single_list_data_item, parent, false), this);

    }

    public ListDataItem getItemAtPosition(int position) {
        return mData.get(position);
    }

    public void setOtherAdapter(ListItemsDisplayAdapter otherAdapter) {
        this.mOtherAdapter = otherAdapter;
    }

    public ListItemsDisplayAdapter getOtherAdapter() {
        return mOtherAdapter;
    }

    public void add(ListDataItem item) {
        if (mIsTickedModeOn) {
            item.setChecked(true);
        } else {
            item.setChecked(false);
        }
        mData.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
}
