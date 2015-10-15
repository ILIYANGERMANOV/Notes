package com.gcode.notes.data;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcode.notes.adapters.ListItemAdapter;
import com.gcode.notes.adapters.viewholders.main.ListItemViewHolder;

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

    public void displayList(Activity activity, ListItemViewHolder holder, int calledFrom) {
        displayBase(holder.getTitleTextView(), holder.getReminderTextView());
        if (hasReminder()) {
            holder.getAttributesDivider().setVisibility(View.VISIBLE);
        }
        RecyclerView mRecyclerView = holder.getRecyclerView();
        setupRecyclerView(activity, mRecyclerView, calledFrom);
    }

    private void setupRecyclerView(Activity activity, RecyclerView recyclerView, int calledFrom) {
        recyclerView.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(activity));
        if (list == null) {
            list = new ArrayList<>();
        }
        recyclerView.setAdapter(new ListItemAdapter(activity, list, this, null, calledFrom));
        recyclerView.setNestedScrollingEnabled(false);
    }

}
