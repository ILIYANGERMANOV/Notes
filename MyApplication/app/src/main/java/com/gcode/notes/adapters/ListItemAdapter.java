package com.gcode.notes.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.SingleListItemViewHolder;
import com.gcode.notes.data.ListDataItem;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<SingleListItemViewHolder> {
    ArrayList<ListDataItem> mData;

    public ListItemAdapter(ArrayList<ListDataItem> data) {
        mData = data;
    }

    @Override
    public SingleListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SingleListItemViewHolder(inflater.inflate(R.layout.single_list_data_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SingleListItemViewHolder holder, int position) {
        mData.get(position).display(holder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
