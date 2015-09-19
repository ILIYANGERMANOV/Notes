package com.gcode.notes.data;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.gcode.notes.adapters.ListItemAdapter;
import com.gcode.notes.adapters.viewholders.ListItemViewHolder;

import java.util.ArrayList;
import java.util.Date;

public class ListData extends ContentBase {
    ArrayList<ListDataItem> list;

    public ListData(String title, int mode, boolean hasAttributes,
                    ArrayList<ListDataItem> list, @NonNull String reminderString) {

        super(title, mode, reminderString);
        this.hasAttributes = hasAttributes;

        this.list = list;
    }

    public ListData(int id, int orderId, int targetId, String title, int mode, boolean hasAttributes,
                    String reminderString, Date creationDate, String expirationDateString) {

        super(id, orderId, targetId, title, mode, hasAttributes, reminderString, creationDate, expirationDateString);
    }

    public ArrayList<ListDataItem> getList() {
        return list;
    }

    public void setList(ArrayList<ListDataItem> list) {
        this.list = list;
    }

    public void displayList(Context mContext, ListItemViewHolder holder) {
        displayBase(holder);
        RecyclerView mRecyclerView = holder.getRecyclerView();
        mRecyclerView.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(mContext));
        if (list == null) {
            list = new ArrayList<>();
        }
        mRecyclerView.setAdapter(new ListItemAdapter(list));
        mRecyclerView.setNestedScrollingEnabled(false);
    }
}
