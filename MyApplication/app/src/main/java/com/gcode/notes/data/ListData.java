package com.gcode.notes.data;


import android.app.Activity;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.main.ListItemViewHolder;
import com.gcode.notes.extras.values.Constants;

import java.util.ArrayList;

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

    public void displayListOnMain(Activity activity, ListItemViewHolder holder) {
        displayBase(holder.getTitleTextView(), holder.getReminderTextView());
        if (hasReminder()) {
            //display divider
            holder.getAttributesDivider().setVisibility(View.VISIBLE);
        }
        LinearLayout containerLayout = holder.getContainerLayout();

        //set holder to default state
        holder.getMoreImageView().setVisibility(View.GONE);
        containerLayout.removeAllViews();

        //add list items to container
        LayoutInflater inflater = LayoutInflater.from(activity);
        for (int i = 0; i < list.size(); ++i) {
            if (i < Constants.MAX_LIST_ITEMS_TO_DISPLAY) {
                //display list data item
                containerLayout.addView(createViewForItem(list.get(i), inflater, containerLayout));
            } else {
                //max items to display is reached, show more image view
                holder.getMoreImageView().setVisibility(View.VISIBLE);
                break;
            }
        }
        containerLayout.invalidate();
    }

    private View createViewForItem(ListDataItem item, LayoutInflater inflater, LinearLayout containerLayout) {
        //create view
        View itemView = inflater.inflate(R.layout.list_data_item_row, containerLayout, false);
        //bind view
        CheckedTextView checkedTextView = (CheckedTextView) itemView.findViewById(R.id.list_data_item_row_checked_text_view);
        if (item.isChecked()) {
            checkedTextView.setChecked(true);
            checkedTextView.setPaintFlags(checkedTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            checkedTextView.setChecked(false);
            checkedTextView.setPaintFlags(checkedTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        checkedTextView.setText(item.getContent());
        return itemView;
    }
}
