package com.gcode.notes.adapters.list.display;

import com.gcode.notes.data.ListDataItem;

import java.util.ArrayList;

public class ListItemsDisplayAdapter extends ListItemsDisplayBaseAdapter {

    public ListItemsDisplayAdapter(ArrayList<ListDataItem> data) {
        super(data);
    }

    public void add(ListDataItem item) {
        item.setChecked(false);
        mData.add(item);
        notifyItemInserted(getItemCount() - 1);
    }
}
