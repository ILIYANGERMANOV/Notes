package com.gcode.notes.adapters.list.display;

import android.support.v7.widget.RecyclerView;

import com.gcode.notes.data.ListDataItem;

import java.util.ArrayList;

public class ListItemsDisplayAdapter extends ListItemsDisplayBaseAdapter {

    public ListItemsDisplayAdapter(ArrayList<ListDataItem> data, RecyclerView recyclerView) {
        super(data, recyclerView);
    }

    public void add(ListDataItem item) {
        item.setChecked(false);
        mData.add(item);
        notifyItemInserted(getItemCount() - 1);
    }
}
