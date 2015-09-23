package com.gcode.notes.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.SingleListItemViewHolder;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.ListDataItem;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<SingleListItemViewHolder> {
    Context mContext;
    ArrayList<ListDataItem> mData;
    ListData mListData;

    public ListItemAdapter(Context context, ArrayList<ListDataItem> data, ListData listData) {
        mContext = context;
        mData = data;
        mListData = listData;
    }

    @Override
    public SingleListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SingleListItemViewHolder(mContext, inflater.inflate(R.layout.single_list_data_item, parent, false),
                mListData);
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
