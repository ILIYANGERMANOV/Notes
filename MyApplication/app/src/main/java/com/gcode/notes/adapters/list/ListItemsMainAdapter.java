package com.gcode.notes.adapters.list;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.list.ListItemBaseViewHolder;
import com.gcode.notes.adapters.viewholders.list.ListItemMainViewHolder;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.extras.values.Constants;

import java.util.ArrayList;

public class ListItemsMainAdapter extends ListItemsBaseAdapter {
    Activity mActivity;
    ListData mListData;

    public ListItemsMainAdapter(Activity activity, ArrayList<ListDataItem> data, ListData listData) {
        super(data);
        mActivity = activity;
        mListData = listData;
    }

    @Override
    public ListItemBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_data_item_main, parent, false);
        return new ListItemMainViewHolder(mActivity, view, mListData);
    }

    @Override
    public int getItemCount() {
        int itemCount = mData.size();
        return itemCount < Constants.MAX_LIST_ITEMS_TO_DISPLAY ? itemCount : Constants.MAX_LIST_ITEMS_TO_DISPLAY;
    }
}
