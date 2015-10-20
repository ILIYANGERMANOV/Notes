package com.gcode.notes.adapters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.SingleListItemViewHolder;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.ListDataItem;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<SingleListItemViewHolder> {
    Activity mActivity;
    ArrayList<ListDataItem> mData;
    ListData mListData;
    RecyclerView mRecyclerView;

    int mCalledFrom;
    boolean mIsTickedModeOn;

    ListItemAdapter mOtherAdapter;

    public ListItemAdapter(Activity activity, ArrayList<ListDataItem> data, ListData listData,
                           RecyclerView recyclerView, int calledFrom) {

        mActivity = activity;
        mData = data;
        mListData = listData;
        mRecyclerView = recyclerView;
        mCalledFrom = calledFrom;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setTickedModeOn() {
        mIsTickedModeOn = true;
    }

    public void setOtherAdapter(ListItemAdapter otherAdapter) {
        this.mOtherAdapter = otherAdapter;
    }

    public ListItemAdapter getOtherAdapter() {
        return mOtherAdapter;
    }

    public ListDataItem getItemAtPosition(int position) {
        return mData.get(position);
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
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

    @Override
    public SingleListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SingleListItemViewHolder(mActivity, inflater.inflate(R.layout.single_list_data_item, parent, false),
                mListData, this, mCalledFrom);
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
