package com.gcode.notes.data;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.gcode.notes.adapters.list.ListItemsMainAdapter;
import com.gcode.notes.adapters.viewholders.main.ListItemViewHolder;
import com.gcode.notes.extras.values.Constants;

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
                    String reminderString, String creationDate, String lastModified, String expirationDateString) {

        super(id, orderId, targetId, title, mode, hasAttributes, reminderString, creationDate, lastModified, expirationDateString);
    }

    public ArrayList<ListDataItem> getList() {
        return list;
    }

    public void setList(ArrayList<ListDataItem> list) {
        this.list = list;
    }

    public void displayList(Activity activity, ListItemViewHolder holder) {
        displayBase(holder.getTitleTextView(), holder.getReminderTextView());
        if (hasReminder()) {
            holder.getAttributesDivider().setVisibility(View.VISIBLE);
        }
        RecyclerView mRecyclerView = holder.getRecyclerView();
        setupRecyclerView(activity, mRecyclerView, holder.getMoreImageView());
    }

    private void setupRecyclerView(Activity activity, RecyclerView recyclerView, ImageView moreImageView) {
        recyclerView.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(activity));
        if (list == null) {
            list = new ArrayList<>();
        } else {
            if (list.size() <= Constants.MAX_LIST_ITEMS_TO_DISPLAY) {
                moreImageView.setVisibility(View.GONE);
            } else {
                moreImageView.setVisibility(View.VISIBLE);
            }
        }
        recyclerView.setAdapter(new ListItemsMainAdapter(activity, list, this));
        recyclerView.setNestedScrollingEnabled(false);
    }

}
