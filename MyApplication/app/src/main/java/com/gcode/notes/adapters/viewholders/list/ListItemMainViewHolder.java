package com.gcode.notes.adapters.viewholders.list;

import android.app.Activity;
import android.view.View;

import com.gcode.notes.adapters.list.ListItemsDisplayAdapter;
import com.gcode.notes.data.ListData;
import com.gcode.notes.listeners.main.ListItemOnClickListener;

public class ListItemMainViewHolder extends ListItemBaseViewHolder {
    public ListItemMainViewHolder(Activity activity, View itemView, ListData listData) {
        super(itemView);
        itemView.setOnClickListener(new ListItemOnClickListener(activity, listData));
        mCheckBox.setClickable(false);
    }
}
