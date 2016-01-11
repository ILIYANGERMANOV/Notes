package com.gcode.notes.adapters.list.display;

import android.content.Context;

import com.gcode.notes.data.list.ListDataItem;

import java.util.ArrayList;

public class ListDisplayAdapter extends ListDisplayBaseAdapter {
    public ListDisplayAdapter(Context context, ArrayList<ListDataItem> data, boolean isDeactivated) {
        super(context, data, isDeactivated);
    }

    @Override
    protected boolean areItemsChecked() {
        return false;
    }

    @Override
    public void add(ListDataItem item) {
        item.setChecked(false);
        super.add(item);
    }
}
